package br.com.doubletelecom.help_desk_tickets.app.domain.dtos;

import org.junit.jupiter.api.Test;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

public class CreateGroupDtoTest {

    private final Validator validator;

    public CreateGroupDtoTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidCreateGroupDto() {
        CreateGroupDto dto = new CreateGroupDto("ValidName", "Valid description for the group.");
        Set<ConstraintViolation<CreateGroupDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testInvalidNameTooShort() {
        CreateGroupDto dto = new CreateGroupDto("A", "Valid description for the group.");
        Set<ConstraintViolation<CreateGroupDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testInvalidNameTooLong() {
        CreateGroupDto dto = new CreateGroupDto("A".repeat(31), "Valid description for the group.");
        Set<ConstraintViolation<CreateGroupDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testInvalidDescriptionTooShort() {
        CreateGroupDto dto = new CreateGroupDto("ValidName", "Short");
        Set<ConstraintViolation<CreateGroupDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testInvalidDescriptionTooLong() {
        CreateGroupDto dto = new CreateGroupDto("ValidName", "A".repeat(301));
        Set<ConstraintViolation<CreateGroupDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testBlankName() {
        CreateGroupDto dto = new CreateGroupDto("", "Valid description for the group.");
        Set<ConstraintViolation<CreateGroupDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testBlankDescription() {
        CreateGroupDto dto = new CreateGroupDto("ValidName", "");
        Set<ConstraintViolation<CreateGroupDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }
}