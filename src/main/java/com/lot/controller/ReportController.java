package com.lot.controller;

import com.alibaba.fastjson.JSON;
import com.lot.service.ReportService;
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
    String getDeviceInfo(@RequestParam("stationNo") String stationNo,
                         @RequestParam("devName") String devName) {
        return JSON.toJSONString(reportService.getDeviceInfo(stationNo, devName));
    }

    @RequestMapping("/getReportContent")
    String getReportContent(@RequestParam("stationNo") String stationNo,
                            @RequestParam("devType") String devType,
                            @RequestParam("devName") String devName,
                            @RequestParam("startTime") String startTime,
                            @RequestParam("endTime") String endTime,
                            @RequestParam("timeIntervalId") Integer timeIntervalId) {

        return JSON.toJSONString(reportService.getReportContent(stationNo, devType, devName, startTime, endTime, timeIntervalId));
    }
}
