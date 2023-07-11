package com.power.doc.builder.rpc.proto;

import java.util.Collection;

/**
 * @author zhangzhongguo <zhangzhongguo@kuaishou.com>
 * Created on 2023-07-09
 */
public class Proto {
    private Collection<ProtoFile> files;

    public Collection<ProtoFile> getFiles() {
        return files;
    }

    public void setFiles(Collection<ProtoFile> files) {
        this.files = files;
    }
}
