package dmit2015.model;


import jakarta.annotation.Generated;

@Generated("jsonschema2pojo")
public class Student {

    private String courseSection;
    private String firstName;
    private Long id;
    private String lastName;
    private Integer version;

    public String getCourseSection() {
        return courseSection;
    }

    public void setCourseSection(String courseSection) {
        this.courseSection = courseSection;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

}
