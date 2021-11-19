package localSupProJ.procJsoup;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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

public class parsingWeather {
	static Logger log = LoggerFactory.getLogger(parsingWeather.class);
	/*
	 * 목록보기
	 */
	public static ArrayList<HashMap<String, String>> parsingWeatherList() throws Exception{

		ArrayList<HashMap<String, String>> returnArray = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> resultHm = new HashMap<String, String>();
		BufferedReader br = null;
		URL url = null;
		HttpURLConnection urlconnection = null;
		JSONParser jsonParser = null;
		JSONArray jsonArr = null;
		JSONObject jsonObj = null;
		String urlstr = "";
		String returnLine = "";
		String urlString	= "http://apis.data.go.kr/1360000/VilageFcstMsgService/getLandFcst"; //2020.07.20 현재
		String urlString2	= "http://apis.data.go.kr/1360000/VilageFcstInfoService/getUltraSrtFcst"; //2020.07.20 현재
		
		String ServiceKey	= "b2dY8BdVpqoPeZJvuAM5qMjubwEA7Z9Lvo3x0TzZi0cZB9eolfVSuRJWfs71pzuvARH8rtdqMut%2FJIJKef%2F6ZA%3D%3D";
		String numOfRows	= "1";
		String pageNo		= "1";
		String dataType		= "json";
		String[][] regid = {
							{"서울특별시","서울","11B10101","60","127"},
							{"인천광역시","인천","11B20201","55","124"},
							{"경기도","성남","11B20605","62","123"},
							{"강원도","강릉","11D20501","87","141"},
							{"대전광역시","대전","11C20401","67","100"},
							{"세종특별시","세종","11C20401","66","103"},
							{"충청남도","태안","11C20102","48","109"},
							{"충청북도","충주","11C10101","76","114"},
							{"전라북도","전주","11F10201","63","89"},
							{"광주광역시","광주","11F20501","58","74"},
							{"전라남도","여수","11F20401","73","66"},
							{"대구광역시","대구","11H10701","89","90"},
							{"울산광역시","울산","11H20101","102","84"},
							{"경상북도","상주","11H10302","81","102"},
							{"부산광역시","부산","11H20201","98","76"},
							{"경상남도","진주","11H20701","81","75"},
							{"제주특별자치도","제주","11G00201","52","38"}
						   };
		try{
			for(int i=0; i<regid.length; i++){
				urlstr = urlString +
						"?ServiceKey=" + ServiceKey +
						"&regId=" + regid[i][2] +
						"&numOfRows=" + numOfRows +
						"&pageNo=" + pageNo +
						"&dataType=" + dataType;
				log.info("urlstr : " + urlstr);
				url = new URL(urlstr);
				urlconnection = (HttpURLConnection) url.openConnection();
				urlconnection.setRequestMethod("GET");
				
				br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(),"UTF-8"));
				
				returnLine = "";
				resultHm = new HashMap<String, String>();
				while((returnLine=br.readLine())!=null){
					//log.info(returnLine);
					jsonObj = new JSONObject();
					jsonArr = new JSONArray();
					jsonParser = new JSONParser();
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
					jsonObj = (JSONObject) jsonParser.parse(returnLine);
					returnLine = jsonObj.get("item").toString();
					if(returnLine.indexOf("[") >= 0 && returnLine.indexOf("[") < 3){
						jsonArr = (JSONArray) jsonParser.parse(returnLine);
						returnLine = jsonArr.get(0).toString();
					}
					jsonObj = (JSONObject) jsonParser.parse(returnLine);
					
					resultHm.put("regRegion", regid[i][0]);
					resultHm.put("regNm", regid[i][1]);
					resultHm.put("regId", util.chkNull(jsonObj.get("regId")));
					resultHm.put("announceTime", util.chkNull(jsonObj.get("announceTime")));
					resultHm.put("numEf", util.chkNull(jsonObj.get("numEf")));
					resultHm.put("wd1", util.chkNull(jsonObj.get("wd1")));
					resultHm.put("wdTnd", util.chkNull(jsonObj.get("wdTnd")));
					resultHm.put("wd2", util.chkNull(jsonObj.get("wd2")));
					resultHm.put("wsIt", util.chkNull(jsonObj.get("wsIt")));
					resultHm.put("rnSt", util.chkNull(jsonObj.get("rnSt")));
					resultHm.put("wf", util.chkNull(jsonObj.get("wf")));
					resultHm.put("wfCd", util.chkNull(jsonObj.get("wfCd")));
					resultHm.put("rnYn", util.chkNull(jsonObj.get("rnYn")));
				}
				if(urlconnection != null) urlconnection.disconnect();
				urlconnection = null;
				br.close();
				
				Date date = new Date();
				SimpleDateFormat sdfymd = new SimpleDateFormat("yyyyMMdd");
				SimpleDateFormat sdfh = new SimpleDateFormat("HH");
				SimpleDateFormat sdfm = new SimpleDateFormat("mm");
				
				date.setTime(date.getTime()-((long)1000*60*60));
				
				System.out.println(sdfh.format(date));
				
				String nowH = sdfh.format(date);
				nowH = nowH.length()==1?"0"+nowH:nowH;
				String nowM = "00";
				
				urlstr = urlString2 +
						"?ServiceKey=" + ServiceKey +
						"&numOfRows=100" +
						"&pageNo=1" +
						"&base_date=" + sdfymd.format(date) +
						"&base_time=" + nowH + nowM +
						"&nx=" + regid[i][3] +
						"&ny=" + regid[i][4] +
						"&dataType=" + dataType;
				log.info("urlstr : " + urlstr);
				url = new URL(urlstr);
				urlconnection = (HttpURLConnection) url.openConnection();
				urlconnection.setRequestMethod("GET");
				
				br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(),"UTF-8"));
				
				returnLine = "";
				while((returnLine=br.readLine())!=null){
					//log.info(returnLine);
					jsonObj = new JSONObject();
					jsonArr = new JSONArray();
					jsonParser = new JSONParser();
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
					jsonObj = (JSONObject) jsonParser.parse(returnLine);
					returnLine = jsonObj.get("item").toString();
					jsonArr = (JSONArray) jsonParser.parse(returnLine);
					
					int chk = 0;
					for(int j=0; j<jsonArr.size(); j++){
						if(chk==0 && jsonArr.get(j).toString().indexOf("T1H") > -1){
							chk++;
							returnLine = jsonArr.get(j).toString();
							jsonObj = (JSONObject) jsonParser.parse(returnLine);
						}
					}
					
					resultHm.put("ta", util.chkNull(jsonObj.get("fcstValue")));
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
