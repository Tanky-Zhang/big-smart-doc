package com.power.doc.builder.rpc.proto;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.power.doc.constants.GRpcConstants;

/**
 * @author zhangzhongguo <zhangzhongguo@kuaishou.com>
 * Created on 2023-07-09
 */
public class ProtoServiceMethod {

    private String name;
    private String description;
    private String requestType;
    private String requestLongType;
    private String requestFullType;
    private boolean requestStreaming;
    private String responseType;
    private String responseLongType;
    private String responseFullType;
    private boolean responseStreaming;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getRequestLongType() {
        return requestLongType;
    }

    public void setRequestLongType(String requestLongType) {
        this.requestLongType = requestLongType;
    }

    public String getRequestFullType() {
        return requestFullType;
    }

    public void setRequestFullType(String requestFullType) {
        this.requestFullType = requestFullType;
    }

    public boolean isRequestStreaming() {
        return requestStreaming;
    }

    public void setRequestStreaming(boolean requestStreaming) {
        this.requestStreaming = requestStreaming;
    }

    public String getResponseType() {
        return responseType;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }

    public String getResponseLongType() {
        return responseLongType;
    }

    public void setResponseLongType(String responseLongType) {
        this.responseLongType = responseLongType;
    }

    public String getResponseFullType() {
        return responseFullType;
    }

    public void setResponseFullType(String responseFullType) {
        this.responseFullType = responseFullType;
    }

    public boolean isResponseStreaming() {
        return responseStreaming;
    }

    public void setResponseStreaming(boolean responseStreaming) {
        this.responseStreaming = responseStreaming;
    }

    public String getDescription() {
        //正则表达式
        String regEx = "[\\\\/:*\"<>|]";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(description);
        return matcher.replaceAll(GRpcConstants.SPACE);
    }
}
