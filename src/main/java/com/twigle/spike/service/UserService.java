package com.twigle.spike.service;

import com.twigle.spike.dao.UserDao;
import com.twigle.spike.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    @Autowired
    UserDao userDao;

    public User getById(int id) {
        return userDao.getById(id);
    }
    @Transactional
    public boolean tx(){
        User u1 = new User();
        u1.setId(2);
        u1.setName("xiaobo");
        userDao.insert(u1);
//
//
//
//        userDao.insert(u2);
        return true;
    }

}
