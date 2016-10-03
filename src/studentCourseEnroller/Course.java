/*************************************************************
 # Course.java
 # java class file of Assignment 1 for PROG32758,
 # handles constructors, getters and setters for course class
 # Author: mchse
 # Created: 2016-Sept-17
 # Updated: 2016-Sept-27
 **********************************************************/


//define package
package studentCourseEnroller;

//import statements
import java.sql.Time;

//course class
public class Course {
    
    //fields
    private int dbId;
    private String courseName;
    private String startTime;
    
    //2 argument constructor, used when creating course
    public Course(String courseName, String startTime) {
        this.courseName = courseName;
        this.startTime = startTime;
    }
    
    //3 argument constructor, used when getting course from database
    public Course(int dbId, String courseName, String startTime) {
        this.dbId = dbId;
        this.courseName = courseName;
        this.startTime = startTime;
    }

    //getters and setters
    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getDbId() {
        return dbId;
    }

    public void setDbId(int dbId) {
        this.dbId = dbId;
    }
}
