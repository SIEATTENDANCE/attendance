package com.sie.attend.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class LoginInterceptor implements HandlerInterceptor {

	/**
	 * 在Controller的处理方法之前被执行
	 * 执行一些初始化，权限判断，日志等处理
	 * 如果返回false 将不会执行Controller中的处理方法
	 * */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		System.out.println("进入拦截器");
		// 获取请求的URL
		String url = request.getRequestURI();
		System.out.println(url);

		if (url.indexOf("login.html") >= 0 || url.indexOf("login/verify") >= 0) {
			return true;
		}
		
		// 获取Session
		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("user_name");

		if (username != null) {
			return true;
		}
		// 不符合条件的，跳转到登录界面
		// request.getRequestDispatcher("/login.html").forward(request,
		// response);
		return false;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
