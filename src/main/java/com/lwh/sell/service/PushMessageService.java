package com.lwh.sell.service;

import com.lwh.sell.dto.OrderDTO;

/**
 * @author lwh
 * @date 2020-02-02
 * @desp
 */
public interface PushMessageService {

    /**
     * 订单状态消息变更
     */
    void orderStatus(OrderDTO orderDTO);
}
