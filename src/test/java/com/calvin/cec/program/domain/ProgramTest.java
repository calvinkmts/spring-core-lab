package com.calvin.cec.program.domain;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Program")
public class ProgramTest {

    // --- Fixed Constants for Testing ---
    private static final Long TEST_PROGRAM_ID = 1L;
    private static final Long TEST_CATEGORY_ID = 100L;
    private static final String LANG_EN = "en";
    private static final String LANG_ID = "id";

    // --- Fixtures ---
    private ProgramDescription englishDesc;
    private ProgramDescription indonesianDesc;

    @BeforeEach
    void setUp() {
        englishDesc = new ProgramDescription(LANG_EN, "DDD Fundamentals", "ddd-fundamentals", ProgramContent.empty(),
                ProgramImages.empty());
        indonesianDesc = new ProgramDescription(LANG_ID, "Dasar-dasar DDD", "dasar-dasar-ddd", ProgramContent.empty(),
                ProgramImages.empty());
    }

    private Program programWithSingleDescription() {
        return new Program(new ProgramId(TEST_PROGRAM_ID), TEST_CATEGORY_ID, Set.of(englishDesc));
    }

    private Program programWithMultiDescription() {
        return new Program(new ProgramId(TEST_PROGRAM_ID), TEST_CATEGORY_ID, Set.of(englishDesc, indonesianDesc));
    }

    // --- Test Cases ---

    @Nested
    @DisplayName("Constructor")
    class Constructor {

        @Test
        void validInputs_CreatesProgram() {
            // Arrange + Act
            Program program = programWithSingleDescription();

            // Assert
            assertThat(program.getId().value()).isEqualTo(1L);
            assertThat(program.getCategoryId()).isEqualTo(100L);
            assertThat(program.getDescriptions()).hasSize(1);
        }

        @Test
        void nullCategoryId_ThrowsIllegalArgumentException() {
            assertThatThrownBy(() -> new Program(new ProgramId(TEST_PROGRAM_ID), null, Set.of(englishDesc)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("categoryId is required");
        }

        @Test
        void nullDescriptions_ThrowsIllegalArgumentException() {
            assertThatThrownBy(() -> new Program(new ProgramId(TEST_PROGRAM_ID), TEST_CATEGORY_ID, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("At least one description is required");
        }

        @Test
        void emptyDescriptions_ThrowsIllegalArgumentException() {
            assertThatThrownBy(() -> new Program(new ProgramId(TEST_PROGRAM_ID), TEST_CATEGORY_ID, Set.of()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("At least one description is required");
        }
    }

    @Nested
    @DisplayName("getNameIn")
    class GetNameIn {

        @Test
        void existingLanguage_ReturnsCorrectName() {
            // Arrange
            Program program = programWithSingleDescription();

            // Act
            String result = program.getNameIn(LANG_EN);

            // Assert
            assertThat(result).isEqualTo("DDD Fundamentals");
        }

        @Test
        void upperCaseLanguageCode_ReturnsSameNameCaseInsensitively() {
            // Arrange
            Program program = programWithSingleDescription();

            // Act
            String result = program.getNameIn("EN");

            // Assert
            assertThat(result).isEqualTo("DDD Fundamentals");
        }

        @Test
        void missingLanguage_ReturnsUntitled() {
            // Arrange
            Program program = programWithSingleDescription();

            // Act
            String result = program.getNameIn(LANG_ID);

            // Assert
            assertThat(result).isEqualTo("untitled");
        }
    }

    @Nested
    @DisplayName("getDescriptionIn")
    class GetDescriptionIn {

        @Test
        void existingLanguage_ReturnDescription() {
            // Arrange
            Program program = programWithMultiDescription();

            // Act
            var result = program.getDescriptionIn("id");

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().name()).isEqualTo("Dasar-dasar DDD");
        }

        @Test
        void missingLanguage_ReturnEmpty() {
            // Arrange
            Program program = programWithMultiDescription();

            // Act
            var result = program.getDescriptionIn("jp");

            // Assert
            assertThat(result).isNotPresent();
        }
    }

    @Nested
    @DisplayName("getDescriptions")
    class Encapsulation {

        @Test
        void mutationAttempt_ThrowsUnsupportedOperationException() {
            // Arrange
            Program program = programWithMultiDescription();

            // Act + Assert
            assertThatThrownBy(() -> program.getDescriptions().add(indonesianDesc))
                    .isInstanceOf(UnsupportedOperationException.class);
        }
    }
}
