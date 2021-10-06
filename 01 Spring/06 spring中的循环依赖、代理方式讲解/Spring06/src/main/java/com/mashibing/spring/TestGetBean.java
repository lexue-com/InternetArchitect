package com.mashibing.spring;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestGetBean {

	
	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");

		
		A a = ctx.getBean("A",A.class);
		System.out.println(ToStringBuilder.reflectionToString(a));

		
		B b = ctx.getBean("B",B.class);
		System.out.println(ToStringBuilder.reflectionToString(b));
		System.out.println(a.getName().equals(""));
		System.out.println(b.getName().equals(""));
	}
}
