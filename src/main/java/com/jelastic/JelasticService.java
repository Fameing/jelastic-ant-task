package com.jelastic;

import com.jelastic.model.*;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Igor Yova
 * Date: 6/17/11
 * Time: 5:27 PM
 */
public class JelasticService {
    private String shema = "http";
    private String apiJelastic = "api.jelastic.com";

    private int port = -1;
    private String version = "1.0";

    private CookieStore cookieStore = null;
    private String urlAuthentication = "/" + version + "/users/authentication/rest/signin";
    private String urlUploader = "/" + version + "/storage/uploader/rest/upload";
    private String urlCreateObject = "/" + version + "/data/base/rest/createobject";
    private String urlDeploy = /*"/" + version + "*/"/deploy/DeployArchive";
    private static ObjectMapper mapper = new ObjectMapper();
    private Project project;


    public String getApiJelastic() {
        return apiJelastic;
    }

    public void setApiJelastic(String apiJelastic) {
        this.apiJelastic = apiJelastic;
    }

    public String getUrlDeploy() {
        return urlDeploy;
    }

    public void setUrlDeploy(String urlDeploy) {
        this.urlDeploy = urlDeploy;
    }

    public JelasticService(Project project) {
        this.project = project;
    }

    public void setProject(Project proj) {
        project = proj;
    }

    public String getShema() {
        return shema;
    }

    public int getPort() {
        return port;
    }

    public CookieStore getCookieStore() {
        return cookieStore;
    }

    public String getUrlAuthentication() {
        return urlAuthentication;
    }

    public String getUrlUploader() {
        return urlUploader;
    }

    public String getUrlCreateObject() {
        return urlCreateObject;
    }

    public Authentication authentication(String email, String password) {
        Authentication authentication = null;

        try {
            DefaultHttpClient httpclient = new DefaultHttpClient();

            List<NameValuePair> qparams = new ArrayList<NameValuePair>();
            qparams.add(new BasicNameValuePair("login", email));
            qparams.add(new BasicNameValuePair("password", password));
            URI uri = URIUtils.createURI(getShema(), getApiJelastic(), getPort(), getUrlAuthentication(), URLEncodedUtils.format(qparams, "UTF-8"), null);
            project.log(uri.toString(),Project.MSG_DEBUG);
            HttpGet httpGet = new HttpGet(uri);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseBody = httpclient.execute(httpGet, responseHandler);
            project.log(responseBody,Project.MSG_DEBUG);
            authentication = mapper.readValue(responseBody, Authentication.class);
            cookieStore = httpclient.getCookieStore();
        } catch (URISyntaxException e) {
            project.log(e.getMessage());
        } catch (ClientProtocolException e) {
            project.log(e.getMessage());
        } catch (IOException e) {
            project.log(e.getMessage());
        }
        return authentication;
    }

    public UpLoader upload(String filename, String dir, Authentication authentication) {
        UpLoader upLoader = null;
        try {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            httpclient.setCookieStore(getCookieStore());

            File file = new File(dir + File.separator + filename);
            if (!file.exists()) {
                throw new BuildException("First build artifact and try again. Artifact not found in : " + dir + File.separator + filename);
            }

            MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            multipartEntity.addPart("fid", new StringBody("123456"));
            multipartEntity.addPart("session", new StringBody(authentication.getSession()));
            multipartEntity.addPart("file", new FileBody(file));


            URI uri = URIUtils.createURI(getShema(), getApiJelastic(), getPort(), getUrlUploader(), null, null);
            project.log(uri.toString(),Project.MSG_DEBUG);
            HttpPost httpPost = new HttpPost(uri);
            httpPost.setEntity(multipartEntity);

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseBody = httpclient.execute(httpPost, responseHandler);
            project.log(responseBody,Project.MSG_DEBUG);
            upLoader = mapper.readValue(responseBody, UpLoader.class);
        } catch (URISyntaxException e) {
            project.log(e.getMessage());
        } catch (ClientProtocolException e) {
            project.log(e.getMessage());
        } catch (IOException e) {
            project.log(e.getMessage());
        }
        return upLoader;
    }

    public CreateObject createObject(String filename, UpLoader upLoader, Authentication authentication) {
        CreateObject createObject = null;
        try {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            httpclient.setCookieStore(getCookieStore());

            List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
            nameValuePairList.add(new BasicNameValuePair("charset", "UTF-8"));
            nameValuePairList.add(new BasicNameValuePair("session", authentication.getSession()));
            nameValuePairList.add(new BasicNameValuePair("type", "JDeploy"));
            nameValuePairList.add(new BasicNameValuePair("data", "{'name':'" + filename + "', 'archive':'" + upLoader.getFile() + "', 'link':0, 'size':" + upLoader.getSize() + ", 'comment':'" + filename + "'}"));

            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairList, "UTF-8");
            URI uri = URIUtils.createURI(getShema(), getApiJelastic(), getPort(), getUrlCreateObject(), null, null);
            project.log(uri.toString(), Project.MSG_DEBUG);
            HttpPost httpPost = new HttpPost(uri);
            httpPost.setEntity(entity);

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseBody = httpclient.execute(httpPost, responseHandler);
            project.log(responseBody, Project.MSG_DEBUG);
            createObject = mapper.readValue(responseBody, CreateObject.class);
        } catch (URISyntaxException e) {
            project.log(e.getMessage());
        } catch (ClientProtocolException e) {
            project.log(e.getMessage());
        } catch (IOException e) {
            project.log(e.getMessage());
        }
        return createObject;
    }

    public Deploy deploy(String context, String environment, Authentication authentication, UpLoader upLoader, CreateObject createObject) {
        Deploy deploy = null;

        try {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            httpclient.setCookieStore(getCookieStore());

            List<NameValuePair> qparams = new ArrayList<NameValuePair>();
            qparams.add(new BasicNameValuePair("charset", "UTF-8"));
            qparams.add(new BasicNameValuePair("session", authentication.getSession()));
            qparams.add(new BasicNameValuePair("archiveUri", upLoader.getFile()));
            qparams.add(new BasicNameValuePair("archiveName", upLoader.getName()));
            qparams.add(new BasicNameValuePair("newContext", context));
            qparams.add(new BasicNameValuePair("domain", environment));

            URI uri = URIUtils.createURI(getShema(), getApiJelastic(), getPort(), getUrlDeploy(), URLEncodedUtils.format(qparams, "UTF-8"), null);
            project.log(uri.toString(), Project.MSG_DEBUG);
            HttpGet httpPost = new HttpGet(uri);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseBody = httpclient.execute(httpPost, responseHandler);
            project.log(responseBody, Project.MSG_DEBUG);
            deploy = mapper.readValue(responseBody, Deploy.class);
        } catch (URISyntaxException e) {
            project.log(e.getMessage());
        } catch (ClientProtocolException e) {
            project.log(e.getMessage());
        } catch (IOException e) {
            project.log(e.getMessage());
        }
        return deploy;
    }
}
