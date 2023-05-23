package com.devsuperior.dsctalog.dto;

import com.devsuperior.dsctalog.services.validadtion.UserInsertValid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@UserInsertValid
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserInsertDTO extends UserDTO{
    private static final long serialVersionUID = 1L;
    private String password;
}
