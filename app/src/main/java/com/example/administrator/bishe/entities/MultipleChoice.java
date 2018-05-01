package com.example.administrator.bishe.entities;

import java.io.Serializable;

public class MultipleChoice implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int multipleChoiceId;
	private String multipleChoiceQuestion;
	private String multipleChoiceA;
	private String multipleChoiceB;
	private String multipleChoiceC;
	private String multipleChoiceD;
	private String multipleChoiceAnswer;
	private int courseId;
	public int getMultipleChoiceId() {
		return multipleChoiceId;
	}
	public void setMultipleChoiceId(int multipleChoiceId) {
		this.multipleChoiceId = multipleChoiceId;
	}
	public String getMultipleChoiceQuestion() {
		return multipleChoiceQuestion;
	}
	public void setMultipleChoiceQuestion(String multipleChoiceQuestion) {
		this.multipleChoiceQuestion = multipleChoiceQuestion;
	}
	public String getMultipleChoiceA() {
		return multipleChoiceA;
	}
	public void setMultipleChoiceA(String multipleChoiceA) {
		this.multipleChoiceA = multipleChoiceA;
	}
	public String getMultipleChoiceB() {
		return multipleChoiceB;
	}
	public void setMultipleChoiceB(String multipleChoiceB) {
		this.multipleChoiceB = multipleChoiceB;
	}
	public String getMultipleChoiceC() {
		return multipleChoiceC;
	}
	public void setMultipleChoiceC(String multipleChoiceC) {
		this.multipleChoiceC = multipleChoiceC;
	}
	public String getMultipleChoiceD() {
		return multipleChoiceD;
	}
	public void setMultipleChoiceD(String multipleChoiceD) {
		this.multipleChoiceD = multipleChoiceD;
	}
	public String getMultipleChoiceAnswer() {
		return multipleChoiceAnswer;
	}
	public void setMultipleChoiceAnswer(String multipleChoiceAnswer) {
		this.multipleChoiceAnswer = multipleChoiceAnswer;
	}
	public int getCourseId() {
		return courseId;
	}
	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}
	
}
