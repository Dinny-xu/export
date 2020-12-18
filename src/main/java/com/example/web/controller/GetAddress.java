package com.example.web.controller;

import com.example.web.dao.GetAddressMapper;
import com.example.web.pojo.WlwDevice;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
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


    @Autowired
    private GetAddressMapper getAddressMapper;

    @GetMapping("/address")
    @ApiOperationSupport(author = "xyy")
    @ApiOperation(value = "通过地址获取经纬度")
    public String getAddress() {
        List<WlwDevice> wlwDevice = getAddressMapper.selectAll();

        wlwDevice.forEach(x -> {
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
        });
        return "ok";
    }


    /**
     * @param addr 查询的地址
     * @return
     * @throws IOException
     */
    public static String[] getCoordinate(String addr) throws IOException {
        String lng = null;//经度
        String lat = null;//纬度
        String address = null;
        try {
            address = java.net.URLEncoder.encode(addr, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        String key = "f247cdb592eb43ebac6ccd27f796e2d2";
        String url = String.format("http://api.map.baidu.com/geocoder?address=%s&output=json&key=%s", address, key);

        URL myURL = null;
        URLConnection httpsConn = null;
        try {
            myURL = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        InputStreamReader insr = null;
        BufferedReader br = null;
        try {
            httpsConn = (URLConnection) myURL.openConnection();// 不使用代理
            if (httpsConn != null) {
                insr = new InputStreamReader(httpsConn.getInputStream(), "UTF-8");
                br = new BufferedReader(insr);
                String data = null;
                int count = 1;
                while ((data = br.readLine()) != null) {
                    if (count == 5) {
                        lng = (String) data.subSequence(data.indexOf(":") + 1, data.indexOf(","));//经度
                        count++;
                    } else if (count == 6) {
                        lat = data.substring(data.indexOf(":") + 1);//纬度
                        count++;
                    } else {
                        count++;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (insr != null) {
                insr.close();
            }
            if (br != null) {
                br.close();
            }
        }
        return new String[]{lng, lat};
    }


    public static void main(String[] args) throws IOException {
        Object[] coordinate = getCoordinate("四川省 成都市 武侯区/川大/川大花园3-4-6");
        Object lng = coordinate[0];
        Object lat = coordinate[1];
        System.out.println("lng:" + lng);
        System.out.println("lat:" + lat);
    }
}
