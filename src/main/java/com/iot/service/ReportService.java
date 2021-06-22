package com.iot.service;

import com.iot.entity.*;

import java.util.List;

/**
 * 报表service
 *
 * @author cs
 * @date 2021/06/05
 */
public interface ReportService {

    /**
     * 获取报表字段实体
     *
     * @param stationNo 站点编号
     * @param devName   设备名称
     * @return DeviceInfo
     */
    DeviceInfo getDeviceInfo(String stationNo, String devName);

    /**
     * 获取报表内容
     *
     * @param stationNo      站点编号
     * @param devType        设备类型
     * @param devName        设备名称
     * @param startTime      开始时间
     * @param endTime        结束时间
     * @param timeIntervalId 时间间隔
     * @return list ReportContent
     */
    List<ReportContent> getReportContent(String stationNo,
                                         String devType,
                                         String devName,
                                         String startTime,
                                         String endTime,
                                         Integer timeIntervalId);

}
