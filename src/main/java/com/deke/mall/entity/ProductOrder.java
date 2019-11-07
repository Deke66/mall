package com.deke.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class ProductOrder extends Model<ProductOrder> {

    private static final long serialVersionUID = 1L;

    @JsonIgnore
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long orderId;

    /**
     * 产品id
     */
    private Long productId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 订单数量
     */
    private Long orderQuantity;

    /**
     * 创建订单时间
     */
    private LocalDateTime createAt;

    /**
     * 订单状态，0新建未支付，1已支付，2已发货，3已收货，4已退款，5已完成
     */
    @JsonIgnore
    private Integer status;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
