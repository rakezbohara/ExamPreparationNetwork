package com.n1technology.app.exampreparationnetwork.model;

/**
 * Created by RAKEZ on 11/30/2017.
 */

public class Course {

    private String id;
    private String name;
    private String max_level;
    private String syllabus;

    public Course() {
    }

    public Course(String id, String name, String max_level, String syllabus) {
        this.id = id;
        this.name = name;
        this.max_level = max_level;
        this.syllabus = syllabus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMax_level() {
        return max_level;
    }

    public void setMax_level(String max_level) {
        this.max_level = max_level;
    }

    public String getSyllabus() {
        return syllabus;
    }

    public void setSyllabus(String syllabus) {
        this.syllabus = syllabus;
    }

    @Override
    public String toString() {
        return id+" "+name+" "+max_level+" "+syllabus;
    }
}
