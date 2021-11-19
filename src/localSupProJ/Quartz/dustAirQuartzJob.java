package localSupProJ.Quartz;

import java.util.Date;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import localSupProJ.dao.dataJsoup;
import localSupProJ.procJsoup.parsingDustAir;
import localSupProJ.procJsoup.parsingJsoup;
import localSupProJ.procJsoup.parsingWeather;

public class dustAirQuartzJob implements Job{
	static Logger log = LoggerFactory.getLogger(dustAirQuartzJob.class);
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("대기정보 수집 크롤링 시작시각 : " + new Date());
		try {
			dataJsoup.procDataDustAir(parsingDustAir.parsingDustAirList());
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("대기정보 수집 크롤링 종료시각 : " + new Date());
	}
}
