package com.bitemii.distancecalc.model;

public class DistanceMatrix {

	private String distanceStringValue;

	private String durationStringValue;
	
	private Float distanceValue;

	private String distanceUnit;
	
	private long totalTimeInMinutes;
	
	private String durationUnit;
	
	private String outputJSONString;
	
	
	public String getDistanceStringValue() {
		return distanceStringValue;
	}

	public void setDistanceStringValue(String distanceStringValue) {
		this.distanceStringValue = distanceStringValue;
	}

	public String getDurationStringValue() {
		return durationStringValue;
	}

	public void setDurationStringValue(String durationStringValue) {
		this.durationStringValue = durationStringValue;
	}

	public Float getDistanceValue() {
		return distanceValue;
	}

	public void setDistanceValue(Float distanceValue) {
		this.distanceValue = distanceValue;
	}

	public String getDistanceUnit() {
		return distanceUnit;
	}

	public void setDistanceUnit(String distanceUnit) {
		this.distanceUnit = distanceUnit;
	}

	public long getTotalTimeInMinutes() {
		return totalTimeInMinutes;
	}

	public void setTotalTimeInMinutes(long totalTimeInMinutes) {
		this.totalTimeInMinutes = totalTimeInMinutes;
	}

	public String getDurationUnit() {
		return durationUnit;
	}

	public void setDurationUnit(String durationUnit) {
		this.durationUnit = durationUnit;
	}

	public String getOutputJSONString() {
		return outputJSONString;
	}

	public void setOutputJSONString(String outputJSONString) {
		this.outputJSONString = outputJSONString;
	}
	

	@Override
	public String toString() {
		return "Address [distanceStringValue=" + distanceStringValue + ", durationStringValue=" + durationStringValue
				+ ", distanceValue=" + distanceValue + ", distanceUnit=" + distanceUnit + ", totalTimeInMinutes="
				+ totalTimeInMinutes + ", durationUnit=" + durationUnit + "]";
	}

	
	
	
	
	
	
}
