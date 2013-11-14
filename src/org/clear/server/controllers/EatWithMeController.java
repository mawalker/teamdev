package org.clear.server.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;

import org.clear.server.data.Friends;
import org.clear.server.data.PMF;
import org.clear.server.data.Posts;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class EatWithMeController {

	@RequestMapping(value = "/data/posts/create", method = RequestMethod.GET)
	public String createPost(@RequestParam("user") String user,
			@RequestParam("where") String restaurant,
			@RequestParam("time") String time, Model model) {

		PersistenceManager pm = PMF.get().getPersistenceManager();

		Posts posts = Posts.byUser(user, pm);
		if (posts == null) {
			posts = new Posts();
			posts.createPost(user, restaurant, time);
		}

		posts.save(pm);
		;
		pm.close();

		model.addAttribute("data", true);

		return "jsonView";
	}

	@RequestMapping(value = "/data/posts/view", method = RequestMethod.GET)
	public String viewFriendPosts(@RequestParam("user") final String user,
			final Model model) {

		PersistenceManager pm = PMF.get().getPersistenceManager();

		Friends friends = Friends.byUser(user, pm);
		List<Posts> list = new ArrayList<Posts>();
		if (friends != null) {
			for (final String friend : friends.getFriendIds()) {
				Posts p = Posts.byUser(friend, pm);
				if (p == null)
					continue;
				list.add(p);
			}
		}
		
		model.addAllAttributes(list);
		pm.close();
		return "jsonView";
	}

}
