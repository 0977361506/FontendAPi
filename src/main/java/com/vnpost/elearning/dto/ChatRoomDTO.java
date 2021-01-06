package com.vnpost.elearning.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ChatRoomDTO {
    private Long id;
    private String name;
    private String code;
    private Long courseId;
    private long ids[];
    private List<Long> userIds = new ArrayList<>();
    private Long userId;
    private String createdUser;
    private String searchUser;
}
