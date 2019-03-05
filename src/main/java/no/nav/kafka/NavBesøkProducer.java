package no.nav.kafka;

import com.google.gson.Gson;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.stream.IntStream;

public class NavBesøkProducer {

    private final static Logger LOG = LogManager.getLogger();

    public static void main(String[] args) {
        final Set<Poststed> poststeder = PoststedLaster.hentPoststeder();


        /// docker run --net host confluentinc/cp-kafka kafka-topics --create  --zookeeper localhost:2181 -topic test2 --replication-factor 3 --partitions 5


        final Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // create safe Producer
        props.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
        props.setProperty(ProducerConfig.ACKS_CONFIG, "all");
        props.setProperty(ProducerConfig.RETRIES_CONFIG, Integer.toString(Integer.MAX_VALUE));
        props.setProperty(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "5");

        final KafkaProducer<String, String> producer = new KafkaProducer<>(props);


        IntStream.range(1, 11).forEach(n -> {

            Optional<Poststed> randomPoststed = poststeder.stream().skip((int) (poststeder.size() * Math.random())).findFirst();
            randomPoststed.ifPresent(poststed -> {

                final String kommuneNummer = poststed.getKommune();
                final String poststedJson = new Gson().newBuilder().create().toJson(poststed);

                final ProducerRecord<String, String> record = new ProducerRecord<>("test2", kommuneNummer, poststedJson);

                //produce record
                producer.send(record, new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata metadata, Exception exception) {
                        if (exception == null) {
                            LOG.info("Record send to Topic: " + metadata.topic() + " Partition: " + metadata.partition() + " Offset: " + metadata.offset());
                        } else {
                            LOG.error("Failed to send record to Kafka", exception);
                        }

                    }
                });
            });

        });
        // flush data
        producer.flush();
        // flush and close producer
        producer.close();

    }

}
