package dmit2015.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.within;
import static org.junit.jupiter.api.Assertions.*;

class CircleTest {

    @Test
    void area_whenCircleIsCreatedWithRadius5_shouldReturnCorrectArea() {
        Circle circle = new Circle(5);
// JUnit
        assertEquals(78.54, circle.getArea(), 0.01);
// AssertJ
        assertThat(circle.getArea())
                .isCloseTo(78.54, within(0.01));
    }
}