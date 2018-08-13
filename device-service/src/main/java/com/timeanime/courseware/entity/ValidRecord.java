package com.timeanime.courseware.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;


@Data
@NoArgsConstructor
@ApiModel
public class ValidRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("设备ID")
    private String deviceId;

    @ApiModelProperty("课程类别")
    private int courseName;

    @ApiModelProperty("激活码")
    private String codeNum;

    @ApiModelProperty(hidden = true)
    private Timestamp expireTime;

    @ApiModelProperty(hidden = true)
    private Timestamp validTime;

    @ApiModelProperty(hidden = true)
    private int id;

}
