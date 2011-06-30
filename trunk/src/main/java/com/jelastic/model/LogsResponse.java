package com.jelastic.model;

/**
 * User: Igor.Yova@gmail.com
 * Date: 6/9/11
 * Time: 7:16 PM
 */
public class LogsResponse {
    private String body;
    private int result;
    private int linescount;
    private String error;

    public LogsResponse() {
    }

    public LogsResponse(String body, int result, int linescount, String error) {
        this.body = body;
        this.result = result;
        this.linescount = linescount;
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getLinescount() {
        return linescount;
    }

    public void setLinescount(int linescount) {
        this.linescount = linescount;
    }
}
