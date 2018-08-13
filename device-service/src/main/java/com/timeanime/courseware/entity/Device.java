package com.timeanime.courseware.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class Device implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;

    private String deviceId;

    private String schoolName;

    private int deviceType;

    private Timestamp timeRegister;

}
