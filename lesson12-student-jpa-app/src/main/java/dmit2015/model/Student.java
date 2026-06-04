package dmit2015.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.datafaker.Faker;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Long id;

    @NotBlank(message = "First name is required.")
    private String firstName;

    @NotBlank(message = "Last name is required.")
    private String lastName;

    private String courseSection;

    public static Student of(Faker faker) {
        Student student = new Student();
        student.setFirstName(faker.name().firstName());
        student.setLastName(faker.name().lastName());
        student.setCourseSection("DMIT2015-1253-OE01");
        return student;
    }
}
