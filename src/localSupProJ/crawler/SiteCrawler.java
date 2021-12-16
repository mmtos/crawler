package localSupProJ.crawler;
import java.util.ArrayList;
import java.util.HashMap;

import localSupProJ.jsoup.JsoupExecutor;
import localSupProJ.parser.SiteParser;
import localSupProJ.crawlingTarget.TargetSite;

import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SiteCrawler {
	static Logger log = LoggerFactory.getLogger(SiteCrawler.class);
	/*
	 * 목록보기
	 */
	public static ArrayList<HashMap<String, String>> crawling (TargetSite targetSite, String httpMethod) throws Exception{
		Document doc = JsoupExecutor.makeJsoupDocument(targetSite.getUrl(),httpMethod);
		SiteParser parser = targetSite.getSiteParser();
		return parser.parse(doc);
	}
}
