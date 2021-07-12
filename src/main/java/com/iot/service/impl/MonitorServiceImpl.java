package com.iot.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.iot.entity.*;
import com.iot.mapper.MonitorMapper;
import com.iot.service.MonitorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * TODO
 *
 * @author cs
 * @since 2020/11/17 15:55
 */
@Slf4j
@Service
public class MonitorServiceImpl implements MonitorService {

    @Resource
    private MonitorMapper monitorMapper;

    @Override
    public long saveMonitorInfo(MonitorInfo monitorInfo) {
        long ret = -1;
        try {
            ret = monitorMapper.saveMonitorInfo(monitorInfo);
        } catch (Exception e) {
            log.error("saveStationInfo stationInfo={}, errorMsg={}", JSON.toJSONString(monitorInfo), e.getMessage());
        }
        return ret;
    }

    @Override
    public long updateMonitorInfo(MonitorInfo monitorInfo) {
        long ret = -1;
        try {
            ret = monitorMapper.updateMonitorInfo(monitorInfo);
        } catch (Exception e) {
            log.error("updateStationInfo stationInfo={}, errorMsg={}", JSON.toJSONString(monitorInfo), e.getMessage());
        }
        return ret;
    }

    @Override
    public List<MonitorInfo> getAllMonitorInfo(String stationNo) {
        try {
            return monitorMapper.getAllMonitorInfo(stationNo);
        } catch (Exception e) {
            log.error("stationNo={}, errorMsg={}", stationNo, e.getMessage());
            return null;
        }
    }

    @Override
    public long deleteMonitorInfo(String monitoringNo) {
        long ret = -1;
        try {
            ret = monitorMapper.deleteMonitorInfo(monitoringNo);
        } catch (Exception e) {
            log.error("deleteStationInfo monitoringNo={}, errorMsg={}", monitoringNo, e.getMessage());
        }
        return ret;
    }

    @Override
    public List<JSONObject> monitoringViewTree(){
        return null;
    }

}
