package com.tts.TechTalentTwitter.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.tts.TechTalentTwitter.model.User;
import com.tts.TechTalentTwitter.service.UserService;

//will be called whenever we make a post request to follow username
@Controller
public class FollowController {
	
	 @Autowired
	    private UserService userService;
	 
	//call userservice to get currently logged in user & user we want to follow
	 //then: get userToFollow 's current followers & currently logged in user put updated list in userToFollow
	 //then: save userto follow & push changes to database & redirect to last page
	 @PostMapping(value = "/follow/{username}")
	    public String follow(@PathVariable(value="username") String username, 
	                         HttpServletRequest request) {
		 User loggedInUser = userService.getLoggedInUser();
		 User userToFollow = userService.findByUsername(username);
		 List<User> followers = userToFollow.getFollowers();
		 followers.add(loggedInUser);
		 userToFollow.setFollowers(followers);
		    userService.save(userToFollow);
		    return "redirect:" + request.getHeader("Referer");
		}
	 
	 @PostMapping(value = "/unfollow/{username}")
	 public String unfollow(@PathVariable(value="username") String username, HttpServletRequest request) {
	     User loggedInUser = userService.getLoggedInUser();
	     User userToUnfollow = userService.findByUsername(username);
	     List<User> followers = userToUnfollow.getFollowers();
	     followers.remove(loggedInUser);
	     userToUnfollow.setFollowers(followers);
	     userService.save(userToUnfollow);
	     return "redirect:" + request.getHeader("Referer");
	 }    
//these follow/unfollow methods lives in form in user.html 
	 }


