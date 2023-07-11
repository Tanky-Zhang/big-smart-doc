package com.power.doc.template;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.power.common.util.FileUtil;
import com.power.doc.builder.ProjectDocConfigBuilder;
import com.power.doc.builder.rpc.proto.DocProperties;
import com.power.doc.builder.rpc.proto.ProtoHandle;
import com.power.doc.builder.rpc.proto.ProtoMessage;
import com.power.doc.builder.rpc.proto.ProtoMessageField;
import com.power.doc.builder.rpc.proto.ProtoService;
import com.power.doc.builder.rpc.proto.ProtoServiceMethod;
import com.power.doc.constants.GRpcConstants;
import com.power.doc.model.ApiConfig;
import com.power.doc.model.ApiParam;
import com.power.doc.model.RpcJavaMethod;
import com.power.doc.model.SourceCodePath;
import com.power.doc.model.annotation.FrameworkAnnotations;
import com.power.doc.model.rpc.RpcApiDoc;
import com.power.doc.utils.ApiParamTreeUtil;

/**
 * @author zhangzhongguo <zhangzhongguo@kuaishou.com>
 * Created on 2023-07-09
 */
public class GRpcDocBuildTemplate implements IDocBuildTemplate<RpcApiDoc>, IRpcDocTemplate {


    private DocProperties docProperties;

    /**
     * this is core method,parse api
     */
    @Override
    public List<RpcApiDoc> getApiData(ProjectDocConfigBuilder projectBuilder) {
        //拿到plugin中生成的config
        ApiConfig apiConfig = projectBuilder.getApiConfig();
        List<RpcApiDoc> docs = new ArrayList<>();
        try {
            generateJson(apiConfig);
        } catch (Exception e) {
            e.printStackTrace();
            return docs;
        }
        try {
            ProtoHandle handle = new ProtoHandle(docProperties);
            Collection<ProtoService> servicesCollection = handle.getServicesCollection();
            //以服务为纬度填充RpcApiDoc
            int order = 1;
            for (ProtoService protoService : servicesCollection) {
                RpcApiDoc rpcApiDoc = new RpcApiDoc();
                rpcApiDoc.setProtocol("gRpc");
                rpcApiDoc.setOrder(order++);
                rpcApiDoc.setName(protoService.getFullName());
                rpcApiDoc.setShortName(protoService.getName());
                rpcApiDoc.setTitle(protoService.getDescription());
                rpcApiDoc.setDesc(protoService.getDescription());
                rpcApiDoc.setUri("gRpc://" + protoService.getFullName());
                rpcApiDoc.setAuthor("smart-doc");
                rpcApiDoc.setVersion("1.0.0");
                List<RpcJavaMethod> methodList = new ArrayList<>();
                Collection<ProtoServiceMethod> methods = protoService.getMethods();
                int i = 0;
                for (ProtoServiceMethod method : methods) {
                    //rpcApiDoc.set
                    RpcJavaMethod rpcJavaMethod = new RpcJavaMethod();
                    rpcJavaMethod.setOrder(++i);
                    rpcJavaMethod.setName(method.getName());
                    rpcJavaMethod.setDesc(method.getDescription());
                    rpcJavaMethod.setDetail(method.getDescription());
                    rpcJavaMethod.setEscapeMethodDefinition(
                            method.getResponseType() + "  " + method.getName() + "(" + method.getRequestType() + ")");
                    rpcJavaMethod.setReturnClassInfo(method.getResponseFullType());
                    //包装请求参数
                    ProtoMessage protoMessage = handle.getMessagesMap().get(method.getRequestFullType());
                    List<ApiParam> requestParams = new ArrayList<>();
                    parseApiParam(handle, protoMessage.getFields(), requestParams, 0);
                    //包装返回参数
                    ProtoMessage responseMessage = handle.getMessagesMap().get(method.getResponseFullType());
                    List<ApiParam> responseParams = new ArrayList<>();
                    parseApiParam(handle, responseMessage.getFields(), responseParams, 0);

                    if (apiConfig.isParamsDataToTree()) {
                        ApiParamTreeUtil.rpcApiParamToTree(requestParams);
                        ApiParamTreeUtil.rpcApiParamToTree(responseParams);
                    }
                    rpcJavaMethod.setRequestParams(requestParams);
                    rpcJavaMethod.setResponseParams(responseParams);
                    methodList.add(rpcJavaMethod);
                }
                rpcApiDoc.setList(methodList);
                docs.add(rpcApiDoc);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return docs;
    }

    /**
     * 递归解析请求参数实体
     */
    private static void parseApiParam(ProtoHandle handle, Collection<ProtoMessageField> fields, List<ApiParam> params,
            int level) {
        for (ProtoMessageField field : fields) {
            ApiParam param1 = new ApiParam();
            param1.setDesc(field.getDescription());
            param1.setField(field.getName());
            param1.setRequired(!"optional".equals(field.getLabel()) || "required".equals(field.getLabel()) || "".equals(
                    field.getLabel()));
            param1.setClassName(field.getFullType());
            param1.setPid(level - 1);
            param1.setId(level);
            param1.setType(field.getType());
            params.add(param1);
            ProtoMessage message = handle.getMessagesMap().getOrDefault(field.getFullType(), null);
            if (message != null) {
                parseApiParam(handle, message.getFields(), params, level + 1);
            }
        }
    }

    public boolean ignoreReturnObject(String typeName, List<String> ignoreParams) {
        return false;
    }

    @Override
    public FrameworkAnnotations registeredAnnotations() {
        return null;
    }

    /**
     * 使用proto-gen-doc生成json
     */
    private void generateJson(ApiConfig config) throws Exception {

        String outPath = config.getOutPath();
        List<SourceCodePath> sourceCodePaths = config.getSourceCodePaths();
        Set<String> paths = new HashSet<>();

        //加载文件目录
        for (SourceCodePath sourceCodePath : sourceCodePaths) {
            File souceFile = new File(sourceCodePath.getPath());
            loadProtobufPaths(new File[] {souceFile}, paths);
        }
        if (paths.isEmpty()) {
            return;
        }
        //创建输入目录
        FileUtil.mkdirs(outPath);

        String sourcePath = System.getProperty("user.dir") + "/target/classes";

        System.out.println("sourcePath-------------------------" + sourcePath);

        docProperties = new DocProperties();
        docProperties.setPath(sourcePath);

        System.out.println("protocPath------------------" + docProperties.getProtocFilePath());
        System.out.println("protocGenDocFilePath------------------" + docProperties.getProtocGenDocFilePath());

        //遍历执行proto文件解析
        for (String path : paths) {
            //生成json的文件名字
            String folderName = path.replace(docProperties.getClassesPath() + GRpcConstants.SLASH,
                    GRpcConstants.EMPTY).replace(GRpcConstants.SLASH, "_");

            //执行protoc文档生成到本地
            String cmd = String.format(GRpcConstants.CMD_FORMAT, docProperties.getProtocFilePath(),
                    docProperties.getProtocGenDocFilePath(), docProperties.getDocJsonPath(), folderName, path, path);

            System.out.println("cmd---------" + cmd);

            //执行命令
            ProcessBuilder builder = new ProcessBuilder("/bin/sh", "-c", cmd);
            Process pro = builder.start();
            pro.waitFor();
        }

    }


    /**
     * 递归寻找包含.proto的文件
     */
    private static void loadProtobufPaths(File[] files, Set<String> outPaths) {
        for (File file : files) {
            if (file.isDirectory()) {
                loadProtobufPaths(Objects.requireNonNull(file.listFiles()), outPaths);
            }
            //不找target目录下的文件，否则可能会找到两遍
            if (file.getName().endsWith(GRpcConstants.PROTO_SUFFIX) && !file.getPath().contains("/target/classes")) {
                outPaths.add(file.getParent());
            }
        }
    }
}
