package com.test.modules.business.entity.bo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * description: 最小化封装岗位信息
 *
 * @author RenShiWei
 * Date: 2020/8/6 20:50
 **/
@Data
public class JobSmallBO implements Serializable {

    private Long id;

    private String name;

}

