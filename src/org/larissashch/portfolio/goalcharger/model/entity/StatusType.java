package org.larissashch.portfolio.goalcharger.model.entity;

public enum StatusType implements java.io.Serializable {
	NEW("NEW"), 
	INPROGRESS("INPROGRESS"),
	PLANCHANGE("PLANCHANGE"),
	ACHIEVED("ACHIEVED"),
	POSTPONED("POSTPONED");

	private final String value;

	private StatusType(String value) {
		this.value = value;
	}

	public static StatusType getAccountType(String value) {

		if (value.equals("NEW")) {
			return StatusType.NEW;
		}
		if (value.equals("INPROGRESS")) {
			return StatusType.INPROGRESS;
		}
		if (value.equals("PLANCHANGE")) {
			return StatusType.PLANCHANGE;
		}
		if (value.equals("ACHIEVED")) {
			return StatusType.ACHIEVED;
		}
		if (value.equals("POSTPONED")) {
			return StatusType.POSTPONED;
		}

		return null;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return value;
	}
}
