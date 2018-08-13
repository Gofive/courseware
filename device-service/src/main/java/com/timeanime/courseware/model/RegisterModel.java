package com.timeanime.courseware.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterModel {

    @ApiModelProperty(value = "设备ID",required=true) private String deviceId;

    @ApiModelProperty(value = "学校全称",required=true)private String schoolName;

    @ApiModelProperty(value = "激活码",required=true)private String codeNum;

    @ApiModelProperty(value = "课程类别",required=true)private int courseName;

    @ApiModelProperty(value = "设备类型",required=true)private int deviceType;
}
