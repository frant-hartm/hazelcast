package com.hazelcast.projectx;

import com.hazelcast.core.HazelcastException;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;

public class KotlinRunnable implements Runnable, IdentifiedDataSerializable {
    private static final ScriptEngineManager ENGINE_MANAGER = new ScriptEngineManager();

    public String script;

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(script);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        script = in.readUTF();
    }

    @Override
    public int getFactoryId() {
        return KotlinEPHook.F_ID;
    }

    @Override
    public int getClassId() {
        return KotlinEPFactory.RUNNABLE_CLASS_ID;
    }

    @Override
    public void run() {
        ScriptEngine kts = ENGINE_MANAGER.getEngineByExtension("kts");
        try {
            kts.eval(script);
        } catch (ScriptException e) {
            e.printStackTrace();
            throw new HazelcastException("Error while running script", e);
        }
    }
}
