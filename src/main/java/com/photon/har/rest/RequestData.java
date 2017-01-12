package com.photon.har.rest;

public class RequestData {
	private String name;
	private String iteration;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIteration() {
		return iteration;
	}
	public void setIteration(String iteration) {
		this.iteration = iteration;
	}
	
	@Override
	public String toString() {
		return "RequestData [name=" + name + ", iteration=" + iteration + "]";
	}
}


