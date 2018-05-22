package com.demo;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * mongodb 案例
 * 创建者  小柒
 * 创建时间    2017年7月19日
 *
 */
public interface IUserService {
    public void saveUser(Users users);

    public Users findUserByName(String name);

    public void removeUser(String name);

    public void updateUser(String name, String key, String value);

    public List<Users> listUser();

    List<Users> getGroup(String companyName);

    Page<Users> paginationQuery(Integer pageNum,Integer pageSize);
}
