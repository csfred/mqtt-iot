package com.iot.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.iot.entity.*;
import com.iot.mapper.DeviceMapper;
import com.iot.service.DeviceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.apache.logging.log4j.util.StringBuilders;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
    public void saveStationInfo(StationInfo stationInfo) {
        try {
            log.debug("===saveStationInfo start== stationInfo=" + JSON.toJSONString(stationInfo));
            long ret = deviceMapper.saveStationInfo(stationInfo);
            log.debug("======saveStationInfo end =ret=" + ret);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void updateStationInfo(StationInfo stationInfo) {
        try {
            log.debug("===stationInfo start== stationInfo=" + JSON.toJSONString(stationInfo));
            long ret = deviceMapper.updateStationInfo(stationInfo);
            log.debug("======stationInfo end =ret=" + ret);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
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
    public void deleteStationInfo(String stationNo) {
        try {
            deviceMapper.deleteStationInfo(stationNo);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
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
    public void saveDeviceInfo(DeviceInfo deviceInfo) {
        try {
            log.debug("===saveDeviceInfo start== deviceInfo=" + JSON.toJSONString(deviceInfo));
            long ret = deviceMapper.saveDeviceInfo(deviceInfo);
            log.debug("======saveDeviceInfo end =ret=" + ret);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public List<DeviceInfo> getDeviceInfoByStationNo(String stationNo) {
        try {
            return deviceMapper.getDeviceInfoByStationNo(stationNo);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public void updateDeviceInfo(DeviceInfo deviceInfo) {
        try {
            log.debug("===updateDeviceInfo start== deviceInfo=" + JSON.toJSONString(deviceInfo));
            long ret = deviceMapper.updateDeviceInfo(deviceInfo);
            log.debug("======updateDeviceInfo end =ret=" + ret);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
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
    public String uploadBinaryFile(Integer type, MultipartFile binaryFile) {
        StringBuilder pathBuilder = new StringBuilder();
        pathBuilder.append(uploadPath);
        String relateFilePath = "";
        switch (type) {
            case 1:
                //设备
                if (uploadPath.endsWith("/")) {
                    pathBuilder.append("device/");
                } else {
                    pathBuilder.append("/device/");
                }
                relateFilePath += "/device/";
                break;
            case 2:
                if (uploadPath.endsWith("/")) {
                    pathBuilder.append("water/");
                } else {
                    pathBuilder.append("/water/");
                }
                relateFilePath += "/water/";
                break;
            default:
                pathBuilder = null;
                break;
        }
        if (null == pathBuilder) {
            return "";
        }
        String formatTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        pathBuilder.append(formatTime).append("/");
        relateFilePath += formatTime + "/";
        String targetFilePath = pathBuilder.toString();

        String fileName = binaryFile.getOriginalFilename();
        String fullFileName = targetFilePath + fileName;
        relateFilePath += fileName;
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
        return relateFilePath;
    }

    @Override
    public boolean downloadBinaryFile(Integer type, String filePath, HttpServletResponse response) {
        try {
            StringBuilder pathBuilder = new StringBuilder();
            pathBuilder.append(uploadPath);
            switch (type) {
                case 1:
                    //设备
                    if (uploadPath.endsWith("/")) {
                        pathBuilder.append("device/");
                    } else {
                        pathBuilder.append("/device/");
                    }
                    break;
                case 2:
                    if (uploadPath.endsWith("/")) {
                        pathBuilder.append("water/");
                    } else {
                        pathBuilder.append("/water/");
                    }
                    break;
                default:
                    pathBuilder = null;
                    break;
            }
            if (null == pathBuilder) {
                return false;
            }
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
            String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();
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
