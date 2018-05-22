package com.demo;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;


@Component("userService")
public class UserServiceImpl implements IUserService {
    @Autowired
    MongoOperations mongoTemplate;

    public void saveUser(Users users) {
        mongoTemplate.save(users);
    }

    public Users findUserByName(String name) {
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
}
