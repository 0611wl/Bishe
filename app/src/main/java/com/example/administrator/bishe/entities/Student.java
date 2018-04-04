package com.example.administrator.bishe.entities;

/**
 * Created by Administrator on 2018/2/7 0007.
 */

import java.io.Serializable;

public class Student implements Serializable{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String studentId;
    private String password;
    private String name;
    private  String faculty;
    private String specializeClass;
    private String telphone;
    private  String email;
    private int state;
    public String getStudentId() {
        return studentId;
    }
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getFaculty() {
        return faculty;
    }
    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }
    public String getSpecializeClass() {
        return specializeClass;
    }
    public void setSpecializeClass(String specializeClass) {
        this.specializeClass = specializeClass;
    }
    public String getTelphone() {
        return telphone;
    }
    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public int getState() {
        return state;
    }
    public void setState(int state) {
        this.state = state;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

}

