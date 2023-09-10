package com.auto.testing.service;

import org.zaproxy.clientapi.core.ClientApi;

import com.auto.testing.config.ZapProperties;

public interface SecurityTestingServiceInterface {

    void homePageActions();
    void launchZap(String ZapLocation);
    String ExecuteZap(ClientApi api,String ZAP_URI_PORT,String zapoption,ZapProperties properties);
    void executeseleniumModule();
}
