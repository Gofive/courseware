package com.timeanime.courseware.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.timeanime.courseware.Protocol.ProtocolResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * rest服务
 */
@RestController
@RequestMapping("/time/v1")
public class RemoteTimeController {

    private static final DateTimeFormatter DEFALUT_FORMATER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取时间服务
     *
     * @param format 自定义格式
     * @return 时间
     */
    @GetMapping("/now")
    public ProtocolResult<String> now(@RequestParam(name = "format", required = false) String format) {
        String time;
        try {
            if (StringUtils.isEmpty(format)) {
                time = LocalDateTime.now().format(DEFALUT_FORMATER);
            } else {
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
                time = LocalDateTime.now().format(dateTimeFormatter);
            }
            return new ProtocolResult<>(0, "success", time);
        } catch (Exception e) {
            return new ProtocolResult<>(-1, e.getMessage());
        }
    }
}
