package com.tts.TechTalentTwitter.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tts.TechTalentTwitter.model.Tweet;
import com.tts.TechTalentTwitter.model.TweetDisplay;
import com.tts.TechTalentTwitter.model.User;
import com.tts.TechTalentTwitter.service.TweetService;
import com.tts.TechTalentTwitter.service.UserService;

@Controller
public class TweetController {
    @Autowired
    private UserService userService;
	
    @Autowired
    private TweetService tweetService;
    
    //allow us to get all tweets
    //sends request to either /tweets or /(home)
    @GetMapping(value = { "/tweets", "/" })
    public String getFeed(@RequestParam(value = "filter", required = false) String filter, Model model) {
        User loggedInUser = userService.getLoggedInUser();
        List<TweetDisplay> tweets = new ArrayList<>();
        if (filter == null) {//if nothing gets passed in we will set it to all
            filter = "all";
        }
        if (filter.equalsIgnoreCase("following")) {
            List<User> following = loggedInUser.getFollowing();
            tweets = tweetService.findAllByUsers(following);
            model.addAttribute("filter", "following");
        } else {
            tweets = tweetService.findAll();
            model.addAttribute("filter", "all");
        }
        model.addAttribute("tweetList", tweets);//add tweet to model and return feed.html
        return "feed";
    }
    
    //serve up the  new tweet page
    @GetMapping(value = "/tweets/new")
    public String getTweetForm(Model model) {
        model.addAttribute("tweet", new Tweet());
        return "newTweet";
    }
    
    //handles form submission
    //this method gets logged in user and associates them with the tweet
    @PostMapping(value = "/tweets")
    public String submitTweetForm(@Valid Tweet tweet, BindingResult bindingResult, Model model) {
        User user = userService.getLoggedInUser();
        if (!bindingResult.hasErrors()) {
            tweet.setUser(user);
            tweetService.save(tweet);
            model.addAttribute("successMessage", "Tweet successfully created!");
            model.addAttribute("tweet", new Tweet());
        }
        return "newTweet";
    }
    //method called whenever we make a GEt request to tweets/tag
    
    @GetMapping(value = "/tweets/{tag}")
    public String getTweetsByTag(@PathVariable(value="tag") String tag, Model model) {
        List<TweetDisplay> tweets = tweetService.findAllWithTag(tag);//return all tweets assoc. with specific tag
        model.addAttribute("tweetList", tweets);//add tweet to model
        model.addAttribute("tag", tag);//add tag to model
        return "taggedTweets";
    }
}