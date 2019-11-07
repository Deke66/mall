package com.deke.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.deke.mall.common.ParamKeys;
import com.deke.mall.core.CommonService;
import com.deke.mall.core.RedisLock;
import com.deke.mall.entity.ProductOrder;
import com.deke.mall.entity.Product;
import com.deke.mall.service.AbstractOrderProductServiceAdapter;
import com.deke.mall.service.IProductOrderService;
import com.deke.mall.service.IProductService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


@Component
@Slf4j
@Primary
public class OrderProductRedisCacheServiceImpl extends AbstractOrderProductServiceAdapter {

    private ConcurrentHashMap<Long,Long> stocks = new ConcurrentHashMap<>();

    private final String PRODUCT = "product";

    @Autowired
    private RedisLock redisLock;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private CommonService commonService;

    @Autowired
    private IProductOrderService orderService;

    @Autowired
    private IProductService iProductService;

    @Override
    public boolean reduceStock(long productId, long orderQuantity) {
        if (!stocks.containsKey(productId)){
            try{
                if (redisLock.tryLock(String.valueOf(productId),30L,()-> stocks.get(productId)==null)){
                    creatCache(productId);
                }
            }finally {
                redisLock.unLock(String.valueOf(productId));
            }
        }

        if (stocks.get(productId) != -1L){
            return doReduceStock(productId, orderQuantity);
        }

        return false;
    }

    private boolean doReduceStock(long productId, long orderQuantity){
        DefaultRedisScript redisScript = new DefaultRedisScript<>();
        redisScript.setLocation(new ClassPathResource("lua/reduceStock.lua"));
        redisScript.setResultType(Long.class);
        List<Object> keys = new ArrayList<>();
        keys.add(PRODUCT);
        keys.add(productId);
        Object response = redisTemplate.execute(redisScript,keys,orderQuantity);
        Long result = (Long) response;
        return result==1;
    }

    private void creatCache(long productId){

        if (stocks.get(productId) != null){
            return;
        }

        HashOperations<String,Long, Long> operations = redisTemplate.opsForHash();
        Object obj = operations.get(PRODUCT,productId);
        if( obj != null){
            log.info("product was putted in redis");
            stocks.put(productId,Long.valueOf((Integer)obj));
            return;
        }

        Product product = iProductService.getProductByProductId(productId);
        if (product == null){
            stocks.put(productId,-1L);
        }else {
            operations.putIfAbsent(PRODUCT,productId,product.getProductStock());
            stocks.put(productId,product.getProductStock());
        }

    }

    @Override
    protected void afterOrderSet(ProductOrder order){
        order.setOrderId(commonService.nextId());
        orderService.asyncSaveOrder(order);
    }

    @Scheduled(fixedRate = 60000)
    public void redisCacheSynWithMySql() {
        MDC.put(ParamKeys.TRACE_NO,"Scheduled-task-redisCacheSynWithMySql");
        Set<Map.Entry<Long,Long>> entrySet = stocks.entrySet();
        HashOperations<String,Long, Long> operations = redisTemplate.opsForHash();
        Long stock;
        Product newProduct;
        Object result;
        UpdateWrapper<Product> updateWrapper = new UpdateWrapper<>();
        for (Map.Entry<Long,Long> entry:entrySet) {
            result = operations.get(PRODUCT,entry.getKey());
            stock = Long.valueOf((Integer)result);
            if (stock!=null && !stock.equals(entry.getValue())){
                stocks.put(entry.getKey(),stock);
                newProduct = new Product();
                newProduct.setProductStock(stock);
                updateWrapper.eq("product_id",entry.getKey());
                iProductService.update(newProduct,updateWrapper);
            }

        }
        MDC.remove(ParamKeys.TRACE_NO);
    }


}
