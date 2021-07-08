package com.iot.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.iot.entity.*;
import com.iot.mapper.DeviceMapper;
import com.iot.service.DeviceService;
import com.iot.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.apache.logging.log4j.util.StringBuilders;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * TODO
 *
 * @author cs
 * @since 2020/11/17 15:55
 */
@Slf4j
@Service
public class DeviceServiceImpl implements DeviceService {

    @Resource
    private DeviceMapper deviceMapper;

    @Value("${upload.path}")
    private String uploadPath;


    @Override
    public long saveStationInfo(StationInfo stationInfo) {
        long ret = -1;
        try {
            ret = deviceMapper.saveStationInfo(stationInfo);
        } catch (Exception e) {
            log.error("saveStationInfo stationInfo={}, errorMsg={}", JSON.toJSONString(stationInfo), e.getMessage());
        }
        return ret;
    }

    @Override
    public long updateStationInfo(StationInfo stationInfo) {
        long ret = -1;
        try {
            log.error("updateStationInfo stationInfo={}, bgDevImgPath={}", JSON.toJSONString(stationInfo), stationInfo.getBgDevImgPath());
            ret = deviceMapper.updateStationInfo(stationInfo);
        } catch (Exception e) {
            log.error("updateStationInfo stationInfo={}, errorMsg={}", JSON.toJSONString(stationInfo), e.getMessage());
        }
        return ret;
    }

    @Override
    public JSONObject getPageAllStationInfo(@RequestParam("page") Integer page,
                                            Integer pageSize,
                                            String stationName) {
        String stationNameLike = "%" + stationName + "%";
        Long total = deviceMapper.getPageAllStationInfoTotal(stationNameLike);
        if (page > 0) {
            page = page - 1;
        }
        List<StationInfo> retList = null;
        try {
            retList = deviceMapper.getPageAllStationInfo(page, pageSize, stationNameLike);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        JSONObject retObject = new JSONObject(8);
        retObject.put("total", total);
        retObject.put("dataList", retList);
        return retObject;
    }

    @Override
    public long deleteStationInfo(String stationNo) {
        long ret = -1;
        try {
            ret = deviceMapper.deleteStationInfo(stationNo);
        } catch (Exception e) {
            log.error("deleteStationInfo stationNo={}, errorMsg={}", stationNo, e.getMessage());
        }
        return ret;
    }

    @Override
    public long deleteBgDevImg(String stationNo, String bgDevImg, Long devNo) {
        long ret = -1;
        try {
            if (null != devNo) {
                List<Long> devNoList = new ArrayList<>(4);
                devNoList.add(devNo);
                return deviceMapper.deleteDeviceInfo(devNoList);
            }
            List<DeviceInfo> dataListTmp = deviceMapper.getDeviceInfoByStationNo(stationNo);
            if (CollectionUtils.isEmpty(dataListTmp)) {
                return 0;
            }
            log.error("deleteBgDevImg dataListTmp={}", JSON.toJSONString(dataListTmp));
            List<Long> devNoList = new ArrayList<>(8);
            for (DeviceInfo deviceInfo : dataListTmp) {
                JSONObject deviceVector = JSON.parseObject(deviceInfo.getDeviceVector());
                if (deviceVector != null && !deviceVector.isEmpty()) {
                    if (bgDevImg.equalsIgnoreCase(deviceVector.getString("bgDevImg"))) {
                        devNoList.add(deviceInfo.getDevNo());
                    }
                }
            }
            if (!CollectionUtils.isEmpty(devNoList)) {
                log.error("deleteBgDevImg devNoList={}", JSON.toJSONString(devNoList));
                ret = deviceMapper.deleteDeviceInfo(devNoList);
            }
            if (ret > 0) {
                StringBuilder pathBuilder = new StringBuilder();
                pathBuilder.append(uploadPath);

                if (bgDevImg.startsWith("/")) {
                    pathBuilder.append(bgDevImg.substring(1));
                } else {
                    pathBuilder.append(bgDevImg);
                }
                File deleteFile = new File(pathBuilder.toString());
                if (deleteFile.isFile() && deleteFile.exists()) {
                    boolean retDel = deleteFile.delete();
                    log.error("deleteBgDevImg retDel={}", retDel);
                }
            }
            StationInfo stationInfo = deviceMapper.getStationInfoByNo(stationNo);
            if (null == stationInfo) {
                return ret;
            }
            log.error("deleteBgDevImg getBgDevImgPath={}", stationInfo.getBgDevImgPath());
            JSONArray jsonArray = JSON.parseArray(stationInfo.getBgDevImgPath());
            if (!CollectionUtils.isEmpty(jsonArray) && jsonArray.contains(bgDevImg)) {
                jsonArray.remove(bgDevImg);
                stationInfo.setBgDevImgPath(jsonArray.toJSONString());
                log.error("deleteBgDevImg jsonArray={}", jsonArray.toJSONString());
                deviceMapper.updateStationInfo(stationInfo);
            }

        } catch (Exception e) {
            log.error("deleteBgDevImg stationNo={}, bgDevImg={}, errorMsg={}", stationNo, bgDevImg, e.getMessage());
        }
        return ret;
    }

    @Override
    public void saveDevice(Device param) {
        try {
            deviceMapper.saveDevice(param);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public long saveDeviceInfo(DeviceInfo deviceInfo) {
        long ret = -1;
        try {
            ret = deviceMapper.saveDeviceInfo(deviceInfo);
        } catch (Exception e) {
            log.error("saveDeviceInfo deviceInfo={}, errorMsg={}", JSON.toJSONString(deviceInfo), e.getMessage());
        }
        return ret;
    }

    @Override
    public List<DeviceInfo> getDeviceInfoByStationNo(String stationNo, String bgDevImg) {
        List<DeviceInfo> dataList = new ArrayList<>(8);
        try {
            List<DeviceInfo> dataListTmp = deviceMapper.getDeviceInfoByStationNo(stationNo);
            if (StringUtils.isEmpty(bgDevImg)) {
                dataList.addAll(dataListTmp);
            } else {
                if (!CollectionUtils.isEmpty(dataListTmp)) {
                    for (DeviceInfo deviceInfo : dataListTmp) {
                        JSONObject deviceVector = JSON.parseObject(deviceInfo.getDeviceVector());
                        if (deviceVector != null && !deviceVector.isEmpty()) {
                            if (bgDevImg.equalsIgnoreCase(deviceVector.getString("bgDevImg"))) {
                                dataList.add(deviceInfo);
                            }
                        }
                    }
                }
            }
            return dataList;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public long updateDeviceInfo(DeviceInfo deviceInfo) {
        long ret = -1;
        try {
            ret = deviceMapper.updateDeviceInfo(deviceInfo);
        } catch (Exception e) {
            log.error("updateDeviceInfo deviceInfo={}, errorMsg={}", JSON.toJSONString(deviceInfo), e.getMessage());
        }
        return ret;
    }


    @Override
    public void updateSameDeviceCounter(String endReceiveTime,
                                        String stationNo,
                                        String varListFieldsMd5) {

        deviceMapper.updateSameDeviceCounter(endReceiveTime, stationNo, varListFieldsMd5);
    }

    @Override
    public Long checkDeviceExist(String stationNo, String varListFieldsMd5) {
        return deviceMapper.checkDeviceExist(stationNo, varListFieldsMd5);
    }


    @Override
    public List<DeviceInfo> getDeviceInfoAll(@Param("stationNo") String stationNo) {
        return deviceMapper.getDeviceInfoAll(stationNo);
    }

    @Override
    public List<DeviceType> getAllDeviceType() {
        return deviceMapper.getAllDeviceType();
    }

    @Override
    public Device getDeviceLiveData(String stationNo, Long devNo) {
        DeviceLiveData deviceLiveData = new DeviceLiveData();
        deviceLiveData.setDevNo(devNo);
        deviceLiveData.setStationNo(stationNo);
        LocalDateTime queryTime = LocalDateTime.now().minusSeconds(20);
        deviceLiveData.setQueryTime(Timestamp.valueOf(queryTime));

        return deviceMapper.getDeviceLiveData(deviceLiveData);
    }

    @Override
    public String uploadBinaryFile(MultipartFile binaryFile) {
        StringBuilder pathBuilder = new StringBuilder();
        pathBuilder.append(uploadPath);

        if (!pathBuilder.toString().endsWith("/")) {
            pathBuilder.append("/");
        }

        String fileName = binaryFile.getOriginalFilename();

        // 取得文件的后缀名。
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase(Locale.ROOT);
        String targetFileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + ext;
        pathBuilder.append(targetFileName);
        String fullFileName = pathBuilder.toString();

        File targetFile = new File(fullFileName);

        FileOutputStream fileOutputStream = null;
        try {
            if (!targetFile.exists()) {
                File parentFile = targetFile.getParentFile();
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }
                targetFile.createNewFile();
            }
            fileOutputStream = new FileOutputStream(targetFile);
            IOUtils.copy(binaryFile.getInputStream(), fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return targetFileName;
    }

    @Override
    public boolean downloadBinaryFile(String filePath, HttpServletResponse response) {
        try {
            StringBuilder pathBuilder = new StringBuilder();
            pathBuilder.append(uploadPath);

            if (filePath.startsWith("/")) {
                pathBuilder.append(filePath.substring(1));
            } else {
                pathBuilder.append(filePath);
            }
            String fullFileName = pathBuilder.toString();
            // path是指欲下载的文件的路径。
            File file = new File(fullFileName);
            // 取得文件名。
            String filename = file.getName();
            // 取得文件的后缀名。
            String ext = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase(Locale.ROOT);
            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(fullFileName));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }
}
