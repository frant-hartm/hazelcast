package com.hazelcast.shell;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.instance.impl.HazelcastInstanceImpl;
import com.hazelcast.spi.impl.NodeEngineImpl;

import java.io.Serializable;

public class EnableDiagnosticsTask implements Runnable, Serializable, HazelcastInstanceAware {

    private HazelcastInstance instance;

    private String directory;

    public EnableDiagnosticsTask(String directory) {
        this.directory = directory;
    }

    @Override
    public void run() {
        HazelcastInstanceImpl hazelcastInstanceImpl = (HazelcastInstanceImpl) instance;
        NodeEngineImpl nodeEngine = hazelcastInstanceImpl.node.nodeEngine;

        nodeEngine.restartDiagnostics(directory);
        hazelcastInstanceImpl.node.getNodeExtension().registerPlugins(nodeEngine.getDiagnostics());
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.instance = hazelcastInstance;
    }
}
