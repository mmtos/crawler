package localSupProJ.quartz;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import localSupProJ.crawler.OpenAPICrawler;
import localSupProJ.crawlingTarget.OpenAPI;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DustAirQuartzJob implements Job{
	static Logger log = LoggerFactory.getLogger(DustAirQuartzJob.class);
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("대기정보 수집 크롤링 시작시각 : " + new Date());
		try {
			//DataCollectDAO.mergeDustAirData(OpenAPICrawler.crawling(OpenAPI.DUST_AIR));
			List<HashMap<String,String>> data = OpenAPICrawler.crawling(OpenAPI.DUST_AIR);
			System.out.println(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("대기정보 수집 크롤링 종료시각 : " + new Date());
	}
}
