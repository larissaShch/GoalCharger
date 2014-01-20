package org.larissashch.portfolio.goalcharger.model.entity;

import java.util.Date;
import java.util.List;

public class Goal extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 9127912174882616256L;
	private String goalName;
	private String description;
	private CategoryType category;
	private StatusType status;
	private Date startDate;
	private Date targetDate;
	private List <KeyWord> keyWords;
	private List <Step> steps;
	private Float percentOfCharge;
	
	public String getGoalName() {
		return goalName;
	}
	public void setGoalName(String goalName) {
		this.goalName = goalName;
	}
	public CategoryType getCategory() {
		return category;
	}
	public void setCategory(CategoryType category) {
		this.category = category;
	}
	public StatusType getStatus() {
		return status;
	}
	public void setStatus(StatusType status) {
		this.status = status;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Float getPercentOfCharge() {
		return percentOfCharge;
	}
	public void setPercentOfCharge(Float percentOfCharge) {
		this.percentOfCharge = percentOfCharge;
	}
	public List<KeyWord> getKeyWords() {
		return keyWords;
	}
	public void setKeyWords(List<KeyWord> keyWords) {
		this.keyWords = keyWords;
	}
	public List<Step> getSteps() {
		return steps;
	}
	public void setSteps(List<Step> steps) {
		this.steps = steps;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
