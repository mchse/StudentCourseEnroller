/*************************************************************
 # CourseDb.java
 # java class file of Assignment 1 for PROG32758,
 # handles course table interactions
 # Author: mchse
 # Created: 2016-Sept-17
 # Updated: 2016-Sept-27
 **********************************************************/

//identify package
package studentCourseEnroller.db;

//import statements
import studentCourseEnroller.Course;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

//database course class
public class CourseDb {

    //set connection variables
    String driver = "com.mysql.jdbc.Driver";
    String connUrl = "jdbc:mysql://localhost/";
    String database = "oakvillerec";
    String user = "root";
    String pass = "MySQL";
    
    //create constants to represent field names
    private final String TABLE_NAME = "course";
    private final String ID = "id";
    private final String NAME = "name";
    private final String START_TIME = "startTime";
    //constant holds value for maximum number of students that can enroll in a course
    private final int COURSE_MAX = 20; 

    //add course to database
    public int addCourse(Course course) throws Exception {
        //variable set to sql statement
        String formatSql = "INSERT INTO %s (%s, %s) VALUES (?, ?);";
        //initialize sql string with constants
        String sql = String.format(formatSql,
                TABLE_NAME,
                NAME,
                START_TIME);

        //connection variabiles initialized
        Connection conn = null;
        PreparedStatement ps = null;
        int result = 0;
        
        //try to connect and execute sql with course data
        try {
            conn = DBConnector.getConnection(driver, connUrl, database, user, pass);
            ps = conn.prepareStatement(sql);
            //set the two parameters to replace question marks in prepare statement above
            ps.setString(1, course.getCourseName());
            ps.setString(2, course.getStartTime());
            result = ps.executeUpdate();
        //catch exception and throw    
        } catch (Exception ex) {
            throw(ex);
        //disconnect from database, closing objects
        } finally {
            DBConnector.closeJDBCObjects(conn, ps);
        }
        //return int indicating success or failure
        return result;
    }   
    
    //get courses below the maximum number of allowed students (constraint: COURSE_MAX)
    //create and output new list of courses below enrollment limit only
    public ArrayList<Course> getCoursesBelowLimit() throws Exception {
        //initialize arraylist for all courses
        ArrayList<Course> courses = null;
        //instantiate arraylist for courses below limit
        ArrayList<Course> coursesBelow = new ArrayList<>();
        
        //instantiate studentCourseDb object
        StudentCourseDb studentCourseDb = new StudentCourseDb();

        //try to populate courses arraylist
        try {
            courses = getCourses();
        } catch (Exception ex) {
            throw(ex);
        }
        //loop through all courses
        for (Course course: courses) {
            //get count of students in course
            int count = studentCourseDb.getStudentCountInCourse(course);
            //if course is below enrollment limit
            if (count < COURSE_MAX) {
                //add course to arraylist for courses below limit
                coursesBelow.add(course);
            }
        }        
        //return arraylist for courses below limit
        return coursesBelow;
    }

    //returns arraylist of all courses in course database
    public ArrayList<Course> getCourses() throws Exception {
        //initialize sql query
        String formatSql = "SELECT * FROM %s";
        String sql = String.format(formatSql, TABLE_NAME);
        
        //initialize database variables
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        //create an arraylist to hold all courses
        ArrayList<Course> courses = new ArrayList<>();
        //try to connect to database and get all courses populated into arraylist of course objects
        try {
            conn = DBConnector.getConnection(driver, connUrl, database, user, pass);
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()){
                Course course = new Course(
                rs.getInt(ID),
                rs.getString(NAME),
                rs.getString(START_TIME));  
                //add new course to arraylist
                courses.add(course);
            }
        } catch (Exception ex) {
            throw(ex);
        } finally {
            DBConnector.closeJDBCObjects(conn, ps, rs);
        }
        //return arraylist to calling method
        return courses;
    }
}
