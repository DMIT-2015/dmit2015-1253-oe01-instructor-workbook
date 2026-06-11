package dmit2015.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import net.datafaker.Faker;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * Represents a specific offering of a course in a term and section.
 *
 * <p>Examples include a course such as DMIT2015 offered in a specific
 * term, section, and delivery mode.
 * </p>
 */
@Entity
@Table(name = "course_offering")
@Getter
@Setter
public class CourseOffering implements Serializable {

    private static final Logger logger = Logger.getLogger(CourseOffering.class.getName());

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_offering_id", nullable = false)
    private Long id;

    @NotBlank(message = "Course Code is required")
    @Column(name="course_course", nullable = false)
    private String courseCode;      // DMIT2015

    @NotBlank(message = "Section Code is required")
    @Column(name="section_code", nullable = false)
    private String sectionCode;     // A01, OE01

    @NotBlank(message = "Term Code is required")
    @Size(min = 4, max = 10, message = "Term must contain 4 to 10 characters")
    @Column(name="term_code", nullable = false, length = 10)
    private String termCode;        // 1253

    private String title;

    @OneToMany(mappedBy = "courseOffering")
    private List<CourseStudent> students = new ArrayList<>();

    public String getOfferingAbbreviation() {
        return String.format("%s.%s.%s", this.courseCode, this.termCode, this.sectionCode);
    }

    public CourseOffering() {

    }

    @Version
    private Integer version;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

    @Column(nullable = false)
    private LocalDateTime updateTime;

    @PrePersist
    void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createTime = now;
        updateTime = now;
    }

    @PreUpdate
    void onUpdate() {
        updateTime = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object obj) {
        return (
                (obj instanceof CourseOffering other) &&
                        Objects.equals(id, other.id)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // Factory method to create a new CourseOffering instance
    public static CourseOffering of(Faker faker) {
        /*
         * - courseCode          // DMIT2015
         * - sectionCode         // A01, OE01
         * - termCode            // 1253
         * - title
         */
        CourseOffering currentCourseOffering = new CourseOffering();
        currentCourseOffering.setCourseCode("DMTI2015");
        currentCourseOffering.setSectionCode("OE01");
        currentCourseOffering.setTermCode("1253");
        return currentCourseOffering;
    }

}