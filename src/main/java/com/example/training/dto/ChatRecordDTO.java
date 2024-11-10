package com.example.training.dto;

import com.example.training.entity.ChatRecord;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Xinyuan Xu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRecordDTO {
    /**
     * chat history
     */
    private List<ChatRecord> chatRecordList;

    /**
     * ipAddress
     */
    private String ipAddress;

    /**
     * ipSource
     */
    private String ipSource;
}
