package com.lot.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * TODO
 *
 * @author cs
 * @since 2021/06/05
 */
@Data
@Accessors(chain = true)
public class ReportContentEntity implements Serializable {

    private String varList;

    private String varFieldMd5;

    private Timestamp receiveTime;
}
