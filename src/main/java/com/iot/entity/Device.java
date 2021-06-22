package com.iot.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * TODO
 *
 * @author cs
 * @since 2021/06/03
 */
@Data
@Accessors(chain = true)
public class Device implements Serializable {

    private long devNo;

    private String stationNo;

    private String varListFields;

    private String varListFieldsMd5;

    private Timestamp startReceiveTime;

    private Timestamp endReceiveTime;

    private Integer receiveCount;
}
