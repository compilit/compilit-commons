package com.compilit.jsonpatch;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

public class PatchableEntityTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String originalValue = "original";
    private final String patchedValue = "new";
    private TestEntity patchableEntity = new TestEntity(originalValue, new ArrayList<>());

    @Test
    void applyPatch_allowed_shouldApplyPatch() throws JsonPatchException, JsonProcessingException {
        assertThat(patchableEntity.getTestField()).isEqualTo(originalValue);
        String input = """
            [{
                "op" : "replace",
                "path" : "/testField",
                "value" : "new"
            }]
            """;
        var jsonPatch = objectMapper.readValue(input, JsonPatch.class);
        patchableEntity = patchableEntity.apply(jsonPatch, TestEntity.class);
        assertThat(patchableEntity.getTestField()).isEqualTo(patchedValue);
    }

    @Test
    void applyPatch_notAllowed_shouldThrowExceptionAndNotApplyPatch()
        throws JsonProcessingException {
        assertThat(patchableEntity.getTestField()).isEqualTo(originalValue);
        String input = """
            [{
                "op" : "add",
                "path" : "/testField",
                "value" : "new"
            }]
            """;
        var jsonPatch = objectMapper.readValue(input, JsonPatch.class);
        assertThatThrownBy(() -> patchableEntity.apply(jsonPatch))
            .isInstanceOf(JsonPatchException.class);
        assertThat(patchableEntity.getTestField()).isEqualTo(originalValue);
    }


    @Test
    void applyPatch_onCollectionAllowed_shouldApplyPatch()
        throws JsonProcessingException, JsonPatchException {
        assertThat(patchableEntity.getTestCollection()).isEmpty();
        String input = """
            [{
                "op" : "add",
                "path" : "/testCollection/-",
                "value" : "new"
            }]
            """;
        var jsonPatch = objectMapper.readValue(input, JsonPatch.class);
        patchableEntity = (TestEntity) patchableEntity.apply(jsonPatch);
        assertThat(patchableEntity.getTestCollection()).contains(patchedValue);
    }

    @Test
    void applyPatch_onCollectionNotAllowed_shouldThrowExceptionAndNotApplyPatch()
        throws JsonProcessingException {
        assertThat(patchableEntity.getTestCollection()).isEmpty();
        String input = """
            [{
                "op" : "replace",
                "path" : "/testCollection/-",
                "value" : "new"
            }]
            """;
        var jsonPatch = objectMapper.readValue(input, JsonPatch.class);
        assertThatThrownBy(() -> patchableEntity.apply(jsonPatch))
            .isInstanceOf(JsonPatchException.class);
        assertThat(patchableEntity.getTestCollection()).isEmpty();
    }

}
