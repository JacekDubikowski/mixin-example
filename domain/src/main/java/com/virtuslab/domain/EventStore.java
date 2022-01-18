package com.virtuslab.domain;

public interface EventStore {
    void store(Event event);

    Event readOne();
}
