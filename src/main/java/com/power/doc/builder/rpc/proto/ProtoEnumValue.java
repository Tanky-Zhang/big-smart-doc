package com.power.doc.builder.rpc.proto;

/**
 * @author zhangzhongguo <zhangzhongguo@kuaishou.com>
 * Created on 2023-07-09
 */
public class ProtoEnumValue {
    private String name;
    private String number;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDescription() {
        return description.replace("\n", " ");
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
