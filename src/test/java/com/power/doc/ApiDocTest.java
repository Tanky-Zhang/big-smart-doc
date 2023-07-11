package com.power.doc;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

import org.junit.jupiter.api.Test;

import com.power.common.util.DateTimeUtil;
import com.power.doc.builder.rpc.grpc.GRpcHtmlBuilder;
import com.power.doc.builder.rpc.proto.DocProperties;
import com.power.doc.constants.GRpcConstants;
import com.power.doc.model.ApiConfig;
import com.power.doc.model.SourceCodePath;
import com.power.doc.utils.FileUtil;

/**
 * Description:
 * ApiDoc测试
 *
 * @author yu 2018/06/11.
 */
public class ApiDocTest {

    /**
     * 包括设置请求头，缺失注释的字段批量在文档生成期使用定义好的注释
     */
    @Test
    public void testBuilderControllersApi() {
        ApiConfig config = new ApiConfig();
        config.setServerUrl("http://127.0.0.1:8899");
        // config.setStrict(true);
        config.setOpenUrl("http://localhost:7700/api");
        config.setAppToken("be4211613a734b45888c075741680e49");
        // config.setAppToken("7b0935531d1144e58a86d7b4f2ad23c6");

        config.setDebugEnvName("测试环境");
        config.setInlineEnum(true);
        config.setStyle("randomLight");
        config.setCreateDebugPage(true);
        config.setFramework("gRpc");
        // config.setAuthor("test");
        config.setDebugEnvUrl("http://127.0.0.1");
        // config.setTornaDebug(true);
        config.setAllInOne(false);
        config.setCoverOld(true);
        config.setParamsDataToTree(true);
        config.setOutPath("/Users/zhangzhongguo/Desktop/tanky/codes/smart-doc/src/main/resources/smart");
        // config.setMd5EncryptedHtmlName(true);
        //        config.setFramework(FrameworkEnum.SPRING.getFramework());
        // 不指定SourcePaths默认加载代码为项目src/main/java下的
        config.setSourceCodePaths(
                SourceCodePath.builder().setDesc("本项目代码")
                        .setPath("/Users/zhangzhongguo/Desktop/tanky/codes/smart-doc/src/main/resources/proto")
        );
        //        config.setPackageFilters("com.example.demo.controller.*");
        //        config.setJarSourcePaths(SourceCodePath.builder()
        //                .setPath("D:\\xxxx-sources.jar")
        //        );

        //        config.setRpcApiDependencies();

        long start = System.currentTimeMillis();
        //        HtmlApiDocBuilder.buildApiDoc(config);
        GRpcHtmlBuilder.buildApiDoc(config);
        long end = System.currentTimeMillis();
        DateTimeUtil.printRunTime(end, start);
    }


    @Test
    public void Test() throws Exception {
        //        System.out.println(DocProperties.class.getResource(""));
        //        System.out.println(System.getProperty("user.dir"));

        ///Users/zhangzhongguo/Desktop/tanky/codes/smart-doc/target/classes/cmd/protoc-23.4-osx-aarch_64
        // --plugin=protoc-gen-doc=/Users/zhangzhongguo/Desktop/tanky/codes/smart-doc/target/classes/cmd/protoc-gen
        // -doc_1.5.1_darwin_arm64 --doc_out=/Users/zhangzhongguo/Desktop/tanky/codes/smart-doc/target/classes/docs
        // /json --doc_opt=json,_Users_zhangzhongguo_Desktop_tanky_codes_smart-doc_target_classes_proto.json
        // /Users/zhangzhongguo/Desktop/tanky/codes/smart-doc/target/classes/proto/*.proto
        // --proto_path=/Users/zhangzhongguo/Desktop/tanky/codes/smart-doc/target/classes/proto
        Runtime runtime = Runtime.getRuntime();
        //        runtime.exec()
        //        Process exec = runtime.exec(
        //                "chmod 777 /Users/zhangzhongguo/Desktop/tanky/codes/smart-doc/target/classes/cmd/protoc-23
        //                .4-osx-aarch_64");
        //        exec.waitFor();
        //
        //        Process exec1 = runtime.exec(
        //                "chmod 777 /Users/zhangzhongguo/Desktop/tanky/codes/smart-doc/target/classes/cmd/protoc-gen
        //                -doc_1.5.1_darwin_arm64");
        //        exec1.waitFor();

                Process process = runtime.exec("chmod 770 /Users/zhangzhongguo/Desktop/tanky/codes/spring-boot-maven-multiple-module/dubbo-provider/target/classes/cmd//protoc-gen-doc_1.5.1_darwin_arm64");
//                Process process = runtime.exec("/Users/zhangzhongguo/Desktop/tanky/codes/smart-doc/target/classes
//                /cmd//protoc-23.4-osx-aarch_64 --plugin=protoc-gen-doc=/Users/zhangzhongguo/Desktop/tanky/codes
//                /smart-doc/target/classes/cmd//protoc-gen-doc_1.5.1_darwin_arm64
//                --doc_out=/Users/zhangzhongguo/Desktop/tanky/codes/smart-doc/target/classes/docs/json
//                --doc_opt=json,_Users_zhangzhongguo_Desktop_tanky_codes_smart-doc_target_classes_proto.json
//                /Users/zhangzhongguo/Desktop/tanky/codes/smart-doc/target/classes/proto/*.proto
//                --proto_path=/Users/zhangzhongguo/Desktop/tanky/codes/smart-doc/target/classes/proto");

        //        FileInputStream errorStream = (FileInputStream)process.getErrorStream();
                InputStreamReader isr = new InputStreamReader(process.getErrorStream(),"gbk");//读取
                System.out.println(isr.getEncoding());
                BufferedReader bufr = new BufferedReader(isr);//缓冲
                String line = null;
                while((line =bufr.readLine())!=null) {
                    System.out.println(line);
                }
                isr.close();
                // waitFor 阻塞等待 异步进程结束，并返回执行状态，0代表命令执行正常结束。
                System.out.println(process.waitFor());

//        ProcessBuilder builder = new ProcessBuilder("/bin/sh", "-c",
//                "/Users/zhangzhongguo/Desktop/tanky/codes/smart-doc/target/classes/cmd//protoc-23.4-osx-aarch_64 "
//                        + "--plugin=protoc-gen-doc=/Users/zhangzhongguo/Desktop/tanky/codes/smart-doc/target/classes"
//                        + "/cmd//protoc-gen-doc_1.5.1_darwin_arm64 "
//                        + "--doc_out=/Users/zhangzhongguo/Desktop/tanky/codes/smart-doc/target/classes/docs/json "
//                        + "--doc_opt=json,_Users_zhangzhongguo_Desktop_tanky_codes_smart"
//                        + "-doc_src_main_java_com_power_doc_proto.json "
//                        + "/Users/zhangzhongguo/Desktop/tanky/codes/smart-doc/src/main/java/com/power/doc/proto/*"
//                        + ".proto --proto_path=/Users/zhangzhongguo/Desktop/tanky/codes/smart-doc/src/main/java/com"
//                        + "/power/doc/proto");
//        int runningStatus = 0;
//        Process pro = builder.start();
//        pro.waitFor();
        //        try {
        //            Process pro=builder.start();
        //            System.out.println("the shell script running");
        //            try {
        //                runningStatus=pro.waitFor();
        //            } catch (InterruptedException e) {
        //                // TODO Auto-generated catch block
        //                e.printStackTrace();
        //            }
        //        } catch (IOException e) {
        //            // TODO Auto-generated catch block
        //            e.printStackTrace();
        //        }
        //
        //        if(runningStatus!=0){
        //            System.out.println("脚本执行失败");
        //        }else{
        //            System.out.println("脚本执行成功");
        //        }
        //
        //        System.out.println("11111111111");

    }

    @Test
    public void test3() {
        System.out.println(
                Objects.requireNonNull(DocProperties.class.getResource(GRpcConstants.PLUGIN_DIRECTORY)).getPath());
    }


    @Test
    public void copy() throws IOException {

        copyPropertyFile("/cmd/protoc-23.4-osx-aarch_64");

    }

    private void copyPropertyFile(String filePath) throws IOException {
        System.out.println(DocProperties.class.getResource(filePath).getPath());
        InputStream inputStream = DocProperties.class.getResourceAsStream(filePath);
        if (null == inputStream) {
            throw new IOException(String.format("MessageStr.ERROR_PROPERTIES.getMessage()", filePath));
        }
        File file = new File(
                "/Users/zhangzhongguo/Desktop/tanky/codes/smart-doc/target/classes/cmd2/protoc-23.4-osx-aarch_64");
        FileUtil.copyInputStreamToFile(inputStream, file);
    }

}
