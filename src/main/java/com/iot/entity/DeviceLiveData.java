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
public class DeviceLiveData implements Serializable {

    /**
     * 设备编号
     */
    private Long devNo;

    /**
     * 站点编号
     */
    private String stationNo;

    /**
     * 轮询查询时间，目前当前时间-20s
     */
    private String queryTime;

}
