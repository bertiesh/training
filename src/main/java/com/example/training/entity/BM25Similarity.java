package com.example.training.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Xuxinyuan
 */
public class BM25Similarity {
    int D;
    /**
     * 文档的数量
     */
    double avgDocLength;
    /**
     * 平均每篇文档的长度
     */
    Map<String, Double> idf = new HashMap<String, Double>();
    /**
     * 每个词语的逆文档频率
     */
    double k1 = (double) 1.5;
    double b = (double) 0.75;


    /**在创建对象的时候初始化相似度模型.需要基于所有语料的分词结果，计算相似度模型的参数。docWordsList为每一篇文档，分词后得到的列表。
     * 由于是一个计数过程，速度比较快,在数据量比较小的时候可以使用这种策略。当数据量比较大的时候，需要将参数保存到文件或数据库，系统启动时加载即可。
     */
    public BM25Similarity(Map<Object, List<String>> docWordsList) {
        this.D = docWordsList.size();
        //初始化文档长度
        this.avgDocLength = getAvgDocLength(docWordsList);
        //初始化语料库中文档的平均长度

        //统计词频和文档频率
        List<HashMap<String, Double>> freqMapList = new ArrayList<HashMap<String, Double>>();
        //每文档中，各个词语出现的次数
        Map<String, Double> docFreqMap = new HashMap<String, Double>();
        //每个词语词语的文档频率
        for (List<String> wordsList : docWordsList.values()) {
            HashMap<String, Double> termFreq = this.getTermFrequecy(wordsList);
            //统计词频
            freqMapList.add(termFreq);
            for (String word : termFreq.keySet()) {
                //统计文档频率
                if (!docFreqMap.containsKey(word)) {
                    docFreqMap.put(word, (double) 1);
                } else {
                    docFreqMap.put(word, docFreqMap.get(word) + 1);
                }

            }
        }


//		基于每个文档的词语列表，计算模型参数
        for(String word: docFreqMap.keySet()) {
            //math.log(self.D-v+0.5)-math.log(v+0.5)
            double idf_value;
            if(this.D > 2* docFreqMap.get(word)) {
                //与经典的IDF计算方式有所区别
                idf_value = Math.log(this.D - docFreqMap.get(word) + 0.5) - Math.log(docFreqMap.get(word) + 0.5);
            }else {//语料库中文档较少；遇到高频词
                idf_value = Math.log(this.D + 0.5) - Math.log(docFreqMap.get(word) + 0.5);
            }
            this.idf.put(word, idf_value);
        }
    }

    /**
     * 获取一个文档的词频数据
     */
    public HashMap<String, Double> getTermFrequecy(List<String> doc){
        HashMap<String, Double> temp= new HashMap<String, Double>();
        for(String word: doc) {
            if(!temp.containsKey(word)){
                temp.put(word, (double) 1);
            }else {
                temp.put(word, temp.get(word) + 1);
            }
        }
        return temp;
    }

    /**
     * 计算一篇文档的平均长度
     */
    public float getAvgDocLength(Map<Object,List<String>> docWordsList) {
        float avgdl = 0;
        for (List<String> wordsList : docWordsList.values()) {
            avgdl += wordsList.size();
        }
        avgdl /= docWordsList.size();
        return avgdl;
    }

    /**
     * 查询一个词语的逆文档频率。如果是未登录词语，取默认值
     */
    public double getIDF(String word) {
        if(this.idf.containsKey(word)){
            return this.idf.get(word);
        }else {
            return Math.log(this.D - + 0.5) - Math.log(0.5);
            //默认值
        }
    }

    /**
     * 计算两篇文档的相似度。输入为两篇文档分词得到的词语列表
     */
    public float similarity(List<String>doc1, List<String>doc2) {
        float score = 0;
        HashMap<String, Double> termFreq2 = this.getTermFrequecy(doc2);
        int doc2Length = doc2.size();
        for(String word: doc1) {
            if(termFreq2.containsKey(word)){
                score += (this.getIDF(word)*termFreq2.get(word)*(this.k1 + 1)
                        / (termFreq2.get(word) + this.k1 * (1 - this.b + this.b * doc2Length
                        / this.avgDocLength)));
                //累积相似度分数
            }
        }
        return score;
    }
}
