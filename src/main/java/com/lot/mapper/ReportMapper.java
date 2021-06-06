package com.lot.mapper;

import com.lot.entity.ReportContent;
import com.lot.entity.DeviceInfo;
import com.lot.entity.TimeIntervalEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * TODO
 *
 * @author cs
 * @date 2021/06/05
 */
@Mapper
@Repository
public interface ReportMapper {

    /**
     * 获取报表字段实体
     *
     * @param stationNo 参数
     * @param devName   设备名称
     * @return DeviceInfo
     */
    DeviceInfo getDeviceInfo(@Param("stationNo") String stationNo,
                             @Param("devName") String devName);

    /**
     * 获取报表内容
     *
     * @param stationNo 站点编号
     * @param devType   设备类型
     * @param devName   设备名称
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return list ReportContentEntity
     */
    List<ReportContent> getReportContent(@Param("stationNo") String stationNo,
                                         @Param("devType") String devType,
                                         @Param("devName") String devName,
                                         @Param("startTime") String startTime,
                                         @Param("endTime") String endTime);


    /**
     * 获取时间间隔信息
     * @return list
     */
    List<TimeIntervalEntity> getTimeInterValInfo();

    /**
     * 根据ID获取时间间隔信息
     * @param id 参数
     * @return list
     */
    TimeIntervalEntity getTimeInterValById(@Param("id") Integer id);
}
