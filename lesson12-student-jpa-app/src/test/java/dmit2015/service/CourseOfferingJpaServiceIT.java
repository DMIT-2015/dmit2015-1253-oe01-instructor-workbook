package dmit2015.service;

import dmit2015.config.ApplicationConfig;
import dmit2015.config.CourseOfferingInitializer;
import dmit2015.model.CourseOffering;
import dmit2015.model.CourseStudent;
import jakarta.annotation.Resource;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.NotSupportedException;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;
import net.datafaker.Faker;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.PomEquippedResolveStage;
import org.junit.jupiter.api.*;
import org.jboss.arquillian.junit5.container.annotation.ArquillianTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ArquillianTest
public class CourseOfferingJpaServiceIT { // The class must be declared as public

    static Faker faker = new Faker();

    static String mavenArtifactIdId;

    @Deployment
    public static WebArchive createDeployment() throws IOException, XmlPullParserException {
        PomEquippedResolveStage pomFile = Maven.resolver().loadPomFromFile("pom.xml");
        MavenXpp3Reader reader = new MavenXpp3Reader();
        Model model = reader.read(new FileReader("pom.xml"));
        mavenArtifactIdId = model.getArtifactId();
        final String archiveName = model.getArtifactId() + ".war";
        return ShrinkWrap.create(WebArchive.class, archiveName)
                .addAsLibraries(pomFile.resolve("org.codehaus.plexus:plexus-utils:3.4.2").withTransitivity().asFile())
                .addAsLibraries(pomFile.resolve("org.hamcrest:hamcrest").withTransitivity().asFile())
                .addAsLibraries(pomFile.resolve("org.assertj:assertj-core").withTransitivity().asFile())
                .addAsLibraries(pomFile.resolve("net.datafaker:datafaker").withTransitivity().asFile())
                .addAsLibraries(pomFile.resolve("com.h2database:h2").withTransitivity().asFile())
                .addAsLibraries(pomFile.resolve("com.microsoft.sqlserver:mssql-jdbc").withTransitivity().asFile())
                .addAsLibraries(pomFile.resolve("com.oracle.database.jdbc:ojdbc17").withTransitivity().asFile())
                .addAsLibraries(pomFile.resolve("org.postgresql:postgresql").withTransitivity().asFile())
//                .addAsLibraries(pomFile.resolve("com.mysql:mysql-connector-j").withTransitivity().asFile())
//                .addAsLibraries(pomFile.resolve("org.mariadb.jdbc:mariadb-java-client").withTransitivity().asFile())
                // .addAsLibraries(pomFile.resolve("org.hibernate.orm:hibernate-spatial").withTransitivity().asFile())
                // .addAsLibraries(pomFile.resolve("org.eclipse:yasson").withTransitivity().asFile())
//                .addPackages(true,
//                        "dmit2015.config",
//                        "dmit2015.model",
//                        "dmit2015.service"
//                )
                .addClasses(ApplicationConfig.class,
                        CourseOffering.class,
                        CourseOfferingService.class,
                        CourseOfferingJpaService.class,
                        CourseOfferingInitializer.class,
                        CourseStudent.class)
                .addAsResource("META-INF/persistence.xml")
                // .addAsResource(new File("src/test/resources/META-INF/persistence-entity.xml"),"META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Inject
//    @Named("jpaCourseOfferingService")
    private CourseOfferingJpaService courseOfferingService;

    @Resource
    private UserTransaction beanManagedTransaction;

    @BeforeAll
    static void beforeAllTests() {
        // code to execute before all tests in the current test class
    }

    @AfterAll
    static void afterAllTests() {
        // code to execute after all tests in the current test class
    }

    @BeforeEach
    void beforeEachTestMethod() throws SystemException, NotSupportedException {
        // Start a new transaction
        beanManagedTransaction.begin();
    }

    @AfterEach
    void afterEachTestMethod() throws SystemException {
        // Rollback the transaction
        beanManagedTransaction.rollback();
    }

    @Test
    @Order(1)
    void shouldInjectCourseOfferingJpaService() {
        assertThat(courseOfferingService).isNotNull();
    }

    @Order(2)
    @Test
    void givenNewCourseOffering_whenAddCourseOffering_thenCourseOfferingIsAdded() {
        // Arrange
        CourseOffering newCourseOffering = CourseOffering.of(faker);

        // Act
        courseOfferingService.createCourseOffering(newCourseOffering);

        // Assert
        assertThat(newCourseOffering.getId())
                .isNotNull();

    }

    @Order(3)
    @Test
    void givenExistingId_whenFindById_thenReturnEntity() {
        // Arrange
        CourseOffering newCourseOffering = CourseOffering.of(faker);

        // Act
        newCourseOffering = courseOfferingService.createCourseOffering(newCourseOffering);

        // Assert
        Optional<CourseOffering> optionalCourseOffering = courseOfferingService.getCourseOfferingById(newCourseOffering.getId());
        assertThat(optionalCourseOffering.isPresent())
                .isTrue();
        // Assert
        var existingCourseOffering = optionalCourseOffering.orElseThrow();
        assertThat(existingCourseOffering)
                .usingRecursiveComparison()
                // .ignoringFields("field1", "field2")
                .isEqualTo(newCourseOffering);

    }

//    @Order(4)
//    @Test
//    void givenExistingEntity_whenUpdatedCourseOffering_thenCourseOfferingIsUpdated() {
//        // Arrange
//        CourseOffering newCourseOffering = CourseOffering.of(faker);
//
//        newCourseOffering = courseOfferingService.createCourseOffering(newCourseOffering);
//        // TODO: change the values of all properties
//        //newCourseOffering.setProperty1(faker.providerName().methodName());
//        //newCourseOffering.setProperty2(faker.providerName().methodName());
//        //newCourseOffering.setProperty3(faker.providerName().methodName());
//
//        // Act
//        CourseOffering updatedCourseOffering = courseOfferingService.updateCourseOffering(newCourseOffering);
//
//        // Assert
//        Optional<CourseOffering> optionalCourseOffering = courseOfferingService.getCourseOfferingById(updatedCourseOffering.getId());
//        assertThat(optionalCourseOffering.isPresent())
//                .isTrue();
//        var existingCourseOffering = optionalCourseOffering.orElseThrow();
//        assertThat(existingCourseOffering)
//                .usingRecursiveComparison()
//                // .ignoringFields("field1", "field2")
//                .isEqualTo(newCourseOffering);
//
//    }

//    @Order(5)
//    @Test
//    void givenExistingId_whenDeleteCourseOffering_thenCourseOfferingIsDeleted() {
//        // Arrange
//        CourseOffering newCourseOffering = CourseOffering.of(faker);
//        newCourseOffering = courseOfferingService.createCourseOffering(newCourseOffering);
//        // Act
//        courseOfferingService.deleteCourseOfferingById(newCourseOffering.getId());
//        // Assert
//        Optional<CourseOffering> optionalCourseOffering = courseOfferingService.getCourseOfferingById(newCourseOffering.getId());
//        assertThat(optionalCourseOffering.isPresent())
//                .isFalse();
//
//    }

    @Order(6)
    @ParameterizedTest
    @CsvSource({"5"})
    void givenMultipleEntity_whenFindAll_thenReturnEntityList(int expectedRecordCount) {
        // Arrange: Set up the initial state

        // Delete all existing data
        assertThat(courseOfferingService).isNotNull();
        courseOfferingService.deleteAllCourseOfferings();
        // Generate expectedRecordCount number of fake data
        CourseOffering firstExpectedCourseOffering = null;
        CourseOffering lastExpectedCourseOffering = null;
        for (int counter = 1; counter <= expectedRecordCount; counter++) {
            CourseOffering currentCourseOffering = CourseOffering.of(faker);
            if (counter == 1) {
                firstExpectedCourseOffering = currentCourseOffering;
            } else if (counter == expectedRecordCount) {
                lastExpectedCourseOffering = currentCourseOffering;
            }

            courseOfferingService.createCourseOffering(currentCourseOffering);
        }

        // Act: Perform the action to be tested
        List<CourseOffering> courseOfferingList = courseOfferingService.getAllCourseOfferings();

        // Assert: Verify the expected outcome
        assertThat(courseOfferingList.size())
                .isEqualTo(expectedRecordCount);

        // Get the first entity and compare with expected results
        var firstActualCourseOffering = courseOfferingList.getFirst();
        assertThat(firstActualCourseOffering)
                .usingRecursiveComparison()
                // .ignoringFields("field1", "field2")
                .isEqualTo(firstExpectedCourseOffering);
        // Get the last entity and compare with expected results
        var lastActualCourseOffering = courseOfferingList.getLast();
        assertThat(lastActualCourseOffering)
                .usingRecursiveComparison()
                // .ignoringFields("field1", "field2")
                .isEqualTo(lastExpectedCourseOffering);

    }



}