package com.mashibing.spring;

public class CarFactory {
	
	private String color;
	
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	

	public CarFactory(String color) {
		super();
		this.color = color;
	}


	public CarFactory() {
		super();
		System.out.println("CarFactory");
	}
	
	
	
	public Car getCarByColor() throws Exception{
		System.out.println("getCarByColor:"+this.color);
		if (color.equals("blue")) {
			
			return new Bmw();
		}else {
			throw new Exception("暂时没法生产这辆车");
		}
	}
	
	public Car getCar(String name) throws Exception{
		System.out.println("getCar:"+name);
		if (name.equals("audi")) {
			return new Audi();
		}else {
			throw new Exception("暂时没法生产这辆车");
		}
	}
	
	
	
	public static Car getCarByPrice(String price) throws Exception{
		System.out.println("getCarByPrice:"+price);
		if (price.equals("120w")) {
			return new Msladi();
		}else {
			throw new Exception("暂时没法生产这辆车");
		}
	}
	
	

}
