package localSupProJ.procJsoup;
import java.util.ArrayList;
import java.util.HashMap;
import localSupProJ.util.util;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class parsingJsoup {
	static Logger log = LoggerFactory.getLogger(parsingJsoup.class);
	/*
	 * 목록보기
	 */
	public static ArrayList<HashMap<String, String>> parsingList (int urlGubun) throws Exception{
		String[] urlStrings = {
					/*0*/	  "http://www.seoulmaeul.org/programs/user/support/list.asp?tabgbn=4", //서울시 마을공동체 종합지원센터 공모사업 게시판
					/*1*/	  "http://www.seoulmaeul.org/programs/user/support/list.asp?tabgbn=4&pageno=2", //서울시 마을공동체 종합지원센터 공모사업 게시판
					/*2*/	  "http://www.seoulmaeul.org/programs/user/support/list.asp?tabgbn=4&pageno=3", //서울시 마을공동체 종합지원센터 공모사업 게시판
					/*3*/	  "https://openapt.seoul.go.kr/boardForm/selectBoardList.do?returnUrl=/portal/dMenu/supportNews/supportList.open&query=poSupportNews.poSupportNewsSelect&bbsId=siGu:suSch:suStat", //서울시 공동주택통합정보마당 알림마당 > 지원소식 게시판
					/*4*/	  "https://www.daejeonmaeul.kr/app/contest/index?md_id=business_contest", //대전광역시 공동체공모사업 신청 및 접수 게시판
					/*5*/	  "https://ggmaeul.or.kr/base/board/list?boardManagementNo=51&menuLevel=2&menuNo=29&page=1", //경기도마을공동체지원센터 지원사업 > 센터 공모사업
					/*6*/	  "https://ggmaeul.or.kr/base/board/list?boardManagementNo=52&menuLevel=2&menuNo=64", //경기도마을공동체지원센터 지원사업 > 시.군 공모사업
					/*7*/	  "http://www.gwmaeul.org/bbs/list?brd=notice&sca=지원사업", //강원도 마을공동체 종합지원센터 공지사항 > 지원사업 게시판
					/*8*/	  "http://www.gwmaeul.org/bbs/list?brd=notice&sca=공고", //강원도 마을공동체 종합지원센터 공지사항 > 공고 게시판
					/*9*/	  "http://www.urcb.or.kr/web/cityUniversity/list.do?mId=52", //부산광역시 도시재생지원센터 사업안내 > 공모사업신청
					/*10*/	  "https://sejongcommunity.com/kor/part/sub_01.html?tabgbn=4", //세종시 사회적경제공동체센터 참여마당 > 신청 및 접수 > 지원사업-공동체
							  };
		
		ArrayList<HashMap<String, String>> resultList = new ArrayList<HashMap<String, String>>();
		ArrayList<HashMap<String, String>> resultDetailList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> resultMap = new HashMap<String, String>();
		HashMap<String, String> resultDetailMap = new HashMap<String, String>();
		Elements tableElements = null;
		
		Document doc = callJsoup.getConnectJsoup(urlStrings[urlGubun],"get");
		
		/* 각 지자체 사이트 별로 문서 파싱 */
		/* 목록을 가져와서 각각의 상세 url 을 수집한후 상세 URL로 접근하여 상세 내용을 파싱한다. */
		if(urlGubun==0 || urlGubun==1 || urlGubun==2){
			log.info(">> 서울시 마을공동체 종합지원센터 공모사업 게시판");
			// 목록 정보 가져오기
			tableElements = doc.select("#programList tbody tr .entry_summary a");
			
			for (Element tbElement : tableElements) {
				resultMap = new HashMap<String, String>();
				resultMap.put("urlGubun", String.valueOf(urlGubun));
				resultMap.put("subject", tbElement.select("h4").text());
				resultMap.put("detailUrl", "http://www.seoulmaeul.org/programs/user/support/"+tbElement.attr("href"));
				resultMap.put("readKey", resultMap.get("detailUrl").substring(resultMap.get("detailUrl").indexOf("idx=")+4,resultMap.get("detailUrl").indexOf("&tabgbn")));
				resultList.add(resultMap);
				Thread.sleep(1000);
			}
			
			for(HashMap<String, String> siteMap : resultList){
				resultDetailMap = new HashMap<String, String>();
				log.info(siteMap.get("urlGubun") +" : "+ siteMap.get("readKey") +" : "+ siteMap.get("subject") +" : "+ siteMap.get("detailUrl"));
				
				doc = callJsoup.getConnectJsoup(siteMap.get("detailUrl"),"get");
				tableElements = doc.select(".entry_content section");
				
				resultDetailMap.put("urlGubun", siteMap.get("urlGubun"));
				resultDetailMap.put("readKey", siteMap.get("readKey"));
				resultDetailMap.put("subject", siteMap.get("subject"));
				resultDetailMap.put("detailUrl", siteMap.get("detailUrl"));
				resultDetailMap.put("SIDO","서울특별시");
				resultDetailMap.put("제목", tableElements.select("header h3").html());
				resultDetailMap.put("작성자", tableElements.select("header .entry_meta .author strong").html());
				resultDetailMap.put("작성일", tableElements.select("header .entry_meta .date").html());
				
				for (Element tbElement : tableElements.select(".tableInfo table tbody tr")) {
					
					/* TH 값을 key, TD 값을 value 로 처리 */
					if(tbElement.select("th").eq(0).text().equals("첨부파일")){
						int i=1;
						for (Element fileElement : tbElement.select("td strong a")) {
							resultDetailMap.put("첨부파일"+i, fileElement.text().substring(0,fileElement.text().indexOf(" [")));
							resultDetailMap.put("첨부파일"+i+"URL", "http://www.seoulmaeul.org"+fileElement.attr("href"));
							i++;
						}
					}else{
						resultDetailMap.put(tbElement.select("th").eq(0).text(), tbElement.select("td").eq(0).text());
					}
					
					/* td가 2벌씩 있을때 (그중에 연락처 분리 있음) */
					if(!tbElement.select("th").eq(1).text().equals("")){
						if(tbElement.select("th").eq(1).text().equals("담당자")){
							String nameTel = tbElement.select("td").eq(1).text();
							int indexNum = 0;
							int indexChk = 0;
							
							for(int i=0; i<nameTel.length(); i++){
								if(util.isNum(String.valueOf(nameTel.charAt(i))) && indexChk == 0){
									indexNum = i;
									indexChk++;
								}
							}
							resultDetailMap.put(tbElement.select("th").eq(1).text(), tbElement.select("td").eq(1).text().substring(0,indexNum));
							resultDetailMap.put("담당자 연락처", tbElement.select("td").eq(1).text().substring(indexNum));
						}else{
							resultDetailMap.put(tbElement.select("th").eq(1).text(), tbElement.select("td").eq(1).text());
						}
					}
				}
				resultDetailMap.put("내용", tableElements.select("article").html());
				
				resultDetailList.add(resultDetailMap);
				Thread.sleep(1000);
			}
			
		}else if(urlGubun==3){
			log.info(">> 서울시 공동주택통합정보마당 알림마당 > 지원소식 게시판");
			
			// 목록 정보 가져오기
			tableElements = doc.select("form[name=subForm] table tbody tr .tdTitle a");
			
			String supportInfoSn = "";
			for (Element tbElement : tableElements) {
				resultMap = new HashMap<String, String>();
				supportInfoSn = tbElement.attr("onclick");
				supportInfoSn = supportInfoSn.substring(supportInfoSn.indexOf("'")+1,supportInfoSn.indexOf(")")-1);
				resultMap.put("urlGubun", String.valueOf(urlGubun));
				resultMap.put("subject", tbElement.text());
				resultMap.put("detailUrl", "https://openapt.seoul.go.kr/boardForm/boardDetail.do?returnUrl=/portal/dMenu/supportNews/supportDetail.open&query=poSupportNews.poSupportNewsDetail&supportInfoSn="+supportInfoSn);
				resultMap.put("readKey", supportInfoSn);
				resultList.add(resultMap);
				Thread.sleep(1000);
			}
			
			String fileInfo = "";
			String[] fileInfos = null;
			String atchFileId = "";
			String fileSn = "";
			for(HashMap<String, String> siteMap : resultList){
				resultDetailMap = new HashMap<String, String>();
				log.info(siteMap.get("urlGubun") +" : "+ siteMap.get("readKey") +" : "+ siteMap.get("subject") +" : "+ siteMap.get("detailUrl"));
				
				doc = callJsoup.getConnectJsoup(siteMap.get("detailUrl"),"post");
				tableElements = doc.select(".readBbsWrap");
				
				resultDetailMap.put("urlGubun", siteMap.get("urlGubun"));
				resultDetailMap.put("readKey", siteMap.get("readKey"));
				resultDetailMap.put("subject", siteMap.get("subject"));
				resultDetailMap.put("detailUrl", siteMap.get("detailUrl"));
				resultDetailMap.put("SIDO","서울특별시");
				resultDetailMap.put("제목", tableElements.select(".readTitle").html());
				resultDetailMap.put("작성자", "");
				resultDetailMap.put("작성일", "");
				
				for (Element tbElement : tableElements.select(".readSubTx dl")) {
					// dt 값을 key, dd 값을 value 로 처리 
					if(tbElement.select("dt").text().equals("등록일")){
						resultDetailMap.put("작성일", tbElement.select("dd").text());
					}else if(tbElement.select("dt").text().equals("연락처")){
						resultDetailMap.put("담당자 연락처", tbElement.select("dd").text());
					}else if(tbElement.select("dt").text().equals("첨부파일")){
						
					}else{
						resultDetailMap.put(tbElement.select("dt").text(), tbElement.select("dd").text());
					}
				}
				
				int i=1;
				for (Element fileElement : tableElements.select("#egov_file_view_table tbody tr td a")) {
					resultDetailMap.put("첨부파일"+i, fileElement.text().substring(0,fileElement.text().lastIndexOf("[")));
					fileInfo = fileElement.attr("href");
					fileInfo = fileInfo.replace("javascript:fn_egov_downFile(", "");
					fileInfo = fileInfo.replace(")", "");
					fileInfo = fileInfo.replace("'", "");
					fileInfos = fileInfo.split(",");
					atchFileId = fileInfos[0];
					fileSn = fileInfos[1];
					resultDetailMap.put("첨부파일"+i+"URL", "https://openapt.seoul.go.kr/open/FileDown.do?atchFileId="+atchFileId+"&fileSn="+fileSn);
					
					i++;
				}
				
				resultDetailMap.put("내용", tableElements.select(".readContent").html());
				resultDetailMap.put("detailUrl", "https://openapt.seoul.go.kr/boardForm/selectBoardList.do?returnUrl=/portal/dMenu/supportNews/supportList.open&query=poSupportNews.poSupportNewsSelect&bbsId=siGu:suSch:suStat");
				resultDetailList.add(resultDetailMap);
				Thread.sleep(1000);
			}
			
		}else if(urlGubun==4){
			log.info(">> 대전광역시 공동체공모사업 신청 및 접수 게시판");
			
			// 목록 정보 가져오기
			tableElements = doc.select(".business-contest-list > ul li a");
			
			for (Element tbElement : tableElements) {
				resultMap = new HashMap<String, String>();
				resultMap.put("urlGubun", String.valueOf(urlGubun));
				resultMap.put("subject", tbElement.select(".cont .title").text());
				resultMap.put("detailUrl", "https://www.daejeonmaeul.kr"+tbElement.attr("href"));
				resultMap.put("readKey", resultMap.get("detailUrl").substring(resultMap.get("detailUrl").indexOf("code=")+5,resultMap.get("detailUrl").indexOf("&page")));
				resultList.add(resultMap);
				Thread.sleep(1000);
			}
			
			for(HashMap<String, String> siteMap : resultList){
				resultDetailMap = new HashMap<String, String>();
				log.info(siteMap.get("urlGubun") +" : "+ siteMap.get("readKey") +" : "+ siteMap.get("subject") +" : "+ siteMap.get("detailUrl"));
				
				doc = callJsoup.getConnectJsoup(siteMap.get("detailUrl"),"get");
				tableElements = doc.select(".contest-view");
				
				resultDetailMap.put("urlGubun", siteMap.get("urlGubun"));
				resultDetailMap.put("readKey", siteMap.get("readKey"));
				resultDetailMap.put("detailUrl", siteMap.get("detailUrl"));
				resultDetailMap.put("SIDO","대전광역시");
				resultDetailMap.put("사업구분", tableElements.select(".view-header .busi-type span").text());
				resultDetailMap.put("subject", tableElements.select(".view-header dl dt h3").text());
				resultDetailMap.put("제목", tableElements.select(".view-header dl dt h3").text());
				resultDetailMap.put("작성자", tableElements.select(".view-header dl dd ul li").eq(0).text());
				resultDetailMap.put("작성일", tableElements.select(".view-header dl dd ul li").eq(1).text());
				
				for (Element tbElement : tableElements.select(".view-body .contest-info ul li")) {
					if(tbElement.html().indexOf(">") <= -1){
						resultDetailMap.put(tbElement.select("strong").text(), tbElement.text());
					}else{
						if(tbElement.select("strong").text().equals("발표일자")){
							resultDetailMap.put("결과발표", tbElement.html().substring(tbElement.html().lastIndexOf(">")+2));
						}else if(tbElement.select("strong").text().equals("사업대상")){
							resultDetailMap.put("신청자격", tbElement.html().substring(tbElement.html().lastIndexOf(">")+2));
						}else if(tbElement.select("strong").text().equals("참여 최소인원")){
							resultDetailMap.put("참여최소인원", tbElement.html().substring(tbElement.html().lastIndexOf(">")+2));
						}else if(tbElement.select("strong").text().equals("연락처")){
							resultDetailMap.put("담당자 연락처", tbElement.html().substring(tbElement.html().lastIndexOf(">")+2));
						}else if(tbElement.select("strong").text().equals("모집기간")){
							String mojibgigan = tbElement.html();
							mojibgigan = mojibgigan.replaceAll("<br>", "");
							mojibgigan = mojibgigan.substring(mojibgigan.lastIndexOf(">")+2);
							resultDetailMap.put("접수기간", mojibgigan);
						}else if(tbElement.select("strong").text().equals("지원예산")){
							resultDetailMap.put("지원금", tbElement.html().substring(tbElement.html().lastIndexOf(">")+2));
						}else{
							resultDetailMap.put(tbElement.select("strong").text(), tbElement.html().substring(tbElement.html().lastIndexOf(">")+2));
						}
					}
				}
				
				
				int i=1;
				if(tableElements.select(".view-body .contest-cont").size() == 1){
					resultDetailMap.put("내용", tableElements.select(".view-body .contest-cont").select(".cont #contents").html().replaceAll("src=\"", "src=\"https://www.daejeonmaeul.kr") );
				}else{
					for (Element fileElement : tableElements.select(".view-body .contest-cont").eq(0).select(".document-list li a") ) {
						resultDetailMap.put("첨부파일"+i, fileElement.select("div").text());
						resultDetailMap.put("첨부파일"+i+"URL", "https://www.daejeonmaeul.kr"+fileElement.attr("href"));
						i++;
					}
					
					resultDetailMap.put("내용", tableElements.select(".view-body .contest-cont").eq(1).select(".cont #contents").html().replaceAll("src=\"", "src=\"https://www.daejeonmaeul.kr") );
				}
				
				resultDetailList.add(resultDetailMap);
				Thread.sleep(1000);
			}
		}else if(urlGubun==5){
			log.info(">> 경기도마을공동체지원센터 지원사업 > 센터 공모사업");
			
			// 목록 정보 가져오기
			tableElements = doc.select(".board_list tbody tr");
			
			String nttId = "";
			for (Element tbElement : tableElements) {
				resultMap = new HashMap<String, String>();
				nttId = tbElement.select(".tit a").attr("href");
				nttId = nttId.substring(nttId.indexOf("boardNo=")+8,nttId.indexOf("&type"));
				resultMap.put("urlGubun", String.valueOf(urlGubun));
				resultMap.put("subject", tbElement.select(".tit a").text());
				resultMap.put("detailUrl", tbElement.select(".tit a").attr("href"));
				resultMap.put("readKey", nttId);
				resultMap.put("지역구분", tbElement.select(".sort").text());
				resultMap.put("접수구분", tbElement.select(".registerstate").text());
				
				resultList.add(resultMap);
				Thread.sleep(1000);
			}
			for(HashMap<String, String> siteMap : resultList){
				resultDetailMap = new HashMap<String, String>();
				log.info(siteMap.get("urlGubun") +" : "+ siteMap.get("readKey") +" : "+ siteMap.get("subject") +" : "+ siteMap.get("detailUrl"));
				
				doc = callJsoup.getConnectJsoup(siteMap.get("detailUrl"),"get");
				tableElements = doc.select(".board_view");
				
				resultDetailMap.put("urlGubun", siteMap.get("urlGubun"));
				resultDetailMap.put("readKey", siteMap.get("readKey"));
				resultDetailMap.put("subject", siteMap.get("subject"));
				resultDetailMap.put("detailUrl", siteMap.get("detailUrl"));
				resultDetailMap.put("SIDO","경기도");
				if(!siteMap.get("지역구분").equals("경기도")){
					resultDetailMap.put("SIGUGUN",siteMap.get("지역구분"));
				}
				resultDetailMap.put("제목", tableElements.select(".board_view_top .tit").html().substring(tableElements.select(".board_view_top .tit").html().lastIndexOf(">")+1));
				resultDetailMap.put("subject", resultDetailMap.get("제목"));
				resultDetailMap.put("작성일", tableElements.select(".board_view_top .info .each").eq(0).text().substring(tableElements.select(".board_view_top .info .each").eq(0).text().indexOf(":")+1));
				
				for (Element tbElement : tableElements.select(".basicTable2 table tbody tr")) {
					
					/* TH 값을 key, TD 값을 value 로 처리 */
					if(tbElement.select("th").eq(0).text().equals("첨부파일")){
						int i=1;
						int realI=1;
						for (Element fileElement : tableElements.select("td .file_box .file_each a") ) {
							if((i % 2)==1){
								resultDetailMap.put("첨부파일"+realI, fileElement.text());
								resultDetailMap.put("첨부파일"+realI+"URL", fileElement.attr("href"));
								realI++;
							}
							i++;
						}
					}else if(tbElement.select("th").eq(0).text().equals("신청대상")){
						resultDetailMap.put("신청자격", tbElement.select("td").eq(0).text());
					}else if(tbElement.select("th").eq(0).text().equals("사업목적 (요약설명)")){
						resultDetailMap.put("사업개요", tbElement.select("td").eq(0).text());
					}else{
						resultDetailMap.put(tbElement.select("th").eq(0).text().replaceAll(" ", ""), tbElement.select("td").eq(0).text());
					}
					
					/* td가 2벌씩 있을때 (그중에 연락처 분리 있음) */
					if(!tbElement.select("th").eq(1).text().equals("")){
						if(tbElement.select("th").eq(1).text().equals("담당자")){
							resultDetailMap.put(tbElement.select("th").eq(1).text()+" 연락처", tbElement.select("td").eq(1).text());
						}else{
							resultDetailMap.put(tbElement.select("th").eq(1).text(), tbElement.select("td").eq(1).text());
						}
					}
				}
				resultDetailMap.put("내용", tableElements.select(".board_view_con .editor_view").html().replaceAll("src=\"", "src=\"http://ggmaeul.or.kr") );
				resultDetailList.add(resultDetailMap);
				Thread.sleep(1000);
			}
		}else if(urlGubun==6){
			log.info(">> 경기도마을공동체지원센터 지원사업 > 시.군 공모사업");
			
			// 목록 정보 가져오기
			tableElements = doc.select(".board_list tbody tr");
			
			String nttId = "";
			for (Element tbElement : tableElements) {
				resultMap = new HashMap<String, String>();
				nttId = tbElement.select(".tit a").attr("href");
				nttId = nttId.substring(nttId.indexOf("boardNo=")+8,nttId.indexOf("&type"));
				resultMap.put("urlGubun", String.valueOf(urlGubun));
				resultMap.put("subject", tbElement.select(".tit a").text());
				resultMap.put("detailUrl", tbElement.select(".tit a").attr("href"));
				resultMap.put("readKey", nttId);
				resultMap.put("지역구분", tbElement.select(".sort").text());
				resultMap.put("접수구분", tbElement.select(".registerstate").text());
				
				resultList.add(resultMap);
				Thread.sleep(1000);
			}
			for(HashMap<String, String> siteMap : resultList){
				resultDetailMap = new HashMap<String, String>();
				log.info(siteMap.get("urlGubun") +" : "+ siteMap.get("readKey") +" : "+ siteMap.get("subject") +" : "+ siteMap.get("detailUrl"));
				
				doc = callJsoup.getConnectJsoup(siteMap.get("detailUrl"),"get");
				tableElements = doc.select(".board_view");
				
				resultDetailMap.put("urlGubun", siteMap.get("urlGubun"));
				resultDetailMap.put("readKey", siteMap.get("readKey"));
				resultDetailMap.put("subject", siteMap.get("subject"));
				resultDetailMap.put("detailUrl", siteMap.get("detailUrl"));
				resultDetailMap.put("SIDO","경기도");
				if(!siteMap.get("지역구분").equals("경기도")){
					resultDetailMap.put("SIGUGUN",siteMap.get("지역구분"));
				}
				resultDetailMap.put("제목", tableElements.select(".board_view_top .tit").html().substring(tableElements.select(".board_view_top .tit").html().lastIndexOf(">")+1));
				resultDetailMap.put("subject", resultDetailMap.get("제목"));
				resultDetailMap.put("작성일", tableElements.select(".board_view_top .info .each").eq(0).text().substring(tableElements.select(".board_view_top .info .each").eq(0).text().indexOf(":")+1));
				
				for (Element tbElement : tableElements.select(".basicTable2 table tbody tr")) {
					
					/* TH 값을 key, TD 값을 value 로 처리 */
					if(tbElement.select("th").eq(0).text().equals("첨부파일")){
						int i=1;
						int realI=1;
						for (Element fileElement : tableElements.select("td .file_box .file_each a") ) {
							if((i % 2)==1){
								resultDetailMap.put("첨부파일"+realI, fileElement.text());
								resultDetailMap.put("첨부파일"+realI+"URL", fileElement.attr("href"));
								realI++;
							}
							i++;
						}
					}else if(tbElement.select("th").eq(0).text().equals("신청대상")){
						resultDetailMap.put("신청자격", tbElement.select("td").eq(0).text());
					}else if(tbElement.select("th").eq(0).text().equals("사업목적 (요약설명)")){
						resultDetailMap.put("사업개요", tbElement.select("td").eq(0).text());
					}else{
						resultDetailMap.put(tbElement.select("th").eq(0).text().replaceAll(" ", ""), tbElement.select("td").eq(0).text());
					}
					
					/* td가 2벌씩 있을때 (그중에 연락처 분리 있음) */
					if(!tbElement.select("th").eq(1).text().equals("")){
						resultDetailMap.put(tbElement.select("th").eq(1).text(), tbElement.select("td").eq(1).text());
					}
				}
				resultDetailMap.put("내용", tableElements.select(".board_view_con .editor_view").html().replaceAll("src=\"", "src=\"http://ggmaeul.or.kr") );
				
				resultDetailList.add(resultDetailMap);
				Thread.sleep(1000);
			}
		}else if(urlGubun==7){
			log.info(">> 강원도 마을공동체 종합지원센터 공지사항 > 지원사업 게시판");
			
			// 목록 정보 가져오기
			tableElements = doc.select(".table-style table tbody tr td a");
			
			for (Element tbElement : tableElements) {
				resultMap = new HashMap<String, String>();
				resultMap.put("urlGubun", String.valueOf(urlGubun));
				resultMap.put("subject", tbElement.text());
				resultMap.put("detailUrl", "http://www.gwmaeul.org"+tbElement.attr("href"));
				resultMap.put("readKey", resultMap.get("detailUrl").substring(resultMap.get("detailUrl").indexOf("poid=")+5,resultMap.get("detailUrl").length()));
				resultList.add(resultMap);
				Thread.sleep(1000);
			}
			
			for(HashMap<String, String> siteMap : resultList){
				resultDetailMap = new HashMap<String, String>();
				log.info(siteMap.get("urlGubun") +" : "+ siteMap.get("readKey") +" : "+ siteMap.get("subject") +" : "+ siteMap.get("detailUrl"));
				
				doc = callJsoup.getConnectJsoup(siteMap.get("detailUrl"),"get");
				tableElements = doc.select(".table-style");
				
				resultDetailMap.put("urlGubun", siteMap.get("urlGubun"));
				resultDetailMap.put("readKey", siteMap.get("readKey"));
				resultDetailMap.put("subject", siteMap.get("subject"));
				resultDetailMap.put("detailUrl", siteMap.get("detailUrl"));
				resultDetailMap.put("SIDO","강원도");
				resultDetailMap.put("제목", tableElements.select(".post_title").text());
				resultDetailMap.put("작성자", tableElements.select("table tbody tr").eq(1).select("td").eq(0).text());
				resultDetailMap.put("작성일", tableElements.select("table tbody tr").eq(1).select("td").eq(1).text());
				
				int i=1;
				for (Element fileElement : tableElements.select("table tbody tr").eq(2).select("td").eq(0).select("ul li a") ) {
					resultDetailMap.put("첨부파일"+i, fileElement.text().substring(0,fileElement.text().lastIndexOf("(")));
					resultDetailMap.put("첨부파일"+i+"URL", "http://www.gwmaeul.org"+fileElement.attr("href"));
					i++;
				}
				resultDetailMap.put("내용", tableElements.select("table tbody tr").eq(3).select("td .view_cont").html().replaceAll("src=\"/uploads", "src=\"http://www.gwmaeul.org/uploads") );
				
				resultDetailList.add(resultDetailMap);
				Thread.sleep(1000);
			}
			
		}else if(urlGubun==8){
			log.info(">> 강원도 마을공동체 종합지원센터 공지사항 > 공고 게시판");
			
			// 목록 정보 가져오기
			tableElements = doc.select(".table-style table tbody tr td a");
			
			for (Element tbElement : tableElements) {
				resultMap = new HashMap<String, String>();
				resultMap.put("urlGubun", String.valueOf(urlGubun));
				resultMap.put("subject", tbElement.text());
				resultMap.put("detailUrl", "http://www.gwmaeul.org"+tbElement.attr("href"));
				resultMap.put("readKey", resultMap.get("detailUrl").substring(resultMap.get("detailUrl").indexOf("poid=")+5,resultMap.get("detailUrl").length()));
				resultList.add(resultMap);
				Thread.sleep(1000);
			}
			
			for(HashMap<String, String> siteMap : resultList){
				resultDetailMap = new HashMap<String, String>();
				log.info(siteMap.get("urlGubun") +" : "+ siteMap.get("readKey") +" : "+ siteMap.get("subject") +" : "+ siteMap.get("detailUrl"));
				
				doc = callJsoup.getConnectJsoup(siteMap.get("detailUrl"),"get");
				tableElements = doc.select(".table-style");
				
				resultDetailMap.put("urlGubun", siteMap.get("urlGubun"));
				resultDetailMap.put("readKey", siteMap.get("readKey"));
				resultDetailMap.put("subject", siteMap.get("subject"));
				resultDetailMap.put("detailUrl", siteMap.get("detailUrl"));
				resultDetailMap.put("SIDO","강원도");
				resultDetailMap.put("제목", tableElements.select(".post_title").text());
				resultDetailMap.put("작성자", tableElements.select("table tbody tr").eq(1).select("td").eq(0).text());
				resultDetailMap.put("작성일", tableElements.select("table tbody tr").eq(1).select("td").eq(1).text());
				
				int i=1;
				for (Element fileElement : tableElements.select("table tbody tr").eq(2).select("td").eq(0).select("ul li a") ) {
					resultDetailMap.put("첨부파일"+i, fileElement.text().substring(0,fileElement.text().lastIndexOf("(")));
					resultDetailMap.put("첨부파일"+i+"URL", "http://www.gwmaeul.org"+fileElement.attr("href"));
					i++;
				}
				resultDetailMap.put("내용", tableElements.select("table tbody tr").eq(3).select("td .view_cont").html().replaceAll("src=\"/uploads", "src=\"http://www.gwmaeul.org/uploads") );
				
				resultDetailList.add(resultDetailMap);
				Thread.sleep(1000);
			}
			
		}else if(urlGubun==9){
			log.info(">> 부산광역시 도시재생지원센터 사업안내 > 공모사업신청");
			
			// 목록 정보 가져오기
			tableElements = doc.select(".listTypeA tbody tr .subject a");
			
			for (Element tbElement : tableElements) {
				resultMap = new HashMap<String, String>();
				resultMap.put("urlGubun", String.valueOf(urlGubun));
				resultMap.put("subject", tbElement.text());
				resultMap.put("detailUrl", "http://www.urcb.or.kr/web/cityUniversity/"+tbElement.attr("href"));
				resultMap.put("readKey", resultMap.get("detailUrl").substring(resultMap.get("detailUrl").indexOf("cuIdx=")+6,resultMap.get("detailUrl").length()));
				resultList.add(resultMap);
				Thread.sleep(1000);
			}
			
			for(HashMap<String, String> siteMap : resultList){
				resultDetailMap = new HashMap<String, String>();
				log.info(siteMap.get("urlGubun") +" : "+ siteMap.get("readKey") +" : "+ siteMap.get("subject") +" : "+ siteMap.get("detailUrl"));
				
				doc = callJsoup.getConnectJsoup(siteMap.get("detailUrl"),"get");
				tableElements = doc.select(".viewTypeA");
				
				resultDetailMap.put("urlGubun", siteMap.get("urlGubun"));
				resultDetailMap.put("readKey", siteMap.get("readKey"));
				resultDetailMap.put("subject", siteMap.get("subject"));
				resultDetailMap.put("detailUrl", siteMap.get("detailUrl"));
				resultDetailMap.put("SIDO","부산광역시");
				resultDetailMap.put("제목", tableElements.select("h2").text());
				
				for (Element tbElement : tableElements.select(".infor li")) {
					if(tbElement.select(".name").text().equals("신청서식")){
						int i=1;
						for (Element fileElement : tableElements.select(".fn_con_file li a") ) {
							resultDetailMap.put("첨부파일"+i, fileElement.text());
							resultDetailMap.put("첨부파일"+i+"URL", "http://www.urcb.or.kr/web/cityUniversity/"+fileElement.attr("href"));
							i++;
						}
					}else{
						if(tbElement.select(".name").text().equals("결과발표일")){
							resultDetailMap.put("결과발표", tbElement.html().substring(tbElement.html().lastIndexOf(">")+1));
						}else if(tbElement.select(".name").text().equals("전화번호")){
							resultDetailMap.put("담당자 연락처", tbElement.html().substring(tbElement.html().lastIndexOf(">")+1));
						}else if(tbElement.select(".name").text().equals("접수기간")){
							resultDetailMap.put("접수기간", tbElement.html().substring(tbElement.html().lastIndexOf(">")+1).replaceAll("&nbsp;", ""));
						}else{
							resultDetailMap.put(tbElement.select(".name").text(), tbElement.html().substring(tbElement.html().lastIndexOf(">")+1));
						}
					}
				}
				
				resultDetailMap.put("내용", tableElements.select(".con").html());
				
				resultDetailList.add(resultDetailMap);
				Thread.sleep(1000);
			}
			
		}else if(urlGubun==10){
			log.info(">> 세종시 사회적경제공동체센터 참여마당 > 신청 및 접수 > 지원사업-공동체");
			
			// 목록 정보 가져오기
			tableElements = doc.select(".btb.board-list table tbody tr .post-summary-box");
			
			for (Element tbElement : tableElements) {
				resultMap = new HashMap<String, String>();
				resultMap.put("urlGubun", String.valueOf(urlGubun));
				resultMap.put("subject", tbElement.select("a").text());
				resultMap.put("detailUrl", "https://sejongcommunity.com/kor/part/sub_01.html?pmode=view&sc_idx="+tbElement.select(".post-other-item .mt20.txt-r .linkhref.input-btn.thin").attr("data-sc_idx"));
				resultMap.put("readKey", tbElement.select(".post-other-item .mt20.txt-r .linkhref.input-btn.thin").attr("data-sc_idx"));
				resultList.add(resultMap);
				Thread.sleep(1000);
			}
			
			for(HashMap<String, String> siteMap : resultList){
				resultDetailMap = new HashMap<String, String>();
				log.info(siteMap.get("urlGubun") +" : "+ siteMap.get("readKey") +" : "+ siteMap.get("subject") +" : "+ siteMap.get("detailUrl"));
				
				doc = callJsoup.getConnectJsoup(siteMap.get("detailUrl"),"get");
				tableElements = doc.select(".ezsboard6 table");
				
				resultDetailMap.put("urlGubun", siteMap.get("urlGubun"));
				resultDetailMap.put("readKey", siteMap.get("readKey"));
				resultDetailMap.put("subject", siteMap.get("subject"));
				resultDetailMap.put("detailUrl", siteMap.get("detailUrl"));
				resultDetailMap.put("SIDO","세종특별자치시");
				resultDetailMap.put("제목", tableElements.select("thead tr th").text());
				
				for (Element tbElement : tableElements.select("tbody tr")) {
					if(!tbElement.select("tr td ul li").toString().equals("")){ //사업구분, 사업기간, 사업예산의 처리
						for (Element liElement : tableElements.select("tr td ul li")) {
							resultDetailMap.put(liElement.select("span").text().replaceAll(" :", ""), liElement.text().replaceAll(liElement.select("span").text()+" ", ""));
						}
					}else if(!tbElement.select("tr td .txt-r.add-file-list.ui-open .view-file-box.ui-list ol li").toString().equals("")){ //첨부파일의 처리
						int i=1;
						for (Element fileElement : tableElements.select("tr td .txt-r.add-file-list.ui-open .view-file-box.ui-list ol li a")) {
							resultDetailMap.put("첨부파일"+i, fileElement.text());
							resultDetailMap.put("첨부파일"+i+"URL", "https://sejongcommunity.com/"+fileElement.attr("href").replaceAll("../../",""));
						}
						i++;
					}else{
						if(tbElement.select("th").text().equals("지원내용")){
							resultDetailMap.put("내용", tbElement.select("td").text());
						}else if(tbElement.select("th").text().equals("사업예산")){
							resultDetailMap.put("지원금", tbElement.select("td").text());
						}else if(tbElement.select("th").text().equals("담당자")){
							resultDetailMap.put("담당자", tbElement.select("td").text().substring(0,tbElement.select("td").text().indexOf("(")-1));
							resultDetailMap.put("담당자 연락처", tbElement.select("td").text().substring(tbElement.select("td").text().indexOf("(")+2,tbElement.select("td").text().indexOf(")")-1));
						}else{
							resultDetailMap.put(tbElement.select("th").text(), tbElement.select("td").text());
						}
						
					}
				}
				
				resultDetailList.add(resultDetailMap);
				Thread.sleep(1000);
			}
		}
		
		return resultDetailList;
	}
}
