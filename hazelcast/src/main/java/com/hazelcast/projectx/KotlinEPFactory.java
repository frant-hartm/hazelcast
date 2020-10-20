package com.hazelcast.projectx;

import com.hazelcast.nio.serialization.DataSerializableFactory;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

public class KotlinEPFactory implements DataSerializableFactory {
    public static final int EP_CLASS_ID = 1;

    @Override
    public IdentifiedDataSerializable create(int typeId) {
        switch (typeId) {
            case EP_CLASS_ID:
                return new KotlinEP();
            default:
                throw new IllegalArgumentException("Unknown class id " + typeId);
        }
    }
}
