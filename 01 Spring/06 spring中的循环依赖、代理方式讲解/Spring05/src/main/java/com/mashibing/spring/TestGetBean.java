package com.mashibing.spring;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestGetBean {

	
	public static void main(String[] args) {
		
		//1、代码new出来之后内存里面真实存在，循环引用不会有问题，但是这些对象永远不会被销毁
		A a = new A();
		B b = new B();
		C c = new C();
		a.setB(b);
		b.setC(c);
		c.setA(a);
		System.out.println(ToStringBuilder.reflectionToString(a));
		System.out.println(ToStringBuilder.reflectionToString(a.getB()));
		System.out.println(ToStringBuilder.reflectionToString(a.getB().getC()));
		
//		//2、通过Spring容器
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		
		A a1 = ctx.getBean("A",A.class);
		System.out.println(ToStringBuilder.reflectionToString(a1));
		System.out.println(ToStringBuilder.reflectionToString(a1.getB()));
		System.out.println(ToStringBuilder.reflectionToString(a1.getB().getC()));
		
		
		System.out.println("------");
		A a2 = ctx.getBean("A",A.class);
		System.out.println(ToStringBuilder.reflectionToString(a2));
		System.out.println(ToStringBuilder.reflectionToString(a2.getB()));
		System.out.println(ToStringBuilder.reflectionToString(a2.getB().getC()));
		
		System.out.println(a1 == a2);
		System.out.println(a1.getB() == a2.getB());
//	
//		
		
//		B b = ctx.getBean("B",B.class);
//		System.out.println(ToStringBuilder.reflectionToString(b));
//		
//		A a = ctx.getBean("A",A.class);
//		System.out.println(ToStringBuilder.reflectionToString(a));
//		System.out.println(a.getName().equals(""));
//		System.out.println(b.getName().equals(""));
		

	}
}
