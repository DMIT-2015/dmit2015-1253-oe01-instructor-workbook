package dmit2015.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.datafaker.Faker;

import java.util.UUID;

@Data
@NoArgsConstructor
public class Student {

    private String id;
    @NotBlank(message = "First Name is required")
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    @Size(min=5)
    private String section;

    // Copy Constructor required for Memory Service Implementation
    public Student(Student other) {
        this.id = other.getId();
        this.firstName = other.getFirstName();
        this.lastName = other.getLastName();
        this.section = other.getSection();
    }

    // static copyOf method required for Memory Service Implementation
    public static Student copyOf(Student other) {
        return new Student(other);
    }

    // static of method required for Memory Service Implementation
    public static Student of(Faker faker) {
        Student newStudent = new Student();
        newStudent.setId(UUID.randomUUID().toString());
        newStudent.setFirstName(faker.name().firstName());
        newStudent.setLastName(faker.name().lastName());
        newStudent.setSection("DMIT2015-OE01");
        return newStudent;
    }
}
