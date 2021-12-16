package localSupProJ.quartz;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SiteQuartzJob implements Job{
	static Logger log = LoggerFactory.getLogger(SiteQuartzJob.class);
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("크롤링 시작시각 : " + new Date());
		try {
			/* jsoup 를 통해 웹사이트 문서 정보를 파싱하여 공동DB처리를 진행한다. */
			//DataCollectDAO.mergeSiteDetailData(SiteCrawler.crawling(TargetSite.SEOUL_MAEUL_1,"get")); //서울시 마을공동체 종합지원센터 공모사업 게시판
			//List<HashMap<String,String>> data = SiteCrawler.crawling(TargetSite.SEOUL_MAEUL_1,"get");
			//System.out.println(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("크롤링 종료시각 : " + new Date());
	}
}
