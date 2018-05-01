package com.example.administrator.bishe.data;

import com.example.administrator.bishe.entities.Course;

import java.util.List;

/**
 * Created by Administrator on 2018/2/7 0007.
 */

public class CourseData {
    private List<Course> courseList;
    private List<Course> notChooseCourseList;
    private List<Course> waitForCheckCourseList;
    private int currentCourse;

    public int getCurrentCourse() {
        return currentCourse;
    }

    public void setCurrentCourse(int currentCourse) {
        this.currentCourse = currentCourse;
    }

    public List<Course> getCourseList() {
        return courseList;
    }

    public CourseData setCourseList(List<Course> courseList) {
        this.courseList = courseList;
        return this;
    }

    public List<Course> getNotChooseCourseList() {
        return notChooseCourseList;
    }

    public CourseData setNotChooseCourseList(List<Course> notChooseCourseList) {
        this.notChooseCourseList = notChooseCourseList;
        return this;
    }

    public List<Course> getWaitForCheckCourseList() {
        return waitForCheckCourseList;
    }

    public void setWaitForCheckCourseList(List<Course> waitForCheckCourseList) {
        this.waitForCheckCourseList = waitForCheckCourseList;
    }
}
