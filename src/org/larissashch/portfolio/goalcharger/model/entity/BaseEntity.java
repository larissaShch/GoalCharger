package org.larissashch.portfolio.goalcharger.model.entity;

import java.util.Date;

public class BaseEntity implements java.io.Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6826409358169945135L;
	private int id;
	private Date createDate;
	private User createdBy;
	
	
	public BaseEntity(){
		id=0;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public User getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	@Override
	public boolean equals(Object obj){
	   if(this.getClass()!=obj.getClass()){
		   return false;
	   }
	   if (this == obj){
		   return true;
	   }
	   BaseEntity entity = (BaseEntity)obj;
	   return (this.getId()==entity.getId());
	}
}