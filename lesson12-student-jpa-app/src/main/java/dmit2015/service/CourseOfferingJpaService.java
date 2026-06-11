package dmit2015.service;

import dmit2015.model.CourseOffering;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Named("jpaCourseOfferingService")
@ApplicationScoped
public class CourseOfferingJpaService implements CourseOfferingService {

    // Assign a unitName if there are more than one persistence unit defined in persistence.xml
    @PersistenceContext //(unitName="pu-name-in-persistence.xml")
    private EntityManager entityManager;

    @Override
    @Transactional
    public CourseOffering createCourseOffering(CourseOffering courseOffering) {
        // If the primary key is not an identity column then write code below here to
        // 1) Generate a new primary key value
        // 2) Set the primary key value for the new entity

        entityManager.persist(courseOffering);
        return courseOffering;
    }

    @Override
    public Optional<CourseOffering> getCourseOfferingById(Long id) {
        try {
            CourseOffering querySingleResult = entityManager.find(CourseOffering.class, id);
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
    public List<CourseOffering> getAllCourseOfferings() {
        return entityManager.createQuery("SELECT o FROM CourseOffering o ", CourseOffering.class)
                .getResultList();
    }

    @Override
    @Transactional
    public CourseOffering updateCourseOffering(CourseOffering courseOffering) {

        Optional<CourseOffering> optionalCourseOffering = getCourseOfferingById(courseOffering.getId());
        if (optionalCourseOffering.isEmpty()) {
            String errorMessage = String.format("The id %s does not exists in the system.", courseOffering.getId());
            throw new RuntimeException(errorMessage);
        } else {
            var existingCourseOffering = optionalCourseOffering.orElseThrow();
            // Update only properties that is editable by the end user

            existingCourseOffering.setCourseCode(courseOffering.getCourseCode());
            existingCourseOffering.setTermCode(courseOffering.getTermCode());
            existingCourseOffering.setSectionCode(courseOffering.getSectionCode());
            existingCourseOffering.setTitle(courseOffering.getTitle());


            courseOffering = entityManager.merge(existingCourseOffering);
        }
        return courseOffering;
    }

    @Override
    @Transactional
    public void deleteCourseOfferingById(Long id) {
        Optional<CourseOffering> optionalCourseOffering = getCourseOfferingById(id);
        if (optionalCourseOffering.isPresent()) {
            CourseOffering courseOffering = optionalCourseOffering.orElseThrow();
            // Write code to throw a RuntimeException if this entity contains child records
            entityManager.remove(courseOffering);
        } else {
            throw new RuntimeException("Could not find CourseOffering with id: " + id);
        }
    }

}