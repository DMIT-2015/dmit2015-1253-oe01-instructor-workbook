package dmit2015.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.datafaker.Faker;

import java.util.UUID;

@Data
@NoArgsConstructor
public class Task {

    private String id;
    @NotBlank(message = "Description is required")
    @Size(min = 3, message = "Description must contain 3 or more characters")
    private String description;

    @NotNull(message = "Priority is required")
    private TaskPriority priority;
    private boolean done;

    // Copy constructor
    public Task(Task other) {
        this.id = other.getId();
        this.description = other.getDescription();
        this.priority = other.getPriority();
        this.done = other.isDone();
    }

    // Static copyOf method
    public static Task copyOf(Task other) {
        return new Task(other);
    }

    // Static of method to return a new instance with fake data
    public static Task of(Faker faker) {
        Task newTask = new Task();
        newTask.setId(UUID.randomUUID().toString());
        newTask.setDescription("Watch " + faker.movie().name());
        newTask.setPriority(TaskPriority.Medium);
        newTask.setDone(false);
        return newTask;
    }
}
