package com.virtuslab.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.virtuslab.domain.Event;
import com.virtuslab.domain.SomethingElseHappenedEvent;
import com.virtuslab.domain.SomethingHappenedEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.virtuslab.jackson.ObjectMappers.OBJECT_MAPPER;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EventSerializationTest {
    private static final String SAMPLE_EVENT_DATA = "someEventData";

    @ParameterizedTest
    @DisplayName("Event serialization should work in expected manner")
    @MethodSource("provideDeSerializationData")
    void shouldSerializeEventCorrectly(Event event, String serializedEvent) throws JsonProcessingException {
        var jacksonSerializedEvent = OBJECT_MAPPER.writeValueAsString(event);

        assertEquals(serializedEvent, jacksonSerializedEvent, "should serialize event in expected manner");
    }

    @ParameterizedTest
    @DisplayName("Event deserialization should work in expected manner")
    @MethodSource("provideDeSerializationData")
    void shouldDeserializeEventCorrectly(Event event, String serializedEvent, Class<? extends Event> eventClass) throws JsonProcessingException {
        var deserializedEvent = OBJECT_MAPPER.readValue(serializedEvent, Event.class);

        assertEquals(event.getClass(), eventClass, "should deserialize event to expected type");
        assertEquals(event, deserializedEvent, "should deserialize event in expected manner");
    }

    @ParameterizedTest
    @DisplayName("Event deserialization and serialization should work in expected manner")
    @MethodSource("provideDeSerializationData")
    void shouldSerializeAndThenDeserializeEventCorrectly(Event event) throws JsonProcessingException {
        var jacksonSerializedEvent = OBJECT_MAPPER.writeValueAsString(event);
        var deserializedEvent = OBJECT_MAPPER.readValue(jacksonSerializedEvent, Event.class);

        assertEquals(event, deserializedEvent, "should serialize and then deserialize event in expected manner");
    }

    private static Stream<Arguments> provideDeSerializationData() {
        return Stream.of(
                ofEvent(new SomethingHappenedEvent(SAMPLE_EVENT_DATA)),
                ofEvent(new SomethingElseHappenedEvent(SAMPLE_EVENT_DATA))
        );
    }

    private static Arguments ofEvent(Event event) {
        var eventClass = event.getClass();
        String serializedEvent = "{\"@type\":\"%s\",\"data\":\"%s\"}"
                .formatted(eventClass.getCanonicalName(), SAMPLE_EVENT_DATA);
        System.out.println(serializedEvent);
        return Arguments.of(event, serializedEvent, eventClass);
    }
}
