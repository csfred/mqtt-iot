package com.iot.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.iot.entity.*;
import com.iot.mapper.DeviceMapper;
import com.iot.mapper.MonitorMapper;
import com.iot.service.MonitorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
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

    @Resource
    private DeviceMapper deviceMapper;

    @Override
    public long saveMonitorInfo(MonitorInfo monitorInfo) {
        long ret = -1;
        try {
            Integer total = monitorMapper.checkExist(monitorInfo.getStationNo(), monitorInfo.getMonitoringNo());
            if (total > 0) {
                ret = -2;
            } else {
                ret = monitorMapper.saveMonitorInfo(monitorInfo);
            }
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
    public List<MonitorInfo> getAllMonitorInfo(String stationNo, String type) {
        try {
            return monitorMapper.getAllMonitorInfo(stationNo, type);
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
    public List<JSONObject> monitoringViewTree() {
        List<String> stationNoList = monitorMapper.getAllStationNoInMonitor();
        if (CollectionUtils.isEmpty(stationNoList)) {
            return null;
        }
        List<JSONObject> retList = new ArrayList<>(8);
        for (String stationNo : stationNoList) {
            StationInfo stationInfo = deviceMapper.getStationInfoByNo(stationNo);
            if (null == stationInfo) {
                continue;
            }
            List<MonitorInfo> monitorInfos = monitorMapper.getAllMonitorInfo(stationNo, "");
            JSONObject jsonObject = new JSONObject(8);
            jsonObject.put("id", stationNo);
            jsonObject.put("label", stationInfo.getStationName());
            if (CollectionUtils.isEmpty(monitorInfos)) {
                jsonObject.put("children", new ArrayList<>(4));
            } else {
                List<JSONObject> childrenList = new ArrayList<>(8);
                for (MonitorInfo monitorInfo : monitorInfos) {
                    JSONObject childrenObject = new JSONObject(8);
                    childrenObject.put("id", monitorInfo.getMonitoringNo());
                    childrenObject.put("label", monitorInfo.getMonitoringName());
                    childrenObject.put("deviceId", monitorInfo.getDeviceId());
                    childrenObject.put("channelId", monitorInfo.getChannelId());
                    childrenObject.put("url", monitorInfo.getUrl());
                    childrenObject.put("type", monitorInfo.getType());
                    childrenObject.put("remarks1", monitorInfo.getRemarks1());
                    childrenObject.put("remarks2", monitorInfo.getRemarks2());
                    childrenObject.put("remarks3", monitorInfo.getRemarks3());
                    childrenList.add(childrenObject);
                }
                jsonObject.put("children", childrenList);
            }
            retList.add(jsonObject);
        }
        return retList;
    }

}
