package com.power.doc.builder.rpc.grpc;

import static com.power.doc.constants.DocGlobalConstants.ALL_IN_ONE_CSS;
import static com.power.doc.constants.DocGlobalConstants.ALL_IN_ONE_CSS_OUT;
import static com.power.doc.constants.DocGlobalConstants.FILE_SEPARATOR;
import static com.power.doc.constants.DocGlobalConstants.FONT_STYLE;
import static com.power.doc.constants.DocGlobalConstants.JQUERY;
import static com.power.doc.constants.DocGlobalConstants.RPC_ALL_IN_ONE_HTML_TPL;
import static com.power.doc.constants.DocGlobalConstants.RPC_ALL_IN_ONE_SEARCH_TPL;

import java.util.List;
import java.util.logging.Logger;

import org.beetl.core.Template;

import com.power.common.util.FileUtil;
import com.power.doc.builder.BaseDocBuilderTemplate;
import com.power.doc.builder.rpc.RpcDocBuilderTemplate;
import com.power.doc.helper.JavaProjectBuilderHelper;
import com.power.doc.model.ApiConfig;
import com.power.doc.model.rpc.RpcApiDoc;
import com.power.doc.utils.BeetlTemplateUtil;
import com.thoughtworks.qdox.JavaProjectBuilder;

/**
 * @author zhangzhongguo <zhangzhongguo@kuaishou.com>
 * Created on 2023-07-09
 */
public class GRpcHtmlBuilder {

    private static final Logger log = Logger.getLogger(GRpcHtmlBuilder.class.getName());

    /**
     * build controller api
     *
     * @param config config
     */
    public static void buildApiDoc(ApiConfig config) {
        //树状展示
        config.setParamsDataToTree(true);
        JavaProjectBuilder javaProjectBuilder = JavaProjectBuilderHelper.create();
        buildApiDoc(config, javaProjectBuilder);
    }

    /**
     * Only for smart-doc maven plugin and gradle plugin.
     *
     * @param config ApiConfig
     * @param javaProjectBuilder ProjectDocConfigBuilder
     */
    public static void buildApiDoc(ApiConfig config, JavaProjectBuilder javaProjectBuilder) {
        //树状展示
        config.setParamsDataToTree(true);
        RpcDocBuilderTemplate builderTemplate = new RpcDocBuilderTemplate();
        //生成接口列表的核心方法-----------------
        List<RpcApiDoc> apiDocList = builderTemplate.getRpcApiDoc(config, javaProjectBuilder);
        //------------------------------------
        Template indexCssTemplate = BeetlTemplateUtil.getByName(ALL_IN_ONE_CSS);
        FileUtil.nioWriteFile(indexCssTemplate.render(), config.getOutPath() + FILE_SEPARATOR + ALL_IN_ONE_CSS_OUT);
        BaseDocBuilderTemplate.copyJarFile("css/" + FONT_STYLE, config.getOutPath() + FILE_SEPARATOR + FONT_STYLE);
        BaseDocBuilderTemplate.copyJarFile("js/" + JQUERY, config.getOutPath() + FILE_SEPARATOR + JQUERY);
        String INDEX_HTML = "rpc-index.html";
        builderTemplate.buildAllInOne(apiDocList, config, javaProjectBuilder, RPC_ALL_IN_ONE_HTML_TPL, INDEX_HTML);
        String SEARCH_JS = "search.js";
        builderTemplate.buildSearchJs(apiDocList, config, javaProjectBuilder, RPC_ALL_IN_ONE_SEARCH_TPL, SEARCH_JS);

    }


}
