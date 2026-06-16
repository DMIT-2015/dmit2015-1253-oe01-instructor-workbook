package dmit2015.service;

import dmit2015.config.ApplicationConfig;
import dmit2015.model.Student;
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
public class StudentJpaServiceIT { // The class must be declared as public

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
//                .addAsLibraries(pomFile.resolve("com.microsoft.sqlserver:mssql-jdbc").withTransitivity().asFile())
//                .addAsLibraries(pomFile.resolve("com.oracle.database.jdbc:ojdbc17").withTransitivity().asFile())
//                .addAsLibraries(pomFile.resolve("org.postgresql:postgresql").withTransitivity().asFile())
//                .addAsLibraries(pomFile.resolve("com.mysql:mysql-connector-j").withTransitivity().asFile())
//                .addAsLibraries(pomFile.resolve("org.mariadb.jdbc:mariadb-java-client").withTransitivity().asFile())
                // .addAsLibraries(pomFile.resolve("org.hibernate.orm:hibernate-spatial").withTransitivity().asFile())
                // .addAsLibraries(pomFile.resolve("org.eclipse:yasson").withTransitivity().asFile())
                .addPackages(true,
                        "dmit2015.config",
                        "dmit2015.model",
                        "dmit2015.service"
                )
                .addAsResource("META-INF/persistence.xml")
                // .addAsResource(new File("src/test/resources/META-INF/persistence-entity.xml"),"META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Inject
    @Named("jpaStudentService")
    private StudentJpaService studentService;

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
    void shouldInjectStudentJpaService() {
        assertThat(studentService).isNotNull();
    }

    @Order(2)
    @Test
    void givenNewStudent_whenAddStudent_thenStudentIsAdded() {
        // Arrange
        Student newStudent = Student.of(faker);

        // Act
        studentService.createStudent(newStudent);

        // Assert
        assertThat(newStudent.getId())
                .isNotNull();

    }

    @Order(3)
    @Test
    void givenExistingId_whenFindById_thenReturnEntity() {
        // Arrange
        Student newStudent = Student.of(faker);

        // Act
        newStudent = studentService.createStudent(newStudent);

        // Assert
        Optional<Student> optionalStudent = studentService.getStudentById(newStudent.getId());
        assertThat(optionalStudent.isPresent())
                .isTrue();
        // Assert
        var existingStudent = optionalStudent.orElseThrow();
        assertThat(existingStudent)
                .usingRecursiveComparison()
                // .ignoringFields("field1", "field2")
                .isEqualTo(newStudent);

    }

    @Order(4)
    @Test
    void givenExistingEntity_whenUpdatedStudent_thenStudentIsUpdated() {
        // Arrange
        Student newStudent = Student.of(faker);

        newStudent = studentService.createStudent(newStudent);
        // TODO: change the values of all properties
        //newStudent.setProperty1(faker.providerName().methodName());
        //newStudent.setProperty2(faker.providerName().methodName());
        //newStudent.setProperty3(faker.providerName().methodName());

        // Act
        Student updatedStudent = studentService.updateStudent(newStudent);

        // Assert
        Optional<Student> optionalStudent = studentService.getStudentById(updatedStudent.getId());
        assertThat(optionalStudent.isPresent())
                .isTrue();
        var existingStudent = optionalStudent.orElseThrow();
        assertThat(existingStudent)
                .usingRecursiveComparison()
                // .ignoringFields("field1", "field2")
                .isEqualTo(newStudent);

    }

    @Order(5)
    @Test
    void givenExistingId_whenDeleteStudent_thenStudentIsDeleted() {
        // Arrange
        Student newStudent = Student.of(faker);
        newStudent = studentService.createStudent(newStudent);
        // Act
        studentService.deleteStudentById(newStudent.getId());
        // Assert
        Optional<Student> optionalStudent = studentService.getStudentById(newStudent.getId());
        assertThat(optionalStudent.isPresent())
                .isFalse();

    }

    @Order(6)
    @ParameterizedTest
    @CsvSource({"10"})
    void givenMultipleEntity_whenFindAll_thenReturnEntityList(int expectedRecordCount) {
        // Arrange: Set up the initial state

        // Delete all existing data
        assertThat(studentService).isNotNull();
        studentService.deleteAllStudents();
        // Generate expectedRecordCount number of fake data
        Student firstExpectedStudent = null;
        Student lastExpectedStudent = null;
        for (int counter = 1; counter <= expectedRecordCount; counter++) {
            Student currentStudent = Student.of(faker);
            if (counter == 1) {
                firstExpectedStudent = currentStudent;
            } else if (counter == expectedRecordCount) {
                lastExpectedStudent = currentStudent;
            }

            studentService.createStudent(currentStudent);
        }

        // Act: Perform the action to be tested
        List<Student> studentList = studentService.getAllStudents();

        // Assert: Verify the expected outcome
        assertThat(studentList.size())
                .isEqualTo(expectedRecordCount);

        // Get the first entity and compare with expected results
        var firstActualStudent = studentList.getFirst();
        assertThat(firstActualStudent)
                .usingRecursiveComparison()
                // .ignoringFields("field1", "field2")
                .isEqualTo(firstExpectedStudent);
        // Get the last entity and compare with expected results
        var lastActualStudent = studentList.getLast();
        assertThat(lastActualStudent)
                .usingRecursiveComparison()
                // .ignoringFields("field1", "field2")
                .isEqualTo(lastExpectedStudent);

    }

    @Order(7)
    @ParameterizedTest
    @CsvSource(value = {
            ", Lee, DMIT2015-1253-OE01, First name is required.",
            "Bruce, , DMIT2015-1253-OE01, Last name is required.",
    }, nullValues = {"null"})
    void givenEntityWithValidationErrors_whenAddStudent_thenThrowException(
            String firstName,
            String lastName,
            String courseSection,
            String expectedExceptionMessage
    ) {
        // Arrange
        Student newStudent = new Student();

         newStudent.setFirstName(firstName);
         newStudent.setLastName(lastName);
         newStudent.setCourseSection(courseSection);

        // Act + Assert using AssertJ to verify the exception message contains the expected text
        assertThatThrownBy(() -> studentService.createStudent(newStudent))
                .hasStackTraceContaining(expectedExceptionMessage);

    }

}