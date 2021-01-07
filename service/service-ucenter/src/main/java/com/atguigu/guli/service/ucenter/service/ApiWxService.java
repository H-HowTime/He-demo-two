package com.atguigu.guli.service.ucenter.service;

import javax.servlet.http.HttpSession;

/**
 * @author hehao
 * @create 2021-01-02 16:36
 */
public interface ApiWxService {
    String genQrConnect(HttpSession session);

    String callback(String code, String state,HttpSession session);
}
