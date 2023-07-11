package com.power.doc.constants;

import java.io.File;

/**
 * @author zhangzhongguo <zhangzhongguo@kuaishou.com>
 * Created on 2023-07-08
 */
public interface GRpcConstants {

    String SLASH = File.separator;

    String CMD_FORMAT = "%s --plugin=protoc-gen-doc=%s --doc_out=%s --doc_opt=json,%s.json %s" + SLASH + "*.proto --proto_path=%s";


    String EMPTY = "";

    String SPACE = " ";

    String DOCS = SLASH + "docs";

    String DOCS_JSON = DOCS + SLASH + "json";

    String PLUGIN_DIRECTORY = "/cmd/";

    String PROTO_SUFFIX = ".proto";

    String PROTOC = "PROTOC";

    String PROTOC_GEN_DOC = "PROTOC_GEN_DOC";

    String MACOS = "Mac OS X";

    String LINUX = "Linux";

}
