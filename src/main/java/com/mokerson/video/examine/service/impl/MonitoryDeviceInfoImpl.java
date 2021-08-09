package com.mokerson.video.examine.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.mokerson.video.examine.module.IotMonitoryVo;
import com.mokerson.video.examine.module.StatisticsConfig;
import com.mokerson.video.examine.service.IMonitoryDeviceInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MonitoryDeviceInfoImpl implements IMonitoryDeviceInfo {

	private static final Logger logger = LoggerFactory.getLogger(MonitoryDeviceInfoImpl.class);
	private static final RestTemplate restTemplate = new RestTemplate();
	private static HttpEntity<String> request;

	@Override
	public List<IotMonitoryVo> getIotMonitoryDeviceAll(StatisticsConfig config) {
		if (request == null) {
			getHeader(config);
		}
		List<IotMonitoryVo> stateList = getDeviceStateAll(config);
		List<IotMonitoryVo> searchList = monitoryPointSearch(config);
		for (IotMonitoryVo stvo : stateList) {
			for (IotMonitoryVo sevo : searchList) {
				if (stvo.getMainId().equals(sevo.getMainId()) && stvo.getSubId().equals(sevo.getSubId())) {
					stvo.setName(sevo.getName());
					break;
				}
			}
		}
		return stateList;
	}

	@Override
	public List<IotMonitoryVo> getDeviceStateAll(StatisticsConfig config) {
		String url = "http://" + config.getMonitoryApiHost() + ":" + config.getMonitoryApiPort()
				+ "/video/device/getDeviceStateAll";
		logger.info("视频设备状态查询访问地址为：{}", url);
		JSONObject response = restTemplate.postForObject(url, request, JSONObject.class);
		List<IotMonitoryVo> result = response.getJSONArray("result").toJavaList(IotMonitoryVo.class);
		return result;
	}

	@Override
	public List<IotMonitoryVo> monitoryPointSearch(StatisticsConfig config) {
		String url = "http://" + config.getMonitoryApiHost() + ":" + config.getMonitoryApiPort()
				+ "/video/device/monitoryPointSearch";
		logger.info("视频设备信息查询访问地址为：{}", url);
		JSONObject response = restTemplate.postForObject(url, request, JSONObject.class);
		List<IotMonitoryVo> result = response.getJSONArray("result").toJavaList(IotMonitoryVo.class);
		return result;
	}

	private String getToken(StatisticsConfig config) {
		Map<String, String> params = new HashMap<>();
		params.put("account", config.getMonitoryApiAccount());
		params.put("password", config.getMonitoryApiPassword());
		String url = "http://" + config.getMonitoryApiHost() + ":" + config.getMonitoryApiPort() + "/login";
		logger.info("视频设备获取Token访问地址为：{}", url);
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);

		request = new HttpEntity<String>(JSONObject.toJSONString(params), header);
		JSONObject response = restTemplate.postForObject(url, request, JSONObject.class, params);
		return response.getObject("result", JSONObject.class).getString("token");
	}

	private void getHeader(StatisticsConfig config) {
		// application/x-www-form-urlencoded传参为key,value形式，key,value必须为String类型，不能传文件；
		// multipart_form_data 也是key,value形式，value可以为文件。
		// map.add("token", getToken(config));
		HttpHeaders header = new HttpHeaders();
		// 需求需要传参为form-data格式
		header.setContentType(MediaType.APPLICATION_JSON);
		header.add("Auth", getToken(config));
		request = new HttpEntity<String>(header);
	}

/*	@Override
	public List<MonitoryPtzInfo> ptzQueryInfo(StatisticsConfig config) {
		//
		//

		String url = "http://" + config.getMonitoryApiHost() + ":" + config.getMonitoryApiPort()
				+ "/video/ptz/presetQuery";
		logger.info("视频统计PTZ查询访问地址：{}", url);
		JSONObject response = restTemplate.postForObject(url, request, JSONObject.class);
		String resultPtz = response.getString("result");

		HttpHeaders header = new HttpHeaders();
		// 需求需要传参为form-data格式
		header.setContentType(MediaType.APPLICATION_JSON);
		header.add("Auth", getToken(config));
		url = "http://" + config.getMonitoryApiHost() + ":" + config.getMonitoryApiPort() + "/video/ptz/getPreset";
		response = restTemplate.postForObject(url, request, JSONObject.class);
		resultPtz = response.getString("result").toString();
		return null;
	}*/

}
