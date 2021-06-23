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

    /**
     * 设备编号
     */
    private long devNo;

    /**
     * 站点编号
     */
    private String stationNo;

    /**
     * 参数列表字段
     */
    private String varListFields;

    /**
     * 参数列表字段MD5
     */
    private String varListFieldsMd5;

    /**
     * 开始接受时间
     */
    private Timestamp startReceiveTime;

    /**
     * 结束接收时间
     */
    private Timestamp endReceiveTime;

    /**
     * 接受个数
     */
    private Integer receiveCount;
}
