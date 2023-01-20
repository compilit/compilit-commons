package com.compilit.jsonpatch;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import java.util.List;

/**
 * This interface enables you to validate each JsonPatch against all the annotated fields.
 */
public interface PatchableEntity {

  /**
   * Default instance of PrePatchValidatable which is used for the validation.
   */
  PrePatchValidatable PATCH_VALIDATOR_INSTANCE = new PrePatchValidatable();
  /**
   * Default instance of the ObjectMapper. It is used to deserialize the JsonPatch object in order to validate it.
   */
  ObjectMapper OBJECT_MAPPER_INSTANCE = new ObjectMapper();

  /**
   * Apply the JsonPatch upon this instance of the PatchableEntity
   *
   * @param patch the actual patch definition contains all desired operations on this entity
   * @return this as a PatchableEntity
   * @throws JsonPatchException which needs to lead to a rollback of this transaction
   */
  default PatchableEntity apply(JsonPatch patch) throws JsonPatchException {
    return apply(patch, getClass());
  }

  /**
   * Apply the JsonPatch upon this instance of the PatchableEntity and specify the return type
   *
   * @param patch       the actual patch definition contains all desired operations on this entity
   * @param entityClass the underlying class of this PatchableEntity
   * @param <T>         the actual return type
   * @return a PatchableEntity cast to the specified entityClass
   * @throws JsonPatchException which needs to lead to a rollback of this transaction
   */
  default <T extends PatchableEntity> T apply(JsonPatch patch, Class<T> entityClass) throws JsonPatchException {
    try {
      var objectMapper = getObjectMapper();
      String jsonPatchDTOSx = getObjectMapper().writeValueAsString(patch);
      var mapType = new TypeReference<List<JsonPatchDTO>>() {};
      List<JsonPatchDTO> jsonPatchDTOS = objectMapper.readValue(jsonPatchDTOSx, mapType);
      PATCH_VALIDATOR_INSTANCE.validate(jsonPatchDTOS, this);
      JsonNode patched = patch.apply(objectMapper.convertValue(this, JsonNode.class));
      return objectMapper.treeToValue(patched, entityClass);
    } catch (JsonProcessingException e) {
      throw new JsonPatchException(e.getMessage());
    }
  }

  /**
   * @return the default ObjectMapper or your custom ObjectMapper when overridden.
   */
  @JsonIgnore
  default ObjectMapper getObjectMapper() {
    return OBJECT_MAPPER_INSTANCE;
  }

}
