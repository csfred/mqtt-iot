package com.iot.mapper;

import com.iot.entity.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * TODO
 *
 * @author cs
 * @since 2020/11/17 15:56
 */
@Mapper
@Repository
public interface DeviceMapper {


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
     * 获取所有站点信息集合
     *
     * @return
     */
    List<StationInfo> getPageAllStationInfo(@Param("page") Integer page,
                                            @Param("pageSize") Integer pageSize,
                                            @Param("stationName") String stationName);

    /**
     * 根据站点名称查找总数
     *
     * @param stationName
     * @return
     */
    Long getPageAllStationInfoTotal(@Param("stationName") String stationName);

    /**
     * 根据站点编号删除站点信息
     *
     * @param stationNo
     */
    long deleteStationInfo(@Param("stationNo") String stationNo);

    /**
     * 保存设备信息
     *
     * @param param 参数
     */
    void saveDevice(Device param);

    /**
     * 保存设备信息
     *
     * @param deviceInfo
     */
    long saveDeviceInfo(DeviceInfo deviceInfo);

    /**
     * 根据站点编号获取站点下所有设备
     *
     * @param stationNo
     * @return
     */
    List<DeviceInfo> getDeviceInfoByStationNo(@Param("stationNo") String stationNo);

    /**
     * 根据站点编号，设备编号更新设备信息
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

    void updateSameDeviceCounter(@Param("endReceiveTime") String endReceiveTime,
                                 @Param("stationNo") String stationNo,
                                 @Param("varListFieldsMd5") String varListFieldsMd5);


    /**
     * 检查VarList是否相同
     *
     * @param varList4FieldsMd5 参数，值 MD5
     * @param stationNo         站点编号
     * @return
     */
    Long checkDeviceExist(@Param("stationNo") String stationNo,
                          @Param("varList4FieldsMd5") String varList4FieldsMd5);

    /**
     * 获取站点下所有设备种类信息
     *
     * @param stationNo 站点
     * @return List
     */
    List<DeviceInfo> getDeviceInfoAll(@Param("stationNo") String stationNo);


    /**
     * 获取所有设备类别信息 ID， 名称
     *
     * @return
     */
    List<DeviceType> getAllDeviceType();

    /**
     * 获取设备实时数据
     *
     * @param deviceLiveData
     * @return
     */
    Device getDeviceLiveData(DeviceLiveData deviceLiveData);
}
