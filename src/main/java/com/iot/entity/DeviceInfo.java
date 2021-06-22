package com.iot.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * TODO
 *
 * @author cs
 * @since 2021/06/05
 */
@Data
@Accessors(chain = true)
public class DeviceInfo implements Serializable {

    private long devNo;

    private String stationNo;

    private String devVarFields;

    private String devName;

    private Integer devType;
}
