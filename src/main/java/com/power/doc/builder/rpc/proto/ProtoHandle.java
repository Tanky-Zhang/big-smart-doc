package com.power.doc.builder.rpc.proto;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.power.doc.utils.FileUtil;

/**
 * @author zhangzhongguo <zhangzhongguo@kuaishou.com>
 * Created on 2023-07-09
 */
public class ProtoHandle {

    //    private Map<String, ProtoEnum> enumsMap;

    private Map<String, ProtoMessage> messagesMap;

    private Collection<ProtoService> servicesCollection;

    public ProtoHandle(DocProperties docProperties) throws IOException {
        File docsFile = new File(docProperties.getDocJsonPath());
        File[] listFiles = docsFile.listFiles();
        if (null == listFiles) {
            return;
        }
        Gson gson = new Gson();
        List<ProtoFile> protoFiles = new ArrayList<>();
        for (File file : listFiles) {
            String fileContent = FileUtil.fileRead(file.getAbsolutePath());
            protoFiles.addAll(gson.fromJson(fileContent, Proto.class).getFiles());
        }
        loadEntity(protoFiles);
    }

    /**
     * 加载proto文件
     */
    private void loadEntity(List<ProtoFile> protoFiles) {
        this.servicesCollection = protoFiles.stream()
                .flatMap(val -> val.getServices().stream()
                        .filter(item -> Objects.nonNull(item.getMethods()) && !item.getMethods().isEmpty()))
                .collect(Collectors.toList());
        this.messagesMap = protoFiles.stream().flatMap(val -> val.getMessages().stream())
                .collect(Collectors.toMap(ProtoMessage::getFullName, Function.identity()));
        //        this.enumsMap = protoFiles.stream()
        //                .flatMap(val -> val.getEnums().stream())
        //                .collect(Collectors.toMap(ProtoEnum::getFullName, Function.identity()));
    }

    //    public Map<String, ProtoEnum> getEnumsMap() {
    //        return enumsMap;
    //    }

    public Map<String, ProtoMessage> getMessagesMap() {
        return messagesMap;
    }

    public Collection<ProtoService> getServicesCollection() {
        return servicesCollection;
    }
}
