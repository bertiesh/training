package com.example.training.util;

import com.alibaba.fastjson.JSON;
import com.example.training.entity.BM25Similarity;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author Xuxinyuan
 */
@Component
public class FormatUtil {
    private static String hdfsUrl = "hdfs://10.32.20.50:9001";
    /**
     * 基于HanLP类分词并去除停用词
     * @param oldString：原中文文本
     * @return 去除停用词之后的中文文本
     */
    public static List<String> RemovalOfStopWords(String oldString){
        try {
            // 中文 停用词 .txt 文件路径
            Configuration conf = new Configuration();
            conf.set("fs.defaultFS", hdfsUrl);
            conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
            conf.setBoolean("dfs.support.append", true);
            FileSystem fileSystem = FileSystem.get(conf);
            FSDataInputStream fsDataInputStream = fileSystem.open(new Path(hdfsUrl + "/hit_stopwords.txt"));
            //目标为txt文件，因此指定对应的UTF-8字符集
            InputStreamReader isr = new InputStreamReader(fsDataInputStream, StandardCharsets.UTF_8);
            //构造一个BufferedReader类来读取停用词文件
            BufferedReader bufferedReader = new BufferedReader(isr);
            List<String> stopWords = new ArrayList<>();
            String temp;
            //使用readLine方法，一次读一行 读取停用词
            while ((temp = bufferedReader.readLine()) != null) {
                stopWords.add(temp.trim());
            }
            //分词
            List<String> termStringList = new ArrayList<>(Objects.requireNonNull(toList(doHanlpApi(oldString))));
            termStringList.removeAll(stopWords);
            return termStringList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将旧字符串转化为已分词的JSON格式新字符串
     * @param text 待分词的字符串
     * @return JSON格式字符串
     */
    public static String doHanlpApi(String text) {
        //请求头中的token
        String token="c2c9f9dff1c445b08866c0e4cdb247bf1666944562918token";
        //申请的接口地址
        String url="http://comdo.hanlp.com/hanlp/v1/segment/standard";
        //所有参数
        Map<String,Object> params=new HashMap<String,Object>();
        params.put("text", text);
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            //添加header请求头，token请放在header里
            httpPost.setHeader("token", token);
            // 创建参数列表
            List<NameValuePair> paramList = new ArrayList<>();
            for (String key : params.keySet()) {
                //所有参数依次放在paramList中
                paramList.add(new BasicNameValuePair(key, (String) params.get(key)));
            }
            //模拟表单
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, "utf-8");
            httpPost.setEntity(entity);
            // 执行http请求
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");
            return resultString;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(response!=null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 将已分词的json字符串转化为list
     * @param result 待转化json字符串
     */
    public static List<String> toList(String result){
        try {
            List<String> words = new ArrayList<>();
            for (Object a: JSON.parseObject(result).getJSONArray("data")) {
                String word = (String)JSON.parseObject(a.toString()).get("word");
                words.add(word);
            }
            return words;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<String, Object> maxSimilarity(String question, Map<Object,List<String>> content){
        Map<String, Object> result = new HashMap(2);
        float max = 0;
        //最大相似度
        String id = null;
        //最大相似度问题id
        BM25Similarity model = new BM25Similarity(content);
        //相似度计算模型
        for(Object key : content.keySet()){
            //计算相似度并找到最大相似度及对应的问题id
            float similarity = model.similarity(Objects.requireNonNull(FormatUtil.RemovalOfStopWords(question)), content.get(key));
            if(max < similarity){
                max = similarity;
                id = String.valueOf(key);
            }
        }
        result.put("id", id);
        result.put("max", max);
        return result;
    }
}
