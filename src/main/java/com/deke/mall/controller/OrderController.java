package com.deke.mall.controller;

import com.deke.mall.core.entity.Payload;
import com.deke.mall.entity.ProductOrder;
import com.deke.mall.service.IOrderProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/order")
@RestController
public class OrderController {

    @Autowired
    private IOrderProductService orderProductService;

    @PostMapping("/applyOrder")
    public Payload<ProductOrder> applyOrder(long userId, long productId, long orderQuantity){
        Payload<ProductOrder> applyOrderResult = null;
        if (orderQuantity<0) {
            return new Payload<>("500","参数错误",null);
        }

        if(orderProductService.reduceStock(productId,orderQuantity)){
            ProductOrder order = orderProductService.createNewOrder(productId,userId,orderQuantity);
            applyOrderResult = new Payload<>("0","下单成功",order);
        }else {
            applyOrderResult = new Payload<>("1","下单失败",null);
        }
        return applyOrderResult;
    }
}
