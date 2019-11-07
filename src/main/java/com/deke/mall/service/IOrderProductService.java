package com.deke.mall.service;

import com.deke.mall.entity.ProductOrder;

public interface IOrderProductService {
    boolean reduceStock(long productId,long OrderQuantity);
    ProductOrder createNewOrder(long productId, long userId, long orderQuantity);
}
