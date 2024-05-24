package com.devsuperior.dsctalog.services.validadtion;

import com.devsuperior.dsctalog.dto.UserInsertDTO;
import com.devsuperior.dsctalog.repositories.UserRepository;
import com.devsuperior.dsctalog.resources.exceptions.FieldMessage;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;

import static java.util.Objects.nonNull;

@RequiredArgsConstructor
public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserInsertDTO> {
    private final UserRepository userRepository;

    @Override
    public void initialize(UserInsertValid ann) {
    }

    @Override
    public boolean isValid(UserInsertDTO dto, ConstraintValidatorContext context) {
        var list = new ArrayList<FieldMessage>();
        var user = userRepository.findByEmail(dto.getEmail());

        if (nonNull(user)) {
            list.add(new FieldMessage("email", "Email já existe"));
        }

        list.forEach(e -> {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage())
                    .addPropertyNode(e.getFieldName())
                    .addConstraintViolation();
        });

        return list.isEmpty();
    }
}