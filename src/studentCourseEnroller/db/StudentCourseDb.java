/*************************************************************
 # StudentCourseDb.java
 # java class file of Assignment 1 for PROG32758#,
 # handles enrollment table interactions
 # Author: mchse
 # Created: 2016-Sept-17
 # Updated: 2016-Sept-27
 **********************************************************/
//identify package
package studentCourseEnroller.db;

//import statements
import studentCourseEnroller.Student;
import studentCourseEnroller.Course;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

//student enrollment in courses database class
public class StudentCourseDb {

    //set connection variables
    String driver = "com.mysql.jdbc.Driver";
    String connUrl = "jdbc:mysql://localhost/";
    String database = "oakvillerec";
    String user = "root";
    String pass = "MySQL";
    
    //create constants to represent field names
    //related to studentcourse table
    private final String TABLE_NAME = "studentcourse";
    private final String STUDENTCOURSE_COURSE_ID = "courseId";
    private final String STUDENTCOURSE_STUDENT_ID = "studentId";
    private final String NUM_ENROLLED = "numEnrolled";
    //related to course table
    private final String TABLE_NAME_COURSE = "course";
    private final String COURSE_NAME = "name";
    private final String COURSE_ID = "id";
    private final String COURSE_START_TIME = "startTime";
    //related to student table
    private final String TABLE_NAME_STUDENT = "student";
    private final String STUDENT_FIRSTNAME = "firstName";
    private final String STUDENT_LASTNAME = "lastName";
    private final String STUDENT_ID = "id";
    
    //inserts student id and course id into studentcourse table
    public int enrollStudentInCourse(Student student, Course course) throws Exception {
        //variable set to sql statement
        String formatSql = "INSERT INTO %s (%s, %s) VALUES (?, ?);";
        //initialize sql string with constants
        String sql = String.format(formatSql,
                TABLE_NAME,
                STUDENTCOURSE_STUDENT_ID,
                STUDENTCOURSE_COURSE_ID);

        //connection variables initialized
        Connection conn = null;
        PreparedStatement ps = null;
        int result = 0;

        //try to connect and execute sql with course data
        try {
            conn = DBConnector.getConnection(driver, connUrl, database, user, pass);

            ps = conn.prepareStatement(sql);
            ps.setInt(1, student.getDbId());
            ps.setInt(2, course.getDbId());
            result = ps.executeUpdate();
            //catch exception and throw    
        } catch (Exception ex) {
            throw(ex);
        //disconnect from database, closing objects    
        } finally {
            DBConnector.closeJDBCObjects(conn, ps);
        }
        return result;
    }

    //finds number of enrolled students in specific course
    public int getStudentCountInCourse(Course course) throws Exception {
        //variable set to sql statement
        String formatSql = "SELECT COUNT(*) AS %s FROM "
                + "%s WHERE %s = ?;";
        //initialize sql string with constants
        String sql = String.format(formatSql,
                NUM_ENROLLED,
                TABLE_NAME,
                STUDENTCOURSE_COURSE_ID);

        //connection variables initialized
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        //count variable initialized
        int count = 0;
        
        //try to connect and execute sql with course data
        try {
            conn = DBConnector.getConnection(driver, connUrl, database, user, pass);

            ps = conn.prepareStatement(sql);
            ps.setInt(1, course.getDbId());
            rs = ps.executeQuery();
            //check if there is a result, advance to first row in result
            rs.next();
            count = rs.getInt(NUM_ENROLLED);
            //catch exception and throw    
        } catch (Exception ex) {
            throw(ex);
        //disconnect from database, closing objects    
        } finally {
            DBConnector.closeJDBCObjects(conn, ps);
        }
        return count;
    }
    
    //returns a list of enrolled courses for a given student (id, name)
    public ArrayList<Course> getEnrollmentsForStudent(Student student) throws Exception {
        //create formatted sql statements and save to variable
        String formatSql = "SELECT %s.%s, %s.%s, %s.%s FROM %s "
                + "INNER JOIN %s "
                + "ON %s.%s = %s.%s "
                + "WHERE %s.%s = ?";

        String sql = String.format(formatSql, 
                TABLE_NAME, STUDENTCOURSE_COURSE_ID,  //SELECT
                TABLE_NAME_COURSE, COURSE_NAME,
                TABLE_NAME_COURSE, COURSE_START_TIME,
                TABLE_NAME, //FROM
                TABLE_NAME_COURSE, //INNER JOIN
                TABLE_NAME, STUDENTCOURSE_COURSE_ID, TABLE_NAME_COURSE, COURSE_ID, //ON
                TABLE_NAME, STUDENTCOURSE_STUDENT_ID); //WHERE
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        //create an arraylist to hold all courses
        ArrayList<Course> enrolledInCourses = new ArrayList<>();
        
        try {
            conn = DBConnector.getConnection(driver, connUrl, database, user, pass);
            ps = conn.prepareStatement(sql);
            ps.setInt(1, student.getDbId());    //set int as student's id
            rs = ps.executeQuery();
            
            //iterate result set make course object for each row, add it to enrolled courses array
            while (rs.next()){
                Course course = new Course(
                rs.getInt(STUDENTCOURSE_COURSE_ID),
                rs.getString(COURSE_NAME),
                rs.getString(COURSE_START_TIME)); 
                
                //add course object to course arraylist
                enrolledInCourses.add(course);
            }
        } catch (Exception ex) {
            throw(ex);
        } finally {
            DBConnector.closeJDBCObjects(conn, ps, rs);
        }
        //return arraylist to calling method
        return enrolledInCourses;
    }

    //removes student from a course in studentcourse table
    public int unenrollStudentFromCourse(Student student, Course course) throws Exception {
        //variable set to sql statement
        String formatSql = "DELETE FROM %s WHERE %s = ? AND %s = ?";
        
        //initialize sql string with constants
        String sql = String.format(formatSql,
                TABLE_NAME,
                STUDENTCOURSE_STUDENT_ID,
                STUDENTCOURSE_COURSE_ID);

        //connection variables initialized
        Connection conn = null;
        PreparedStatement ps = null;
        int result = 0;

        //try to connect and execute sql with course data
        try {
            conn = DBConnector.getConnection(driver, connUrl, database, user, pass);

            ps = conn.prepareStatement(sql);
            ps.setInt(1, student.getDbId());
            ps.setInt(2, course.getDbId());
            result = ps.executeUpdate();
            //catch exception and throw    
        } catch (Exception ex) {
            throw(ex);
        //disconnect from database, closing objects    
        } finally {
            DBConnector.closeJDBCObjects(conn, ps);
        }
        return result;
    }
    
    //returns arraylist of students enrolled in given course from db
    public ArrayList<Student> getStudentsEnrolledInCourse(String courseName) throws Exception {
        //create formatted sql statements and save to variable
        String formatSql = "SELECT %s.%s, %s.%s "
                + "FROM %s "
                + "INNER JOIN %s "
                + "ON %s.%s = %s.%s "
                + "INNER JOIN %s "
                + "ON %s.%s = %s.%s "
                + "WHERE %s.%s = ?";

        String sql = String.format(formatSql, 
                TABLE_NAME_STUDENT, STUDENT_FIRSTNAME,  //Select
                TABLE_NAME_STUDENT, STUDENT_LASTNAME,  //Select
                TABLE_NAME_STUDENT, //from
                TABLE_NAME,   //inner join
                TABLE_NAME_STUDENT, STUDENT_ID,  //on
                TABLE_NAME, STUDENTCOURSE_STUDENT_ID, //on
                TABLE_NAME_COURSE,  //inner join
                TABLE_NAME_COURSE, COURSE_ID,  //on
                TABLE_NAME, STUDENTCOURSE_COURSE_ID,  //on
                TABLE_NAME_COURSE, COURSE_NAME); //where
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        //create an arraylist to hold all courses
        ArrayList<Student> studentsEnrolledInCourse = new ArrayList<>();
        // try to connect to database, pass course name and build an arraylist of students in that course
        try {
            conn = DBConnector.getConnection(driver, connUrl, database, user, pass);
            ps = conn.prepareStatement(sql);
            ps.setString(1, courseName);    //set string as course name
            rs = ps.executeQuery();
            
            //iterate result set make course object for each row, add it to enrolled courses array
            while (rs.next()){
                Student student = new Student(
                rs.getString(STUDENT_FIRSTNAME),
                rs.getString(STUDENT_LASTNAME)); 
                
                //add student object to students arraylist
                studentsEnrolledInCourse.add(student);
            }
        } catch (Exception ex) {
            throw(ex);
        } finally {
            DBConnector.closeJDBCObjects(conn, ps, rs);
        }
        //return arraylist to calling method
        return studentsEnrolledInCourse;
    } 
    
    //delete course from database
    public int deleteCourse (Course course) throws Exception {
        //create formatted sql statements and save to variable
        String formatSql = "DELETE FROM %s "
                + "WHERE %s.%s = ?";

        String sql = String.format(formatSql, 
                TABLE_NAME_COURSE, //delete from
                TABLE_NAME_COURSE, COURSE_NAME); //where
        
        Connection conn = null;
        PreparedStatement ps = null;
        int result = 0;
        //set course name to string, from course object
        String courseName = course.getCourseName();

        try {
            conn = DBConnector.getConnection(driver, connUrl, database, user, pass);
            ps = conn.prepareStatement(sql);
            ps.setString(1, courseName);    //set string as course name
            result = ps.executeUpdate();

        } catch (Exception ex) {
            throw(ex);
        } finally {
            DBConnector.closeJDBCObjects(conn, ps);
        }
        return result;
    }

    //unenroll student from all enrolled studentcourse table
    public int unenrollStudentFromAllCourses(Student student) throws Exception {
        //create formatted sql statements and save to variable
        String formatSql = "DELETE FROM %s "
                + "WHERE %s = ?";

        String sql = String.format(formatSql, 
                TABLE_NAME, //delete from
                STUDENTCOURSE_STUDENT_ID); //where
        
        Connection conn = null;
        PreparedStatement ps = null;
        int result = 0;
        //set id to int from student object
        int id = student.getDbId();

        try {
            conn = DBConnector.getConnection(driver, connUrl, database, user, pass);
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);    //set string as course name
            result = ps.executeUpdate();

        } catch (Exception ex) {
            throw(ex);
        } finally {
            DBConnector.closeJDBCObjects(conn, ps);
        }
        return result;
    }

    //delete student from student table    
    public int deleteStudent(Student student) throws Exception {
        //create formatted sql statements and save to variable
        String formatSql = "DELETE FROM %s "
                + "WHERE %s = ?";

        String sql = String.format(formatSql, 
                TABLE_NAME_STUDENT, //delete from
                STUDENT_ID); //where
        
        Connection conn = null;
        PreparedStatement ps = null;
        int result = 0;
        //set id to int from student object
        int id = student.getDbId();

        try {
            conn = DBConnector.getConnection(driver, connUrl, database, user, pass);
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);    //set string as course name
            result = ps.executeUpdate();

        } catch (Exception ex) {
            throw(ex);
        } finally {
            DBConnector.closeJDBCObjects(conn, ps);
        }
        return result;
    }
}
