package com.gengzi.ui.request;

import com.gengzi.ui.entity.RequestEntityBase;

import java.util.stream.Stream;

public class DefaultReqLLM extends RequestLLMAbs{
    @Override
    public Stream<String> req(RequestEntityBase requestEntity) {
        return Stream.empty();
    }
}
