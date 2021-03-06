package com.iot.controller;

import com.alibaba.fastjson.JSON;
import com.iot.entity.DeviceInfo;
import com.iot.entity.MonitorInfo;
import com.iot.entity.StationInfo;
import com.iot.service.DeviceService;
import com.iot.service.MonitorService;
import com.iot.utils.Result;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 报表控制器
 *
 * @author cs
 * @date 2021/06/05
 */
@CrossOrigin
@RestController
@RequestMapping("/base")
public class BaseController {

    @Resource
    private DeviceService deviceService;

    @Resource
    private MonitorService monitorService;

    @RequestMapping("/test")
    String testController() {
        return "Base Hello World";
    }


    @PostMapping(value = "/testPost")
    String testPost(@RequestBody StationInfo stationInfo) {
        return JSON.toJSONString(stationInfo);
    }


    /**
     * 保存站点信息参数JSON串如下
     *
     * @param stationInfo
     * @return
     */
    @RequestMapping(value = "/saveStationInfo")
    Result saveStationInfo(@RequestBody StationInfo stationInfo) {
        long ret = deviceService.saveStationInfo(stationInfo);
        if (ret < 0) {
            return Result.error("保存失败，服务器异常");
        }
        return Result.success("保存成功");
    }

    /**
     * 获取全部站点信息
     *
     * @return
     */
    @RequestMapping("/getPageAllStationInfo")
    Result getPageAllStationInfo(@RequestParam("page") Integer page,
                                 @RequestParam("pageSize") Integer pageSize,
                                 @RequestParam("stationName") String stationName) {
        Object data;
        try {
            data = deviceService.getPageAllStationInfo(page, pageSize, stationName);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
        return Result.success("请求成功", data);
    }

    /**
     * 根据站点编号删除站点信息
     *
     * @param stationNo
     * @return
     */
    @RequestMapping("/deleteStationInfo")
    Result deleteStationInfo(@RequestParam("stationNo") String stationNo) {
        long ret = deviceService.deleteStationInfo(stationNo);
        if (ret < 0) {
            return Result.error("删除失败，服务器异常");
        }
        return Result.success("请求成功");
    }


    @RequestMapping("/deleteBgDevImg")
    Result deleteBgDevImg(@RequestParam("stationNo") String stationNo,
                          @RequestParam("bgDevImg") String bgDevImg,
                          @RequestParam(value = "devNo", required = false) Long devNo) {

        long ret = deviceService.deleteBgDevImg(stationNo, bgDevImg, devNo);
        if (ret < 0) {
            return Result.error("删除失败，服务器异常");
        }
        return Result.success("请求成功");
    }

    /**
     * 根据站点编号修改站点信息
     *
     * @param stationInfo
     * @return
     */
    @RequestMapping(value = "/updateStationInfo")
    Result updateStationInfo(@RequestBody StationInfo stationInfo) {
        long ret = deviceService.updateStationInfo(stationInfo);
        if (ret < 0) {
            return Result.error("更新失败，服务器异常");
        }
        return Result.success("请求成功");
    }

    /**
     * 获取所有设备类别
     *
     * @return
     */
    @RequestMapping("/getAllDeviceType")
    Result getAllDeviceType() {
        Object data;
        try {
            data = deviceService.getAllDeviceType();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
        return Result.success("请求成功", data);
    }

    /**
     * 保存设备信息
     *
     * @param deviceInfo
     * @return
     */
    @RequestMapping(value = "/saveDeviceInfo")
    Result saveDeviceInfo(@RequestBody DeviceInfo deviceInfo) {
        long ret = deviceService.saveDeviceInfo(deviceInfo);
        if (ret < 0) {
            return Result.error("保存失败，服务器异常");
        }
        return Result.success("请求成功");
    }

    /**
     * 通过站点编号获取设备信息
     *
     * @param stationNo
     * @return
     */
    @RequestMapping("/getDeviceInfoByStationNo")
    Result getDeviceInfoByStationNo(@RequestParam(value = "stationNo") String stationNo,
                                    @RequestParam(value = "bgDevImg", required = false, defaultValue = "") String bgDevImg) {
        Object dataList;
        try {
            dataList = deviceService.getDeviceInfoByStationNo(stationNo, bgDevImg);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
        return Result.success("请求成功", dataList);
    }

    /**
     * 根据站点编号，设备编号更新设备信息
     *
     * @return
     */
    @RequestMapping(value = "/updateDeviceInfo")
    Result updateDeviceInfo(@RequestBody DeviceInfo deviceInfo) {
        long ret = deviceService.updateDeviceInfo(deviceInfo);
        if (ret < 0) {
            return Result.error("更新失败，服务器异常");
        }
        return Result.success("请求成功");
    }

    /**
     * 根据站点编号，设备编号批量更新设备信息
     *
     * @return
     */
    @RequestMapping(value = "/batchUpdateDeviceInfo")
    Result batchUpdateDeviceInfo(@RequestBody List<DeviceInfo> deviceInfoList) {
        if (CollectionUtils.isEmpty(deviceInfoList)) {
            return Result.error("参数为空");
        }
        deviceService.batchUpdateDeviceInfo(deviceInfoList);
        return Result.success("请求成功");
    }

    /**
     * 获取站点下某个设备的实时数据
     *
     * @param stationNo 站点编号
     * @param deviceNo  设备编号
     * @return
     */
    @RequestMapping("/getDeviceLiveData")
    Result getDeviceLiveData(@RequestParam("stationNo") String stationNo,
                             @RequestParam("deviceNo") Long deviceNo) {
        Object data;
        try {
            data = deviceService.getDeviceLiveData(stationNo, deviceNo, false);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
        return Result.success("请求成功", data);
    }

    @RequestMapping("/uploadBinaryFile")
    Result uploadBinaryFile(@RequestParam("binaryFile") MultipartFile binaryFile) {

        String relateFilePath = deviceService.uploadBinaryFile(binaryFile);
        if (StringUtils.isEmpty(relateFilePath)) {
            return Result.error("上传失败", "");
        } else {
            return Result.success("上传成功", relateFilePath);
        }
    }

    @RequestMapping("/downloadBinaryFile")
    Result downloadBinaryFile(@RequestParam("filePath") String filePath,
                              HttpServletResponse response) {

        boolean ret = deviceService.downloadBinaryFile(filePath, response);
        if (ret) {
            return Result.success("下载成功");
        } else {
            return Result.error("下载失败");
        }
    }


    /**
     * 保存摄像机信息
     *
     * @param monitorInfo
     * @return
     */
    @RequestMapping("/saveMonitorInfo")
    Result saveMonitorInfo(@RequestBody MonitorInfo monitorInfo) {

        long ret = monitorService.saveMonitorInfo(monitorInfo);
        if (ret < 0) {
            return Result.error("保存摄像机信息失败", ret);
        } else {
            return Result.success("保存摄像机信息成功");
        }
    }

    /**
     * 更新摄像机信息
     *
     * @param monitorInfo
     * @return
     */
    @RequestMapping("/updateMonitorInfo")
    Result updateMonitorInfo(@RequestBody MonitorInfo monitorInfo) {
        long ret = monitorService.updateMonitorInfo(monitorInfo);
        if (ret < 0) {
            return Result.error("更新摄像机信息失败");
        } else {
            return Result.success("更新摄像机信息成功");
        }
    }

    /**
     * 根据站点编号，获取站点下全部摄像头信息
     *
     * @return
     */
    @RequestMapping("/getAllMonitorInfo")
    Result getAllMonitorInfo(@RequestParam("stationNo") String stationNo,
                             @RequestParam("type") String type) {
        Object data;
        try {
            data = monitorService.getAllMonitorInfo(stationNo, type);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
        return Result.success("请求成功", data);
    }

    /**
     * 根据摄像机编号删除摄像机信息
     *
     * @param monitoringNo
     * @return
     */
    @RequestMapping("/deleteMonitorInfo")
    Result deleteMonitorInfo(@RequestParam("monitoringNo") String monitoringNo) {
        long ret = monitorService.deleteMonitorInfo(monitoringNo);
        if (ret < 0) {
            return Result.error("删除失败，服务器异常");
        }
        return Result.success("请求成功");
    }

    /**
     * 获取所有站点下摄像机信息
     *
     * @return
     */
    @RequestMapping("/monitoringViewTree")
    Result monitoringViewTree() {
        Object data;
        try {
            data = monitorService.monitoringViewTree();
        } catch (Exception e) {
            return Result.error("获取监控树信息失败，服务器异常");
        }
        return Result.success("请求成功", data);
    }

    @RequestMapping("/checkStationIsOnline")
    Result checkStationIsOnline(@RequestParam("stationNo") String stationNo) {
        Object result;
        try {
            result = deviceService.checkStationIsOnline(stationNo);
        } catch (Exception e) {
            return Result.error("获取站点在线状态失败，服务器异常");
        }
        return Result.success("请求成功", result);
    }
}
