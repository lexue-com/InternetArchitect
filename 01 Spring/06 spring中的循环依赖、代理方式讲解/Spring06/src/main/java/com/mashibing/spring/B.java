package com.mashibing.spring;

public class B {
	
	private C c;
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "B [name=" + name + "]";
	}


	public B() {
		super();
		System.out.println("B init ~");
	}

	public C getC() {
		return c;
	}

	public void setC(C c) {
		this.c = c;
	}
	
	

	
	
	
}
