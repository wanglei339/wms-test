package com.lsh.wms.integration.model;

import java.io.Serializable;

public class OrderResponse implements Serializable {

    private Integer code;

    private String gatewayToken;//gatewayToken

    private boolean succcess;

    private String message;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getGatewayToken() {
        return gatewayToken;
    }

    public void setGatewayToken(String gatewayToken) {
        this.gatewayToken = gatewayToken;
    }

    public boolean isSucccess() {
        return succcess;
    }

    public void setSucccess(boolean succcess) {
        this.succcess = succcess;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}