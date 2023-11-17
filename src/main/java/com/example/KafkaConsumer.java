package com.example;

import com.example.pojo.ClickStreamEvent;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.formats.json.JsonDeserializationSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.io.InputStream;
import java.util.Properties;

// create a topic "demo" and setup a datagen connector using JSON (NOT JSON schema!) and "ClickStream" template
public class KafkaConsumer {
    public static void main(String[] argv) throws Exception {
        System.out.println("=====[KafkaConsumer Job started]=====");

        Properties consumerConfig = new Properties();
        try (InputStream stream = KafkaConsumer.class.getClassLoader().getResourceAsStream("consumer.properties")) {
            consumerConfig.load(stream);
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


        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.fromSource(demoSource, WatermarkStrategy.noWatermarks(), "demoSource")
                // lets make it look a bit like a web server access log
                .map(event -> String.format("[%s] %s %s %s",
                            event.getTime(),
                            event.getIp(),
                            event.getRequest(),
                            event.getAgent()
                        )
                )
                .print();

        env.execute();
    }

}
