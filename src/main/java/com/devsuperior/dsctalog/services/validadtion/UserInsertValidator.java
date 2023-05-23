package com.devsuperior.dsctalog.services.validadtion;

import com.devsuperior.dsctalog.dto.UserInsertDTO;
import com.devsuperior.dsctalog.entities.User;
import com.devsuperior.dsctalog.repositories.UserRepository;
import com.devsuperior.dsctalog.resources.exceptions.FieldMessage;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserInsertDTO> {
    private final UserRepository userRepository;
    
    @Override
    public void initialize(UserInsertValid ann) {
    }

    @Override
    public boolean isValid(UserInsertDTO dto, ConstraintValidatorContext context) {
        List<FieldMessage> list = new ArrayList<>();

        User user = this.userRepository.findByEmail(dto.getEmail());
        if(user != null){
            list.add(new FieldMessage("email", "Email já existe"));
        }

        for (FieldMessage e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
                    .addConstraintViolation();
        }
        return list.isEmpty();
    }
}