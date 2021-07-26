package com.iot.service;

import com.alibaba.fastjson.JSONObject;
import com.iot.entity.Device;
import com.iot.entity.DeviceInfo;
import com.iot.entity.DeviceType;
import com.iot.entity.StationInfo;
import com.iot.mqtt.MQTTListener;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * TODO
 *
 * @author cs
 * @since 2020/11/17 15:35
 */
public interface DeviceService {


    /**
     * 设置mqttListener
     *
     * @param mqttListener
     */
    void setMqttListener(MQTTListener mqttListener);

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
     * 修改站点是否正常连接
     *
     * @param stationNo
     * @param isOnline
     * @return
     */
    long updateStationOnline(String stationNo, boolean isOnline);

    /**
     * 判断站点是否在线
     *
     * @param stationNo
     * @return
     */
    Map<String, Object> checkStationIsOnline(String stationNo);


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
     * @return
     */
    long deleteStationInfo(String stationNo);

    /**
     * 删除站点小该底图下所有设备
     *
     * @param stationNo
     * @param bgDevImg
     * @param devNo
     * @return
     */
    long deleteBgDevImg(String stationNo, String bgDevImg, Long devNo);

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
     * 批量更新设备
     *
     * @param deviceInfoList
     * @return
     */
    void batchUpdateDeviceInfo(List<DeviceInfo> deviceInfoList);

    /**
     * 线程处理
     *
     * @param deviceInfoList
     * @return
     */
    String batchUpdateDeviceInfoThread(List<DeviceInfo> deviceInfoList);


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
     * @param devNo
     * @param varListFieldsMd5
     * @return total
     */
    Long checkDeviceExist(String stationNo, Long devNo, String varListFieldsMd5);


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
     * @param isSelfCheck
     * @return
     */
    Device getDeviceLiveData(String stationNo, Long devNo, boolean isSelfCheck);

    /**
     * 文件上传
     *
     * @param binaryFile
     */
    String uploadBinaryFile(MultipartFile binaryFile);

    /**
     * 下载文件
     *
     * @param filePath
     * @param response
     * @return
     */
    boolean downloadBinaryFile(String filePath, HttpServletResponse response);

    /**
     * 将旧的设备的是否显示变更为不显示，成为历史
     *
     * @param devNo
     */
    void updateToHistory(Long devNo);

}
