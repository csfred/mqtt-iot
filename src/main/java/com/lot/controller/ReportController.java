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

    @RequestMapping("/getReportFields")
    String getReportFieldsEntry(@RequestParam("stationNo") String stationNo) {
        return JSON.toJSONString(reportService.getReportFieldsEntry(stationNo));
    }

    @RequestMapping("/getReportContent")
    String getReportContentEntryList(@RequestParam("stationNo") String stationNo,
                                     @RequestParam("devType") String devType,
                                     @RequestParam("devName") String devName,
                                     @RequestParam("startTime") String startTime,
                                     @RequestParam("endTime") String endTime) {

        return JSON.toJSONString(reportService.getReportContentEntryList(stationNo, devType, devName, startTime, endTime));
    }
}
