package com.deke.mall.service;

import com.deke.mall.entity.ProductOrder;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

public abstract class AbstractOrderProductServiceAdapter implements IOrderProductService{
    @Override
    public ProductOrder createNewOrder(long productId, long userId, long OrderQuantity) {
        ProductOrder order = new ProductOrder();
        order.setCreateAt(LocalDateTime.now());
        order.setUserId(userId);
        order.setOrderQuantity(OrderQuantity);
        order.setProductId(productId);
        afterOrderSet(order);
        return order;
    }

    protected void afterOrderSet(@NonNull ProductOrder order){

    }

}
