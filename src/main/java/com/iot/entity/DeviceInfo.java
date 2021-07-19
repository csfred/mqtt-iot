package com.iot.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Map;

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

    public DeviceInfo toDeviceInfoEntity(Map<String, Object> deviceInfoMap) {
        boolean isValid = true;
        if (null != deviceInfoMap.get("devNo")) {
            this.setDevNo(Long.parseLong(deviceInfoMap.get("devNo").toString()));
        } else {
            isValid = false;
        }
        if (null != deviceInfoMap.get("stationNo")) {
            this.setStationNo(deviceInfoMap.get("stationNo").toString());
        } else {
            isValid = false;
        }
        if (null != deviceInfoMap.get("devName")) {
            this.setDevName(deviceInfoMap.get("devName").toString());
        }
        if (null != deviceInfoMap.get("devVarFields")) {
            this.setDevVarFields(deviceInfoMap.get("devVarFields").toString());
        }
        if (null != deviceInfoMap.get("devType")) {
            this.setDevType(Integer.parseInt(deviceInfoMap.get("devType").toString()));
        }
        if (null != deviceInfoMap.get("deviceVector")) {
            this.setDeviceVector(deviceInfoMap.get("deviceVector").toString());
        }

        if (isValid) {
            return this;
        } else {
            return null;
        }

    }
}
