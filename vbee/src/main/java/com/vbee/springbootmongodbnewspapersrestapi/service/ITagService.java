package com.vbee.springbootmongodbnewspapersrestapi.service;

import java.util.List;

import com.vbee.springbootmongodbnewspapersrestapi.collections.Tag;

public interface ITagService {

	List<Tag> findAll();
	
	Tag findTagByName(String name);

	Tag inserTag(Tag tag);
	
	Tag updateTag(Tag tag);
	
	void deleteTag(String name);
	
	boolean checkTagExist(String name);

}
