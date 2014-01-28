package org.larissashch.portfolio.goalcharger.model.entity;

import java.io.File;

public enum ApplicationProperties {
	DB_PATH("C:"+File.separator+"GaolChargerDB"+File.separator);

	private final String value;

	private ApplicationProperties(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return value;
	}

}
