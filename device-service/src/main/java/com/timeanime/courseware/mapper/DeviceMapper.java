package com.timeanime.courseware.mapper;

import com.timeanime.courseware.entity.Device;
import org.apache.ibatis.annotations.*;

@Mapper
public interface DeviceMapper {


    @Insert("insert into ta_device (device_id, device_type, school_name, time_register) " +
            "values (#{deviceId}, #{deviceType}, #{schoolName}, #{timeRegister})")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    void insertDevice(Device device);

    @Select("select * from ta_device where device_id = #{deviceId}")
    Device findDevice(@Param("deviceId") String deviceId);
}
