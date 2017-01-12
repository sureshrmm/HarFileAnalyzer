package com.photon.har.rest;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ResponseBean {
	
	private int status;
	private String message;
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}	
	
	@Override
	public String toString() {
		return "ResponseBean [status=" + status + ", message=" + message + "]";
	}
}
