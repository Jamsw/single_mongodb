package com.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;

@EnableAutoConfiguration
@ComponentScan(basePackages={"com.demo"})
public class Application implements CommandLineRunner {

    @Autowired
    private IUserService userService;
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        try {
            for(int i=0;i<100;i++){
                Users users = new Users("1", "小明", 10);
                users.setAddress("青岛市");
                userService.saveUser(users);
                List<Users> list = userService.listUser();
            }
            for(int i=0;i<100;i++){
                Users users = new Users("1", "小红", 20);
                users.setAddress("北京市");
                userService.saveUser(users);
                List<Users> list = userService.listUser();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
