package dmit2015.service;

import dmit2015.model.CourseOffering;

import java.util.List;
import java.util.Optional;

public interface CourseOfferingService {

    CourseOffering createCourseOffering(CourseOffering courseOffering);

    Optional<CourseOffering> getCourseOfferingById(Long id);

    List<CourseOffering> getAllCourseOfferings();

    CourseOffering updateCourseOffering(CourseOffering courseOffering);

    void deleteCourseOfferingById(Long id);

    void deleteAllCourseOfferings();
}