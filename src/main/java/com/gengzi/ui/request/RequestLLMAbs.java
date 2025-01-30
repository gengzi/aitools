package com.gengzi.ui.request;


import com.gengzi.ui.entity.RequestEntityBase;
import com.gengzi.ui.util.GsonUtil;
import com.google.gson.Gson;
import com.intellij.openapi.diagnostic.Logger;

import java.util.stream.Stream;

/**
 * RequestLLMAbs
 */
public abstract class RequestLLMAbs implements RequestLLMInterface{

    private static final Logger LOG = Logger.getInstance(RequestLLMAbs.class);
    @Override
    public Stream<String> requestLlm(RequestEntityBase requestEntity) {
        LOG.info("request info:\n "+ GsonUtil.toJson(requestEntity));
        return req(requestEntity);
    }

    public abstract Stream<String> req(RequestEntityBase requestEntity);


}
