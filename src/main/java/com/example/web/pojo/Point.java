package com.example.web.pojo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * 坐标点
 *
 * @author TripleH
 * @date 2020-11-01
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("坐标点")
public class Point implements Serializable {
    /**
     * 经度
     */
    @ApiModelProperty(value = "经度")
    private double lng;
    /**
     * 纬度
     */
    @ApiModelProperty(value = "纬度")
    private double lat;
}