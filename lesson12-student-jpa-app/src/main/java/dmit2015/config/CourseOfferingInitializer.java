package dmit2015.config;

import dmit2015.model.CourseOffering;
import dmit2015.service.CourseOfferingJpaService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class CourseOfferingInitializer {
    private final Logger logger = Logger.getLogger(CourseOfferingInitializer.class.getName());

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
        logger.info("Initializing courseOfferings");

        if (courseOfferingJpaService.getAllCourseOfferings().isEmpty()) {

            try {
                CourseOffering offering1 = new CourseOffering();
                offering1.setCourseCode("DMTI2015");
                offering1.setTermCode("1253");
                offering1.setSectionCode("OE01");
                courseOfferingJpaService.createCourseOffering(offering1);

                CourseOffering offering2 = new CourseOffering();
                offering2.setCourseCode("SDEV2301");
                offering2.setTermCode("1253");
                offering2.setSectionCode("OE01");
                courseOfferingJpaService.createCourseOffering(offering2);

            } catch (Exception ex) {
                logger.warning(ex.getMessage());
            }

            logger.info("Created " + courseOfferingJpaService.getAllCourseOfferings().size() + " records.");
        }
    }
}