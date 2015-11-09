package org.smvc.test.orm.model;

import org.smvc.framework.orm.annotation.Table;
/**
 * 
 * @author Big Martin
 *
 */
@Table(name = "grade")
public class Grade {
    private int id;
    
    /**
     * student's id
     */
    private int studentId;
    
    /**
     * 课程名称
     */
    private String className;
    
    /**
     * 成绩
     */
    private int score;

    public Grade(int id, int studentId, String className, int score) {
        super();
        this.id = id;
        this.studentId = studentId;
        this.className = className;
        this.score = score;
    }

    public Grade(int studentId, String className, int score) {
        super();
        this.studentId = studentId;
        this.className = className;
        this.score = score;
    }

    public Grade() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
    
    
    
}
