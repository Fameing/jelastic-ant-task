package com.jelastic.model;

/**
 * User: Igor.Yova@gmail.com
 * Date: 6/9/11
 * Time: 4:13 PM
 */
public class Deploy {
    private JelasticResponse response;
    private int result;
    private String error;
    private Debug debug;

    public Deploy() {
    }

    public Deploy(JelasticResponse response, int result, String error, Debug debug) {
        this.response = response;
        this.result = result;
        this.error = error;
        this.debug = debug;
    }

     public static class JelasticResponse {
        private int result;
        private String error;
        private String out;

         public int getResult() {
             return result;
         }

         public void setResult(int result) {
             this.result = result;
         }

         public String getError() {
             return error;
         }

         public void setError(String error) {
             this.error = error;
         }

         public String getOut() {
             return out;
         }

         public void setOut(String out) {
             this.out = out;
         }
     }

    public JelasticResponse getResponse() {
        return response;
    }

    public void setResponse(JelasticResponse response) {
        this.response = response;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Debug getDebug() {
        return debug;
    }

    public void setDebug(Debug debug) {
        this.debug = debug;
    }
}
