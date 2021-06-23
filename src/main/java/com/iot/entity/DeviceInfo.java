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

    /**
     * 设备编号，数据库自增字段，不需要传入
     */
    private long devNo;

    /**
     * 站点编号
     */
    private String stationNo;

    /**
     * 设备变量字段列表 按照拼音排序升序
     * eg.. ["调节池提升泵备用","调节池提升泵故障","调节池提升泵程控","调节池提升泵运行"]
     */
    private String devVarFields;

    /**
     * 设备名称
     */
    private String devName;

    /**
     * 设备类别,ID
     */
    private Integer devType;

    /**
     * 设备相对底图的位置坐标
     * eg.. {"x":15, "y":30}
     */
    private String deviceVector;
}
