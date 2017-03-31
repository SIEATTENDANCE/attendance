package com.sie.attend.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 登录过滤
 * 
 * @author Lenovo
 *
 */
public class SessionFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// 判断是否是http请求
		if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
			throw new ServletException("OncePerRequestFilter just supports HTTP requests");
		}
		// 获得在下面代码中要用的request,response,session对象
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpSession session = httpRequest.getSession(true);

		String[] strs = {"login"}; // 路径中包含这些字符串的,可以不用登录直接访问
		StringBuffer url = httpRequest.getRequestURL();

		/**
		 * 过滤掉根目录
		 */
		String path = httpRequest.getContextPath();
		String protAndPath = httpRequest.getServerPort() == 80 ? "" : ":" + httpRequest.getServerPort();
		String basePath = httpRequest.getScheme() + "://" + httpRequest.getServerName() + protAndPath + path + "/";
		if (basePath.equalsIgnoreCase(url.toString())) {
			chain.doFilter(request, response);
			return;
		}
		// 特殊用途的路径可以直接访问
		if (strs != null && strs.length > 0) {
			for (String str : strs) {
				if (url.indexOf(str) >= 0) {
					chain.doFilter(request, response);
					return;
				}
			}
		}
		// 从session中获取用户信息
		String loginInfo = (String) session.getAttribute("username");
		if (!(null == loginInfo || "".equals(loginInfo))) {
			// 用户存在,可以访问此地址
			chain.doFilter(request, response);
		} else {
			// 用户不存在,踢回登录页面
			String returnUrl = httpRequest.getContextPath() + "/html/login.html";
			httpRequest.setCharacterEncoding("UTF-8");
			httpResponse.setContentType("text/html; charset=UTF-8"); // 转码
			httpResponse.getWriter().println(
					"<script language=\"javascript\">alert(\"您还没有登录，请先登录!\");if(window.opener==null){window.top.location.href=\""
							+ returnUrl + "\";}else{window.opener.top.location.href=\"" + returnUrl
							+ "\";window.close();}</script>");
			return;
		}

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
