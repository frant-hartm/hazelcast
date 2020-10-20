import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;

public class TestServer {
    public static void main(String[] args) {
        Config config = new Config();
//        config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
//        config.getNetworkConfig().getJoin().getAutoDetectionConfig().setEnabled(false);
        Hazelcast.newHazelcastInstance(config);
    }
}