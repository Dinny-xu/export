package com.example.web.pojo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class WlwDevice implements Serializable {
    /**
     * id
     */
    private String id;

    /**
     * 设备编码
     */
    private String spCode;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 用户id
     */
    private Integer deviceUserId;

    /**
     * 用户名
     */
    private String deviceUserName;

    /**
     * 地址
     */
    private String deviceAddress;

    /**
     * 详细地址
     */
    private String deviceAddressDetail;

    /**
     * 设备imei
     */
    private String deviceImei;

    /**
     * 户型大小
     */
    private Integer houseTypeSize;

    /**
     * 户型
     */
    private String houseType;

    /**
     * 厂家
     */
    private String manufacturer;

    /**
     * 纬度
     */
    private Double lat;

    /**
     * 经度
     */
    private Double lng;

    private static final long serialVersionUID = 1L;

}

