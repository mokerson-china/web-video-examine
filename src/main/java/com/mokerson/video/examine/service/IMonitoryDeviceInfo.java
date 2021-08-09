package com.mokerson.video.examine.service;

import com.mokerson.video.examine.module.IotMonitoryVo;
import com.mokerson.video.examine.module.StatisticsConfig;

import java.util.List;


public interface IMonitoryDeviceInfo {
	List<IotMonitoryVo> getIotMonitoryDeviceAll(StatisticsConfig config);

	List<IotMonitoryVo> getDeviceStateAll(StatisticsConfig config);

	List<IotMonitoryVo> monitoryPointSearch(StatisticsConfig config);

	//List<MonitoryPtzInfo> ptzQueryInfo(StatisticsConfig config);
}
