package org.larissashch.portfolio.goalcharger.model.entity;

import java.util.Date;
import java.util.List;

public class Step extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 645663155218272023L;
	
	private String name;
	private String description;
	private Date startDate;
	private Date targetDate;
	private StatusType status;
	private List<String> keyWords;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getTargetDate() {
		return targetDate;
	}
	public void setTargetDate(Date targetDate) {
		this.targetDate = targetDate;
	}
	public StatusType getStatus() {
		return status;
	}
	public void setStatus(StatusType status) {
		this.status = status;
	}
	public List<String> getKeyWords() {
		return keyWords;
	}
	public void setKeyWords(List<String> keyWords) {
		this.keyWords = keyWords;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
	

}
