package com.power.doc.builder.rpc.proto;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.power.doc.constants.GRpcConstants;
import com.power.doc.utils.FileUtil;

/**
 * @author zhangzhongguo <zhangzhongguo@kuaishou.com>
 * Created on 2023-07-09
 */
public class DocProperties {

    private String classesPath;

    private String docPath;

    private String docJsonPath;


    private String protocFilePath;

    private String protocGenDocFilePath;

    /**
     * 设定目录
     */
    public void setPath(String path) throws IOException, InterruptedException {
        this.classesPath = path;
        this.docPath = path + GRpcConstants.PLUGIN_DIRECTORY;
        this.docJsonPath = path + GRpcConstants.DOCS_JSON;
        com.power.common.util.FileUtil.mkdirs(docJsonPath);
        // proto执行文件
        this.setProtocFilePath();
        //proto-gen-doc可执行文件
        this.setProtocGenDocFilePath();
    }

    public String getClassesPath() {
        return classesPath;
    }

    public String getDocJsonPath() {
        return docJsonPath;
    }


    public String getProtocFilePath() {
        return protocFilePath;
    }

    private void setProtocFilePath() throws IOException, InterruptedException {
        String filePath = GRpcConstants.PLUGIN_DIRECTORY + getPluginName(GRpcConstants.PROTOC);
        String pluginPath = copyPropertyFile(filePath, getPluginName(GRpcConstants.PROTOC));
        this.protocFilePath = docPath + File.separator + getPluginName(GRpcConstants.PROTOC);
        settingPermission(pluginPath);
    }

    public String getProtocGenDocFilePath() {
        return protocGenDocFilePath;
    }

    private void setProtocGenDocFilePath() throws IOException, InterruptedException {
        String filePath = GRpcConstants.PLUGIN_DIRECTORY + getPluginName(GRpcConstants.PROTOC_GEN_DOC);
        String pluginPath = copyPropertyFile(filePath, getPluginName(GRpcConstants.PROTOC_GEN_DOC));
        this.protocGenDocFilePath = docPath + File.separator + getPluginName(GRpcConstants.PROTOC_GEN_DOC);
        System.out.println("pluginPath------" + pluginPath);
        settingPermission(pluginPath);
    }

    private void settingPermission(String pluginPath) throws IOException, InterruptedException {
        //        if (!getSystem().contains(GRpcConstants.LINUX)) {
        //            return;
        //        }
        Runtime runtime = Runtime.getRuntime();
        String command = "chmod 770 " + pluginPath;
        Process process = runtime.exec(command);
        process.waitFor();
        process.exitValue();
    }

    /**
     * jar包中的可执行文件需要拷贝出来执行
     */
    private String copyPropertyFile(String filePath, String fileName) throws IOException {
        final InputStream inputStream = DocProperties.class.getResourceAsStream(filePath);
        if (null == inputStream) {
            throw new IOException(String.format("MessageStr.ERROR_PROPERTIES.getMessage()", filePath));
        }
        File file = new File(this.docPath + File.separator + fileName);
        FileUtil.copyInputStreamToFile(inputStream, file);
        return file.getAbsolutePath();
    }


    /**
     * 获取protoc的可执行文件
     */
    private String getPluginName(String pluginName) throws IOException {
        String osName = getSystem();
        Map<String, Map<String, String>> pluginsMap = new HashMap<>();
        HashMap<String, String> windowsPluginMap = new HashMap<>();
        windowsPluginMap.put(GRpcConstants.PROTOC, "protoc-23.4-osx-aarch_64");
        windowsPluginMap.put(GRpcConstants.PROTOC_GEN_DOC, "protoc-gen-doc_1.5.1_darwin_arm64");
        HashMap<String, String> linuxPluginMap = new HashMap<>();
        linuxPluginMap.put(GRpcConstants.PROTOC, "protoc-3.19.3-linux-x86_64");
        linuxPluginMap.put(GRpcConstants.PROTOC_GEN_DOC, "protoc-gen-doc_1.5.1_linux_amd64");
        pluginsMap.put(GRpcConstants.MACOS, windowsPluginMap);
        pluginsMap.put(GRpcConstants.LINUX, linuxPluginMap);
        String systemKey = pluginsMap.keySet()
                .stream()
                .filter(osName::contains)
                .findFirst()
                .orElseThrow(
                        () -> new IOException(String.format("MessageStr.NONSUPPORT_SYSTEM.getMessage()---%s", osName)));
        Map<String, String> pluginMap = pluginsMap.get(systemKey);
        return pluginMap.get(pluginName);
    }

    private String getSystem() {
        return System.getProperty("os.name");
    }
}
