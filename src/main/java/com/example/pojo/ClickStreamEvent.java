package com.example.pojo;

import com.example.util.ClickStreamUseridConvertor;
import lombok.Data;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Loosely matches the Datagen schema for ClickStream - I ignore fields I dont care about
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClickStreamEvent {
    private String ip;

    @JsonDeserialize(using = ClickStreamUseridConvertor.class)
    private String userid;
    private String time;
    private String request;
    private String status; ;
    private String bytes;
    private String referrer;
    private String agent;
}
