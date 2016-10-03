/*************************************************************
 * studentCourseEnroller.java
 * Main file of Assignment 1 for PROG32758
 * Author: mchse
 * Created: 2016-Sept-17
 * Updated: 2016-Sept-27
 * Requirements:
 *-------------
 * Create a database to register students into courses.
 * Import records for already existing and enrolled students.
 * Note: All courses have a maximum of 20 students
 *************************************************************/
//define package
package studentCourseEnroller;

//import statements
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import studentCourseEnroller.db.CourseDb;
import studentCourseEnroller.db.StudentCourseDb;
import studentCourseEnroller.db.StudentDb;

//Assignment class
public class StudentCourseEnroller {

    //main class
    public static void main(String[] args) throws IOException, Exception {
        //print program title
        System.out.println("******** Oakville Parks and Recreation Registration System ********");
        System.out.println();
        //call menu
        menu();
    }
    
    //has to be static because main method is present and is static
    private static void menu() throws IOException, Exception {
        //exit case for while loop, when flag is true
        boolean flag = false;
        //list menu options to user      
        String menu = "\n****************** Menu Options ******************\n"
                + "0 - To exit press 0\n"
                + "1 - To Add a Course press 1\n"
                + "2 - To Add a Student press 2\n"
                + "3 - To Enroll a Student in a Course press 3\n"
                + "4 - To Un-enroll a Student from a Course press 4\n"
                + "5 - To Remove a Course press 5\n"
                + "6 - To Remove a Student press 6\n"
                + "7 - To List Students, given a course name press 7\n"
                +     "**************************************************\n";
        //create scanner object
        Scanner input = new Scanner(System.in);

        //run loop until flag is true, user has to hit zero to exit menu
        while(!flag){
            System.out.println(menu);

            //read input from user
            System.out.print("Please enter a menu choice: ");
            String choice = input.nextLine().trim();

            //switch statement for menu choices
            switch(choice) {
                //option 0 - return from menu()
                case "0":
                    flag = true;
                    break;
                //option 1 - call addCourse()
                case "1":
                    addCourse();
                    break;
                //option 2 - call addStudent()
                case "2":
                    addStudent();
                    break;
                //option 3 - call enrollStudent()
                case "3":
                    enrollStudent();
                    break;
                //option 4 - call unEnrollStudent()
                case "4":
                    unEnrollStudent();
                    break;
                //option 5 - call removeCourse()
                case "5":
                    removeCourse();
                    break;
                //option 6 - call removeStudent()
                case "6":
                    removeStudent();
                    break;
                //option 7 - call listStudentsInCourse()
                case "7":
                    listStudentsInCourse();
                    break;
                //default - display error message
                default:
                    System.out.println("Invalid menu choice.");
            }
        }
    }
    
    //add Course to database
    private static void addCourse() {
        //create scanner object and coursename, startTime variables
        Scanner input = new Scanner(System.in);
        String courseName = null;
        String startTime = null;
        
        //flag for while loop
        Boolean flag = false;
        //validate for empty input and non-letters for input
        while (!flag) {
            //ask user for course name
            System.out.print("Enter course name: ");
            courseName = input.nextLine();
            flag = isValidName(courseName);
        }
        
        //reset flag
        flag = false;
        //validate for empty input
        while(!flag) {
            //query user for course start time
            System.out.print("Enter course start time (hh:mm:ss): ");
            startTime = input.nextLine();
            flag = isNotEmpty(startTime);
        }
        
        //instantiate course with input provided
        Course course = new Course(courseName, startTime);
        //instantiate courseDb object
        CourseDb courseDb = new CourseDb();
        // try to add course, provide feedback message if added or not
        try {
            int result = courseDb.addCourse(course);
            if (result > 0) {
                System.out.println("Course added!");
            } else {
                System.out.println("Course not added.");
            }
        } catch (Exception ex) {
            System.out.println("Course not added.");
            System.out.println(ex.getMessage());
        }
    }
    
    //validates string is letters only (code based on John Karolyi's validation code in Java 1)
    private static boolean isValidName(String name) {
        //empty string check
       if (!isNotEmpty(name)) {
           return false;
       }
        
        //if the string is not letters, return false
        for (int i = 0; i < name.length(); i++) {
            //extract character
            //charAt requires index of char to be returned, returns char at specified index
            char c = name.charAt(i);
            //if letter is not a letter
            if((c < 65 || c > 90) && (c < 97 || c > 122)) {
                //not a letter... inform user
                System.out.println("Name must contain only letters.");
                return false;
            }
        }
        return true;
    }
    
    //validate input is not zero
    private static boolean isNotEmpty(String input) {
        //empty string check
        if (input.isEmpty()) {
            System.out.println("Input cannot be blank.");
            return false;
        }
        return true; 
    }
    
    //add Student to database
    private static void addStudent() {
        //variables and scanner object
        String firstName = null;
        String lastName = null;
        String studentAge = null;
        Scanner input = new Scanner(System.in);
        Boolean flag = false;
        
        //validate input for first name
        while(!flag) {
            //query user for student first name
            System.out.print("Enter student's first name: ");
            firstName = input.nextLine();
            flag = isValidName(firstName);
        }

        flag = false;   //reset flag
        //validate input for last name
        while(!flag) {
            //query user for student last name
            System.out.print("Enter student's last name: ");
            lastName = input.nextLine();
            flag = isValidName(lastName);
        }
        
        flag = false;   //reset flag
        //validate input for age is not empty
        while(!flag) {
            //query user for student age
            System.out.print("Enter student's age: ");
            studentAge = input.nextLine();
            flag = isNotEmpty(studentAge);
        }
        
        //convert age to int
        int age = Integer.parseInt(studentAge);
        
        //instantiate student with input provided
        Student student = new Student(firstName, lastName, age);
        //instantiate Student Db object
        StudentDb studentDb = new StudentDb();
        
        // try to add student, provide feedback message if added or not
        try {
            int result = studentDb.addStudent(student);
            if (result > 0) {
                System.out.println("Student added!");
            } else {
                System.out.println("Student not added.");
            }
        } catch (Exception ex) {
            System.out.println("Student not added.");
            System.out.println(ex.getMessage());
        }
    }
    
    //enroll a Student in a Course
    private static void enrollStudent() {
        //user selects student from list
        Student student = getStudentFromList();
        
        //user selects course from list
        Course course = getCourseBelowLimitFromList();
        
        //if no courses are open for enrollment, display message, exit method.
        if(course == null) {
            System.out.println();
            System.out.println("*** No courses are available for enrollment. ***");
            return;
        }
        
        //enroll student in course
        StudentCourseDb studentCourseDb = new StudentCourseDb();
        
        //declare result variable
        int result;
        //try to enroll student in course
        try {
            result = studentCourseDb.enrollStudentInCourse(student, course);
            //display status: success or failure
            if (result > 0) {
                System.out.println("Successfully enrolled " 
                        + student.getFirstName() + " " + student.getLastName() 
                        + " in " + course.getCourseName() + "!");
            } else {
                System.out.println("Failed to enroll.");
            }
        } catch (Exception ex) {
            System.out.println("Failed: " + ex.toString());
        }
    }
    
    //have user select student from list of all students, returns student object
    private static Student getStudentFromList() {
        //display all students to user and assign arraylist of all students
        ArrayList<Student> students = listStudents();
                
        //create scanner object and input variable for input data
        Scanner input = new Scanner(System.in);
        String menuChoice = null;
        int position = 0;
        
        //set flag
        Boolean flag = false;
        //validate is not empty
        while (!flag) {
            //ask user to select student from the list
            System.out.print("Please select a student: ");
            menuChoice = input.nextLine();
            flag = isNotEmpty(menuChoice); //validate is not empty
        }
        //store user input number as positon, subtract 1 to account for not starting list at 0
        position = Integer.parseInt(menuChoice) - 1;
        
        //return student object based on number selected by user
        return students.get(position);
    }
    
    //have user select course from list of all courses, returns course object
    private static Course getCourseBelowLimitFromList() {
        //display courses with enrollment space to user and assign arraylist of those courses
        ArrayList<Course> coursesBelow = listCoursesBelowLimit();
        //return null if there are no courses below limit
        if (coursesBelow.isEmpty()) {
            return null;
        }
        
        //create scanner object and input variable for input data
        Scanner input = new Scanner(System.in);
        String menuChoice = null;
        int position = 0;
        
        //set flag
        Boolean flag = false;
        //validate is not empty
        while (!flag) {
            //ask user to select course from the list
            System.out.print("Please select a course: ");
            menuChoice = input.nextLine();
            flag = isNotEmpty(menuChoice); //validate is not empty
        }
        //store user input number as positon, subtract 1 to account for not starting list at 0
        position = Integer.parseInt(menuChoice) - 1;
        
        //return course object based on number selected by user
        return coursesBelow.get(position);
    }
    
    //display all students to user, return arraylist of students
    private static ArrayList<Student> listStudents() {
        //create instance of StudentDb
        StudentDb studentDb = new StudentDb();
        //create instance of arraylist for students
        ArrayList<Student> students = null;
        
        try {
            //call getStudents method, get back arraylist of students
            students = studentDb.getStudents();
            System.out.println("Here are the students:");
            //id: first name  last name
            String format = "%d: %s %s"; 
            //loop through arraylist, number each row and list both names for each student
            for (int i = 0; i < students.size(); i++) {
                String output = String.format(format,
                        i + 1,
                        students.get(i).getFirstName(),
                        students.get(i).getLastName());
                System.out.println(output);
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        //return students list
        return students;
    }   
    
    //list courses below the maximum number of allowed students (constraint: COURSE_MAX)
    private static ArrayList<Course> listCoursesBelowLimit() {
        //create instance of CourseDb
        CourseDb courseDb = new CourseDb();
        
        //create an arraylist to hold all courses
        ArrayList<Course> coursesBelow = null;
        
        try {
            //call getCourses method, get back ArrayList of courses to display
            coursesBelow = courseDb.getCoursesBelowLimit();
            System.out.println("Here are the courses available for enrollment:");
            
            String format = "%d: %s"; //id: name
            
            //already have course list, no need to query db again
            for (int i = 0; i < coursesBelow .size(); i++) {
                String output = String.format(format,
                        i + 1,
                        coursesBelow.get(i).getCourseName());
                System.out.println(output);
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        return coursesBelow;
    }
    
    //lists all courses from database
    private static ArrayList<Course> listCourses() {
        //create instance of CourseDb
        CourseDb courseDb = new CourseDb();
        
        //create an arraylist to hold all courses
        ArrayList<Course> courses = null;
        
        try {
            //call getCourses method, get back ArrayList of courses to display
            courses = courseDb.getCourses();
            System.out.println("Here are the courses:");
            
            String format = "%d: %s"; //id: name

            //already have course list, no need to query db again
            for (int i = 0; i < courses.size(); i++) {
                String output = String.format(format,
                        i + 1,
                        courses.get(i).getCourseName());
                System.out.println(output);
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        return courses;
    }
    
    //un-enroll Student from Course
    private static void unEnrollStudent() {
        //display listing of students: id, fname, lname
        //ask user to choose which student (assume valid id entered)
        Student student = getStudentFromList();

        //show listing of enrolled courses (id, name) for student selected by user
        //ask user to select which course (assume valid id entered) to unenroll from
        Course course = getCourseFromEnrolledList(student); 
        
        //if student is not enrolled in courses, display message, exit method.
        if(course == null) {
            System.out.println();
            System.out.println("*** Student is not enrolled in any course. ***");
            return;
        }
        
        //unenroll student in course
        StudentCourseDb studentCourseDb = new StudentCourseDb();
        
        //declare result variable
        int result;
        //try to unenroll student in course and display whether successful (remove record in bridge table)
        try {
            result = studentCourseDb.unenrollStudentFromCourse(student, course);
            //display status: success or failure
            if (result > 0) {
                System.out.println("***"
                        + "Successfully unenrolled " 
                        + student.getFirstName() + " " + student.getLastName() 
                        + " from the " + course.getCourseName() + " course.***");
            } else {
                System.out.println("Failed to unenroll.");
            }
        } catch (Exception ex) {
            System.out.println("Failed: " + ex.toString());
        }
    }

    //display listing of courses student is enrolled in, get user to select one
    private static Course getCourseFromEnrolledList(Student student) {
        //display listing of courses student is enrolled in
        ArrayList<Course> enrolledCourses = listEnrollmentsForStudent(student);
        
        //check if student is not enrolled in any courses
        if(enrolledCourses.isEmpty()) {
            return null;
        }
        
        //create scanner object and input variable for input data
        Scanner input = new Scanner(System.in);
        String menuChoice = null;
        int position = 0;
        
        //set flag
        Boolean flag = false;
        //validate is not empty
        while (!flag) {
            //ask user to select course from list of courses student is enrolled in
            System.out.print("Please select a course to unenroll student from: ");
            menuChoice = input.nextLine();
            flag = isNotEmpty(menuChoice); //validate is not empty
        }
        //store user input number as positon, subtract 1 to account for not starting list at 0
        position = Integer.parseInt(menuChoice) - 1;
        
        //return course object based on number selected by user
        return enrolledCourses.get(position);
    }
    
    //display listing of enrolled courses for selected student
    private static ArrayList<Course> listEnrollmentsForStudent(Student student) {
        //instantiate student course db object
        StudentCourseDb studentCourseDb = new StudentCourseDb();
        ArrayList<Course> enrolledCourses = null; 
        
        //try to get list of courses student is enrolled in from database
        try {
            enrolledCourses = studentCourseDb.getEnrollmentsForStudent(student);
                       
            String format = "%d: %s"; //id: name

            //output menu number and course name
            for (int i = 0; i < enrolledCourses.size(); i++) {
                String output = String.format(format,
                        i + 1,
                        enrolledCourses.get(i).getCourseName());
                System.out.println(output);
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        
        //return array of enrolled courses
        return enrolledCourses;
    }
    
    //remove selected course from database if empty
    private static void removeCourse() {
        //user selects course from list to remove
        Course course = getCourseFromList();
        
        //store course name as string
        String courseName = course.getCourseName();

        //instantiate student course db object
        StudentCourseDb studentCourseDb = new StudentCourseDb();
        //instantiate array list for students in that course
        ArrayList<Student> studentsEnrolledInCourse = null;

        //try to get list of students enrolled in course, or display error message if non enrolled
        try {
            studentsEnrolledInCourse = studentCourseDb.getStudentsEnrolledInCourse(courseName);
           
            //if no students are enrolled in course, delete course
            if(studentsEnrolledInCourse.isEmpty()) {
                //delete course
                studentCourseDb.deleteCourse(course);
                System.out.println("*** The course has been deleted. ***");
            } else { //there are students in the course
                System.out.println("*** The course has not been deleted");
                // display list of enrolled students
                System.out.println("Here are the students enrolled in the course: ");
                //format is int fname lname
                String format = "%d: %s %s";
                //loop through arraylist, number each row and list both names for each student
                for (int i = 0; i < studentsEnrolledInCourse.size(); i++) {
                    String output = String.format(format,
                        i + 1,
                        studentsEnrolledInCourse.get(i).getFirstName(),
                        studentsEnrolledInCourse.get(i).getLastName());
                System.out.println(output);
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }   
    
        
    //display list of courses to user, have them select a course to remove, returns course id to remove
    private static Course getCourseFromList() {
        //display courses to user and assign arraylist of those courses
        ArrayList<Course> courses = listCourses();
        
        //create scanner object and input variable for input data
        Scanner input = new Scanner(System.in);
        String menuChoice = null;
        int position = 0;
        
        //set flag
        Boolean flag = false;
        //validate is not empty
        while (!flag) {
            //ask user to choose which course (by id, assume valid id entered)
            System.out.print("Please select a course number: ");
            menuChoice = input.nextLine();
            flag = isNotEmpty(menuChoice); //validate is not empty
        }
        //store user input number as positon, subtract 1 to account for not starting list at 0
        position = Integer.parseInt(menuChoice) - 1;
        
        //return course object based on number selected by user
        Course courseToRemove = courses.get(position);
        
        //return course to remove
        return courseToRemove;
    }
    
    //get user to input course name, list students given a course name
    private static void listStudentsInCourse() {
        boolean flag = false;   //set flag
        
        //create scanner object and input variable for input data
        Scanner input = new Scanner(System.in);
        String courseName = null;
        //validate input for course name
        while(!flag) {
            //query user for course name
            System.out.print("Please enter a name of a course: ");
            courseName = input.nextLine();
            flag = isValidName(courseName);
        }
        
        //instantiate student course db object
        StudentCourseDb studentCourseDb = new StudentCourseDb();
        //instantiate array list for students in that course
        ArrayList<Student> studentsEnrolledInCourse = null;

        //try to get list of students enrolled in course, or display error message
        try {
            studentsEnrolledInCourse = studentCourseDb.getStudentsEnrolledInCourse(courseName);
           
            //display the students enrolled in the course, or give message if no students
            if(studentsEnrolledInCourse.isEmpty()) {
                System.out.println("*** There are no students registered in that course. ***");
                return;
            }
            
            //display students registered in given course
            System.out.println("Here are the students registered in the course: "); 
            //format is: number, first name, last name
            String format = "%d: %s %s";
            
            //output menu number and course name
            for (int i = 0; i < studentsEnrolledInCourse.size(); i++) {
                String output = String.format(format,
                        i + 1,
                        studentsEnrolledInCourse.get(i).getFirstName(),
                        studentsEnrolledInCourse.get(i).getLastName());
                System.out.println(output);
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }  

    //unenrolls student from all courses and delets student record
    private static void removeStudent() throws Exception {
        try {
            //unenroll the student from all courses then remove student record
            unenrollAndDeleteStudentRecord();
        } catch (Exception ex) {
            throw(ex);
        }    
    }

    //unenroll Student from ALL Courses, and remove student record
    private static void unenrollAndDeleteStudentRecord() throws Exception {
        //display listing of students: id, fname, lname
        //ask user to choose which student (assume valid id entered)
        Student student = getStudentFromList();
        
        //instantiate student course db object
        StudentCourseDb studentCourseDb = new StudentCourseDb();
        
        //declare result variable
        int result = 0;         //unenroll student from all courses
        int deleteResult = 0;   //delete student record
        //try to unenroll student from all courses and display whether successful
        try {
            result = studentCourseDb.unenrollStudentFromAllCourses(student);
            //display status: success or failure
            if (result > 0) {
                System.out.println("*** Successfully unenrolled student from all courses. ***");
            }
        } catch (Exception ex) {
            System.out.println("Failed: " + ex.toString());
        }
        
        try {
            //try to delete student record and display whether successful
            deleteResult = studentCourseDb.deleteStudent(student);
            //display status: success or failure
            if (deleteResult > 0) {
                System.out.println("*** Successfully deleted student record. ***");

            } else {
                System.out.println("*** Failed to delete student record. ***");
            }
        } catch (Exception ex2) {
            System.out.println("Failed: " + ex2.toString());
        }
    }
}
   
        
    