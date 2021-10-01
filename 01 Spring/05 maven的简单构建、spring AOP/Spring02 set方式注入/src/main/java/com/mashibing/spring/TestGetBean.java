package com.mashibing.spring;


import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestGetBean3 {

	public static void main(String[] args) {

		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		Person3 person1 = ctx.getBean("person",Person3.class);
		System.out.println(ToStringBuilder.reflectionToString(person1));
	}
}
