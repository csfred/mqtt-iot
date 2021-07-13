package com.iot.service;

import com.alibaba.fastjson.JSONObject;
import com.iot.entity.*;
import java.util.List;

/**
 * TODO
 *
 * @author cs
 * @since 2020/11/17 15:35
 */
public interface MonitorService {


    /**
     * 存储摄像机信息
     *
     * @param monitorInfo
     */
    long saveMonitorInfo(MonitorInfo monitorInfo);

    /**
     * 根据摄像机编号，修改摄像机信息
     *
     * @param monitorInfo
     */
    long updateMonitorInfo(MonitorInfo monitorInfo);


    /**
     * 根据站带你编号，获取站点下所有摄像机信息集合
     *
     * @param stationNo 站点编号
     * @return
     */
    List<MonitorInfo> getAllMonitorInfo(String stationNo, String type);

    /**
     * 根据摄像机编号删除摄像机信息
     *
     * @param monitoringNo
     * @return
     */
    long deleteMonitorInfo(String monitoringNo);

    /**
     * 获取所有站点下所有摄像头信息，组装拼接
     *
     * @return
     */
    List<JSONObject> monitoringViewTree();

}
