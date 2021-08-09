package com.mokerson.video.examine.module;

import lombok.Getter;
import lombok.Setter;

/**
 * @author MokersonForTGZ
 */
@Setter
@Getter
public class StatisticsConfig {

    /**
     * 视频导出配置
     */
    private String monitoryApiHost = "";
    private String monitoryApiPort = "";
    private String monitoryApiAccount = "";
    private String monitoryApiPassword = "";
    private String mainId;


}
