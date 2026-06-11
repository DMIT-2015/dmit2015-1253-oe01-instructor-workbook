package dmit2015.config;

import dmit2015.model.CourseOffering;
import dmit2015.model.CourseStudent;
import dmit2015.model.Student;
import dmit2015.service.CourseOfferingJpaService;
import dmit2015.service.CourseStudentJpaService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import net.datafaker.Faker;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class CourseStudentInitializer {
    private final Logger logger = Logger.getLogger(CourseStudentInitializer.class.getName());

    @Inject
    private CourseStudentJpaService courseStudentJpaService;

    @Inject
    private CourseOfferingJpaService courseOfferingJpaService;

    /**
     * Using the combination of `@Observes` and `@Initialized` annotations, you can
     * intercept and perform additional processing during the phase of beans or events
     * in a CDI container.
     * <p>
     * The @Observers is used to specify this method is in observer for an event
     * The @Initialized is used to specify the method should be invoked when a bean type of `ApplicationScoped` is being
     * initialized
     * <p>
     * Execute code to create the test data for the entity.
     * This is an alternative to using a @WebListener that implements a ServletContext listener.
     * <p>
     * ]    * @param event
     */
    public void initialize(@Observes @Initialized(ApplicationScoped.class) Object event) {
        logger.info("Initializing courseStudents");

        Faker faker = new Faker();
        if (courseOfferingJpaService.getAllCourseOfferings().isEmpty()) {
            CourseOffering newCourseOffering = CourseOffering.of(faker);
            newCourseOffering = courseOfferingJpaService.createCourseOffering(newCourseOffering);
            if (courseStudentJpaService.getAllCourseStudents().isEmpty()) {

                try {
                    for(int count = 1; count <= 32; count++) {
                        CourseStudent currentStudent = CourseStudent.of(faker);
                        currentStudent.setCourseOffering(newCourseOffering);
                        courseStudentJpaService.createCourseStudent(currentStudent);
                    }
                } catch (Exception ex) {
                    logger.warning(ex.getMessage());
                }

                logger.info("Created " + courseStudentJpaService.getAllCourseStudents().size() + " records.");
            }
        }
    }
}