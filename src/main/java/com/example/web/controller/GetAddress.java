package com.example.web.controller;

import com.example.web.dao.GetAddressMapper;
import com.example.web.pojo.WlwDevice;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: test_pdf
 * @description: 根据地址获取经纬度
 * @author: Xu·yan
 * @create: 2020-12-16 11:04
 **/
@Api(tags = "坐标转换")
@RestController
public class GetAddress {

    @Resource
    private GetAddressMapper getAddressMapper;

    @GetMapping("/address")
    @ApiOperationSupport(author = "xyy")
    @ApiOperation(value = "通过地址获取经纬度")
    public String getAddress() {
//        List<WlwDevice> wlwDevice = getAddressMapper.selectAll();
        List<WlwDevice> wlwDevice = getAddressMapper.selectList(null);
    /*    wlwDevice.forEach(x->{
            WlwDevice wlwDevice1 = new WlwDevice();
            Point point = gcj02ToWgs84(x.getLng(), x.getLat());
            wlwDevice1.setLng(point.getLng());
            wlwDevice1.setLat(point.getLat());
            getAddressMapper.updateById(wlwDevice1,x.getId());
        });*/


     /*   wlwDevice.forEach(x -> {
            WlwDevice wlwDevice1 = new WlwDevice();
            try {
                String[] coordinate = getCoordinate(x.getDeviceAddress());
                String lng = coordinate[0];
                String lat = coordinate[1];
                wlwDevice1.setLng(lng);
                wlwDevice1.setLat(lat);
                getAddressMapper.updateById(wlwDevice1,x.getId());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });*/
        return "ok";
    }
}
