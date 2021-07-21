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
public class StationInfo implements Serializable {

    /**
     * 站点编号
     */
    private String stationNo;

    /**
     * 站点名称
     */
    private String stationName;

    /**
     * 站点经度
     */
    private Double stationLon;

    /**
     * 站点纬度
     */
    private Double stationLat;

    /**
     * 站点地址
     */
    private String stationAddress;

    /**
     * 处理工艺
     */
    private String processTech;

    /**
     * 处理规模
     */
    private Integer processScale;

    /**
     * 运维人数
     */
    private Integer devOpsNum;

    /**
     * 站点负责人
     */
    private String principal;

    /**
     * 联系电话
     */
    private String connectTel;

    /**
     * 设备背景底图路径[1,2,3]
     */
    private String bgDevImgPath;

    /**
     * 水质检测底图路径[1,2,3]
     */
    private String bgWaterImgPath;

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
