package com.deke.mall.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author Deke
 * @since 2019-11-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Product extends Model<Product> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long productId;

    /**
     * 产品名
     */
    private String productName;

    /**
     * 产品描述
     */
    private String productDescription;

    /**
     * 产品单价
     */
    private BigDecimal productPrice;

    /**
     * 库存
     */
    private Long productStock;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
