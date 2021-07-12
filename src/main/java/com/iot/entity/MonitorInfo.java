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
public class MonitorInfo implements Serializable {

    /**
     * 站点编号
     */
    private String stationNo;

    /**
     * 摄像机编号
     */
    private String monitoringNo;

    /**
     * 摄像机名称
     */
    private String monitoringName;

    /**
     * 设备序列号 摄像机专用
     */
    private String deviceId;

    /**
     * 通道号
     */
    private String channelId;

    /**
     * url
     */
    private String url;

    /**
     * 备注1
     */
    private String remarks1;

    /**
     * 备注2
     */
    private String remarks2;

    /**
     * 备注3
     */
    private String remarks3;
}
