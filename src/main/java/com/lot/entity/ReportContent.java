package com.lot.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * TODO
 *
 * @author cs
 * @since 2021/06/05
 */
@Data
@Accessors(chain = true)
public class ReportContent implements Serializable {

    private String stationNo;

    private String devName;

    private String devType;

    private String varList4Fields;

    private Integer receiveCount;

    private String startReceiveTime;

    private String endReceiveTime;

    private String timePoint;

    public static ReportContent copy(ReportContent reportContent) {
        ReportContent reportContentRet = new ReportContent();
        reportContentRet.setStationNo(reportContent.getStationNo());
        reportContentRet.setDevName(reportContent.getDevName());
        reportContentRet.setDevType(reportContent.getDevType());
        reportContentRet.setVarList4Fields(reportContent.getVarList4Fields());
        reportContentRet.setReceiveCount(reportContent.getReceiveCount());
        reportContentRet.setStartReceiveTime(reportContent.getStartReceiveTime());
        reportContentRet.setEndReceiveTime(reportContent.getEndReceiveTime());
        reportContentRet.setTimePoint(reportContent.getTimePoint());
        return reportContentRet;
    }
}
