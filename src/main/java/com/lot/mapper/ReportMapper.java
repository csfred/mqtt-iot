package com.lot.mapper;

import com.lot.entity.ReportContentEntity;
import com.lot.entity.ReportFieldsEntity;
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
     * @return ReportFieldsEntity
     */
    ReportFieldsEntity getReportFieldsEntry(@Param("stationNo") String stationNo);

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
    List<ReportContentEntity> getReportContentEntryList(@Param("stationNo") String stationNo,
                                                        @Param("devType") String devType,
                                                        @Param("devName") String devName,
                                                        @Param("startTime") String startTime,
                                                        @Param("endTime") String endTime);


}
