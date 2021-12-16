package localSupProJ.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import localSupProJ.persistence.config.DataBaseConfig;
import localSupProJ.persistence.service.DBAccessFactory;
import localSupProJ.persistence.service.DBAccessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataCollectDAO {
	static Logger log = LoggerFactory.getLogger(DataCollectDAO.class);

	public static void mergeSiteDetailData(ArrayList<HashMap<String, String>> siteDetailList) throws Exception{
		DBAccessService dbAccessService = null;
		HashMap<String, String> paramMap = new HashMap<String, String>();
		String sqlId = "mergeData";
		String sqlId2 = "mergeBoard";
		String sqlId3 = "pidBoard";
		
		try{
			dbAccessService = DBAccessFactory.getDBAccessService();
			
			for(HashMap<String, String> siteMap : siteDetailList){
				paramMap = new HashMap<String, String>();
				paramMap = parsingColumn(siteMap);
				mergeData(dbAccessService,paramMap,sqlId);
				mergeData(dbAccessService,paramMap,sqlId2);
			}
			updateData(dbAccessService,paramMap,sqlId3);
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			DataBaseConfig.getInstance().closeSqlSession();
		}
	}
	
	private static HashMap<String, String> parsingColumn(HashMap<String, String> siteMap) throws Exception{
		HashMap<String, String> returnMap = new HashMap<String, String>();
		Set<String> hmKeys = null;
		Iterator<String> itrt = null;
		String hmKey = "";
		String hmVal = "";
		HashMap<String, String> fileMap = new HashMap<String, String>();
		HashMap<String, String> fileUrlMap = new HashMap<String, String>();
		String attachFiles = "";
		String attachFileUrls = "";
		
		hmKeys = siteMap.keySet();
		itrt = hmKeys.iterator();
		
		while(itrt.hasNext()){
			hmKey = itrt.next();
			hmVal = siteMap.get(hmKey).trim().replaceAll("'", "\"");
			
			if(hmKey.equals("readKey")){returnMap.put("readKey",hmVal);}
			else if(hmKey.equals("detailUrl")){returnMap.put("detailUrl",hmVal);}
			else if(hmKey.equals("subject")){returnMap.put("SUBJECT",hmVal);} /* 제목 을 써도되고 subject 를 써도됨. 제목에는 접수여부 들어있음. */
			else if(hmKey.equals("SIDO")){returnMap.put("SIDO",hmVal);}
			else if(hmKey.equals("SIGUGUN")){returnMap.put("SIGUGUN",hmVal);}
			else if(hmKey.equals("내용")){returnMap.put("CONTENT",hmVal);}
			else if(hmKey.equals("사업구분")){returnMap.put("PROJ_GUBUN",hmVal);}
			else if(hmKey.equals("사업명")){returnMap.put("PROJ_NAME",hmVal);}
			else if(hmKey.equals("사업단계")){returnMap.put("PROJ_STEP",hmVal);} /* 사업단계 */
			else if(hmKey.equals("사업기간")){returnMap.put("PROJ_PERIOD",hmVal);}
			else if(hmKey.equals("신청자격")){returnMap.put("PROJ_TARGET",hmVal);}
			else if(hmKey.equals("사업개요")){returnMap.put("PROJ_SUMMARY",hmVal);}
			else if(hmKey.equals("지원금")){returnMap.put("SUPPORT_MONEY",hmVal);}
			else if(hmKey.equals("참여최소인원")){returnMap.put("PART_NUM",hmVal);} /* 참여최소인원 */
			else if(hmKey.equals("작성자")){returnMap.put("REG_POST_NAME",hmVal);}
			else if(hmKey.equals("작성일")){returnMap.put("REG_POST_DATE",hmVal);}
			else if(hmKey.equals("구분")){returnMap.put("GUBUN",hmVal);}
			else if(hmKey.equals("접수구분")){returnMap.put("RECEIPT_GUBUN",hmVal);} /* 접수구분 */
			else if(hmKey.equals("접수기간")){returnMap.put("RECEIPT_PERIOD",hmVal);}
			else if(hmKey.equals("접수방법")){returnMap.put("RECEIPT_METHOD",hmVal);}
			else if(hmKey.equals("결과발표")){returnMap.put("RESULT_DATE",hmVal);}
			else if(hmKey.equals("주관기관")){returnMap.put("ORGANIZATION",hmVal);}
			else if(hmKey.equals("담당부서")){returnMap.put("PROJ_DEPT",hmVal);}
			else if(hmKey.equals("담당자")){returnMap.put("PROJ_CHARGE",hmVal);}
			else if(hmKey.equals("담당자 연락처")){returnMap.put("PROJ_CONTACT",hmVal);}
			else if(hmKey.equals("첨부파일1")){fileMap.put("1", hmVal);}
			else if(hmKey.equals("첨부파일2")){fileMap.put("2", hmVal);}
			else if(hmKey.equals("첨부파일3")){fileMap.put("3", hmVal);}
			else if(hmKey.equals("첨부파일4")){fileMap.put("4", hmVal);}
			else if(hmKey.equals("첨부파일5")){fileMap.put("5", hmVal);}
			else if(hmKey.equals("첨부파일6")){fileMap.put("6", hmVal);}
			else if(hmKey.equals("첨부파일7")){fileMap.put("7", hmVal);}
			else if(hmKey.equals("첨부파일8")){fileMap.put("8", hmVal);}
			else if(hmKey.equals("첨부파일9")){fileMap.put("9", hmVal);}
			else if(hmKey.equals("첨부파일1URL")){fileUrlMap.put("1", hmVal);}
			else if(hmKey.equals("첨부파일2URL")){fileUrlMap.put("2", hmVal);}
			else if(hmKey.equals("첨부파일3URL")){fileUrlMap.put("3", hmVal);}
			else if(hmKey.equals("첨부파일4URL")){fileUrlMap.put("4", hmVal);}
			else if(hmKey.equals("첨부파일5URL")){fileUrlMap.put("5", hmVal);}
			else if(hmKey.equals("첨부파일6URL")){fileUrlMap.put("6", hmVal);}
			else if(hmKey.equals("첨부파일7URL")){fileUrlMap.put("7", hmVal);}
			else if(hmKey.equals("첨부파일8URL")){fileUrlMap.put("8", hmVal);}
			else if(hmKey.equals("첨부파일9URL")){fileUrlMap.put("9", hmVal);}
		}
		if(fileMap.size()>0){
			for(int i=1; i<=fileMap.size(); i++){
				if(i != 1){
					attachFiles += "|";
					attachFileUrls += "|";
				}
				attachFiles += fileMap.get(String.valueOf(i));
				attachFileUrls += fileUrlMap.get(String.valueOf(i));
			}
		}
		returnMap.put("INFO_ATTACH_FILE", attachFiles);
		returnMap.put("INFO_ATTACH_FILE_URL", attachFileUrls);
		
/*		log.info("readKey : " + returnMap.get("readKey"));
		log.info("detailUrl : " + returnMap.get("detailUrl"));
		log.info("SIDO : " + returnMap.get("SIDO"));
		log.info("SUBJECT : " + returnMap.get("SUBJECT"));
		log.info("CONTENT : " + returnMap.get("CONTENT"));
		log.info("PROJ_GUBUN : " + returnMap.get("PROJ_GUBUN"));
		log.info("PROJ_NAME : " + returnMap.get("PROJ_NAME"));
		log.info("PROJ_STEP : " + returnMap.get("PROJ_STEP"));
		log.info("PROJ_PERIOD : " + returnMap.get("PROJ_PERIOD"));
		log.info("PROJ_TARGET : " + returnMap.get("PROJ_TARGET"));
		log.info("PROJ_SUMMARY : " + returnMap.get("PROJ_SUMMARY"));
		log.info("SUPPORT_MONEY : " + returnMap.get("SUPPORT_MONEY"));
		log.info("PART_NUM : " + returnMap.get("PART_NUM"));
		log.info("REG_POST_NAME : " + returnMap.get("REG_POST_NAME"));
		log.info("REG_POST_DATE : " + returnMap.get("REG_POST_DATE"));
		log.info("GUBUN : " + returnMap.get("GUBUN"));
		log.info("RECEIPT_PERIOD : " + returnMap.get("RECEIPT_PERIOD"));
		log.info("RECEIPT_METHOD : " + returnMap.get("RECEIPT_METHOD"));
		log.info("RESULT_DATE : " + returnMap.get("RESULT_DATE"));
		log.info("ORGANIZATION : " + returnMap.get("ORGANIZATION"));
		log.info("PROJ_DEPT : " + returnMap.get("PROJ_DEPT"));
		log.info("PROJ_CHARGE : " + returnMap.get("PROJ_CHARGE"));
		log.info("PROJ_CONTACT : " + returnMap.get("PROJ_CONTACT"));
		log.info("INFO_ATTACH_FILE : " + returnMap.get("INFO_ATTACH_FILE"));
		log.info("INFO_ATTACH_FILE_URL : " + returnMap.get("INFO_ATTACH_FILE_URL"));*/
		
		return returnMap;
	}
	
	public static void mergeWeatherData(ArrayList<HashMap<String, String>> weatherList) throws Exception{
		DBAccessService dbAccessService = null;
		HashMap<String, String> paramMap = new HashMap<String, String>();
		String sqlId = "mergeWeatherData";
		
		try{
			dbAccessService = DBAccessFactory.getDBAccessService();
			
			for(HashMap<String, String> weatherMap : weatherList){
				paramMap = new HashMap<String, String>();
				mergeData(dbAccessService,weatherMap,sqlId);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			DataBaseConfig.getInstance().closeSqlSession();
		}
	}
	
	public static void mergeDustAirData(ArrayList<HashMap<String, String>> dustAirList) throws Exception{
		DBAccessService dbAccessService = null;
		HashMap<String, String> paramMap = new HashMap<String, String>();
		String sqlId = "mergeDustAirData";
		
		try{
			dbAccessService = DBAccessFactory.getDBAccessService();
			
			for(HashMap<String, String> dustAirMap : dustAirList){
				paramMap = new HashMap<String, String>();
				mergeData(dbAccessService,dustAirMap,sqlId);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			DataBaseConfig.getInstance().closeSqlSession();
		}
	}
	
	private static void mergeData(DBAccessService service, HashMap<String, String> paramMap, String sqlId) throws Exception{
		service.mergeData(paramMap, sqlId);
	}
	
	private static void updateData(DBAccessService service, HashMap<String, String> paramMap, String sqlId) throws Exception{
		service.updateData(paramMap, sqlId);
	}
}
