package analytics;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class KafkaSaleConsumer implements Runnable {

    private final AnalyticsDAO dao;

    public KafkaSaleConsumer(AnalyticsDAO dao) {
        this.dao = dao;
    }

    @Override
    public void run() {
        // Step 1: Configure Kafka consumer
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"); // Kafka broker address
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "analytics-consumer-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

        // Step 2: Subscribe to the topic
        consumer.subscribe(Collections.singletonList("sale-id-count"));

        System.out.println("[KafkaConsumer] Listening for sale access events...");

        // Step 3: Poll for messages
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(500));
                for (ConsumerRecord<String, String> record : records) {
                    String saleId = record.value();
                    System.out.println("[KafkaConsumer] Received saleId: " + saleId);

                    // Step 4: Update analytics via DAO
                    boolean updated = dao.incrementSaleAccessCount(saleId);
                    if (updated) {
                        System.out.println("[KafkaConsumer] Successfully updated view count for sale ID: " + saleId);
                    } else {
                        System.out.println("[KafkaConsumer] Failed to update view count for sale ID: " + saleId);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            consumer.close();
        }
    }
}
