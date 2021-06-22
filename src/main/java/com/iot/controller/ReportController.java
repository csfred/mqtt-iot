package com.iot.controller;

import com.iot.service.ReportService;
import com.iot.utils.Result;
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
@RequestMapping("/report")
public class ReportController {

    @Resource
    private ReportService reportService;

    @RequestMapping("/test")
    String testController() {
        return "Hello World";
    }

    @RequestMapping("/getDeviceInfo")
    Result getDeviceInfo(@RequestParam("stationNo") String stationNo,
                         @RequestParam("devName") String devName) {
        Object data;
        try {
            data = reportService.getDeviceInfo(stationNo, devName);
        } catch (Exception e) {
            return Result.error("请求失败");
        }
        return Result.success("请求成功", data);
    }

    /**
     * 根据时间间隔获取报表
     * @param stationNo 站点编号
     * @param devType 设备类别
     * @param devName 设备名称
     * @param startTime 起始时间
     * @param endTime 结束时间
     * @param timeIntervalId 时间间隔
     * @return
     */
    @RequestMapping("/getReportContent")
    Result getReportContent(@RequestParam("stationNo") String stationNo,
                            @RequestParam("devType") String devType,
                            @RequestParam("devName") String devName,
                            @RequestParam("startTime") String startTime,
                            @RequestParam("endTime") String endTime,
                            @RequestParam("timeIntervalId") Integer timeIntervalId) {

        Object data;
        try {
            data = reportService.getReportContent(stationNo, devType, devName, startTime, endTime, timeIntervalId);
        } catch (Exception e) {
            return Result.error("请求失败");
        }
        return Result.success("请求成功", data);
    }
}
