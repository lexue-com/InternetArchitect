package com.mashibing.spring;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestGetBean {

	/**
	 * Spring 对对象的生产来说就两种:
	 * 1.单例 singleton -> ws request(一次请求get一个bean) session application ->生命周期绑定
	 * 2. new 出来的prototype
	 * @param args
	 */
	
	public static void main(String[] args) {
		
		

		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		Car car1 = ctx.getBean("car1",Car.class);
		System.out.println("动态工厂car1，是Spring通过成员变量set注入，name:"+car1.getName() + "  price:" +car1.getPrice());
		
		Car car2 = ctx.getBean("car2",Car.class);
		System.out.println("动态工厂car2，是Spring通过工厂类的方法注入，name:"+car2.getName() + "  price:" +car2.getPrice());

		Car car3 = ctx.getBean("car3",Car.class);
		System.out.println("静态工厂car3，是Spring通过工厂类的静态方法注入，name:"+car3.getName() + "  price:" +car3.getPrice());
		try {
			Car car4= CarFactory.getCarByPrice("120w");
			System.out.println("我是程序直接调用工厂静态方法获取的car4，name:"+car4.getName() + "  price:" +car4.getPrice());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	
				
	
		

	}
}
