package com.timeanime.courseware.service;


import com.timeanime.courseware.entity.ValidRecord;
import com.timeanime.courseware.mapper.ValidRecordMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ValidRecordService {

    @Resource
    ValidRecordMapper validRecordMapper;

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
}
