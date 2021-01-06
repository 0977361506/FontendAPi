package com.vnpost.elearning.dto.course;

import org.springframework.stereotype.Component;

@Component
public class CourseCategoryDtos {
    private Long id;

    private String name;
    
    public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


}
