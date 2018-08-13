package com.timeanime.courseware.entity;

import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class Code implements Serializable {

    private static final long serialVersionUID = 1L;

    private String num;

    private String deviceId;

    private int course;

    private int duration;

    private int isUsed;

    private Timestamp createTime;

    private Timestamp usedTime;

}
