package com.warehouse.model;

public class StatisticDataModel {

	private Double yValue;
	private String labelValue;

	public StatisticDataModel(Double yValue, String labelValue) {
		this.yValue = yValue;
		this.labelValue = labelValue;
	}

	public Double getyValue() {
		return yValue;
	}

	public void setyValue(Double yValue) {
		this.yValue = yValue;
	}

	public String getLabelValue() {
		return labelValue;
	}

	public void setLabelValue(String labelValue) {
		this.labelValue = labelValue;
	}
}
