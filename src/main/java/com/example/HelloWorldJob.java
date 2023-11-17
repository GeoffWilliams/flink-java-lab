package com.example;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class HelloWorldJob {
    public static void main(String[] argv) throws Exception {
        System.out.println("=====[HelloWorld Job started]=====");
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.fromElements(1, 2, 3, 4, 5)
                .map(i -> 2 * i)
                .print();

        env.execute("HelloWorldJob");
    }
}
