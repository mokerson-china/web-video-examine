package com.mokerson.video.examine.module;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author MokersonForTGZ
 */
@Setter
@Getter
@ToString
public class StatisticsConfig {

    /**
     * 视频导出配置
     */
    private String monitoryApiHost = "";
    private String monitoryApiPort = "";
    private String monitoryApiAccount = "";
    private String monitoryApiPassword = "";
    private String mainId;
    private String pageSize;
    private String pageNo;
    private String isTcp;

}
