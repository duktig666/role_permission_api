package com.test.modules.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * description: 在线用户，存储redis
 *
 * @author RenShiWei
 * Date: 2020/8/3 15:21
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OnlineUser {

    private String userName;

    private String nickName;

    private String job;

    private String browser;

    private String ip;

    private String address;

    private String key;

    private Date loginTime;


}
