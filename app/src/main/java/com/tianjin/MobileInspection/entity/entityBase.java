package com.tianjin.MobileInspection.entity;

import org.xutils.db.annotation.Column;

public class entityBase {

	@Column(name="id" ,isId=true)
	int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
}
