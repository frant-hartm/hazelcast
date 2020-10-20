package com.hazelcast.sql.impl.calcite.opt.physical.visitor;

import com.hazelcast.function.FunctionEx;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.schema.FunctionParameter;
import org.apache.calcite.schema.ScalarFunction;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class CallFunction implements ScalarFunction, Serializable {

    private FunctionEx<String, String> supplier;

    public CallFunction(FunctionEx<String, String> supplier) {
        this.supplier = supplier;
    }

    public String call(String param) {
        return supplier.apply(param);
    }
    @Override
    public List<FunctionParameter> getParameters() {
        return Collections.singletonList(new FunctionParameter() {
            @Override
            public int getOrdinal() {
                return 0;
            }

            @Override
            public String getName() {
                return "param";
            }

            @Override public RelDataType getType(RelDataTypeFactory typeFactory) {
                return typeFactory.createJavaType(String.class);
            }

            @Override public boolean isOptional() {
                return false;
            }
        });
    }

    @Override
    public RelDataType getReturnType(RelDataTypeFactory typeFactory) {
        return typeFactory.createJavaType(String.class);
    }
}