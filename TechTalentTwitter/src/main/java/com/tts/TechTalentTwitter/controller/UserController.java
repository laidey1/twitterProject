package com.tts.TechTalentTwitter.controller;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


import com.tts.TechTalentTwitter.model.TweetDisplay;
import com.tts.TechTalentTwitter.model.User;
import com.tts.TechTalentTwitter.service.TweetService;
import com.tts.TechTalentTwitter.service.UserService;


@Controller
public class UserController {

	    @Autowired
	    private UserService userService;
	    
	    @Autowired
	    private TweetService tweetService;
	    
	    @GetMapping(value = "/profile")
	    public String getUserLogged(Model model) {
	        User loggedInUser = userService.getLoggedInUser();
	        return "redirect:users/"+loggedInUser.getUsername();
	    }
	    
	    //pathVAriable -access whatever is in the URL after /users/in the variable username
	    //Then: getuser calls userservice to find user by username
	    //& tweetservice finds all tweets linked to that user
	    //boolean indicates if profile page returned belongs to current user
	    
	    @GetMapping(value = "/users/{username}")
		public String getUser(@PathVariable(value = "username") String username, Model model) {
			User loggedInUser = userService.getLoggedInUser();
			User user = userService.findByUsername(username);
			List<TweetDisplay> tweets = tweetService.findAllByUser(user);
			List<User> following = loggedInUser.getFollowing();
			boolean isFollowing = false;
			for (User followedUser : following) {
				if (followedUser.getUsername().equals(username)) {
					isFollowing = true;
				}
			}
			boolean isSelfPage = loggedInUser.getUsername().equals(username);
			model.addAttribute("tweetList", tweets);
			model.addAttribute("user", user);
			model.addAttribute("following", isFollowing);
			model.addAttribute("isSelfPage", isSelfPage);
			return "user";
		}
	    //return diffrent list of users based on request parameter
	    @GetMapping(value = "/users")
		public String getUsers(@RequestParam(value = "filter", required = false) String filter, Model model) {
			List<User> users = new ArrayList<User>();//blank arrayList to hold users
			//get list of users followers & users they are following
			User loggedInUser = userService.getLoggedInUser();
			List<User> usersFollowing = loggedInUser.getFollowing();
			List<User> usersFollowers = loggedInUser.getFollowers();
			if (filter == null) {
				filter = "all";
			}
			if (filter.equalsIgnoreCase("followers")) {
				users = usersFollowers;
				model.addAttribute("filter", "followers");
			} else if (filter.equalsIgnoreCase("following")) {
				users = usersFollowing;
				model.addAttribute("filter", "following");
			} else {
				users = userService.findAll();
				model.addAttribute("filter", "all");
			}
			model.addAttribute("users", users);

			SetTweetCounts(users, model);
			SetFollowingStatus(users, usersFollowing, model);

			return "users";
		}
//store tweet counts(how many times a user has tweeted)
		//this method is taking list of users & model , update model to include tweet counts
		private void SetTweetCounts(List<User> users, Model model) {
			HashMap<String, Integer> tweetCounts = new HashMap<>();
			for (User user : users) {
				List<TweetDisplay> tweets = tweetService.findAllByUser(user);
				tweetCounts.put(user.getUsername(), tweets.size());
			}
			model.addAttribute("tweetCounts", tweetCounts);
		}

		//dot contains method to see if the user is(not) the same as the logged in user the status if false (cant follow yourself)
		private void SetFollowingStatus(List<User> users, List<User> usersFollowing, Model model) {
			HashMap<String, Boolean> followingStatus = new HashMap<>();
			String username = userService.getLoggedInUser().getUsername();
			//iterate thru each user to see if they are being followed & add result to hashmap model
			for (User user : users) {
				if (usersFollowing.contains(user)) {
					followingStatus.put(user.getUsername(), true);
				} else if (!user.getUsername().equals(username)) {
					followingStatus.put(user.getUsername(), false);
				}
			}
			model.addAttribute("followingStatus", followingStatus);
		}
	}
	
	

