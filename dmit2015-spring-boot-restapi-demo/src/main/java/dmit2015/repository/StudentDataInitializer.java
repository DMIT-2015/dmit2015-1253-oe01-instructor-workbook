package dmit2015.repository;

import dmit2015.entity.Student;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StudentDataInitializer {

    @Bean
    CommandLineRunner seedDatabase(StudentRepository studentRepository) {
        return args -> {
            if (studentRepository.count() == 0) {

                try {
                    var faker = new Faker();
                    for (int count = 1; count <= 10; count++) {
                        var currentStudent = Student.of(faker);
                        studentRepository.save(currentStudent);
                    }

                } catch (Exception ex) {
//                    logger.fine(ex.getMessage());
                }
            }
        };
    }
}
