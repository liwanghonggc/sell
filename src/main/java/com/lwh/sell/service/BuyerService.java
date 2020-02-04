package com.lwh.sell.service;

import com.lwh.sell.dto.OrderDTO;

/**
 * @author lwh
 * @date 2020-01-31
 * @desp
 */
public interface BuyerService {

    /**
     * 查询一个订单
     */
    OrderDTO findOrderOne(String openid, String orderId);

    /**
     * 取消订单
     */
    OrderDTO cancelOrder(String openid, String orderId);
}
