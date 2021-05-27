package com.lot.device.entity;

import java.io.Serializable;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * TODO
 *
 * @author Mr.Qu
 * @title: Device
 * @since 2020/11/17 15:42
 */
@Data
@Accessors(chain = true)
public class Device implements Serializable {

    private Integer cmdId;

    private Integer devId;

    private Integer devNo;

    private Integer type;

    private String version;

    private String md5;

}
