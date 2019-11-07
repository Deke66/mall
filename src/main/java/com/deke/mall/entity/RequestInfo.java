package com.deke.mall.entity;

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
public class RequestInfo extends Model<RequestInfo> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 应用id
     */
    private Integer applicationId;

    /**
     * 日志追踪号
     */
    private String traceNo;

    /**
     * 请求定位到的类名
     */
    private String className;

    /**
     * 请求定位到的方法名
     */
    private String methodName;

    /**
     * 请求耗时ms
     */
    private Long spendMs;

    /**
     * 处理结果
     */
    private String status;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
