package com.example.administrator.bishe.data;

import com.example.administrator.bishe.entities.Course;
import com.example.administrator.bishe.entities.Student;

import java.util.List;

/**
 * Created by Administrator on 2018/2/7 0007.
 */

public class DataCenter {
    private static DataCenter instance = new DataCenter();
    public static DataCenter getInstance(){
        return  instance;
    }
    private StudentData studentData = new StudentData();
    private CourseData courseData = new CourseData();
    private ChapterData chapterData = new ChapterData();

    public ChapterData getChapterData() {
        return chapterData;
    }

    public void setChapterData(ChapterData chapterData) {
        this.chapterData = chapterData;
    }

    public StudentData getStudentData() {
        return studentData;
    }

    public void setStudentData(StudentData studentData) {
        this.studentData = studentData;
    }

    public CourseData getCourseData() {
        return courseData;
    }

    public void setCourseData(CourseData courseData) {
        this.courseData = courseData;
    }

    public Student getStudent(){
        return studentData.getStudent();
    }
    public void setStudent(Student student){
        studentData.setStudent(student);
    }

    public void setNotChooseCourses(List<Course> courses){
        courseData.setNotChooseCourseList(courses);
    }
    public void setWaitForCheckCourse(List<Course> courses){
        courseData.setWaitForCheckCourseList(courses);
    }
    public List<Course> getNodeChooseCourse(){
        return courseData.getNotChooseCourseList();
    }
    public void setChooseCourse(List<Course> courses){
        courseData.setCourseList(courses);
    }
    public List<Course> getChooseCourse(){
        return courseData.getCourseList();
    }
}
