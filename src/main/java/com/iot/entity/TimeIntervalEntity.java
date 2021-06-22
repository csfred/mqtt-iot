package com.iot.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * TODO
 *
 * @author cs
 * @since 2021/06/03
 */
@Data
@Accessors(chain = true)
public class TimeIntervalEntity implements Serializable {

    private Integer id;

    private Integer timeInterval;

    private Integer unit;
}
