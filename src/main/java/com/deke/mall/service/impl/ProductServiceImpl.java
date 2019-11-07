package com.deke.mall.service.impl;

import com.deke.mall.entity.Product;
import com.deke.mall.mapper.ProductMapper;
import com.deke.mall.service.IProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IProductService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public Product getProductByProductId(long productId) {
        return this.productMapper.selectOneByProductId(productId);
    }
}
