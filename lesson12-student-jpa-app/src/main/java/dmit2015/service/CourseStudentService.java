package dmit2015.service;

import dmit2015.model.CourseStudent;

import java.util.List;
import java.util.Optional;

public interface CourseStudentService {

    CourseStudent createCourseStudent(CourseStudent courseStudent);

    Optional<CourseStudent> getCourseStudentById(Long id);

    List<CourseStudent> getAllCourseStudents();

    CourseStudent updateCourseStudent(CourseStudent courseStudent);

    void deleteCourseStudentById(Long id);
}