package com.deke.mall.service.impl;

import com.deke.mall.common.ParamKeys;
import com.deke.mall.core.CommonService;
import com.deke.mall.entity.ProductOrder;
import com.deke.mall.entity.Product;
import com.deke.mall.exception.ApplicationException;
import com.deke.mall.service.AbstractOrderProductServiceAdapter;
import com.deke.mall.service.IProductOrderService;
import com.deke.mall.service.IProductService;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalNotification;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class OrderProductLocalCacheServiceImpl extends AbstractOrderProductServiceAdapter {

    @Autowired
    private CommonService commonService;

    @Autowired
    private IProductOrderService orderService;

    @Autowired
    private IProductService iProductService;

    private Cache<Long, Product> productCache = CacheBuilder.newBuilder()
            //设置访问缓存失效时间，失效后不保证会调用removalListener
            .expireAfterAccess(60, TimeUnit.SECONDS)
            .maximumSize(1000)
            .removalListener(this::productCacheOnRemove)
            .build();

    @PreDestroy
    public void destroyCache(){
        productCache.cleanUp();
    }

    private void productCacheOnRemove(RemovalNotification<Long, Product> notification){
        MDC.put(ParamKeys.TRACE_NO,"product-after-remove-then-update");
        log.info("product is removing from the local cache, productId is {}",notification.getKey());
        Product product = notification.getValue();
        Product newProduct = new Product();
        newProduct.setId(product.getId());
        newProduct.setProductStock(product.getProductStock());
        iProductService.saveOrUpdate(newProduct);
        MDC.remove(ParamKeys.TRACE_NO);
    }


    @Override
    public boolean reduceStock(long productId, long orderQuantity) {
        Product product;
        try {
            product = productCache.get(productId,()->iProductService.getProductByProductId(productId));
        } catch (ExecutionException e) {
            throw  new ApplicationException("3","商品不存在");
        }
        synchronized (product){
            long stock = product.getProductStock();
            if(stock>=orderQuantity){
                product.setProductStock(stock-orderQuantity);
                return true;
            }
        }
        return false;
    }

    @Override
    protected void afterOrderSet(ProductOrder order){
        order.setOrderId(commonService.nextId());
        orderService.asyncSaveOrder(order);
    }

}
