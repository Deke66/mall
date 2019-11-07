package com.deke.mall.service.impl;

import com.deke.mall.entity.User;
import com.deke.mall.mapper.UserMapper;
import com.deke.mall.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
