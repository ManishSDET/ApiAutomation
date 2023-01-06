package org.example.DataProvider;

import org.example.Models.RequestModels.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UserDP {

    Random rand = new Random();

    public User createUser(){
        User user = new User();
        int id = rand.nextInt(10000);
        user.setId(id);
        user.setUserName("test" + id);
        user.setFirstName("FN" + id);
        user.setEmail("test"+id+"@gmail.com");
        user.setPassword("test"+id);
        user.setPhone(""+rand.nextInt(100000000));
        user.setUserStatus(1);
        return user;
    }

    public List<User> createMultipleUsersWithArray(int numberOfUsers) {
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < numberOfUsers; i++)
            userList.add(createUser());
        return userList;
    }

    public User updateUser(String username){
        User user = new User();
        int id = rand.nextInt(10000);
        user.setUserName(username + id);
        user.setFirstName("FN" + id);
        user.setEmail(username+id+"@gmail.com");
        user.setPassword(username+id);
        user.setPhone(""+rand.nextInt(100000000));
        user.setUserStatus(1);
        return user;
    }

    public List<User> createDuplicateMultipleUser(){
        List<User> users = new ArrayList<>();
        User user = createUser();
        users.add(user);
        users.add(user);
        return users;
    }

}
