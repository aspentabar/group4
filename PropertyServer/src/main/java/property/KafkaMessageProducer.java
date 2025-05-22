package property;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import java.util.Properties;

public class KafkaMessageProducer {

    private static final String TOPIC = "sale-id-count";
    private static KafkaProducer<String, String> producer;

    static {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producer = new KafkaProducer<>(props);
    }

    public static void sendSaleAccessEvent(String saleId) {
        String message = "{\"saleId\":\"" + saleId + "\"}";
        producer.send(new ProducerRecord<>(TOPIC, saleId, message));
    }
}
