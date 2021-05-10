package com.offcn.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class OSSTest {
    /*
    * oss使用步骤  阿里云
    * 1.引入sdk
    * 2.配置好相应的属性
    * */
    public static void main(String[] args) throws FileNotFoundException {
        // Endpoint以北京为例，其它Region请按实际情况填写。
        String endpoint="http://oss-cn-beijing.aliyuncs.com";

        String accessKeyId = "LTAI5t6cSM18GqZMo1WDL9SA";
        String accessKeySecret = "pgC81eMfmt06YVhSd7ijqT15ePrW59";
    //创建ossclient实例
        OSS oss = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        //上传文件流
        InputStream inputStream = new FileInputStream(new File("D:\\java学习资料\\Java后端开发学习路线-高清版.jpg"));
        oss.putObject("offcn1231","pic/010.jpg",inputStream);
        //关闭ossclient
        oss.shutdown();
        System.out.println("测试完成!");
    }


}
