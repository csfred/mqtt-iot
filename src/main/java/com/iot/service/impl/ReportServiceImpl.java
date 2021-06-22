package com.iot.service.impl;

import com.iot.entity.Constants;
import com.iot.entity.ReportContent;
import com.iot.entity.DeviceInfo;
import com.iot.entity.TimeIntervalEntity;
import com.iot.mapper.ReportMapper;
import com.iot.service.ReportService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    public DeviceInfo getDeviceInfo(String stationNo, String devName) {
        return reportMapper.getDeviceInfo(stationNo, devName);
    }

    @Override
    public List<ReportContent> getReportContent(String stationNo,
                                                String devType,
                                                String devName,
                                                String startTime,
                                                String endTime,
                                                Integer timeIntervalId) {

        List<ReportContent> reportContentListRet = new ArrayList<>(8);
        List<ReportContent> reportContentList = reportMapper.getReportContent(
                stationNo, devType, devName, startTime, endTime);
        if (CollectionUtils.isEmpty(reportContentList)) {
            return reportContentListRet;
        }
        LocalDateTime endLocalDateTime = LocalDateTime.parse(endTime, DateTimeFormatter.ofPattern(Constants.TIME_STAMP_FORMAT));
        TimeIntervalEntity timeIntervalEntity = reportMapper.getTimeInterValById(timeIntervalId);
        if (null != timeIntervalEntity) {
            for (ReportContent reportContent : reportContentList) {
                LocalDateTime startReceiveTime = LocalDateTime.parse(reportContent.getStartReceiveTime(),
                        DateTimeFormatter.ofPattern(Constants.TIME_STAMP_FORMAT));
                Integer receiveCount = reportContent.getReceiveCount();
                ReportContent reportContentTmp;
                Integer timeInterval = timeIntervalEntity.getTimeInterval();
                LocalDateTime plusLocalDateTime = null;
                for (int idx = 0; idx < receiveCount; idx++) {
                    reportContentTmp = ReportContent.copy(reportContent);
                    switch (timeIntervalEntity.getUnit()) {
                        //秒
                        case 0:
                            plusLocalDateTime = startReceiveTime.plusSeconds(idx * timeInterval);
                            break;
                        //分
                        case 1:
                            plusLocalDateTime = startReceiveTime.plusMinutes(idx * timeInterval);
                            break;
                        //时
                        case 2:
                            plusLocalDateTime = startReceiveTime.plusHours(idx * timeInterval);
                            break;
                        //天
                        case 3:
                            plusLocalDateTime = startReceiveTime.plusDays(idx * timeInterval);
                            break;
                        default:
                            break;
                    }
                    if (null == plusLocalDateTime || plusLocalDateTime.isAfter(endLocalDateTime)) {
                        break;
                    }
                    reportContentTmp.setTimePoint(plusLocalDateTime.format(DateTimeFormatter.ofPattern(
                            Constants.TIME_STAMP_FORMAT)));
                    reportContentListRet.add(reportContentTmp);
                }
            }
        }
        return reportContentListRet;
    }

}
