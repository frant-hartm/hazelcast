package com.hazelcast.compute;

public interface ComputeEngine {

    default <T> T getJet() {
        return (T) this;
    };

}
