package dmit2015.service;

import dmit2015.model.Student;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.security.enterprise.SecurityContext;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Named("jpaStudentService")
@ApplicationScoped
public class StudentJpaService implements StudentService {

    @Inject
    private SecurityContext securityContext;

    // Assign a unitName if there are more than one persistence unit defined in persistence.xml
    @PersistenceContext //(unitName="postgresql-jpa-pu")
    private EntityManager entityManager;

    @Override
    @Transactional
    public Student createStudent(Student student) {
        // If the primary key is not an identity column then write code below here to
        // 1) Generate a new primary key value
        // 2) Set the primary key value for the new entity
        String username = securityContext.getCallerPrincipal().getName();
        if (username.equalsIgnoreCase("anonymous")) {
            throw new RuntimeException("Access denied. Anonymous users does not have access to this method.");
        }

        if (student.getUsername() == null) { // temp workaround where initializer is assigning username
            student.setUsername(username);
        }

        entityManager.persist(student);
        return student;
    }

    @Override
    public Optional<Student> getStudentById(Long id) {
        try {
            Student querySingleResult = entityManager.find(Student.class, id);
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
    public List<Student> getAllStudents() {
        // Security Rules:
        // 1) Sales role can only view data created with their own account
        // 2) Shipping role can view all data from all users
        // 3) Anonymous users are denied access
        String username = securityContext.getCallerPrincipal().getName();
        if (username.equalsIgnoreCase("anonymous")) {
            throw new RuntimeException("Access denied. Anonymous users does not have access to this method.");
        }
        boolean hasRequiredRoles = securityContext.isCallerInRole("Sales")
                || securityContext.isCallerInRole("Shipping");
        if (!hasRequiredRoles) {
            throw new RuntimeException("Access denied. Your role does not permission to access this method.");
        }
        boolean hasSalesRole = securityContext.isCallerInRole("Sales");
        if (hasSalesRole) {
            return entityManager.createQuery(
                    "SELECT s FROM Student s WHERE s.username = :username", Student.class)
                    .setParameter("username", username)
                    .getResultList();
        }

        return entityManager.createQuery("SELECT o FROM Student o ", Student.class)
                .getResultList();
    }

    @Override
    @Transactional
    public Student updateStudent(Student student) {

        Optional<Student> optionalStudent = getStudentById(student.getId());
        if (optionalStudent.isEmpty()) {
            String errorMessage = String.format("The id %s does not exists in the system.", student.getId());
            throw new RuntimeException(errorMessage);
        } else {
            var existingStudent = optionalStudent.orElseThrow();
            // Update only properties that is editable by the end user

            existingStudent.setFirstName(student.getFirstName());
            existingStudent.setLastName(student.getLastName());
            existingStudent.setCourseSection(student.getCourseSection());

            student = entityManager.merge(existingStudent);
        }
        return student;
    }

    @Override
    @Transactional
    public void deleteStudentById(Long id) {
        Optional<Student> optionalStudent = getStudentById(id);
        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.orElseThrow();
            // Write code to throw a RuntimeException if this entity contains child records
            entityManager.remove(student);
        } else {
            throw new RuntimeException("Could not find Student with id: " + id);
        }
    }

    @Override
    public void deleteAllStudents() {
        entityManager.createQuery("DELETE FROM Student").executeUpdate();
    }

    @Override
    public long count() {
        return entityManager.createQuery("SELECT COUNT(o) FROM Student o", Long.class).getSingleResult();
    }

}