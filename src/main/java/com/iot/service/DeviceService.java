package com.iot.service;

import com.alibaba.fastjson.JSONObject;
import com.iot.entity.Device;
import com.iot.entity.DeviceInfo;
import com.iot.entity.DeviceType;
import com.iot.entity.StationInfo;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * TODO
 *
 * @author cs
 * @since 2020/11/17 15:35
 */
public interface DeviceService {


    /**
     * 存储站点信息
     *
     * @param stationInfo
     */
    long saveStationInfo(StationInfo stationInfo);

    /**
     * 根据站点编号修改站点信息
     *
     * @param stationInfo
     */
    long updateStationInfo(StationInfo stationInfo);


    /**
     * 获取站点信息集合
     *
     * @param page
     * @param pageSize
     * @param stationName
     * @return
     */
    JSONObject getPageAllStationInfo(@RequestParam("page") Integer page,
                                     @RequestParam("pageSize") Integer pageSize,
                                     @RequestParam("stationName") String stationName);

    /**
     * 根据站点编号删除站点信息
     *
     * @param stationNo
     */
    long deleteStationInfo(String stationNo);

    /**
     * 删除站点小该底图下所有设备
     *
     * @param stationNo
     * @param bgDevImg
     * @return
     */
    long deleteBgDevImg(String stationNo, String bgDevImg);

    /**
     * 存储设备主信息
     *
     * @param param 参数
     */
    void saveDevice(Device param);

    /**
     * 存储设备信息，增加设备
     *
     * @param deviceInfo
     */
    long saveDeviceInfo(DeviceInfo deviceInfo);

    /**
     * 根据站点编号查询所有设备信息
     *
     * @param stationNo
     * @return
     */
    List<DeviceInfo> getDeviceInfoByStationNo(String stationNo, String bgDevImg);

    /**
     * 更新设备信息
     *
     * @param deviceInfo
     */
    long updateDeviceInfo(DeviceInfo deviceInfo);


    /**
     * 更新相同站点相同设备参数计数
     *
     * @param endReceiveTime   最后接收时间
     * @param stationNo        站点编号
     * @param varListFieldsMd5 参数变量值MD5
     */
    void updateSameDeviceCounter(String endReceiveTime,
                                 String stationNo,
                                 String varListFieldsMd5);


    /**
     * 检查varListMd5是否存在
     *
     * @param stationNo
     * @param varListFieldsMd5
     * @return total
     */
    Long checkDeviceExist(String stationNo, String varListFieldsMd5);


    /**
     * 获取站点下所有设备种类信息
     *
     * @param stationNo 站点
     * @return List
     */
    List<DeviceInfo> getDeviceInfoAll(String stationNo);

    /**
     * 获取所有设备类别
     *
     * @return
     */
    List<DeviceType> getAllDeviceType();

    /**
     * 获取设备实时数据
     *
     * @param stationNo
     * @param devNo
     * @return
     */
    Device getDeviceLiveData(String stationNo, Long devNo);

    /**
     * 文件上传
     *
     * @param binaryFile
     * @param type       类型 设备的1，水质的2....
     */
    String uploadBinaryFile(Integer type, MultipartFile binaryFile);

    /**
     * 下载文件
     *
     * @param type     设备的1，水质的2....
     * @param filePath
     * @param response
     * @return
     */
    boolean downloadBinaryFile(Integer type, String filePath, HttpServletResponse response);

}
