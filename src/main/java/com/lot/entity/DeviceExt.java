package com.lot.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * TODO
 *
 * @author Mr.Qu
 * @title: Device
 * @since 2020/11/17 15:42
 */
@Data
@Accessors(chain = true)
public class DeviceExt implements Serializable {

    private String deviceMd5;

    private String varListMd5;

    private String varList;

    private String varFields;

    private String varFieldsMd5;
}
