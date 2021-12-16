package localSupProJ.parser;

import localSupProJ.jsoup.JsoupExecutor;
import localSupProJ.util.CommonUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;

public class SeoulMaeulSiteParser implements SiteParser {

    static Logger log = LoggerFactory.getLogger(SeoulMaeulSiteParser.class);

    @Override
    public ArrayList<HashMap<String, String>> parse(Document doc) throws Exception {
        ArrayList<HashMap<String, String>> resultList = new ArrayList<HashMap<String, String>>();
        ArrayList<HashMap<String, String>> resultDetailList = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> resultMap = new HashMap<String, String>();
        HashMap<String, String> resultDetailMap = new HashMap<String, String>();
        Elements tableElements = null;

        /* 각 지자체 사이트 별로 문서 파싱 */
        /* 목록을 가져와서 각각의 상세 url 을 수집한후 상세 URL로 접근하여 상세 내용을 파싱한다. */
        log.info(">> 서울시 마을공동체 종합지원센터 공모사업 게시판");
        // 목록 정보 가져오기
        tableElements = doc.select("#programList tbody tr .entry_summary a");

        for (Element tbElement : tableElements) {
            resultMap = new HashMap<String, String>();
            resultMap.put("urlGubun", doc.location());
            resultMap.put("subject", tbElement.select("h4").text());
            resultMap.put("detailUrl", "http://www.seoulmaeul.org/programs/user/support/" + tbElement.attr("href"));
            resultMap.put("readKey", resultMap.get("detailUrl").substring(resultMap.get("detailUrl").indexOf("idx=") + 4, resultMap.get("detailUrl").indexOf("&tabgbn")));
            resultList.add(resultMap);
            Thread.sleep(1000);
        }

        for (HashMap<String, String> siteMap : resultList) {
            resultDetailMap = new HashMap<String, String>();
            log.info(siteMap.get("urlGubun") + " : " + siteMap.get("readKey") + " : " + siteMap.get("subject") + " : " + siteMap.get("detailUrl"));

            doc = JsoupExecutor.makeJsoupDocument(siteMap.get("detailUrl"), "get");
            tableElements = doc.select(".entry_content section");

            resultDetailMap.put("urlGubun", siteMap.get("urlGubun"));
            resultDetailMap.put("readKey", siteMap.get("readKey"));
            resultDetailMap.put("subject", siteMap.get("subject"));
            resultDetailMap.put("detailUrl", siteMap.get("detailUrl"));
            resultDetailMap.put("SIDO", "서울특별시");
            resultDetailMap.put("제목", tableElements.select("header h3").html());
            resultDetailMap.put("작성자", tableElements.select("header .entry_meta .author strong").html());
            resultDetailMap.put("작성일", tableElements.select("header .entry_meta .date").html());

            for (Element tbElement : tableElements.select(".tableInfo table tbody tr")) {

                /* TH 값을 key, TD 값을 value 로 처리 */
                if (tbElement.select("th").eq(0).text().equals("첨부파일")) {
                    int i = 1;
                    for (Element fileElement : tbElement.select("td strong a")) {
                        resultDetailMap.put("첨부파일" + i, fileElement.text().substring(0, fileElement.text().indexOf(" [")));
                        resultDetailMap.put("첨부파일" + i + "URL", "http://www.seoulmaeul.org" + fileElement.attr("href"));
                        i++;
                    }
                } else {
                    resultDetailMap.put(tbElement.select("th").eq(0).text(), tbElement.select("td").eq(0).text());
                }

                /* td가 2벌씩 있을때 (그중에 연락처 분리 있음) */
                if (!tbElement.select("th").eq(1).text().equals("")) {
                    if (tbElement.select("th").eq(1).text().equals("담당자")) {
                        String nameTel = tbElement.select("td").eq(1).text();
                        int indexNum = 0;
                        int indexChk = 0;

                        for (int i = 0; i < nameTel.length(); i++) {
                            if (CommonUtil.isNum(String.valueOf(nameTel.charAt(i))) && indexChk == 0) {
                                indexNum = i;
                                indexChk++;
                            }
                        }
                        resultDetailMap.put(tbElement.select("th").eq(1).text(), tbElement.select("td").eq(1).text().substring(0, indexNum));
                        resultDetailMap.put("담당자 연락처", tbElement.select("td").eq(1).text().substring(indexNum));
                    } else {
                        resultDetailMap.put(tbElement.select("th").eq(1).text(), tbElement.select("td").eq(1).text());
                    }
                }
            }
            resultDetailMap.put("내용", tableElements.select("article").html());

            resultDetailList.add(resultDetailMap);
            Thread.sleep(1000);
        }
        return resultDetailList;
    }
}
