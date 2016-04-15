package ru.ang5545.utils;

import java.io.File;
import java.io.IOException;

public class Path {

	private static String appPath = null;
	
	public static String getAppPath() {
		if (appPath != null) {
			return appPath;
		} else {
			try {
				appPath =  new File(".").getCanonicalPath();
				return appPath;
			} catch (IOException e) {
				System.out.println("app directory not found");
				e.printStackTrace();
				appPath = "";
				return appPath;
			}
		}
	}
	
}
