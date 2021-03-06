package com.sie.attend.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.javassist.bytecode.analysis.MultiArrayType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sie.attend.common.bo.CommonBO;
import com.sie.attend.util.FloadNumber;

/**
 * 点击新增后进入后的页面
 * 
 * @return
 */
@RestController // 等价于@controller加@ResponseBody
@RequestMapping("/ExceptionChange")
public class ExceptionChangeController {

	@Resource(name = "commonBO")
	private CommonBO commonBO;

	// 添加异常变更申请,点击保存触发
	@RequestMapping(value = { "/addExceptionChange" }, method = { RequestMethod.POST }, produces = {
			"application/json" })
	public Map<String, Object> addExceptionChange(HttpServletRequest request,MultipartFile pictureFile) throws Exception, IOException {
		SimpleDateFormat dateStyle = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat TimeStyle = new SimpleDateFormat("HH:mm:ss");
		String date = dateStyle.format(new Date());
		String time = TimeStyle.format(new Date());
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String excepChange = request.getParameter("excepChange");// 获取到的是要申请的异常串,操作明细表
		String exc_cha_id =null;// 获取附件的地址,操作附件表
		String note = request.getParameter("note");// 获取备注信息
		HttpSession session = request.getSession();// 获取用户信息
		String username = (String) session.getAttribute("emp_id");
		//进行图片上传
		System.out.println("图片："+pictureFile);
		if(pictureFile!=null && pictureFile.getOriginalFilename()!=null && pictureFile.getOriginalFilename().length()>0){
			//图片上传成功后，将图片的地址写到数据库
			String filePath = "E:\\pic\\";
			//上传文件原始名称
			String originalFilename = pictureFile.getOriginalFilename();
			//新的图片名称
			String newFileName = UUID.randomUUID() +originalFilename.substring(originalFilename.lastIndexOf("."));
			//新文件
			File file = new java.io.File(filePath+newFileName);
			//将内存中的文件写入磁盘
			pictureFile.transferTo(file);
			//图片上传成功，将新图片地址写入数据库
			exc_cha_id=newFileName;
		}
		System.out.println("图片地址："+exc_cha_id);
		/*
		 * // 测试数据 String excepChange = "1&原因1-2&原因2-3&原因3"; String username =
		 * "123"; String note = "写入备注信息";
		 */
		String foladNumber = FloadNumber.getFloadNumber(date, time);// 得到一个流水号
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("fload_id", foladNumber);
		params.put("username", username);
		params.put("date", date);
		params.put("note", note);
		// 判断订单是否存在,防止多次提交
		Map<String, Object> ifRecordExit = this.commonBO.selectOne("com.sie.data.ExceptionChange.ifRecordExit", params);
		/*
		 * System.out.println("判断：");
		 * System.out.println(ifRecordExit.get("count(*)"));
		 * System.out.println(ifRecordExit.get("count(*)").equals("0"));
		 * System.out.println(ifRecordExit.get("count(*)").toString().equals("0"
		 * ));
		 */
		if (!(ifRecordExit.get("count(*)").toString().equals("0"))) {
			resultMap.put("result", "你已经保存成功了");
			return resultMap;
		}

		String foladNumberReq = request.getParameter("foladNumber");
		//得到流水号
		System.out.println(foladNumberReq);
		if (!(null == foladNumberReq || "".equals(foladNumberReq))) {
			params.put("ExceptionFloadId", foladNumberReq);
			Map<String, Object> ifRecordExit2 = this.commonBO.selectOne("com.sie.data.ExceptionChange.ifRecordExit",
					params);
			if (!(ifRecordExit2.get("count(*)").toString().equals("0"))) {
				// 删除原来的数据;
				this.commonBO.deleteOne("com.sie.data.ExceptionChange.deleteExceptionRecord", params);
				this.commonBO.deleteOne("com.sie.data.ExceptionChange.deleteExceptionDetail", params);
			}
		}

		// 插入异常变更单
		this.commonBO.insertOne("com.sie.data.ExceptionChange.insertExceptionChange", params);
		// 插入异常明细表
		String exId = null;
		String exReason = null;
		// System.out.println("接受到的异常变更串"+excepChange);
		String[] id_reasons = excepChange.split("-"); // 可能有注入的危险，需要处理
		for (int i = 0; i < id_reasons.length; i++) {
			// System.out.println(id_reasons[i]);
			String[] idAndreason = id_reasons[i].split("&");
			exId = idAndreason[0];
			if(idAndreason.length<2){
				exReason="";
			}else{
				exReason=idAndreason[1];
			}
			System.out.println("分割出来后的数据" + exId + " : " + exReason);
			params.put("exId", exId);
			params.put("exReason", exReason);
			// 插入异常明细的记录
			this.commonBO.insertOne("com.sie.data.ExceptionChange.insertExceptionDetail", params);
		}
		// 插入附件表

		resultMap.put("result", "异常申请单保存成功");
		resultMap.put("foladNumber", foladNumber);// 保存成功后，返回该成功订单的流水号，以便用户直接点提交
		return resultMap;
	}

	// 点击提交按钮触发
	@RequestMapping(value = { "/commitExceptionChange" }, method = { RequestMethod.POST }, produces = {
			"application/json" })
	public Map<String, Object> commitExceptionChange(HttpServletRequest request) {
		String ExceptionFloadId = request.getParameter("ExceptionFloadId");// 异常申请单的流水号
		SimpleDateFormat dateStyle = new SimpleDateFormat("yyyy-MM-dd");
		String date = dateStyle.format(new Date());
		// String ExceptionFloadId="YCBG20170410003";//测试数据

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ExceptionFloadId", ExceptionFloadId);
		params.put("ex_state", "commit");
		params.put("ex_node", "new");
		params.put("date", date);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 先判断有没有保存,保存了才能提交
		Map<String, Object> ifCommit = this.commonBO.selectOne("com.sie.data.ExceptionChange.ifCommit", params);
		if (!(ifCommit.get("ex_state").toString().equals("new"))) {
			resultMap.put("result", "请先保存订单");
			return resultMap;
		}
		// 更新异常表的ex_state字段
		int upResult = this.commonBO.updateOne("com.sie.data.ExceptionChange.updatetExceptionState", params);
		if (upResult != 0) {
			resultMap.put("result", "提交成功");
		} else {
			resultMap.put("result", "提交失败");
		}
		return resultMap;
	}

	// 点击中止触发
	@RequestMapping(value = { "/stopExceptionChange" }, method = { RequestMethod.POST }, produces = {
			"application/json" })
	public Map<String, Object> stopExceptionChange(HttpServletRequest request) {
		String ExceptionFloadId = request.getParameter("ExceptionFloadId");// 异常申请单的id
		// String ExceptionFloadId="YCBG20170410003";//测试数据
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ExceptionFloadId", ExceptionFloadId);
		params.put("ex_state", "stop");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 先判断有没有提交,提交了才能中止
		Map<String, Object> ifCommit = this.commonBO.selectOne("com.sie.data.ExceptionChange.ifCommit", params);
		if ((ifCommit.get("ex_state").toString().equals("commit"))) {
			resultMap.put("result", "异常订单还没有提交,不能中止");
			return resultMap;
		} else if (ifCommit.get("ex_state").toString().equals("stop")) {
			resultMap.put("result", "异常订单已经被中止了");
			return resultMap;
		} else if (ifCommit.get("ex_state").toString().equals("over")) {
			resultMap.put("result", "异常订单完成了");
			return resultMap;
		} else {
			if (!(ifCommit.get("ex_node").toString().equals("new"))) {
				resultMap.put("result", "异常订单在审核,不能中止了");
				return resultMap;
			}

		}
		// 更新异常表的ex_state字段
		int upResult = this.commonBO.updateOne("com.sie.data.ExceptionChange.updatetExceptionState", params);
		if (upResult != 0) {
			resultMap.put("result", "异常订单中止成功");
		} else {
			resultMap.put("result", "异常订单终止失败");
		}
		return resultMap;
	}

	// 查询异常记录,点击+号按钮触发弹出所有异常记录
	@RequestMapping(value = { "/selectExceptionRecord" }, method = { RequestMethod.POST }, produces = {
			"application/json" })
	public Map<String, Object> selectExceptionRecord(HttpServletRequest request) {

		HttpSession session = request.getSession();// 获取用户信息
		String username = (String) session.getAttribute("emp_id");
		int showPage = Integer.parseInt(request.getParameter("pageNumber"));// 当第几页
		int pageSize = Integer.parseInt(request.getParameter("pageSize"));// 每一页多少数据

		// 测试数据
		// String username="123";

		int startShow = (showPage - 1) * pageSize;// 分页计算
		Map<String, Object> params = new HashMap<String, Object>(3);
		params.put("username", username);
		params.put("startShow", startShow);
		params.put("pageSize", pageSize);
		List<Map<String, Object>> exceptionRecord = this.commonBO
				.selectList("com.sie.data.ExceptionChange.selectExceptionRecord", params);
		params = this.commonBO.selectOne("com.sie.data.ExceptionChange.selectExceptionRecordCount", params);
		Map<String, Object> resultMap = new HashMap<String, Object>(2);
		resultMap.put("total", params.get("count(*)".toString()));
		resultMap.put("rows", exceptionRecord);
		return resultMap;
	}

	// 添加异常的打卡记录后的页面点击确定触发
	@RequestMapping(value = { "/selectChoiceExcep" }, method = { RequestMethod.POST }, produces = {
			"application/json" })
	public List<Map<String, Object>> selectChoiceExcep(HttpServletRequest request) {
		/*
		 * //测试数据 String choiceRecordId="15-16-21";
		 */
		String choiceRecordId = request.getParameter("choiceRecordId");// 获取备注信息
		String[] choices = choiceRecordId.split(",");// 前端传递数据的方式：id-id-id

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> params = new HashMap<String, Object>();

		for (int i = 0; i < choices.length; i++) {
			params.put("choice", choices[i]);
			Map<String, Object> exceptionRecord = this.commonBO
					.selectOne("com.sie.data.ExceptionChange.selectChoiceExcep", params);
			list.add(exceptionRecord);
		}
		return list;
	}

}
