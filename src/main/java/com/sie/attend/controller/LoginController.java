package com.sie.attend.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sie.attend.common.bo.CommonBO;
import com.sie.attend.exception.CustomException;

@RestController
@RequestMapping("/login")
public class LoginController {

	@Resource(name="commonBO")
	private CommonBO commonBO;
	/**
	 * 验证登录信息
	 * @param request
	 * @return
	 * @throws CustomException 
	 */
	@RequestMapping(value = { "/getLoginMess" }, method = 
			RequestMethod.POST, produces = { "application/json" })
	public Map<String, Object> getLoginMess(HttpServletRequest request) throws Exception {
		Map<String, Object> returnMap = new HashMap<String, Object>(5);
		// 获取页面传回的用户名、密码、验证码
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String checkCode = request.getParameter("CheckImg");
		//获取session中的验证码，用于和前台传回的验证码比较
		HttpSession session = request.getSession();
		String _checkCode = (String) session.getAttribute("checkcode_session");
		
//		System.out.println("requese:"+checkCode);
//		System.out.println("sesion:"+_checkCode);
		
		//从session中删除。
		session.removeAttribute("checkcode_session");
		
		//判断输入框是否为空
		if ("".equals(username) || username == null || "".equals(password) || password == null) {
			returnMap.put("success", "no");
			returnMap.put("error_msg","请输入完整信息");
			return returnMap;
		}
		Map<String, Object> map = new HashMap<String, Object>(4);
		map.put("emp_id", username);
		//获取账户信息	
		map = commonBO.selectOne("com.sie.attend.pojo.LoginMapper.selectByEmpId", map);
	
		if (map == null || map.size() == 0) { //判断用户名是否存在
			returnMap.put("success", "no");
			returnMap.put("error_msg", "账号不存在");
			
			System.out.println("logincontroller:"+returnMap.toString());
			return returnMap;
			//throw new CustomException("账户不存在");//抛出全局异常
		}else{
			if (map.get("emp_pwd").equals(password)) { //判断密码是否正确
				if(checkCode.equals(_checkCode)){	//判断验证码是否正确
					String emp_id = (String) map.get("emp_id");
					session.setAttribute("emp_id", emp_id);
					returnMap.put("success", "yes");
					returnMap.put("emp_id",emp_id);
					System.out.println("logincontroller:"+returnMap);
					return returnMap;//校验成功
				}else{
					returnMap.put("success", "no");
					returnMap.put("error_msg","验证码错误");
					return returnMap;
				}
			}else{
				returnMap.put("success", "no");
				returnMap.put("error_msg","密码错误");
				return returnMap;//密码错误
			}
		}
	}
	
	/**
	 * 获取菜单目录
	 * @param emp_id
	 * @return
	 */
	@RequestMapping(value={"/getMenuMess/{emp_id}"},method={RequestMethod.GET},produces={"application/json"})
	public List<Map<String,Object>> getMenuMess(@PathVariable("emp_id") String emp_id){
		Map<String,Object> map = new HashMap<String,Object>(1);
		map.put("emp_id", emp_id);
		List<Map<String,Object>> list = this.commonBO.selectList("com.sie.attend.pojo.LoginMapper.selectMenuByEmpId", map);
		//System.out.println(list);
		return list;
	}
	
	/**
	 * 获取员工信息
	 * @param emp_id
	 * @return
	 */
	@RequestMapping(value={"/getEmpMess/{emp_id}"},method=RequestMethod.GET,produces={"application/json"})
	public Map<String,Object> getEmpMess(@PathVariable("emp_id") String emp_id){
		Map<String,Object> map = new HashMap<String,Object>(5);
		map.put("emp_id", emp_id);
		map = this.commonBO.selectOne("com.sie.attend.pojo.LoginMapper.selectByEmpId", map);
		return map;
	}
	/**
	 * 退出登录
	 * @param request
	 * @return
	 */
	@RequestMapping(value={"/quitEmp"},method=RequestMethod.POST,produces={"application/json"})
	public Map<String,Object> quitEmp(HttpServletRequest request){
		Map<String , Object> quitMess = new HashMap<String, Object>(1);
		quitMess.put("quitMess", 1);
		
		HttpSession session = request.getSession(false);//防止创建Session  
		if(session == null){  
			return quitMess;  
		}  
       
		session.removeAttribute("emp_id");  //去除userMess中的session
		return quitMess;  
	}
	
}
