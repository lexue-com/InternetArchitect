package com.mashibing.spring;

public class A {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "A [name=" + name + "]";
	}

	public A() {
		super();
		System.out.println("A init ~");
	}



	
}
