package com.mashibing.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import com.mashibing.spring.entity.User;
import com.mashibing.spring.service.MainService;

@Controller("mainController")
public class MainController {

	/**
	 *负责逻辑跳转
	 *在web环境下，由controller层先接入
	 * @return
	 */
	
	@Autowired
	private MainService srv;
	
	public String list() {
		
		String loginName = "zhangfg";
		String password = "123456";
		User user = srv.login(loginName,password);
		
		if(user == null) {
			return "登录成功";
		}else {
			return "登录失败";
		}
	}
	
	
	// get/set ���������� -> �ϵ��ӽ�  Proxy CGLib
}
