package com.hazelcast.projectx;

import com.hazelcast.internal.serialization.DataSerializerHook;
import com.hazelcast.internal.serialization.impl.FactoryIdHelper;
import com.hazelcast.nio.serialization.DataSerializableFactory;

import static com.hazelcast.internal.serialization.impl.FactoryIdHelper.KOTLIN_DS_FACTORY;
import static com.hazelcast.internal.serialization.impl.FactoryIdHelper.KOTLIN_DS_FACTORY_ID;

public class KotlinEPHook implements DataSerializerHook {
    public static final int F_ID = FactoryIdHelper.getFactoryId(KOTLIN_DS_FACTORY, KOTLIN_DS_FACTORY_ID);

    @Override
    public int getFactoryId() {
        return F_ID;
    }

    @Override
    public DataSerializableFactory createFactory() {
        return new KotlinEPFactory();
    }
}
