package com.photon.har.rest;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ResponseBean {
	
	private int status;
	private String message;
	private String jsonObject;
	private String downloadUrl;
	private String firstHARUrl;
	private String secondHARUrl;
	
	public String getFirstHARUrl() {
		return firstHARUrl;
	}
	public void setFirstHARUrl(String firstHARUrl) {
		this.firstHARUrl = firstHARUrl;
	}
	public String getSecondHARUrl() {
		return secondHARUrl;
	}
	public void setSecondHARUrl(String secondHARUrl) {
		this.secondHARUrl = secondHARUrl;
	}
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
	public String getDownloadUrl() {
		return downloadUrl;
	}
	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}
	public String getJsonObject() {
		return jsonObject;
	}
	public void setJsonObject(String jsonObject) {
		this.jsonObject = jsonObject;
	}
	
}
