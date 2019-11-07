package com.deke.mall.mapper;

import com.deke.mall.entity.Product;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Deke
 * @since 2019-11-01
 */
public interface ProductMapper extends BaseMapper<Product> {
    @Update("UPDATE product SET product_stock =product_stock-#{orderQuantity} WHERE product_id = #{productId} and product_stock>=#{orderQuantity}")
    int updateStockByProductIdAndOrderQuantity(long productId,long orderQuantity);

    Product selectOneByProductId(long productId);
}
