package com.timeanime.courseware.controller;

import com.timeanime.courseware.DeviceConstants;
import com.timeanime.courseware.model.RegisterModel;
import com.timeanime.courseware.service.CodeService;
import com.timeanime.courseware.service.DeviceService;
import com.timeanime.courseware.service.ValidRecordService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;


import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/v1")
@EnableAutoConfiguration
@Api(value = "激活码管理", description = "激活码验证，生成，查询")
public class ValidProcessController {

    @Autowired
    private CodeService codeService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private ValidRecordService validRecordService;


    @ApiOperation(value = "查询所有激活码")
    @RequestMapping(value = "/code", method = RequestMethod.GET)
    Map<String, Object> findAllCode(@RequestParam(value = "currentPage",required = false,defaultValue = "1") int currentPage,
                                    @RequestParam(value = "pageSize",required = false,defaultValue = "10") int pageSize){
//        return codeService.findAllCode();

        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> pagination = new HashMap<>();

        pagination.put("total", codeService.findAllCode().size());
        pagination.put("pageSize", pageSize);
        pagination.put("current", currentPage);
        resultMap.put("pagination", pagination);
        resultMap.put("list", codeService.findAllCode());
        return resultMap;
    }

    @ApiOperation(value = "激活码验证")
    @ApiImplicitParam(name = "num",
            value = "激活码字符串",
            dataType = "String",
            required = true,
            paramType = "query")
    @RequestMapping(value = "/codeValid", method = RequestMethod.GET)
    int codeValid(@RequestParam String num){
        return codeService.isCodeValid(num);
    }

    @ApiOperation(value = "获取标准时间")
    @RequestMapping(value = "/time" , method = RequestMethod.GET)
    String currentTime(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        String BeijingTime = dateFormat.format(new Date());
        return BeijingTime;
    }

    @ApiOperation(value = "激活信息写入")
    @RequestMapping(value = "/valid" , method = RequestMethod.POST)
    @ApiImplicitParam(name = "validRecord", value = "激活信息", required = true, dataType = "RegisterModel")
    public Map<String,Object> deviceValid(@RequestBody RegisterModel validRecord) {

        Map<String,Object> map = null;
        try {
            map= validRecordService.valid(validRecord);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("message", e);
        }
        return map;
    }

    @ApiOperation(value = "获取设备激活状态")
    @RequestMapping(value = "/status" , method = RequestMethod.GET)
    @ApiImplicitParams({
        @ApiImplicitParam(name = "deviceId", value = "设备ID", required = true, dataType = "String"),
        @ApiImplicitParam(name = "courseName", value = "课程类别", required = true, dataType = "int"),
    })
    Map<String,Object> deviceStatus(@RequestParam String deviceId, int courseName){

        Map<String,Object> map = new HashMap<String,Object>();
        map = validRecordService.getDevice(deviceId,courseName);
        if (map!=null){
            return map;
        }

        map = new HashMap<String,Object>();
        map.put("message","record not exited");
        map.put("code", DeviceConstants.VALID_FAILED);

        return map;
    }

}
