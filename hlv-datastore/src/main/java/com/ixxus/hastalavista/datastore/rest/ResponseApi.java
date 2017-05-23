package com.ixxus.hastalavista.datastore.rest;

/**
 * Bean used to send data.
 * Created by alexneatu on 22/05/2017.
 */
public class ResponseApi {

    private String status;
    private String message;
    private Object data;

    public ResponseApi(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
