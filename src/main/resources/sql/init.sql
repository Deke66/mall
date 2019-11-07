DROP TABLE IF EXISTS `request_info`;
CREATE TABLE `request_info` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `application_id` SMALLINT(4) NOT NULL COMMENT '应用id',
    `trace_no` varchar(32) NOT NULL COMMENT '日志追踪号',
    `class_name` varchar(256) NOT NULL COMMENT '请求定位到的类名',
    `method_name` varchar(256) NOT NULL COMMENT '请求定位到的方法名',
    `spend_ms` bigint(20) NOT NULL COMMENT '请求耗时ms',
    `status` character(1) NOT NULL default 'S' COMMENT '处理结果',
    PRIMARY KEY (`id`),
    UNIQUE KEY `u_trace_no` (`trace_no`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL COMMENT '用户ID，手机号码',
  `nickname` varchar(255) NOT NULL,
  `password` varchar(32) DEFAULT NULL COMMENT 'MD5',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_id` BIGINT(20) NOT NULL,
  `product_name` varchar(64) NOT NULL COMMENT '产品名',
  `product_description` varchar(256) NOT NULL COMMENT '产品描述',
  `product_price` decimal(10,2) NOT NULL COMMENT '产品单价',
  `product_stock` bigint(20) DEFAULT NULL COMMENT '库存',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_product_id` (`product_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `product_order`;
CREATE TABLE `product_order` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `order_id` bigint(20) NOT NULL,
  `product_id` BIGINT(20) NOT NULL COMMENT '产品id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `order_quantity` bigint(10) DEFAULT NULL COMMENT '订单数量',
  `create_at` TIMESTAMP  COMMENT '创建订单时间',
  `status` tinyint(4) DEFAULT '0' COMMENT '订单状态，0新建未支付，1已支付，2已发货，3已收货，4已退款，5已完成',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_order_id` (`order_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;