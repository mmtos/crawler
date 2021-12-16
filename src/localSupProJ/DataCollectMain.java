package localSupProJ;

import localSupProJ.jsoup.JsoupExecutor;
import localSupProJ.parser.OpenAPIParser;
import localSupProJ.parser.SiteParser;
import localSupProJ.crawlingTarget.OpenAPI;
import localSupProJ.crawlingTarget.TargetSite;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;

public class DataCollectMain {
	static Logger log = LoggerFactory.getLogger(DataCollectMain.class);
	public static void main(String[] args){
		log.info("스케줄러 시작!!!");
		SchedulerFactory schedulerFactory = new StdSchedulerFactory();
		try {
			/* 1. 쿼츠 스케줄러를 통해 주기적으로 실행 */
			// 지자체 지원사업 크롤링
//			Scheduler scheduler = schedulerFactory.getScheduler();
//			JobDetail job = newJob(SiteQuartzJob.class).withIdentity("CommunityCrawlingJob", Scheduler.DEFAULT_GROUP).build();
//			//Trigger trigger = newTrigger().withIdentity("CrawlingTrigger", Scheduler.DEFAULT_GROUP).withSchedule(cronSchedule("0 0 12 * * ?")).build();
//			Trigger trigger = newTrigger().withIdentity("CrawlingTrigger", Scheduler.DEFAULT_GROUP).withSchedule(cronSchedule("*/30 * * * * ?")).build();
//			scheduler.scheduleJob(job, trigger);
//			scheduler.start();
			
			// 기상정보 공공데이터포털 API
//			Scheduler schedulerWeather = schedulerFactory.getScheduler();
//			JobDetail jobWeather = newJob(WeatherQuartzJob.class).withIdentity("WeatherJob", schedulerWeather.DEFAULT_GROUP).build();
//			//Trigger triggerWeather = newTrigger().withIdentity("WeatherTrigger", schedulerWeather.DEFAULT_GROUP).withSchedule(cronSchedule("0 25 * * * ?")).build();
//			Trigger triggerWeather = newTrigger().withIdentity("WeatherTrigger", schedulerWeather.DEFAULT_GROUP).withSchedule(cronSchedule("0 0/1 * * * ?")).build();
//			schedulerWeather.scheduleJob(jobWeather, triggerWeather);
//			schedulerWeather.start();
			
			// 대기정보 공공데이터포털 API
//			Scheduler schedulerDustAir = schedulerFactory.getScheduler();
//			JobDetail jobDustAir = newJob(DustAirQuartzJob.class).withIdentity("DustAirJob", schedulerDustAir.DEFAULT_GROUP).build();
//			//Trigger triggerDustAir = newTrigger().withIdentity("DustAirTrigger", schedulerDustAir.DEFAULT_GROUP).withSchedule(cronSchedule("0 20 * * * ?")).build();
//			Trigger triggerDustAir = newTrigger().withIdentity("DustAirTrigger", schedulerDustAir.DEFAULT_GROUP).withSchedule(cronSchedule("0 0/2 * * * ?")).build();
//			schedulerDustAir.scheduleJob(jobDustAir, triggerDustAir);
//			schedulerDustAir.start();


			/* 2. 테스트 */
			// 사이트 크롤링 테스트
			for(TargetSite site : EnumSet.allOf(TargetSite.class)){
				SiteParser siteParser = site.getSiteParser();
				List<HashMap<String,String>> data = siteParser.parse(JsoupExecutor.makeJsoupDocument(site.getUrl(),site.getHttpMethod()));
				if(data != null){
					log.info(data.toString());
				}
			}

			// API 테스트
			for(OpenAPI api : EnumSet.allOf(OpenAPI.class)){
				OpenAPIParser apiParser = api.getOpenAPIParser();
				List<HashMap<String,String>> data = apiParser.parse(api);
				if(data != null){
					log.info(data.toString());
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}