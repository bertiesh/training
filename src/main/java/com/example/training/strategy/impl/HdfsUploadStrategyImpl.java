package com.example.training.strategy.impl;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Xuxinyuan
 */
@Service("hdfsUploadStrategyImpl")
public class HdfsUploadStrategyImpl extends AbstractUploadStrategyImpl{
    /**
     * hdfs路径
     */
    @Value("${upload.hdfs.path}")
    private String hdfsPath;

    /**
     * 访问url
     */
    @Value("${upload.hdfs.url}")
    private String hdfsUrl;

    @Override
    public Boolean exists(String filePath) {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", hdfsUrl);
        conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");

        try(FileSystem fs = FileSystem.get(conf)) {
            return fs.exists(new Path(hdfsPath + filePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void upload(String path, String fileName, InputStream inputStream) {
        // 判断目录是否存在
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", hdfsUrl);
        conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");

        try(FileSystem fs = FileSystem.get(conf)) {
            FSDataOutputStream outputStream = fs.create(new Path(hdfsPath + path + fileName));
            // 写入文件
            IOUtils.copyBytes(inputStream, outputStream, 2048, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public String getFileAccessUrl(String filePath) {
        return hdfsUrl + hdfsPath + filePath;
    }
}
