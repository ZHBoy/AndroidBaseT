package com.zhboy.ycwwz.base_library.event.login;

/**
 * @author: zhou_hao
 * @date: 2020-02-25
 * @description: 登录的eventBus
 **/
public class LoginEvent {

    private String wx_code;

    public LoginEvent(String wx_code) {
        this.wx_code = wx_code;
    }

    public String getWx_code() {
        return wx_code;
    }

    public void setWx_code(String wx_code) {
        this.wx_code = wx_code;
    }

}
