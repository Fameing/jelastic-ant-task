package com.jelastic;

import com.jelastic.model.Authentication;
import com.jelastic.model.CreateObject;
import com.jelastic.model.Deploy;
import com.jelastic.model.UpLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class Jelastic extends Task {
    String email;
    String password;
    String context;
    String environment;

    public String dir;
    public String fileName;

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDir() {
        return dir;
    }

    public String getFileName() {
        return fileName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getContext() {
        return context;
    }

    public String getEnvironment() {
        return environment;
    }

    @Override
    public void execute() throws BuildException {
        JelasticService jelasticService = new JelasticService(getProject());

        Authentication authentication = jelasticService.authentication(getEmail(), getPassword());
        if (authentication.getResult() == 0) {
            log("------------------------------------------------------------------------");
            log("   Authentication : SUCCESS");
            log("          Session : " + authentication.getSession());
            log("              Uid : " + authentication.getUid());
            log("------------------------------------------------------------------------");
            UpLoader upLoader = jelasticService.upload(getFileName(), getDir(), authentication);
            if (upLoader.getResult() == 0) {
                log("      File UpLoad : SUCCESS");
                log("         File URL : " + upLoader.getFile());
                log("        File size : " + upLoader.getSize());
                log("------------------------------------------------------------------------");
                CreateObject createObject = jelasticService.createObject(getFileName(), upLoader, authentication);
                if (createObject.getResult() == 0) {
                    log("File registration : SUCCESS");
                    log("  Registration ID : " + createObject.getObject().getId());
                    log("     Developer ID : " + createObject.getObject().getDeveloper());
                    log("------------------------------------------------------------------------");
                    Deploy deploy = jelasticService.deploy(getContext(), getEnvironment(), authentication, upLoader, createObject);
                    if (deploy.getResult() == 0) {
                        log("      Deploy file : SUCCESS");
                        log("       Deploy log :");
                        log(deploy.getResponse().getOut());
                    } else {
                        log("          Deploy : FAILED");
                        log("           Error : " + deploy.getError());
                    }
                }
            } else {
                log("File upload : FAILED");
                log("      Error : " + upLoader.getError());
            }
        } else {
            log("Authentication : FAILED");
            log("         Error : " + authentication.getError());
        }


    }
}
