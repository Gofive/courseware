package com.timeanime.courseware.mapper;

import com.timeanime.courseware.entity.Code;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface CodeMapper {

    @Select("select * from valid_code")
    List<Code> findAllCode();

    @Select("select * from valid_code where id = #{id}")
    Code findCodeById(@Param("id") int id);

    @Select("select * from valid_code where num = #{num}")
    Code findCodeByNum(@Param("num") String num);

    @Update("update valid_code set is_used = #{isUsed},device_id = #{deviceId},used_time=now() where num = #{num}")
    int updateCodeByNum(@Param("num") String num, @Param("deviceId") String deviceId, @Param("isUsed") int isUsed);
}
