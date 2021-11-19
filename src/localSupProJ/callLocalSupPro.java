package localSupProJ;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import localSupProJ.Quartz.dustAirQuartzJob;
import localSupProJ.Quartz.quartzJob;
import localSupProJ.Quartz.weatherQuartzJob;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import org.jsoup.nodes.Element;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import localSupProJ.callLocalSupPro;
import localSupProJ.dao.dataJsoup;
import localSupProJ.procJsoup.parsingDustAir;
import localSupProJ.procJsoup.parsingJsoup;
import localSupProJ.procJsoup.parsingWeather;

import java.util.regex.Pattern;

public class callLocalSupPro{
	static Logger log = LoggerFactory.getLogger(callLocalSupPro.class);
	public static void main(String[] args){
		log.info("스케줄러 시작!!!");
		SchedulerFactory schedulerFactory = new StdSchedulerFactory();
		try {
			/* 쿼츠 스케줄러를 통해 주기적으로 실행 */
/*			// 지자체 지원사업 크롤링 
			Scheduler scheduler = schedulerFactory.getScheduler();
			JobDetail job = newJob(quartzJob.class).withIdentity("CommunityCrawlingJob", Scheduler.DEFAULT_GROUP).build();
			Trigger trigger = newTrigger().withIdentity("CrawlingTrigger", Scheduler.DEFAULT_GROUP).withSchedule(cronSchedule("0 0 12 * * ?")).build();
			scheduler.scheduleJob(job, trigger);
			scheduler.start();
			
			// 기상정보 공공데이터포털 
			Scheduler schedulerWeather = schedulerFactory.getScheduler();
			JobDetail jobWeather = newJob(weatherQuartzJob.class).withIdentity("WeatherJob", schedulerWeather.DEFAULT_GROUP).build();
			Trigger triggerWeather = newTrigger().withIdentity("WeatherTrigger", schedulerWeather.DEFAULT_GROUP).withSchedule(cronSchedule("0 25 * * * ?")).build();
			schedulerWeather.scheduleJob(jobWeather, triggerWeather);
			schedulerWeather.start();
			
			// 대기정보 공공데이터포털 
			Scheduler schedulerDustAir = schedulerFactory.getScheduler();
			JobDetail jobDustAir = newJob(dustAirQuartzJob.class).withIdentity("DustAirJob", schedulerDustAir.DEFAULT_GROUP).build();
			Trigger triggerDustAir = newTrigger().withIdentity("DustAirTrigger", schedulerDustAir.DEFAULT_GROUP).withSchedule(cronSchedule("0 20 * * * ?")).build();
			schedulerDustAir.scheduleJob(jobDustAir, triggerDustAir);
			schedulerDustAir.start();*/
			
			/* 기상,대기 정보 정리 */
/*			Scheduler schedulerDel = schedulerFactory.getScheduler();
			JobDetail jobDel = newJob(quartzJob.class).withIdentity("DelJob", schedulerDel.DEFAULT_GROUP).build();
			Trigger triggerDel = newTrigger().withIdentity("DelTrigger", schedulerDel.DEFAULT_GROUP).withSchedule(cronSchedule("0 0 02 * * ?")).build();
			schedulerDel.scheduleJob(jobDel, triggerDel);
			schedulerDel.start();*/
			
			/* 새 사이트 테스트용 */
			
/*			ArrayList<HashMap<String, String>> parsingList = parsingJsoup.parsingList(0);
			
			HashMap<String, String> returnMap = new HashMap<String, String>();
			Set<String> hmKeys = null;
			Iterator<String> itrt = null;
			String hmKey = "";
			String hmVal = "";
			
			for(HashMap<String, String> siteMap : parsingList){
				hmKeys = siteMap.keySet();
				itrt = hmKeys.iterator();
				
				while(itrt.hasNext()){
					hmKey = itrt.next();
					hmVal = siteMap.get(hmKey);
					if(!hmKey.equals("내용")){
						System.out.println(hmKey + " : " + hmVal);
					}
				}
				System.out.println("");
			}
			*/
			
			/* 새 사이트 최종 테스트 */
			//dataJsoup.procDataJsoup(parsingJsoup.parsingList(5));
			//dataJsoup.procDataJsoup(parsingJsoup.parsingList(5));
			
			parsingWeather.parsingWeatherList();
			//parsingDustAir.parsingDustAirList();
			System.out.println("");
/*			String schStr = "";
			boolean resultChk = true;
			boolean resultChk1 = true;
			
			String[] strText = {"!","1","a","A","ㅁ","ㅏ","한"};
			
			for(int i=0; i<strText.length; i++){
				schStr = strText[i];
				resultChk = Pattern.matches("(?=.*[\\W\\_]).{"+schStr.length()+"}", schStr); // 특수문자 체크 여기서 false 라면 사용가능 true 라면 아래 한번 더 탐
				resultChk1 = schStr.matches("^[가-힣]*$"); // 한글 체크 여기서 false 라면 사용불가 true 라면 사용가능
				System.out.println("schStr : " + schStr + " resultChk : " + resultChk + " resultChk1 : " + resultChk1);
			}*/
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}