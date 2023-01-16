package com.compilit.jsonpatch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
record JsonPatchDTO(@JsonProperty(value = "op") String op,
                    @JsonProperty(value = "path") String path) {
}
