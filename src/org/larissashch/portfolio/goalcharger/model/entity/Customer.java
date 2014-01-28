package org.larissashch.portfolio.goalcharger.model.entity;

public class Customer extends User{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 6039017931788277703L;

	/**<b>Tester account flag</b>
	 * Not null
	 * Valid values:
	 *  - true
	 *  - false
	 * */
	private boolean testerAccountFlag;
	
	/**<b>Auto delete flag</b>
	 * Not null
	 * Valid values:
	 *  - true
	 *  - false
	 * */
	private boolean autoDeleteFlag;

	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public boolean isTesterAccountFlag() {
		return testerAccountFlag;
	}

	public void setTesterAccountFlag(boolean testerAccountFlag) {
		this.testerAccountFlag = testerAccountFlag;
	}

	public boolean isAutoDeleteFlag() {
		return autoDeleteFlag;
	}

	public void setAutoDeleteFlag(boolean autoDeleteFlag) {
		this.autoDeleteFlag = autoDeleteFlag;
	}

}
