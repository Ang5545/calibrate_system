package ru.ang5545.calibrate_system.model;

public class ThresholdParameters {
	
	private int min;
	private int max;
	
	public ThresholdParameters(int min, int max) {
		this.min = min;
		this.max = max;
	}
	
	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}
}
