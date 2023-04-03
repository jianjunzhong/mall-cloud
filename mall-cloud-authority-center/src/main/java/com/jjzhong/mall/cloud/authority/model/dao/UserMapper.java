package com.jjzhong.mall.cloud.authority.model.dao;

import com.jjzhong.mall.cloud.authority.model.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User row);

    int insertSelective(User row);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User row);

    int updateByPrimaryKey(User row);
    User selectByUserName(String userName);
    User selectLogin(@Param("userName") String userName, @Param("password") String password);
    User selectByEmailAddress(String emailAddress);
}