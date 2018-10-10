package com.timeanime.courseware.service;


import com.timeanime.courseware.DeviceConstants;
import com.timeanime.courseware.entity.Code;
import com.timeanime.courseware.entity.Device;
import com.timeanime.courseware.entity.ValidRecord;
import com.timeanime.courseware.mapper.CodeMapper;
import com.timeanime.courseware.mapper.ValidRecordMapper;
import com.timeanime.courseware.model.RegisterModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ValidRecordService {

    @Resource
    ValidRecordMapper validRecordMapper;

    @Resource
    CodeService codeService;

    @Resource
    DeviceService deviceService;

    public int insertValidRecord(ValidRecord validRecord){

        validRecordMapper.insertValidRecord(validRecord);
        return validRecord.getId();
    }

    public ValidRecord getValidRecord(String deviceId, int courseName){
        ValidRecord validRecord = new ValidRecord();
        validRecord.setDeviceId(deviceId);
        validRecord.setCourseName(courseName);
        return validRecordMapper.findRecordByDeviceIdAndCourseName(validRecord);
    }

    public ValidRecord getValidRecordById(int id){
        return validRecordMapper.findRecordById(id);
    }

    @Transactional(rollbackFor = SQLException.class)
    public Map<String,Object> valid(RegisterModel validRecord) throws SQLException, ParseException {

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
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        Date expireDate = sdf.parse(sdf.format(new Date()));
        Calendar cal = Calendar.getInstance();
        cal.setTime(expireDate);
        cal.add(Calendar.DATE,code.getDuration());
        expireDate = cal.getTime();

        Date validDate = sdf.parse(sdf.format(new Date()));
        Timestamp validTime = new Timestamp(validDate.getTime());
        Timestamp expireTime = new Timestamp(expireDate.getTime());
        System.out.print(validTime);

        ValidRecord validRecord1 = new ValidRecord();
        validRecord1.setCourseName(validRecord.getCourseName());
        validRecord1.setDeviceId(validRecord.getDeviceId());
        validRecord1.setCodeNum(validRecord.getCodeNum());
        validRecord1.setValidTime(validTime);
        validRecord1.setExpireTime(expireTime);

        //更新验证码状态
        if (codeService.updateCodeByNum(validRecord.getCodeNum(),validRecord.getDeviceId())<1){
            throw new SQLException();
        }
        //查询设备表，无则注册
        if (deviceService.findDevice(validRecord.getDeviceId())==null){
            Date date = sdf.parse(sdf.format(new Date()));
            Timestamp ts = new Timestamp(date.getTime());

            Device device = new Device();
            device.setDeviceType(validRecord.getDeviceType());
            device.setDeviceId(validRecord.getDeviceId());
            device.setSchoolName(validRecord.getSchoolName());
            device.setTimeRegister(ts);
            deviceService.registerDevice(device);
        }
        //写入激活信息
        int id = insertValidRecord(validRecord1);
        map = new HashMap<String,Object>();
        map.put("message","device is valid @"+validTime.toString());
        map.put("code", DeviceConstants.VALID_SUCCESS);
        map.put("record",getValidRecordById(id));
        return map;
    }

    public  Map<String,Object> getDevice(String deviceId, int courseName){
        Map<String,Object> map = new HashMap<String,Object>();
        ValidRecord validRecord = getValidRecord(deviceId, courseName);
        if (validRecord!= null){
            map.put("message","device is valid @"+validRecord.getValidTime().toString());
            map.put("code", DeviceConstants.DEVICE_IS_VALID);
            map.put("record", validRecord);
            return map;
        }

        return null;
    }

    public void findAndDelExpiredRecord(){
        Date date = new Date();
        System.out.println(date);
        List<ValidRecord> list = validRecordMapper.findAll();
        for (int i=0;i<list.size();i++){
            ValidRecord record = list.get(i);
            Timestamp epDate = record.getExpireTime();
            System.out.println(epDate);
            if (epDate.before(date)){
                validRecordMapper.delExpRecord(record);
//                System.out.println("已经过期");
            }
        }
    }
}
