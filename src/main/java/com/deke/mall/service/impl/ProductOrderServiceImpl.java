package com.deke.mall.service.impl;

import com.deke.mall.entity.ProductOrder;
import com.deke.mall.mapper.ProductOrderMapper;
import com.deke.mall.service.IProductOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Deke
 * @since 2019-11-01
 */
@Service
public class ProductOrderServiceImpl extends ServiceImpl<ProductOrderMapper, ProductOrder> implements IProductOrderService {

    @Async
    @Override
    public void asyncSaveOrder(ProductOrder order) {
        this.save(order);
    }
}
