package com.tts.TechTalentTwitter.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tts.TechTalentTwitter.model.Tag;


//ability to get a hashtag based on its phrase
@Repository
public interface TagRepository extends CrudRepository<Tag, Long>  {
    
	Tag findByPhrase(String phrase);

}