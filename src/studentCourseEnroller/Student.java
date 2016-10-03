/*************************************************************
 # Student.java
 # java class file of Assignment 1 for PROG32758,
 # handles constructors, getters and setters for student class
 # Author: mchse
 # Created: 2016-Sept-17
 # Updated: 2016-Sept-27
 **********************************************************/
//define package
package studentCourseEnroller;

//student class
public class Student {
    
    //create fields
    private int dbId;
    private String firstName;
    private String lastName;
    private int age;

    //4 arg constructor
    public Student(String firstName, String lastName, int age, int dbId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.dbId = dbId;
    }
    
    //3 arg constructor, used when adding new student with no id yet
    public Student(String firstName, String lastName, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }
    
    //2 arg constructor, used getting student in specific course
    public Student(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    //getters and setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getDbId() {
        return dbId;
    }

    public void setDbId(int dbId) {
        this.dbId = dbId;
    }
    
}
