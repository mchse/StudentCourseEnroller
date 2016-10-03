/*************************************************************
 # StudentDb.java
 # java class file of Assignment 1 for PROG32758,
 # handles student table interactions
 # Author: mchse
 # Created: 2016-Sept-17
 # Updated: 2016-Sept-27
 **********************************************************/
//identify package
package studentCourseEnroller.db;

//import statements
import studentCourseEnroller.Student;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

//student database class
public class StudentDb {
    //set connection variables
    String driver = "com.mysql.jdbc.Driver";
    String connUrl = "jdbc:mysql://localhost/";
    String database = "oakvillerec";
    String user = "root";
    String pass = "MySQL";
    
    //create constants to represent field names
    private final String TABLE_NAME = "student";
    private final String ID = "id";
    private final String FIRST_NAME = "firstName";
    private final String LAST_NAME = "lastName";
    private final String AGE = "age";

    //add student to database
    public int addStudent(Student student) throws Exception {
        //variable set to sql statement
        String formatSql = "INSERT INTO %s (%s, %s, %s) VALUES (?, ?, ?);";
        //initialize sql string with constants
        String sql = String.format(formatSql,
                TABLE_NAME,
                FIRST_NAME,
                LAST_NAME,
                AGE);

        //connection variables initialized
        Connection conn = null;
        PreparedStatement ps = null;
        int result = 0;

        //try to connect and execute sql with course data
        try {
            conn = DBConnector.getConnection(driver, connUrl, database, user, pass);

            ps = conn.prepareStatement(sql);
            ps.setString(1, student.getFirstName());
            ps.setString(2, student.getLastName());
            ps.setInt(3, student.getAge());
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

    //returns all students from database
    public ArrayList<Student> getStudents() throws Exception {
        String formatSql = "SELECT * FROM %s";
        
        String sql = String.format(formatSql, TABLE_NAME);
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        //create an arraylist to hold all courses
        ArrayList<Student> students = new ArrayList<>();
        
        try {
            conn = DBConnector.getConnection(driver, connUrl, database, user, pass);
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()){
                Student student = new Student(
                rs.getString(FIRST_NAME),
                rs.getString(LAST_NAME),
                rs.getInt(AGE),
                rs.getInt(ID));  
                
                //add student object to students arraylist
                students.add(student);
            }
        } catch (Exception ex) {
            throw(ex);
        } finally {
            DBConnector.closeJDBCObjects(conn, ps, rs);
        }
        //return arraylist to calling method
        return students;
    }
   
}
