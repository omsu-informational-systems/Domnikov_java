import org.apache.kafka.clients.admin.AdminClient;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class KafkaCheck {
    public static void main(String[] args) throws Exception {
        String u = System.getenv("KAFKA_USERNAME");
        String p = System.getenv("KAFKA_PASSWORD");

        Properties props = new Properties();
        props.put("bootstrap.servers", "omsu.7bits.it:9094");
        props.put("security.protocol", "SASL_SSL");
        props.put("sasl.mechanism", "SCRAM-SHA-256");
        props.put("ssl.endpoint.identification.algorithm", "https");
        props.put("sasl.jaas.config",
                "org.apache.kafka.common.security.scram.ScramLoginModule required username=\""
                        + u + "\" password=\"" + p + "\";");

        try (AdminClient admin = AdminClient.create(props)) {
            var cluster = admin.describeCluster();
            System.out.println("Cluster id: " + cluster.clusterId().get(10, TimeUnit.SECONDS));
            System.out.println("Nodes: " + cluster.nodes().get(10, TimeUnit.SECONDS));
        }
    }
}