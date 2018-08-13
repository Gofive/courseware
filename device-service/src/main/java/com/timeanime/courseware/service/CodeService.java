package com.timeanime.courseware.service;

import com.timeanime.courseware.DeviceConstants;
import com.timeanime.courseware.entity.Code;
import com.timeanime.courseware.mapper.CodeMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CodeService {

    @Resource
    private CodeMapper codeMapper;

    public int isCodeValid(String num){
        Code code = codeMapper.findCodeByNum(num);

        if (code == null) {
            return DeviceConstants.CODE_NOT_EXISTED;
        }else if (code.getIsUsed() == 1){
            return DeviceConstants.CODE_IS_USED;
        }

        return DeviceConstants.CODE_IS_VALID;
    }

    public Code findCodeByNum(String num){
        return codeMapper.findCodeByNum(num);
    }

    public int updateCodeByNum(String num, String deviceId){
        //消费激活码
        return codeMapper.updateCodeByNum(num,deviceId,1);
    }
}
