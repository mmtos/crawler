package localSupProJ.procJsoup;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import localSupProJ.Quartz.quartzJob;
import localSupProJ.util.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twitter4j.internal.org.json.XML;

public class parsingDustAir {
	static Logger log = LoggerFactory.getLogger(parsingDustAir.class);
	/*
	 * 목록보기
	 */
	public static ArrayList<HashMap<String, String>> parsingDustAirList() throws Exception{
		
		ArrayList<HashMap<String, String>> returnArray = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> resultHm = new HashMap<String, String>();
		BufferedReader br = null;
		URL url = null;
		HttpURLConnection urlconnection = null;
		JSONParser jsonParser = null;
		JSONArray jsonArr = null;
		JSONObject jsonObj = null;
		StringBuffer result = new StringBuffer();
		
		String urlstr = "";
		String returnLine = "";
		//String urlString	= "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty";
		//String urlString	= "http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty";
		String urlString	= "http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty";
		String ServiceKey	= "b2dY8BdVpqoPeZJvuAM5qMjubwEA7Z9Lvo3x0TzZi0cZB9eolfVSuRJWfs71pzuvARH8rtdqMut%2FJIJKef%2F6ZA%3D%3D";
		String dataTerm		= "daily";
		String numOfRows	= "1";
		String pageNo		= "1";
		
		String[][] station = {
							  {"서울특별시","서울"},
							  {"인천광역시","인천"},
							  {"경기도","경기"},
							  {"강원도","강원"},
							  {"대전광역시","대전"},
							  {"세종특별시","세종"},
							  {"충청남도","충남"},
							  {"충청북도","충북"},
							  {"전라북도","전북"},
							  {"광주광역시","광주"},
							  {"전라남도","전남"},
							  {"대구광역시","대구"},
							  {"울산광역시","울산"},
							  {"경상북도","경북"},
							  {"부산광역시","부산"},
							  {"경상남도","경남"},
							  {"제주특별자치도","제주"}
							  };
		try{
			for(int i=0; i<station.length; i++){
				resultHm = new HashMap<String, String>();
				urlstr = urlString +
						"?serviceKey=" + ServiceKey +
						"&sidoName=" + URLEncoder.encode(station[i][1], "UTF-8") +
						"&dataTerm=" + dataTerm +
						"&numOfRows=" + numOfRows +
						"&pageNo=" + pageNo +
						"&ver=1.3" +
						"&returnType=json";
				log.info("urlstr : " + urlstr);
				url = new URL(urlstr);
				urlconnection = (HttpURLConnection) url.openConnection();
				urlconnection.setRequestMethod("GET");
				
				br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(),"UTF-8"));
				
				returnLine = "";
				result = new StringBuffer();
				while((returnLine=br.readLine())!=null){
					result.append(returnLine);
				}
				
				jsonObj = new JSONObject();
				jsonArr = new JSONArray();
				jsonParser = new JSONParser();
				
				returnLine = result.toString();
				if(returnLine.indexOf("[") >= 0 && returnLine.indexOf("[") < 3){
					jsonArr = (JSONArray) jsonParser.parse(returnLine);
					returnLine = jsonArr.get(0).toString();
				}
				jsonObj = (JSONObject) jsonParser.parse(returnLine);
				returnLine = jsonObj.get("response").toString();
				if(returnLine.indexOf("[") >= 0 && returnLine.indexOf("[") < 3){
					jsonArr = (JSONArray) jsonParser.parse(returnLine);
					returnLine = jsonArr.get(0).toString();
				}
				jsonObj = (JSONObject) jsonParser.parse(returnLine);
				returnLine = jsonObj.get("body").toString();
				if(returnLine.indexOf("[") >= 0 && returnLine.indexOf("[") < 3){
					jsonArr = (JSONArray) jsonParser.parse(returnLine);
					returnLine = jsonArr.get(0).toString();
				}
				jsonObj = (JSONObject) jsonParser.parse(returnLine);
				returnLine = jsonObj.get("items").toString();
				if(returnLine.indexOf("[") >= 0 && returnLine.indexOf("[") < 3){
					jsonArr = (JSONArray) jsonParser.parse(returnLine);
					returnLine = jsonArr.get(0).toString();
				}
				if(!returnLine.trim().equals("")){
					jsonObj = (JSONObject) jsonParser.parse(returnLine);
					if(returnLine.indexOf("[") >= 0 && returnLine.indexOf("[") < 3){
						jsonArr = (JSONArray) jsonParser.parse(returnLine);
						returnLine = jsonArr.get(0).toString();
					}
					jsonObj = (JSONObject) jsonParser.parse(returnLine);
					
					resultHm.put("stationName", util.chkNull(jsonObj.get("stationName")));
					resultHm.put("stationNm", station[i][1]);
					resultHm.put("stationRegion", station[i][0]);
					resultHm.put("dataTime", util.chkNull(jsonObj.get("dataTime")));
					resultHm.put("pm10Value", util.chkNull(jsonObj.get("pm10Value")));
					resultHm.put("pm10Grade", util.chkNull(jsonObj.get("pm10Grade")));
					resultHm.put("pm10Grade1h", util.chkNull(jsonObj.get("pm10Grade1h")));
					resultHm.put("pm25Value", util.chkNull(jsonObj.get("pm25Value")));
					resultHm.put("pm25Grade", util.chkNull(jsonObj.get("pm25Grade")));
					resultHm.put("pm25Grade1h", util.chkNull(jsonObj.get("pm25Grade1h")));
				}else{
					
					resultHm.put("stationName", "");
					resultHm.put("stationNm", station[i][1]);
					resultHm.put("stationRegion", station[i][0]);
					resultHm.put("dataTime", "");
					resultHm.put("pm10Value", "");
					resultHm.put("pm10Grade", "");
					resultHm.put("pm10Grade1h", "");
					resultHm.put("pm25Value", "");
					resultHm.put("pm25Grade", "");
					resultHm.put("pm25Grade1h", "");
				}
				urlconnection.disconnect();
				urlconnection = null;
				br.close();
				
				returnArray.add(resultHm);
				Thread.sleep(3000); //sleep 를 않주면 ddos 공격으로 판단.
			}
		}catch(Exception e){
			urlconnection.disconnect();
			urlconnection = null;
			br.close();
			e.printStackTrace();
		}
		
		return returnArray;
	}
}
