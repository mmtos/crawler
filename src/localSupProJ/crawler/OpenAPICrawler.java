package localSupProJ.crawler;
import java.util.ArrayList;
import java.util.HashMap;

import localSupProJ.crawlingTarget.OpenAPI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpenAPICrawler {
	static Logger log = LoggerFactory.getLogger(OpenAPICrawler.class);
	/*
	 * 목록보기
	 */
	public static ArrayList<HashMap<String, String>> crawling(OpenAPI api) throws Exception{
		return api.getOpenAPIParser().parse(api);
	}
}
