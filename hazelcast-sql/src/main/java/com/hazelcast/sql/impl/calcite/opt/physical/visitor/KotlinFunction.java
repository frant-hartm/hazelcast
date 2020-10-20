package com.hazelcast.sql.impl.calcite.opt.physical.visitor;

import com.hazelcast.core.HazelcastException;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.schema.FunctionParameter;
import org.apache.calcite.schema.ScalarFunction;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class KotlinFunction implements ScalarFunction, Serializable {

    private static final ScriptEngineManager ENGINE_MANAGER = new ScriptEngineManager();
    private final String script;

    public KotlinFunction(String script) {
        this.script = script;
    }

    public String call(String param) {
        ScriptEngine kts = ENGINE_MANAGER.getEngineByExtension("kts");
        try {
            Bindings bindings = kts.createBindings();
            bindings.put("param", param);
            return (String) kts.eval(script, bindings);
        } catch (ScriptException e) {
            e.printStackTrace();
            throw new HazelcastException("Error while running script", e);
        }
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