package com.vbee.springbootmongodbnewspapersrestapi.collections;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "category")
public class Category {
	
	@Id
	Integer id;
	@NotBlank
	String name;
	public Category() {
	}
	
	public Category(String name, Integer categoryId) {
		this.name = name;
		this.id = categoryId;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer categoryId) {
		this.id = categoryId;
	}

	@Override
	public String toString() {
		return String.format("Category[ name='%s']", name);
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

}
