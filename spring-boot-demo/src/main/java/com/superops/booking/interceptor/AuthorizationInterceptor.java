package com.superops.booking.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.superops.user.service.UserService;

@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

	@Autowired
	UserService userService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String token, encodedPassword;
		Boolean auth;

		token = request.getHeader("Authorization");
		encodedPassword = userService.getEncodedPassword(token);
		auth = userService.validateUser(encodedPassword);
		if (!auth) {
			response.getWriter().write("{ \"Error\": \"Invalid Token\"}");
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.setStatus(401);
		}
		return auth;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("inside the post");

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		System.out.println("inside the agter");

	}

}
