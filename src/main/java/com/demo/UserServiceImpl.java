package com.demo;

import java.awt.print.Pageable;
import java.util.Arrays;
import java.util.List;

import com.demo.model.PageModel;
import com.demo.model.SpringbootPageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

@Component("userService")
public class UserServiceImpl implements IUserService {
    @Autowired
    MongoOperations mongoTemplate;

    public void saveUser(Users users) {
        mongoTemplate.save(users);
    }

    public Users findUserByName(String name) {
        Query query = new Query();
        return mongoTemplate.findOne(
                new Query(Criteria.where("name").is(name)), Users.class);
    }

    public void removeUser(String name) {
        mongoTemplate.remove(new Query(Criteria.where("name").is(name)),
                Users.class);
    }

    public void updateUser(String name, String key, String value) {
        mongoTemplate.updateFirst(new Query(Criteria.where("name").is(name)),
                Update.update(key, value), Users.class);

    }

    public List<Users> listUser() {
        return mongoTemplate.findAll(Users.class);
    }

    @Override
    public  List<Users> getGroup(String companyName){
        Aggregation agg = Aggregation.newAggregation(
                Arrays.asList(
                        //Aggregation.project("name"),
                        // 1：sql where 语句筛选符合条件的记录
                /*Aggregation.match(
                        Criteria.where("name").is(companyName).and("addedDate").gte(startTime).lte(endTime)),*/
                        // 2：分组条件，设置分组字段
                        Aggregation.group("$name").sum("$age").as("age")
                        //.count().as("allCount")// 增加COUNT为分组后输出的字段
                        .last("$name").as("name"), // 增加publishDate为分组后输出的字段
                        // 3：重新挑选字段
                        Aggregation.project("name","age")
                )

        );
        AggregationResults<Users> results = mongoTemplate.aggregate(agg, "users", Users.class);
        List<Users> list = results.getMappedResults();
        return list;
    }

    /**
     * 分页
     * @param pageNum
     * @return
     */
    @Override
    public Page<Users> paginationQuery(Integer pageNum,Integer pageSize) {
        SpringbootPageable pageable = new SpringbootPageable();
        PageModel pm=new PageModel();
        Query query = new Query();
        List<Order> orders = new ArrayList<Order>();  //排序
        orders.add(new Order(Direction.DESC, "age"));
        Sort sort = new Sort(orders);
        // 开始页
        pm.setPagenumber(pageNum);
        // 每页条数
        pm.setPagesize(pageSize);
        // 排序
        pm.setSort(sort);
        pageable.setPage(pm);
        // 查询出一共的条数
        Long count = mongoTemplate.count(query, Users.class);
        // 查询
        List<Users> list = mongoTemplate.find(query.with(pageable), Users.class);
        // 将集合与分页结果封装
        Page<Users> pagelist = new PageImpl<Users>(list, pageable, count);
        return pagelist;
    }
}
