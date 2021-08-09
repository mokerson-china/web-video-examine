package com.mokerson.video.examine.controller;

import com.mokerson.video.examine.service.IMonitoryDeviceInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@Controller
@Slf4j
public class VideoController {

    @Autowired
    private IMonitoryDeviceInfo monitoryDeviceInfo;

    @RequestMapping("/update")
    public String update(HttpServletResponse response) {
        log.info("正在更新输出信息，请稍等...");
        String path = "导出失败，请检查参数";
        log.info("已更新物联网平台接入信息");
        return path;
    }


    @RequestMapping("/1")
    public String getIndex(){
        return "/templates/index.html";
    }
}
