package pl.maciejnierzwicki.simpleforumapp.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import pl.maciejnierzwicki.simpleforumapp.object.ForumProperties;

@Component
public class ForumSetupModeInterceptor implements HandlerInterceptor{
	
	private ForumProperties properties;
	
	@Autowired
	public ForumSetupModeInterceptor(ForumProperties properties) {
		this.properties = properties;
	}

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
    	if(properties.isSetupMode()) {
	    	try {
				response.sendRedirect("/install");
			} catch (IOException e) {}
    	}
        return true;
    }
}