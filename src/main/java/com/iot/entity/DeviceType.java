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
public class DeviceType implements Serializable {

    private Integer id;

    private String devTypeName;
}
