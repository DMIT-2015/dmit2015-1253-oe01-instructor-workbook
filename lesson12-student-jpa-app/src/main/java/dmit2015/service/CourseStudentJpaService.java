package dmit2015.service;

import dmit2015.model.CourseStudent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Named("jpaCourseStudentService")
@ApplicationScoped
public class CourseStudentJpaService implements CourseStudentService {

    // Assign a unitName if there are more than one persistence unit defined in persistence.xml
    @PersistenceContext //(unitName="pu-name-in-persistence.xml")
    private EntityManager entityManager;

    @Override
    @Transactional
    public CourseStudent createCourseStudent(CourseStudent courseStudent) {
        // If the primary key is not an identity column then write code below here to
        // 1) Generate a new primary key value
        // 2) Set the primary key value for the new entity

        entityManager.persist(courseStudent);
        return courseStudent;
    }

    @Override
    public Optional<CourseStudent> getCourseStudentById(Long id) {
        try {
            CourseStudent querySingleResult = entityManager.find(CourseStudent.class, id);
            if (querySingleResult != null) {
                return Optional.of(querySingleResult);
            }
        } catch (Exception ex) {
            // id value not found
            throw new RuntimeException(ex);
        }
        return Optional.empty();
    }

    @Override
    public List<CourseStudent> getAllCourseStudents() {
        return entityManager.createQuery("SELECT o FROM CourseStudent o ", CourseStudent.class)
                .getResultList();
    }

    @Override
    @Transactional
    public CourseStudent updateCourseStudent(CourseStudent courseStudent) {

        Optional<CourseStudent> optionalCourseStudent = getCourseStudentById(courseStudent.getId());
        if (optionalCourseStudent.isEmpty()) {
            String errorMessage = String.format("The id %s does not exists in the system.", courseStudent.getId());
            throw new RuntimeException(errorMessage);
        } else {
            var existingCourseStudent = optionalCourseStudent.orElseThrow();
            // Update only properties that is editable by the end user

            existingCourseStudent.setFirstName(courseStudent.getFirstName());
            existingCourseStudent.setLastName(courseStudent.getLastName());
            existingCourseStudent.setCourseOffering(courseStudent.getCourseOffering());

            courseStudent = entityManager.merge(existingCourseStudent);
        }
        return courseStudent;
    }

    @Override
    @Transactional
    public void deleteCourseStudentById(Long id) {
        Optional<CourseStudent> optionalCourseStudent = getCourseStudentById(id);
        if (optionalCourseStudent.isPresent()) {
            CourseStudent courseStudent = optionalCourseStudent.orElseThrow();
            // Write code to throw a RuntimeException if this entity contains child records
            entityManager.remove(courseStudent);
        } else {
            throw new RuntimeException("Could not find CourseStudent with id: " + id);
        }
    }

}