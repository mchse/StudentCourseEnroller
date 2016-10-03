/*************************************************************
 # OakvilleRec.sql
 # SQL file of Assignment 1 for PROG32758
 # Author: mchse
 # Created: 2016-Sept-17
 # Updated: 2016-Sept-27
 # Requirements:
 #-------------
 # SQL file with Student, Course, StudentCourse tables
 **********************************************************/
# Create database oakvillerec and use it  
#  CREATE DATABASE oakvillerec;         # I did this manually in Netbeans, but otherwise this is how to do it
#  USE oakvillerec;

# Drop tables if exist, to start fresh
DROP TABLE IF EXISTS StudentCourse;
DROP TABLE IF EXISTS Student;
DROP TABLE IF EXISTS Course;


# Create Student table and columns
CREATE TABLE Student (
    id INT AUTO_INCREMENT,
    firstName VARCHAR(25) NOT NULL,
    lastName VARCHAR(25) NOT NULL,
    age INT,
    PRIMARY KEY(id)
);

# Create Course table and columns
CREATE TABLE Course (
    id INT AUTO_INCREMENT,
    `name` VARCHAR(25) NOT NULL, # name is reserved word, escape characters used
    startTime TIME NOT NULL,
    PRIMARY KEY(id)
);

# Create StudentCourse bridge table and columns
CREATE TABLE StudentCourse (
    studentId INT,  # composite primary key
    courseId INT,   # composite primary key
    PRIMARY KEY(studentId, courseId),
    CONSTRAINT student_fk                     # add foreign keys
    FOREIGN KEY (studentId) REFERENCES Student(id),
    CONSTRAINT course_fk
    FOREIGN KEY (courseId) REFERENCES Course(id)
);

# Insert manual records into Student table
INSERT INTO Student (firstName, lastName, age) 
VALUES
    ('John', 'Johnson', 10),
    ('Bob', 'Bobson', 11),
    ('Maddie', 'Maddison', 15),
    ('Mary', 'Molson', 12),
    ('Ed', 'Edison', 13),
    ('Mike', 'Molson', 11);

# Insert manual records into Course table
INSERT INTO Course (`name`, startTime)
VALUES
    ('Swimming', '9:00'),
    ('Tennis', '11:00'),
    ('Soccer', '15:00' );

# Insert records into StudentCourse based on registration info 
# (did not hardcode ids of courses)
INSERT INTO StudentCourse (StudentId, CourseId)
VALUES
# John Johnson course registrations
    ((SELECT id from Student WHERE firstName = 'John'),
    (SELECT id from Course WHERE name = 'Swimming')),
    ((SELECT id from Student WHERE firstName = 'John'),
    (SELECT id from Course WHERE name = 'Tennis')),
    ((SELECT id from Student WHERE firstName = 'John'),
    (SELECT id from Course WHERE name = 'Soccer')),
# Bob Bobson course registrations    
    ((SELECT id from Student WHERE firstName = 'Bob'),
    (SELECT id from Course WHERE name = 'Swimming')),
    ((SELECT id from Student WHERE firstName = 'Bob'),
    (SELECT id from Course WHERE name = 'Tennis')),
# Maddie Maddison course registrations    
    ((SELECT id from Student WHERE firstName = 'Maddie'),
    (SELECT id from Course WHERE name = 'Swimming')),
    ((SELECT id from Student WHERE firstName = 'Maddie'),
    (SELECT id from Course WHERE name = 'Tennis')),
    ((SELECT id from Student WHERE firstName = 'Maddie'),
    (SELECT id from Course WHERE name = 'Soccer')),
# Mary Molson course registrations    
    ((SELECT id from Student WHERE firstName = 'Mary'),
    (SELECT id from Course WHERE name = 'Swimming')),
    ((SELECT id from Student WHERE firstName = 'Mary'),
    (SELECT id from Course WHERE name = 'Soccer')),
# Ed Edison course registrations    
    ((SELECT id from Student WHERE firstName = 'Ed'),
    (SELECT id from Course WHERE name = 'Swimming')),
    ((SELECT id from Student WHERE firstName = 'Ed'),
    (SELECT id from Course WHERE name = 'Soccer')),
# Mike Molson course registrations    
    ((SELECT id from Student WHERE firstName = 'Mike'),
    (SELECT id from Course WHERE name = 'Tennis')),
    ((SELECT id from Student WHERE firstName = 'Mike'),
    (SELECT id from Course WHERE name = 'Soccer'));
