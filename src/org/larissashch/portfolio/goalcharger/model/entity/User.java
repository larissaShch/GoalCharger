package org.larissashch.portfolio.goalcharger.model.entity;

import java.util.Date;

public class User implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5459808285327793615L;
	private int id;
	private Date createDate;
	
	/**<b>Frist Name</b>
	 * Not null
	 * Max size: 255
	 * Min size: 3
	 * Valid values:
	 *  - no special symbols except " ", "-"
	 * */
	private String firstName;
	
	/**<b>Last Name</b>
	 * Not null
	 * Max size: 255
	 * Min size: 3
	 * Valid values:
	 *  - no special symbols except " ", "-"
	 * */
	private String lastName;
	
	/**<b>Email</b>
	 * Usual email format
	 * */
	private String email;
	
	/**<b>Password</b>
	 * - latin letters
	 * - numbers
	 * - no special symbols except "_", "-", ".", "@", ",", ";"
	 * */
	private String password;
	
	/**<b>Birth Date</b>
	 * yyyy/MM/dd
	 * */
	private Date birthDate;
	
	

	public String getFirstName() {
		return firstName;
	}



	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}



	public String getLastName() {
		return lastName;
	}



	public void setLastName(String lastName) {
		this.lastName = lastName;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}



	public Date getBirthDate() {
		return birthDate;
	}



	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}



	public String getUserType(){
		return this.getClass().getSimpleName();
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

	@Override
	public boolean equals(Object obj){
	   if(this.getClass()!=obj.getClass()){
		   return false;
	   }
	   if (this == obj){
		   return true;
	   }
	   User user = (User)obj;
	   return (this.getId()==user.getId());
	}

}
