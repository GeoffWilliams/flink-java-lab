package com.example;

public class NotAJob {
    public static void main(String[] argv) throws InterruptedException {
        System.out.println("=====[NotAJob Job started]=====");


        // If we just have a while loop and dont invoke the flink engine it will not be registered in the Flink UI -
        // run this and see what (doesnt) happen...
        while (true) {
            Thread.sleep(1000L);
            System.out.println(".");
        }
    }
}
