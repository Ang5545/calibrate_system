package ru.ang5545.calibrate_system.model;

import java.util.ArrayList;
import java.util.List;

import org.bytedeco.javacpp.opencv_core.CvPoint;

public class CalibFrameLine {

	private List<CvPoint> points;
	
	public CalibFrameLine() {
		this.points = new ArrayList<CvPoint>();
	}
	
	public void add(CvPoint point) {
		points.add(point);
	}

	@Override
	public String toString() {
		return "CalibFrameLine [points=" + points + "]";
	}

	public int size() {
		return points.size();
	}
	
	public CvPoint get(int index) {
		return points.get(index);
	}
}
