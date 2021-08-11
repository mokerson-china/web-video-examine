package com.mokerson.video.examine.service.impl;

import com.alibaba.fastjson.JSONArray;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MonitoryDeviceInfoImpl implements IMonitoryDeviceInfo {

	private static final Logger logger = LoggerFactory.getLogger(MonitoryDeviceInfoImpl.class);
	private static final RestTemplate restTemplate = new RestTemplate();
	private static HttpEntity<String> request;
	private static String baseUrl;
	private int requestTotal = 5;

	public MonitoryDeviceInfoImpl() {
	}

	@Override
	public List<IotMonitoryVo> getIotMonitoryDeviceAll(StatisticsConfig config) {

		return null;
	}

	/**
	 * 获取设备状态情况（是否在线）
	 *
	 * @param config 视频模块连接信息
	 * @return 返回设备的主ID和子ID
	 */
	@Override
	public List<IotMonitoryVo> getDeviceStateAll(StatisticsConfig config) {
		String url = baseUrl
				+ "/video/device/getDeviceStateAll";
		logger.info("视频设备状态查询访问地址为：{}", url);
		return getIotMonitoryVos(config, url);
	}

	/**
	 * 通过获取设备的详情
	 *
	 * @param config 视频模块配置
	 * @param url    请求URL
	 * @return 获取的设备情况
	 */
	private List<IotMonitoryVo> getIotMonitoryVos(StatisticsConfig config, String url) {
		if (request == null) {
			getHeader(config);
		}
		JSONObject response = restTemplate.postForObject(url, request, JSONObject.class);
		List<IotMonitoryVo> result = response.getJSONArray("result").toJavaList(IotMonitoryVo.class);
		return getSelectMainId(result, config.getMainId());
	}

	@Override
	public List<IotMonitoryVo> monitoryPointSearch(StatisticsConfig config) {
		String url = baseUrl
				+ "/video/device/monitoryPointSearch";
		logger.info("视频设备信息查询访问地址为：{}", url);
		return getIotMonitoryVos(config, url);
	}

	@Override
	public List<IotMonitoryVo> getFlvVideoPlay(StatisticsConfig config) {
		List<IotMonitoryVo> monitoryVos = this.getDeviceStateAll(config);
		for(IotMonitoryVo vo:monitoryVos){
			requestTotal = 1;
			vo.setFlvUrl(getFlvHttpPlay(vo.getMainId(),vo.getSubId(),config.getIsTcp()));
		}
		return monitoryVos;
	}

	/**
	 * 获取FLV视频流
	 * @param mainId	主ID
	 * @param subId	子ID
	 * @param isTcp	是否为TCP流
	 * @return	返回获取到的播放URL
	 */
	private String getFlvHttpPlay(String mainId, String subId, String isTcp) {
		String flvUrl;
		String url = baseUrl + "/video/device/getHttpFlvUrl";
		Map<String, String> params = new HashMap<>();
		params.put("mainId",mainId);
		params.put("subId", subId);
		params.put("isTcp", isTcp);
		logger.info("请求FLV视频流地址：{}",url);
		JSONObject response = restTemplate.postForObject(url, request, JSONObject.class, params);
		flvUrl =response.getString("result");
		logger.info("第"+requestTotal+"次获取，获取内容为：{}",response);
		if(("".equals(flvUrl)||flvUrl ==null)&& requestTotal <= 3){
			requestTotal++;
			return getFlvHttpPlay(mainId,subId,isTcp);
		}
		return flvUrl;
	}

	private String getToken(StatisticsConfig config) {
		Map<String, String> params = new HashMap<>();
		params.put("account", config.getMonitoryApiAccount());
		params.put("password", config.getMonitoryApiPassword());
		baseUrl = "http://" + config.getMonitoryApiHost() + ":" + config.getMonitoryApiPort();
		String url = baseUrl + "/login";
		logger.info("视频设备获取Token访问地址为：{}", url);
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);

		request = new HttpEntity<>(JSONObject.toJSONString(params), header);
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
		request = new HttpEntity<>(header);
	}

	private List<IotMonitoryVo> getSelectMainId(List<IotMonitoryVo> params, String mainId) {
		List<IotMonitoryVo> result;
		if (null == params || mainId == null || "".equals(mainId)) {
			return params;
		} else {
			result = new ArrayList<>();
		}
		for (IotMonitoryVo param : params) {
			if (mainId.equals(param.getMainId())) {
				result.add(param);
			}
		}
		return result;
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
