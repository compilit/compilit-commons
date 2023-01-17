package com.compilit.jsonpatch;

import static com.compilit.jsonpatch.ExceptionMessages.patchException;

import com.compilit.core.api.annotations.Patchable;
import com.github.fge.jsonpatch.JsonPatchException;
import jakarta.json.JsonPatch.Operation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.reflect.FieldUtils;

final class PrePatchValidator {

  private final Map<String, Field> patchableFieldMap = new HashMap<>();

  public void validate(List<JsonPatchDTO> jsonPatchDTOS, PatchableEntity patchableEntity)
    throws JsonPatchException {
    var patchableClass = patchableEntity.getClass();
    for (JsonPatchDTO jsonPatchDTO : jsonPatchDTOS) {
      var operation = Operation.fromOperationName(jsonPatchDTO.op());
      var path = jsonPatchDTO.path();
      var actualTargetPath = resolvePath(patchableClass, path);
      applyValidation(patchableClass, operation, path, actualTargetPath);
    }
  }

  private void applyValidation(
    Class<? extends PatchableEntity> patchableClass,
    Operation operation,
    String path,
    String actualTargetPath
  ) throws JsonPatchException {
    Field targetField = setupPatch(operation, actualTargetPath, patchableClass);
    validate(patchableClass, operation, path, actualTargetPath, targetField);
  }

  private Field setupPatch(
    Operation calledOperation,
    String calledPath,
    Class<?> patchableClass
  ) throws JsonPatchException {
    if (patchableFieldMap.isEmpty()) {
      resolvePatchableFields(patchableClass);
    }
    if (!patchableFieldMap.containsKey(calledPath)) {
      throw new JsonPatchException(patchException(
        calledOperation,
        calledPath,
        patchableClass.getName()
      ));
    }
    return patchableFieldMap.get(calledPath);
  }

  private void resolvePatchableFields(Class<?> patchableClass) {
    var patchableFields = FieldUtils.getFieldsListWithAnnotation(
      patchableClass,
      Patchable.class
    );
    patchableFields.forEach(field -> {
      var path = resolvePath(patchableClass, "/" + field.getName());
      patchableFieldMap.put(path, field);
    });
  }

  private static String resolvePath(Class<?> patchableClass, String calledPath) {
    if (calledPath == null) {
      return null;
    }
    return patchableClass.getSimpleName() + calledPath.replaceAll("/-", "")
                                                      .replaceAll("/[0-9]", "");
  }

  private static void validate(
    Class<? extends PatchableEntity> patchableClass,
    Operation operation,
    String path,
    String actualTargetPath,
    Field targetField
  ) throws JsonPatchException {
    var annotation = targetField.getAnnotation(Patchable.class);
    var allowedOperationsOnField = List.of(annotation.allow());
    var allowedPath = resolvePath(patchableClass, path);
    if (!allowedOperationsOnField.contains(operation)
      || !actualTargetPath.contains(allowedPath)) {
      throw new JsonPatchException(patchException(
        operation,
        actualTargetPath,
        patchableClass.getName()
      ));
    }
  }

}
