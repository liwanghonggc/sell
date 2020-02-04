package com.lwh.sell.dto;

import lombok.Data;

/**
 * @author lwh
 * @date 2020-01-31
 * @desp
 */
@Data
public class CartDTO {

    private String productId;

    private Integer productQuantity;

    public CartDTO(String productId, Integer productQuantity) {
        this.productId = productId;
        this.productQuantity = productQuantity;
    }
}
