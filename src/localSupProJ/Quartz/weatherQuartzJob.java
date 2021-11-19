package localSupProJ.Quartz;

import java.util.Date;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import localSupProJ.dao.dataJsoup;
import localSupProJ.procJsoup.parsingJsoup;
import localSupProJ.procJsoup.parsingWeather;

public class weatherQuartzJob implements Job{
	static Logger log = LoggerFactory.getLogger(weatherQuartzJob.class);
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("기상정보 수집 크롤링 시작시각 : " + new Date());
		try {
			dataJsoup.procDataWeather(parsingWeather.parsingWeatherList());
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("기상정보 수집 크롤링 종료시각 : " + new Date());
	}
}
