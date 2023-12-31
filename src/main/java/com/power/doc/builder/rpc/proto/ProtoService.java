package com.power.doc.builder.rpc.proto;

import java.util.Collection;

/**
 * @author zhangzhongguo <zhangzhongguo@kuaishou.com>
 * Created on 2023-07-09
 */
public class ProtoService {
    private String name;
    private String longName;
    private String fullName;
    private String description;
    private Collection<ProtoServiceMethod> methods;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDescription() {
        return description.replace("\n", " ");
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Collection<ProtoServiceMethod> getMethods() {
        return methods;
    }

    public void setMethods(Collection<ProtoServiceMethod> methods) {
        this.methods = methods;
    }
}
