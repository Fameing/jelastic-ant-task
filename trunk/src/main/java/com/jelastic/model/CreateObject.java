package com.jelastic.model;

/**
 * User: Igor.Yova@gmail.com
 * Date: 6/9/11
 * Time: 12:31 PM
 */
public class CreateObject {
    private int id;
    private int result;
    private JelasticObject object;
    private Debug debug;
    private String error;

    public CreateObject() {
    }

    public CreateObject(int id, int result, JelasticObject object, Debug debug, String error) {
        this.id = id;
        this.result = result;
        this.object = object;
        this.debug = debug;
        this.error = error;
    }

    public static class JelasticObject {
        private int id;
        private String developer;
        private String uploadDate;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDeveloper() {
            return developer;
        }

        public void setDeveloper(String developer) {
            this.developer = developer;
        }

        public String getUploadDate() {
            return uploadDate;
        }

        public void setUploadDate(String uploadDate) {
            this.uploadDate = uploadDate;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public JelasticObject getObject() {
        return object;
    }

    public void setObject(JelasticObject object) {
        this.object = object;
    }

    public Debug getDebug() {
        return debug;
    }

    public void setDebug(Debug debug) {
        this.debug = debug;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
