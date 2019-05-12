package com.vbee.springbootmongodbnewspapersrestapi.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vbee.springbootmongodbnewspapersrestapi.collections.Tag;
import com.vbee.springbootmongodbnewspapersrestapi.repository.TagMongoRepository;

@Service
public class TagService implements ITagService{

	@Autowired
	TagMongoRepository tagRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(TagService.class);
	
	@Override
	public Tag findTagByName(String tagName) {
		return tagRepository.findTagByName(tagName);
	}

	@Override
	public Tag inserTag(Tag tag) {
		return tagRepository.save(tag);
	}

	@Override
	public Tag updateTag(Tag tag) {
		return tagRepository.save(tag);
	}

	@Override
	public void deleteTag(String name) {
		tagRepository.delete(name);
	}

	@Override
	public boolean checkTagExist(String name) {
		Tag tag = tagRepository.findTagByName(name);
		if(tag == null)
			return false;
		return true;			
	}


	@Override
	public List<Tag> findAll() {
		return tagRepository.findAll();
	}

}
