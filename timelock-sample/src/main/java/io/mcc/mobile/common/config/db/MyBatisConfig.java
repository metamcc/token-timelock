package io.mcc.mobile.common.config.db;

import java.io.IOException;

import javax.sql.DataSource;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import io.mcc.mobile.common.CustomBeanNameGenerator;

import io.mcc.common.config.db.support.MNWise;

public abstract class MyBatisConfig {
	
	public static final String BASE_PACKAGE_PREFIX = "io.mcc.mobile";
	
	public static final String CONFIG_LOCATION_PATH = "classpath:mybatis-config.xml";
	
	protected void configureSqlSessionFactory(SqlSessionFactoryBean sessionFactoryBean, DataSource dataSource) throws IOException {
		PathMatchingResourcePatternResolver pathResolver = new PathMatchingResourcePatternResolver();
		
		sessionFactoryBean.setDataSource(dataSource);
		sessionFactoryBean.setConfigLocation(pathResolver.getResource(CONFIG_LOCATION_PATH));
	}
}

@Configuration
@MapperScan(annotationClass=Mapper.class, basePackages={MyBatisConfig.BASE_PACKAGE_PREFIX, "io.mcc"}, sqlSessionFactoryRef = "mobileSqlSessionFactory", nameGenerator = CustomBeanNameGenerator.class)
class MobileMybatisConfig extends MyBatisConfig {
	
	@Bean(name = "mobileSqlSessionFactory")
	@Primary
	public SqlSessionFactory mobileSqlSessionFactory(@Qualifier("mobileDataSource") DataSource mobileDataSource) throws Exception {
		SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
		configureSqlSessionFactory(sessionFactoryBean, mobileDataSource);
		return sessionFactoryBean.getObject();
	}
}

@Configuration
@MapperScan(annotationClass=MNWise.class, basePackages={MyBatisConfig.BASE_PACKAGE_PREFIX, "io.mcc"}, sqlSessionFactoryRef = "mailSqlSessionFactory", nameGenerator = CustomBeanNameGenerator.class)
class MailMybatisConfig extends MyBatisConfig {
	
	@Bean(name = "mailSqlSessionFactory")
	public SqlSessionFactory mailSqlSessionFactory(@Qualifier("mailDataSource") DataSource mailDataSource) throws Exception {
		SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
		configureSqlSessionFactory(sessionFactoryBean, mailDataSource);
		return sessionFactoryBean.getObject();
	}
}
