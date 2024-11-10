package com.example.training.consumer;

import com.alibaba.fastjson.JSON;
import com.example.training.constant.MQPrefixConst;
import com.example.training.dao.ElasticsearchDao;
import com.example.training.dto.ArticleSearchDTO;
import com.example.training.dto.MaxwellDataDTO;
import com.example.training.entity.Article;
import com.example.training.util.BeanCopyUtils;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * maxwell monitor data
 * @author Xinyuan Xu
 */
@Component
@RabbitListener(queues = MQPrefixConst.MAXWELL_QUEUE)
public class MaxWellConsumer {
    @Autowired
    private ElasticsearchDao elasticsearchDao;

    @RabbitHandler
    public void process(byte[] data) {
        // get monitoring information
        MaxwellDataDTO maxwellDataDTO = JSON.parseObject(new String(data), MaxwellDataDTO.class);
        // get article data
        Article article = JSON.parseObject(JSON.toJSONString(maxwellDataDTO.getData()), Article.class);
        // define operation type
        switch (maxwellDataDTO.getType()) {
            case "insert":
            case "update":
                // update es article
                elasticsearchDao.save(BeanCopyUtils.copyObject(article, ArticleSearchDTO.class));
                break;
            case "delete":
                // delete article
                elasticsearchDao.deleteById(article.getId());
                break;
            default:
                break;
        }
    }
}
