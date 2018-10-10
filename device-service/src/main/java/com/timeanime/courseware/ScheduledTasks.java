package com.timeanime.courseware;

import com.timeanime.courseware.service.ValidRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class ScheduledTasks {

    @Autowired
    ValidRecordService validRecordService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void delExpiredRecord() {
        //遍历数据库删除所有过期激活记录
        validRecordService.findAndDelExpiredRecord();
    }

}
