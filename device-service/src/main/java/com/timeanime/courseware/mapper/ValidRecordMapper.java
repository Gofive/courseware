package com.timeanime.courseware.mapper;


import com.timeanime.courseware.entity.ValidRecord;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ValidRecordMapper {

    @Insert("insert into valid_record (device_id, course_name, expire_time, valid_time,code_num) " +
            "values (#{deviceId}, #{courseName}, #{expireTime}, #{validTime}, #{codeNum})")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    void insertValidRecord(ValidRecord validRecord);


    @Select("select * from valid_record where device_id = #{deviceId} and course_name = #{courseName}")
    ValidRecord findRecordByDeviceIdAndCourseName(ValidRecord validRecord);

    @Select("select * from valid_record where id = #{id}")
    ValidRecord findRecordById(@Param("id") int id);
}
