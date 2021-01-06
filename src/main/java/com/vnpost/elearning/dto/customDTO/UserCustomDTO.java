package com.vnpost.elearning.dto.customDTO;

import com.vnpost.elearning.dto.UsersDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class UserCustomDTO implements Serializable {
    private List<UsersDTO> usersDTOList;
    private Integer count;
}
