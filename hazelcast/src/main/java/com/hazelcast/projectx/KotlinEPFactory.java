package com.hazelcast.projectx;

import com.hazelcast.nio.serialization.DataSerializableFactory;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

public class KotlinEPFactory implements DataSerializableFactory {
    public static final int ENTRY_PROCESSOR_CLASS_ID = 1;
    public static final int RUNNABLE_CLASS_ID = 2;


    @Override
    public IdentifiedDataSerializable create(int typeId) {
        switch (typeId) {
            case ENTRY_PROCESSOR_CLASS_ID:
                return new KotlinEP();
            case RUNNABLE_CLASS_ID:
                return new KotlinRunnable();
            default:
                throw new IllegalArgumentException("Unknown class id " + typeId);
        }
    }
}
