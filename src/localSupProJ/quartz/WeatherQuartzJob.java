package localSupProJ.quartz;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import localSupProJ.crawlingTarget.OpenAPI;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import localSupProJ.crawler.OpenAPICrawler;

public class WeatherQuartzJob implements Job{
	static Logger log = LoggerFactory.getLogger(WeatherQuartzJob.class);
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("기상정보 수집 크롤링 시작시각 : " + new Date());
		try {
			//DataCollectDAO.mergeWeatherData(OpenAPICrawler.crawling(OpenAPI.WEATHER));
			List<HashMap<String,String>> data = OpenAPICrawler.crawling(OpenAPI.WEATHER);
			log.info(data.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("기상정보 수집 크롤링 종료시각 : " + new Date());
	}
}
