package com.virtuslab.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.virtuslab.domain.Event;

public class ObjectMappers {
    static final ObjectMapper OBJECT_MAPPER = JsonMapper.builder()
            .addModule(new SimpleModule().setMixInAnnotation(Event.class, EventMixIn.class))
            .build();
}
