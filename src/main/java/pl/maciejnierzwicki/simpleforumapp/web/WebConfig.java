package pl.maciejnierzwicki.simpleforumapp.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements WebMvcConfigurer {
	
	@Autowired
	private ForumSetupModeInterceptor setup_mode_interceptor;
	
	@Autowired
	private ForumNotSetupModeInterceptor normal_mode_interceptor;
	
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(setup_mode_interceptor).addPathPatterns("/**").excludePathPatterns("/install/**");
        registry.addInterceptor(normal_mode_interceptor).addPathPatterns("/install/**");
    }
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/login").setViewName("user/login");
	}


}
