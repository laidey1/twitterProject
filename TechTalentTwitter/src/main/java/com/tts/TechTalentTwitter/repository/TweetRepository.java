package com.tts.TechTalentTwitter.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


import com.tts.TechTalentTwitter.model.Tweet;
import com.tts.TechTalentTwitter.model.User;


//interacts with database 
//allows us to convert objects in code to rows in tweet table
//mthod to get all tweets that contain a given hashtag
@Repository
public interface TweetRepository extends CrudRepository<Tweet, Long> {
	 List<Tweet> findAllByOrderByCreatedAtDesc();

	    List<Tweet> findAllByUserOrderByCreatedAtDesc(User user);

	    List<Tweet> findAllByUserInOrderByCreatedAtDesc(List<User> users);

	    List<Tweet> findByTags_PhraseOrderByCreatedAtDesc(String phrase);
}