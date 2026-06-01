package dmit2015.view;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Named
@ApplicationScoped
public class StudentInfo {

    @Inject
    @ConfigProperty(name = "student.fullname", defaultValue = "Your Full Name")
    private String studentFullName;

    public String getStudentFullName() {
        return studentFullName;
    }

}