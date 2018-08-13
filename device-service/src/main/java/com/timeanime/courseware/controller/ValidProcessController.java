package com.timeanime.courseware.controller;

import com.timeanime.courseware.DeviceConstants;
import com.timeanime.courseware.entity.Code;
import com.timeanime.courseware.entity.Device;
import com.timeanime.courseware.entity.ValidRecord;
import com.timeanime.courseware.mapper.CodeMapper;
import com.timeanime.courseware.model.RegisterModel;
import com.timeanime.courseware.service.CodeService;
import com.timeanime.courseware.service.DeviceService;
import com.timeanime.courseware.service.ValidRecordService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
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
    @Transactional
    Map<String,Object> deviceValid(@RequestBody RegisterModel validRecord) throws ParseException, SQLException {

        Map<String,Object> map;
        String deviceId = validRecord.getDeviceId();
        String codeNum = validRecord.getCodeNum();
        Code code = codeService.findCodeByNum(codeNum);

        //设备是否已经激活
        map = getDevice(validRecord.getDeviceId(), validRecord.getCourseName());
        if (map!=null){
            return map;
        }

        //验证激活码有效性
        map = new HashMap<String,Object>();
        if (code == null){
            map.put("message","code not existed");
            map.put("code", DeviceConstants.CODE_NOT_EXISTED);
            return map;
        }else if (code.getIsUsed()==1){
            map.put("message","code is used");
            map.put("code", DeviceConstants.CODE_IS_USED);
            return map;
        }else if(code.getCourse()!=validRecord.getCourseName()){
            map.put("message","course not match");
            map.put("code", DeviceConstants.COURSE_NOT_MATCH);
            return map;
        }

        //计算过期时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date expireDate = sdf.parse(sdf.format(new Date()));
        Calendar cal = Calendar.getInstance();
        cal.setTime(expireDate);
        cal.add(Calendar.DATE,code.getDuration());
        expireDate = cal.getTime();

        Date validDate = new Date();
        Timestamp validTime = new Timestamp(validDate.getTime());
        Timestamp expireTime = new Timestamp(expireDate.getTime());

        ValidRecord validRecord1 = new ValidRecord();
        validRecord1.setCourseName(validRecord.getCourseName());
        validRecord1.setDeviceId(validRecord.getDeviceId());
        validRecord1.setCodeNum(validRecord.getCodeNum());
        validRecord1.setValidTime(validTime);
        validRecord1.setExpireTime(expireTime);

        //更新验证码状态
        int index = codeService.updateCodeByNum(validRecord.getCodeNum(),validRecord.getDeviceId());
        if (index<1){
            throw new SQLException();
        }
        //查询设备表，无则注册
        if (deviceService.findDevice(validRecord.getDeviceId())==null){
            Date date = new Date();
            Timestamp ts = new Timestamp(date.getTime());

            Device device = new Device();
            device.setDeviceType(validRecord.getDeviceType());
            device.setDeviceId(validRecord.getDeviceId());
            device.setSchoolName(validRecord.getSchoolName());
            device.setTimeRegister(ts);
            deviceService.registerDevice(device);
        }
        //写入激活信息
        int id = validRecordService.insertValidRecord(validRecord1);
        map = new HashMap<String,Object>();
        map.put("message","device is valid @"+validTime.toString());
        map.put("code", DeviceConstants.VALID_SUCCESS);
        map.put("record",validRecordService.getValidRecordById(id));
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
        map = getDevice(deviceId,courseName);
        if (map!=null){
            return map;
        }

        map = new HashMap<String,Object>();
        map.put("message","record not exited");
        map.put("code", DeviceConstants.VALID_FAILED);

        return map;
    }

    private  Map<String,Object> getDevice(String deviceId, int courseName){
        Map<String,Object> map = new HashMap<String,Object>();
        ValidRecord validRecord = validRecordService.getValidRecord(deviceId, courseName);
        if (validRecord!= null){
            map.put("message","device is valid @"+validRecord.getValidTime().toString());
            map.put("code", DeviceConstants.DEVICE_IS_VALID);
            map.put("record", validRecord);
            return map;
        }

        return null;
    }

}
