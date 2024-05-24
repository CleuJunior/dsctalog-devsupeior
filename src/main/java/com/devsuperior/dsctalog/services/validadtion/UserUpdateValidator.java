package com.devsuperior.dsctalog.services.validadtion;

import com.devsuperior.dsctalog.dto.UserUpdateDTO;
import com.devsuperior.dsctalog.repositories.UserRepository;
import com.devsuperior.dsctalog.resources.exceptions.FieldMessage;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.Map;

import static java.lang.Long.parseLong;
import static java.util.Objects.nonNull;
import static org.springframework.web.servlet.HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE;

@RequiredArgsConstructor
public class UserUpdateValidator implements ConstraintValidator<UserUpdateValid, UserUpdateDTO> {
    private final HttpServletRequest httpServletRequest;
    private final UserRepository userRepository;

    @Override
    public void initialize(UserUpdateValid ann) {
    }

    @Override
    public boolean isValid(UserUpdateDTO dto, ConstraintValidatorContext context) {
        @SuppressWarnings("unchecked")
        var uriVars = (Map<String, String>) httpServletRequest.getAttribute(URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        var userId = parseLong(uriVars.get("id"));
        var list = new ArrayList<FieldMessage>();
        var user = userRepository.findByEmail(dto.getEmail());

        if (nonNull(user) && userId != user.getId()) {
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