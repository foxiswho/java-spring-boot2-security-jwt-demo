package com.foxwho.demo.service.impl;

import com.foxwho.demo.exception.HttpAesException;
import com.foxwho.demo.model.JwtUser;
import com.foxwho.demo.model.User;
import com.foxwho.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static java.util.Collections.emptyList;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new HttpAesException(username);
        }
        System.out.println("loadUserByUsername :");
        System.out.println(user);
//        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), emptyList());
        return new JwtUser(user);
    }

}
