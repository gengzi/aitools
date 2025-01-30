package com.gengzi.ui.request;


import com.gengzi.ui.entity.RequestEntityBase;

import java.util.stream.Stream;

/**
 * 请求大模型顶级接口
 *
 */
public interface RequestLLMInterface {

    /**
     * 请求大模型流示返回
     *
     * @return
     */
    Stream<String> requestLlm(RequestEntityBase requestEntity);


}
