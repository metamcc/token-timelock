package io.mcc.mobile.common.config;

import io.mcc.mobile.common.config.version.ApiVersionRequestMappingHandlerMapping;
import io.mcc.mobile.common.resolver.UserAgentWebArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;
import java.util.Locale;

@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {
	
	public static final int API_VERSION = 1;
	public static final int API_VERSION_2 = 2;

	protected RequestMappingHandlerMapping createRequestMappingHandlerMapping() {
		return new ApiVersionRequestMappingHandlerMapping("api/v");
	}
    
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
    	
    	registry.addResourceHandler("swagger-ui.html")
    			.addResourceLocations("classpath:/META-INF/resources/");
    	
    	registry.addResourceHandler("/webjars/**")
    			.addResourceLocations("classpath:/META-INF/resources/webjars/");
	}
    
    /**
     * Resolver
     */
    @Bean 
    public UserAgentWebArgumentResolver uerAgentWebArgumentResolver() {
    	return new UserAgentWebArgumentResolver();
    };
    
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
    	argumentResolvers.add(uerAgentWebArgumentResolver());
    }
    
    /**
     * Interceptor
     */
    @Autowired
	private HandlerInterceptor interceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(interceptor)
				.addPathPatterns("/**")
				.excludePathPatterns("/api/v**/test/**");
	}
	
	
	   @Value("${spring.redis.host}")
	    private String redisHost;

	    @Value("${spring.redis.port}")
	    private int redisPort;
	    

	    @Bean
	    public RedisConnectionFactory redisConnectionFactory() {
	    	
	    	RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();        
	    	redisStandaloneConfiguration.setHostName(redisHost);        
	    	redisStandaloneConfiguration.setPort(redisPort);        


	        return new LettuceConnectionFactory(redisStandaloneConfiguration);
	    }
	    
	    @Bean
	    public  RedisTemplate<String, Object> redisTemplate() {
	    	try {
	            RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
	            redisTemplate.setConnectionFactory(redisConnectionFactory());
	            redisTemplate.setKeySerializer(new StringRedisSerializer());
	            //redisTemplate.setValueSerializer(new StringRedisSerializer());
	            redisTemplate.setHashKeySerializer(new StringRedisSerializer());
	            //redisTemplate.setHashValueSerializer(new StringRedisSerializer());
	            return redisTemplate;	
	    	} catch (Exception e) {
	    		e.printStackTrace();
	    	}
	    	return null;
	    }

	    @Override
		public LocaleResolver localeResolver() {
			AcceptHeaderLocaleResolver alr = new AcceptHeaderLocaleResolver();
			alr.setDefaultLocale(Locale.US);
			return alr;
		}
}
