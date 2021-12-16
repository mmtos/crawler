package localSupProJ.persistence.service;

import localSupProJ.persistence.config.DataBaseConfig;
import localSupProJ.quartz.DustAirQuartzJob;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBAccessFactory {
    static Logger log = LoggerFactory.getLogger(DBAccessFactory.class);
    public static DBAccessService getDBAccessService(){
        SqlSession session = null;
        try {
            session = openSqlSession();
        }catch(Exception e){
            log.error("DB접속에 실패했습니다.");
        }finally{
            closeSqlSession();
        }
        return new DBAccessServiceImpl(session);
    }
    private static SqlSession openSqlSession() throws Exception{
        return DataBaseConfig.getInstance().openSqlSession();
    }
    private static void closeSqlSession(){
        DataBaseConfig.getInstance().closeSqlSession();
    }
}
