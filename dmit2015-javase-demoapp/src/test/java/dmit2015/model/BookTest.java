package dmit2015.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    @Test
    void getPrice_whenBookCreated_shouldReturnCorrectPrice() {
        // Arrange
        Book currentBook = new Book("Java Crash Course",64.99);
        // Act
        double actualPrice = currentBook.getPrice();
        // Assert
        assertThat(actualPrice)
                .isCloseTo(64.99, within(0.01));
    }

    @Test
    void constructor_whenPriceIsNegative_shouldThrowException() {
        assertThatThrownBy(() ->
                new Book("Invalid Price",-1))
                .isInstanceOf(IllegalArgumentException.class);
    }
}