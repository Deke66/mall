package com.deke.mall.service.impl;

import com.deke.mall.core.CommonService;
import com.deke.mall.entity.ProductOrder;
import com.deke.mall.mapper.ProductMapper;
import com.deke.mall.service.AbstractOrderProductServiceAdapter;
import com.deke.mall.service.IProductOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class OrderProductServiceImpl extends AbstractOrderProductServiceAdapter {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CommonService commonService;

    @Autowired
    private IProductOrderService orderService;

    @Override
    public boolean reduceStock(long productId,long orderQuantity) {

        return productMapper.updateStockByProductIdAndOrderQuantity(productId, orderQuantity)>=1;
    }

    @Override
    protected void afterOrderSet(ProductOrder order){
        order.setOrderId(commonService.nextId());
        orderService.asyncSaveOrder(order);
    }


}
