package com.example.administrator.bishe.entities;

import java.io.Serializable;

public class Course implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	private int courseId;
	private String courseName;
	private int maxSize;
	private String teacherName;
	public int getCourseId() {
		return courseId;
	}
	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public int getMaxSize() {
		return maxSize;
	}
	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
}
