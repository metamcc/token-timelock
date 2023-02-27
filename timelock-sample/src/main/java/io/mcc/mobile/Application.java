package io.mcc.mobile;

import java.util.TimeZone;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import io.mcc.mobile.common.CustomBeanNameGenerator;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

import lombok.extern.slf4j.Slf4j;


@EnableAspectJAutoProxy
@ComponentScan(basePackages = "io.mcc", nameGenerator = CustomBeanNameGenerator.class)
@EnableCaching
@EnableRetry
@EnableTransactionManagement
@SpringBootApplication
@EnableEncryptableProperties
@Slf4j
public class Application implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}
}