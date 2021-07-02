package com.iot.controller;

import com.iot.entity.DeviceInfo;
import com.iot.entity.StationInfo;
import com.iot.service.DeviceService;
import com.iot.utils.Result;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

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
        try {
            deviceService.deleteStationInfo(stationNo);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
        return Result.success("请求成功");
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
    @RequestMapping("/saveDeviceInfo")
    Result saveDevice(@RequestBody DeviceInfo deviceInfo) {
        try {
            deviceService.saveDeviceInfo(deviceInfo);
        } catch (Exception e) {
            return Result.error(e.getMessage());
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
    Result getDeviceInfoByStationNo(@RequestParam("stationNo") String stationNo) {
        Object data;
        try {
            data = deviceService.getDeviceInfoByStationNo(stationNo);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
        return Result.success("请求成功", data);
    }

    /**
     * 根据站点编号，设备编号更新设备信息
     *
     * @return
     */
    @RequestMapping("/updateDeviceInfo")
    Result updateDeviceInfo(@RequestBody DeviceInfo deviceInfo) {
        try {
            deviceService.updateDeviceInfo(deviceInfo);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
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
            data = deviceService.getDeviceLiveData(stationNo, deviceNo);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
        return Result.success("请求成功", data);
    }

    @RequestMapping("/uploadBinaryFile")
    Result uploadBinaryFile(@RequestParam("type") Integer type,
                            @RequestParam("binaryFile") MultipartFile binaryFile) {

        boolean ret = deviceService.uploadBinaryFile(type, binaryFile);
        if (ret) {
            return Result.success("上传成功");
        } else {
            return Result.error("上传失败");
        }
    }

    @RequestMapping("/downloadBinaryFile")
    Result downloadBinaryFile(@RequestParam("type") Integer type,
                              @RequestParam("filePath") String filePath,
                              HttpServletResponse response) {

        boolean ret = deviceService.downloadBinaryFile(type, filePath, response);
        if (ret) {
            return Result.success("下载成功");
        } else {
            return Result.error("下载失败");
        }
    }

    @RequestMapping("/preivewBgImg")
    Result downloadBinaryFile(@RequestParam("type") Integer type,
                              @RequestParam("filePath") String filePath,
                              HttpServletResponse response) {

        boolean ret = deviceService.downloadBinaryFile(type, filePath, response);
        if (ret) {
            return Result.success("下载成功");
        } else {
            return Result.error("下载失败");
        }
    }

}
