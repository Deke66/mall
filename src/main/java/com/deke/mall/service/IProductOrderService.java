package com.deke.mall.service;

import com.deke.mall.entity.ProductOrder;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Deke
 * @since 2019-11-01
 */
public interface IProductOrderService extends IService<ProductOrder> {
    void asyncSaveOrder(ProductOrder order);
}
