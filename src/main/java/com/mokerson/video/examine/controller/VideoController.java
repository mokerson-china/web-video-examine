package com.mokerson.video.examine.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mokerson.video.examine.module.IotMonitoryVo;
import com.mokerson.video.examine.module.StatisticsConfig;
import com.mokerson.video.examine.service.IMonitoryDeviceInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/VideoController")
public class VideoController {

    private static List<IotMonitoryVo> monitors;
    private static StatisticsConfig config;
    @Autowired
    private IMonitoryDeviceInfo monitoryDeviceInfo;

    @RequestMapping("/update")
    public String update(HttpServletResponse response) {
        log.info("正在更新输出信息，请稍等...");
        String path = "导出失败，请检查参数";
        log.info("已更新物联网平台接入信息");
        return path;
    }


    @RequestMapping(value = "/getMonitoryPage", method = RequestMethod.POST)
    public String getMonitoryPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        config = new StatisticsConfig();
        config.setMonitoryApiHost(request.getParameter("monitoryApiHost"));
        config.setMonitoryApiPort(request.getParameter("monitoryApiPort"));
        config.setMainId(request.getParameter("mainId"));
        config.setMonitoryApiAccount(request.getParameter("monitoryApiAccount"));
        config.setMonitoryApiPassword(request.getParameter("monitoryApiPassword"));

        // 获取设备的列表
        monitors = monitoryDeviceInfo.getFlvVideoPlay(config);

        // 获取FLV播放视频流
        Map<Object, Object> map = new HashMap<>();
        map.put("deviceInfo", monitors);
        map.put("msg", "success");
        String jsonMap = new ObjectMapper().writeValueAsString(map);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(jsonMap);
        return "flv-start.html";
    }
}
