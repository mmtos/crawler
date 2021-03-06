package localSupProJ.persistence.config;

import java.io.InputStream;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class DataBaseConfig {
	private String mybatis_config_path = null;
	private SqlSession mFactorySession = null;
	
	public DataBaseConfig() {
		this.mybatis_config_path = "localSupProJ/persistence/config/mybatis-config.xml";
	}
	
	public static DataBaseConfig getInstance() {
		return LazyHolder.INSTANCE;
	}
	
	private SqlSessionFactory sqlSessionFactory() throws Exception{
		InputStream inputStream = Resources.getResourceAsStream(mybatis_config_path);
		SqlSessionFactory bean = new SqlSessionFactoryBuilder().build(inputStream);
		return bean;
	}
	
	public SqlSession openSqlSession() throws Exception{
		if(mFactorySession == null){
			SqlSessionFactory factory = sqlSessionFactory();
			mFactorySession = factory.openSession();
		}
		return mFactorySession;
	}
	
	public void closeSqlSession(){
		if(mFactorySession != null){
			mFactorySession.close();
		}
		mFactorySession = null;
	}
	
	private static class LazyHolder{
		private static final DataBaseConfig INSTANCE = new DataBaseConfig();
	}
}
