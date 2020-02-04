package com.lwh.sell.controller;

import com.lly835.bestpay.model.PayResponse;
import com.lwh.sell.dto.OrderDTO;
import com.lwh.sell.enums.ResultEnum;
import com.lwh.sell.exception.SellException;
import com.lwh.sell.service.OrderService;
import com.lwh.sell.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @author lwh
 * @date 2020-02-01
 * @desp
 */
@Controller
@RequestMapping("/pay")
public class PayController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private PayService payService;

    @GetMapping("/create")
    public ModelAndView create(@RequestParam("orderId") String orderId,
                               @RequestParam("returnUrl") String returnUrl,
                               Map<String, Object> map) throws UnsupportedEncodingException {
        // 1.查询订单
        OrderDTO orderDTO = orderService.findOne(orderId);
        if(orderDTO == null){
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }

        // 2.发起支付
        PayResponse payResponse = payService.create(orderDTO);

        map.put("payResponse", payResponse);
        map.put("returnUrl", returnUrl.startsWith("http://") ? returnUrl : URLEncoder.encode(returnUrl, "utf-8"));
        return new ModelAndView("pay/create");
    }

    /**
     * 微信异步通知
     */
    @PostMapping("/notify")
    public ModelAndView notify(@RequestBody String notifyData){
        payService.notify(notifyData);

        // 微信会一直回调,这边返回给微信处理结果
        return new ModelAndView("/pay/success");
    }
}
