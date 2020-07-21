package com.smile.atozapp.parameters;

public class UpdateParameters {

    String version_name, version_code;

    public UpdateParameters() {
    }

    public UpdateParameters(String version_name, String version_code) {
        this.version_name = version_name;
        this.version_code = version_code;
    }

    public String getVersion_name() {
        return version_name;
    }

    public void setVersion_name(String version_name) {
        this.version_name = version_name;
    }

    public String getVersion_code() {
        return version_code;
    }

    public void setVersion_code(String version_code) {
        this.version_code = version_code;
    }
}
