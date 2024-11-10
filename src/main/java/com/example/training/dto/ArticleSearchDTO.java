package com.example.training.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author Xinyuan Xu
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "article")
public class ArticleSearchDTO {
    /**
     * article id
     */
    @Id
    private Integer id;

    /**
     * title
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String articleTitle;

    /**
     * content
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String articleContent;

    /**
     * isDelete
     */
    @Field(type = FieldType.Integer)
    private Integer isDelete;

    /**
     * status
     */
    @Field(type = FieldType.Integer)
    private Integer status;
}
