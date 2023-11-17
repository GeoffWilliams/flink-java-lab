package com.example.util;

import com.example.pojo.ClickStreamEvent;
import com.example.pojo.Snipe;
import com.example.pojo.User;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.streaming.api.functions.co.RichCoFlatMapFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.configuration.Configuration;

public class SnipeEnricher extends RichCoFlatMapFunction<ClickStreamEvent, User, Snipe> {
    private ValueState<ClickStreamEvent> clickState;
    private ValueState<User> userState;

    @Override
    public void open(Configuration config) {
        clickState = getRuntimeContext().getState(new ValueStateDescriptor<>("saved ride", ClickStreamEvent.class));
        userState = getRuntimeContext().getState(new ValueStateDescriptor<>("saved fare", User.class));
    }

    @Override
    public void flatMap1(ClickStreamEvent value, Collector<Snipe> out) throws Exception {
        User user = userState.value();
        if (user != null) {
            userState.clear();
            out.collect(Snipe.newInstance(value, user));
        } else {
            clickState.update(value);
        }
    }

    @Override
    public void flatMap2(User value, Collector<Snipe> out) throws Exception {
        ClickStreamEvent click = clickState.value();
        if (click != null) {
            clickState.clear();
            out.collect(Snipe.newInstance(click, value));
        } else {
            userState.update(value);
        }
    }
}
