package com.lot.service.impl;

import com.lot.entity.ReportContentEntity;
import com.lot.entity.ReportFieldsEntity;
import com.lot.mapper.ReportMapper;
import com.lot.service.ReportService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * TODO
 *
 * @author cs
 * @date 2021/06/05
 */
@Service
public class ReportServiceImpl implements ReportService {

    @Resource
    private ReportMapper reportMapper;

    @Override
    public ReportFieldsEntity getReportFieldsEntry(String stationNo) {
        return reportMapper.getReportFieldsEntry(stationNo);
    }

    @Override
    public List<ReportContentEntity> getReportContentEntryList(String stationNo,
                                                               String devType,
                                                               String devName,
                                                               String startTime,
                                                               String endTime) {
        return reportMapper.getReportContentEntryList(stationNo, devType, devName, startTime, endTime);
    }

}
