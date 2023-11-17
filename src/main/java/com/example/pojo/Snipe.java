package com.example.pojo;

import lombok.Data;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Snipe {
    private String userid;
    private String regionid;
    private String gender;
    private String request;

    public static Snipe newInstance(ClickStreamEvent clickStreamEvent, User user) {
        Snipe snipe = new Snipe();
        snipe.setGender(user.getGender());
        snipe.setRegionid(user.getRegionid());
        snipe.setUserid(user.getUserid());
        snipe.setRequest(clickStreamEvent.getRequest());

        return snipe;
    }

    public String toString() {
        return String.format("userid=%s regionid=%s gender=%s request=%s",
            getUserid(),
            getRegionid(),
            getGender(),
            getRequest()
        );
    }
}
