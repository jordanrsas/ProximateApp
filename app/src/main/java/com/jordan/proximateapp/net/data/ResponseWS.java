package com.jordan.proximateapp.net.data;

/**
 * Created by jordan on 06/02/2018.
 */

public class ResponseWS {

    Boolean success;
    Boolean error;
    String message;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
