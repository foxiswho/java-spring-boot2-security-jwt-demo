package com.foxwho.demo.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author zhaoxinguo on 2017/9/13.
 */
@Entity
@Data
@Table(name = "user")
public class User implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String password;
    private String role;
}
