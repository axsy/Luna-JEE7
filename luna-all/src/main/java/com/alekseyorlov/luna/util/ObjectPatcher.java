package com.alekseyorlov.luna.util;

import com.alekseyorlov.luna.dto.Patch;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.zjsonpatch.JsonPatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ObjectPatcher {

    @Autowired(required = false)
    private ObjectMapper mapper;

    public <T> T patch(T source, Patch patch, Class<T> destinationClass) throws IOException {
        JsonNode diffNode = mapper.convertValue(patch.getOperations(), JsonNode.class);
        JsonNode objectNode  = mapper.convertValue(source, JsonNode.class);

        return mapper.treeToValue(JsonPatch.apply(diffNode, objectNode), destinationClass);
    }

}
