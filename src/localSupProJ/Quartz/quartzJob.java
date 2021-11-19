package localSupProJ.Quartz;

import java.util.Date;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import localSupProJ.dao.dataJsoup;
import localSupProJ.procJsoup.parsingJsoup;

public class quartzJob implements Job{
	static Logger log = LoggerFactory.getLogger(quartzJob.class);
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("크롤링 시작시각 : " + new Date());
		try {
			/* jsop 를 통해 웹사이트 문서 정보를 파싱하여 공동DB처리를 진행한다. */
			dataJsoup.procDataJsoup(parsingJsoup.parsingList(0)); //서울시 마을공동체 종합지원센터 공모사업 게시판
			dataJsoup.procDataJsoup(parsingJsoup.parsingList(1)); //서울시 마을공동체 종합지원센터 공모사업 게시판 2page
			dataJsoup.procDataJsoup(parsingJsoup.parsingList(2)); //서울시 마을공동체 종합지원센터 공모사업 게시판 3page
			dataJsoup.procDataJsoup(parsingJsoup.parsingList(3)); //서울시 공동주택통합정보마당 알림마당 > 지원소식 게시판
			dataJsoup.procDataJsoup(parsingJsoup.parsingList(4)); //대전광역시 공동체공모사업 신청 및 접수 게시판
			dataJsoup.procDataJsoup(parsingJsoup.parsingList(5)); //경기도마을공동체지원센터 공지사항 > 공모사업 게시판
			dataJsoup.procDataJsoup(parsingJsoup.parsingList(6)); //강원도 마을공동체 종합지원센터 공지사항 > 지원사업 게시판
			dataJsoup.procDataJsoup(parsingJsoup.parsingList(7)); //강원도 마을공동체 종합지원센터 공지사항 > 공고 게시판
			dataJsoup.procDataJsoup(parsingJsoup.parsingList(8)); //부산광역시 도시재생지원센터 사업안내 > 공모사업신청
			dataJsoup.procDataJsoup(parsingJsoup.parsingList(9)); //세종시 사회적경제공동체센터 참여마당 > 신청 및 접수 > 지원사업-공동체
			
/*			parsingJsoup.parsingList(0);
			parsingJsoup.parsingList(1);
			parsingJsoup.parsingList(2);
			parsingJsoup.parsingList(3);
			parsingJsoup.parsingList(4);
			parsingJsoup.parsingList(5);
			parsingJsoup.parsingList(6);
			parsingJsoup.parsingList(7);*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("크롤링 종료시각 : " + new Date());
	}
}
