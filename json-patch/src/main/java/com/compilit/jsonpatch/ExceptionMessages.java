package com.compilit.jsonpatch;

import jakarta.json.JsonPatch;

final class ExceptionMessages {

    private static final String MESSAGE = "Patch operation %s on path %s is not allowed for %s";
    static String patchException(JsonPatch.Operation operation, String path, String clazz) {
        return String.format(MESSAGE, operation, path, clazz);
    }

}
