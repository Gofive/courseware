package com.timeanime.courseware.service;


import com.timeanime.courseware.entity.Device;
import com.timeanime.courseware.mapper.DeviceMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class DeviceService {

    @Resource
    DeviceMapper deviceMapper;

    public int registerDevice(Device device){

        deviceMapper.insertDevice(device);
        return device.getId();
    }

    public Device findDevice(String deviceId){

        return deviceMapper.findDevice(deviceId);
    }
}
