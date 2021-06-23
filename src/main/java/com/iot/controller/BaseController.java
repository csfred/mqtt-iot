package com.iot.controller;

import com.iot.entity.StationInfo;
import com.iot.service.DeviceService;
import com.iot.utils.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 报表控制器
 *
 * @author cs
 * @date 2021/06/05
 */
@RestController
@RequestMapping("/base")
public class BaseController {

    @Resource
    private DeviceService deviceService;

    @RequestMapping("/test")
    String testController() {
        return "Base Hello World";
    }


    /**
     * 保存站点信息参数JSON串如下
     *
     * @param stationInfo
     * @return
     */
    @RequestMapping("/saveStationInfo")
    Result saveStationInfo(@RequestBody StationInfo stationInfo) {
        try {
            deviceService.saveStationInfo(stationInfo);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
        return Result.success();
    }

    /**
     * 根据经纬度范围获取全部站点信息
     *
     * @param stationLon
     * @param stationLat
     * @return
     */
    @RequestMapping("/getStationInfoByLonLat")
    Result getStationInfoByLonLat(@RequestParam("stationLon") Double stationLon,
                                  @RequestParam("stationLat") Double stationLat) {
        Object data;
        try {
            data = deviceService.getStationInfoByLonLat(stationLon, stationLat);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
        return Result.success("请求成功", data);
    }

    /**
     * 根据站点编号修改站点信息
     *
     * @param stationInfo
     * @return
     */
    @RequestMapping("/updateStationInfo")
    Result getStationInfoByLonLat(@RequestBody StationInfo stationInfo) {
        try {
            deviceService.updateStationInfo(stationInfo);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
        return Result.success("请求成功");
    }

}
