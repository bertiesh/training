package com.example.training.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Xinyuan Xu
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    /**
     * id
     */
    private Integer id;

    /**
     * orderCode
     */
    private String orderCode;

    /**
     * order items
     */
    List<OrderItemDTO> orderItemList;

    /**
     * address id
     */
    private Integer addressId;

    /**
     * user id
     */
    private Integer userId;

    /**
     * address
     */
    private UserAddressDTO userAddressDTO;

    /**
     * totalPoints
     */
    private Integer totalPoints;

    /**
     * order status 1Awaiting shipment 2 Awaiting receipt 3 Completed 4 Closed
     */
    private Integer status;

    /**
     * freight id
     */
    private String freightIds;

    /**
     * freights
     */
    private List<FreightDTO> freights;

    /**
     * remarks
     */
    private String remarks;

    /**
     * createTime
     */
    private LocalDateTime createTime;

    /**
     * endTime
     */
    private LocalDateTime endTime;
}
