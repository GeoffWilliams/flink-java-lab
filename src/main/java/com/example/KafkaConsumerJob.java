package com.example;

import com.example.pojo.ClickStreamEvent;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.formats.json.JsonDeserializationSchema;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.io.InputStream;
import java.util.Properties;

// create a topic "demo" and setup a datagen connector using JSON (NOT JSON schema!) and "ClickStream" template
public class KafkaConsumerJob {
    public static void main(String[] argv) throws Exception {
        System.out.println("=====[KafkaConsumer Job started]=====");

        Properties consumerConfig = new Properties();
        try (InputStream stream = KafkaConsumerJob.class.getClassLoader().getResourceAsStream("consumer.properties")) {
            consumerConfig.load(stream);
        } catch (Exception e) {
            System.out.println("error loading consumer.properties = crash!");
        }

        // example of how to configure a producer
//        Properties producerConfig = new Properties();
//        try (InputStream stream = KafkaConsumer.class.getClassLoader().getResourceAsStream("producer.properties")) {
//            producerConfig.load(stream);
//        }

        KafkaSource<ClickStreamEvent> demoSource = KafkaSource.<ClickStreamEvent>builder()
                .setProperties(consumerConfig)
                .setTopics("demo")
                .setStartingOffsets(OffsetsInitializer.latest())
                // JSON with **no** schema
                .setValueOnlyDeserializer(new JsonDeserializationSchema<>(ClickStreamEvent.class))
                .build();



        // Tweak/fine tune operators? yes!
        // https://nightlies.apache.org/flink/flink-docs-release-1.18/docs/deployment/finegrained_resource/
        //
        // Im just not going there(!) Let flink do its thing until you have good reason to fiddle
        // Status: MVP (Nov 2023/Flink 1.18)
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.fromSource(demoSource, WatermarkStrategy.noWatermarks(), "demoSource")
                // lets make it look a bit like a web server access log
                .map(event -> String.format("[%s] [userid: %s] %s %s %s",
                        event.getTime(),
                        event.getUserid(),
                        event.getIp(),
                        event.getRequest(),
                        event.getAgent()
                ))
                .print();

        env.execute("KafkaConsumerJob");
    }

}
