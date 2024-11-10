package com.example.training.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Xinyuan Xu
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvitationDTO {
    /**
     * id
     */
    private Integer id;

    /**
     * user invitationCode
     */
    private String invitationCode;
}
