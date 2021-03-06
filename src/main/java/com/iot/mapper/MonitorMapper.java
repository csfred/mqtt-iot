package com.iot.mapper;

import com.iot.entity.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;


/**
 * TODO
 *
 * @author cs
 * @since 2020/11/17 15:56
 */
@Mapper
@Repository
public interface MonitorMapper {


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
    List<MonitorInfo> getAllMonitorInfo(@Param("stationNo") String stationNo,
                                        @Param("type") String type);

    /**
     * 检测是否存在摄像机
     * @param stationNo
     * @param monitoringNo
     * @return
     */
    Integer checkExist(@Param("stationNo") String stationNo,
                       @Param("monitoringNo") String monitoringNo);

    /**
     * 根据摄像机编号删除摄像机信息
     *
     * @param monitoringNo
     */
    long deleteMonitorInfo(@Param("monitoringNo") String monitoringNo);

    /**
     * 获取摄像机信息中所有的站点编号，不重复的
     *
     * @return
     */
    List<String> getAllStationNoInMonitor();

}
