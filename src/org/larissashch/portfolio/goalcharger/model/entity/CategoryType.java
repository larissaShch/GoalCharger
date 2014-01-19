package org.larissashch.portfolio.goalcharger.model.entity;

public enum CategoryType implements java.io.Serializable {
	CAREER("CAREER"), 
	EDUCATION("EDUCATION"),
	FAMILY("FAMILY"),
	FINANCIAL("FINANCIAL"),
	HOME("HOME"),
	TRAVEL("TRAVEL"),
	OTHER("OTHER");

	private final String value;

	private CategoryType(String value) {
		this.value = value;
	}

	public static CategoryType getAccountType(String value) {

		if (value.equals("CAREER")) {
			return CategoryType.CAREER;
		}
		if (value.equals("EDUCATION")) {
			return CategoryType.EDUCATION;
		}
		if (value.equals("FAMILY")) {
			return CategoryType.FAMILY;
		}
		if (value.equals("FINANCIAL")) {
			return CategoryType.FINANCIAL;
		}
		if (value.equals("HOME")) {
			return CategoryType.HOME;
		}
		if (value.equals("TRAVEL")) {
			return CategoryType.TRAVEL;
		}
		if (value.equals("OTHER")) {
			return CategoryType.OTHER;
		}
		return null;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return value;
	}
}