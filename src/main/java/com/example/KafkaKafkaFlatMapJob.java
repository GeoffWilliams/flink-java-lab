package com.example;

import com.example.pojo.ClickStreamEvent;
import com.example.pojo.Snipe;
import com.example.pojo.User;
import com.example.util.SnipeEnricher;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.formats.json.JsonDeserializationSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.io.InputStream;
import java.util.Properties;


// needs 2x connectors:
// 1: ClickStream JSON -> demo
// 2: Users JSON -> users
public class KafkaKafkaFlatMapJob {
    public static void main(String[] argv) throws Exception {
        System.out.println("=====[KafkaKafkaJoin (flatmap) Job started]=====");

        Properties consumerConfig = new Properties();
        try (InputStream stream = KafkaConsumerJob.class.getClassLoader().getResourceAsStream("consumer.properties")) {
            consumerConfig.load(stream);
        } catch (Exception e) {
            System.out.println("error loading consumer.properties = crash!");
        }

        // example of how to configure a producer
        Properties producerConfig = new Properties();
        try (InputStream stream = KafkaConsumerJob.class.getClassLoader().getResourceAsStream("producer.properties")) {
            producerConfig.load(stream);
        } catch (Exception e) {
            System.out.println("error loading producer.properties = crash!");
        }


        // Tweak/fine tune operators? yes!
        // https://nightlies.apache.org/flink/flink-docs-release-1.18/docs/deployment/finegrained_resource/
        //
        // Im just not going there(!) Let flink do its thing until you have good reason to fiddle
        // Status: MVP (Nov 2023/Flink 1.18)
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // just for fun
        env.setParallelism(2);

        KafkaSource<ClickStreamEvent> clicksSource = KafkaSource.<ClickStreamEvent>builder()
                .setProperties(consumerConfig)
                .setTopics("demo")
                .setStartingOffsets(OffsetsInitializer.latest())
                // JSON with **no** schema
                .setValueOnlyDeserializer(new JsonDeserializationSchema<>(ClickStreamEvent.class))
                .build();

        KafkaSource<User> usersSource = KafkaSource.<User>builder()
                .setProperties(consumerConfig)
                .setTopics("users")
                .setStartingOffsets(OffsetsInitializer.latest())
                // JSON with **no** schema
                .setValueOnlyDeserializer(new JsonDeserializationSchema<>(User.class))
                .build();


        // sources -> datastreams
        final DataStreamSource<ClickStreamEvent> clickStream =
                env.fromSource(clicksSource, WatermarkStrategy.noWatermarks(), "clicks");
        final DataStreamSource<User> usersStream =
                env.fromSource(usersSource, WatermarkStrategy.noWatermarks(), "users");

        DataStream<Snipe> enrichedStream =
                clickStream
                        .connect(usersStream)
                        .keyBy(ClickStreamEvent::getUserid, User::getUserid)
                        .flatMap(new SnipeEnricher());

        enrichedStream.map(snipe -> String.format("SNIPED via FlatMap! " + snipe.toString()))
            .print();

        env.execute("KafkaKafkaFlatMapJob");
    }

}
