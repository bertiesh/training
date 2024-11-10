package com.example.training.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author Xinyuan Xu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MaxwellDataDTO {
    /**
     * database
     */
    private String database;

    /**
     * xid
     */
    private Integer xid;

    /**
     * data
     */
    private Map<String, Object> data;

    /**
     * commit or not
     */
    private Boolean commit;

    /**
     * type
     */
    private String type;

    /**
     * table
     */
    private String table;

    /**
     * ts
     */
    private Integer ts;
}
