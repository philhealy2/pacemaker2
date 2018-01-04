package controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Optional;

import models.Activity;
import models.Location;
import models.User;

public class PacemakerAPI {

	private Map<String, User> emailIndex = new HashMap<>();
	private Map<String, User> userIndex = new HashMap<>();
	private Map<String, Collection<User>> friendIndex = new HashMap<>();
	private Map<String, Activity> activitiesIndex = new HashMap<>();

	public PacemakerAPI() {
	}

	public Collection<User> getUsers() {
		return userIndex.values();
	}

	public void deleteUsers() {
		userIndex.clear();
		emailIndex.clear();
	}

	public User createUser(String firstName, String lastName, String email, String password) {
		User user = new User(firstName, lastName, email, password);
		emailIndex.put(email, user);
		userIndex.put(user.id, user);
		return user;
	}

	public Activity createActivity(String id, String type, String location, double distance) {
		Activity activity = null;
		Optional<User> user = Optional.fromNullable(userIndex.get(id));
		System.out.println("user" + user.get().id);
		if (user.isPresent()) {
			activity = new Activity(type, location, distance);
			user.get().activities.put(activity.id, activity);
			activitiesIndex.put(activity.id, activity);
		}
		System.out.println("Returning activity" + activity.id);
		return activity;
	}

	public void deleteActivities(String id) {
		Optional<User> user = Optional.fromNullable(userIndex.get(id));
		if (user.isPresent()) {
			user.get().activities.values().forEach(activity -> activitiesIndex.remove(activity.getId()));
			user.get().activities.clear();
		}
	}

	public Activity getActivity(String id) {
		return activitiesIndex.get(id);
	}

	public Collection<Activity> getActivities(String id) {
		Collection<Activity> activities = null;
		Optional<User> user = Optional.fromNullable(userIndex.get(id));
		if (user.isPresent()) {
			activities = user.get().activities.values();
		}
		return activities;
	}

	public Collection<Location> getLocations(String id) {
		Collection<Location> locations = null;

		Optional<Activity> activity = Optional.fromNullable(activitiesIndex.get(id));
		Location location = null;
		if (activity.isPresent()) {
			locations = activity.get().route;
		}
		return locations;
	}

	public List<Activity> listActivities(String userId, String sortBy) {
		System.out.println("List activities called: " + userId + " " + sortBy);
		List<Activity> activities = new ArrayList<>();
		activities.addAll(userIndex.get(userId).activities.values());
		switch (sortBy) {
		case "type":
			activities.sort((a1, a2) -> a1.type.compareTo(a2.type));
			break;
		case "location":
			activities.sort((a1, a2) -> a1.location.compareTo(a2.location));
			break;
		case "distance":
			activities.sort((a1, a2) -> Double.compare(a1.distance, a2.distance));
			break;
		}
		return activities;
	}

	public Location addLocation(String id, double latitude, double longitude) {
		Optional<Activity> activity = Optional.fromNullable(activitiesIndex.get(id));
		Location location = null;
		if (activity.isPresent()) {
			location = new Location(latitude, longitude);
			activity.get().route.add(location);
		}
		return location;
	}

	public User followFriend(String id, String email) {

		System.out.println("Follow friend requested:" + id + " " + email);
		Optional<User> user = Optional.fromNullable(userIndex.get(id));
		User friend = null;
		
		if (user.isPresent()) {
			friend = getUserByEmail(email);
			System.out.println("Following friend:" + friend.getEmail());
			Collection<User> friends = null;
			
			if (friendIndex.get(id) != null){
				friends = friendIndex.get(id);
				friends.add(friend);
			}
			else
			{
				friends = new ArrayList<User>();
				friends.add(friend);
				friendIndex.put(id, friends);
			}
			
			System.out.println("Following friend complete:" + friend.getEmail());
		}

		return friend;
	}

	public User deleteFriend(String id, String email) {

		Optional<User> user = Optional.fromNullable(userIndex.get(id));
		if (user.isPresent()) {
			User friend = getUserByEmail(email);
			Collection<User> friends = friendIndex.get(id);
			friends.remove(friend);
		}

		return user.get();
	}

	public Collection<User> listFriends(String id) {
		System.out.println("List friend requested:" + id);
		Collection<User> friends = null;
		Optional<User> user = Optional.fromNullable(userIndex.get(id));
		
		if (user.isPresent()) {
			friends = friendIndex.get(id);
		}
		return friends;
	}
	
	public Collection<String> listMessages(String id) {
		System.out.println("List msgs requested: " + id);
		Collection<String> msgs = null;
		Optional<User> user = Optional.fromNullable(userIndex.get(id));
		if (user.isPresent()) {
			msgs = user.get().messages;
		}
		return msgs;
	}

	public List<Activity> getFriends(String email, String sortBy) {
		List<Activity> activities = new ArrayList<>();
		activities.addAll(emailIndex.get(email).activities.values());
		switch (sortBy) {
		case "type":
			activities.sort((a1, a2) -> a1.type.compareTo(a2.type));
			break;
		case "location":
			activities.sort((a1, a2) -> a1.location.compareTo(a2.location));
			break;
		case "distance":
			activities.sort((a1, a2) -> Double.compare(a1.distance, a2.distance));
			break;
		}
		return activities;
	}

	public List<String> messageFriend(String email, String message) {
		System.out.println("Message friend requested:" + email + " " + message);
		User user = emailIndex.get(email);
		System.out.println("user" + user.id);

		if (user != null) {
			user.messages.add(message);
		}
		return user.messages;
	}

	public User getUserByEmail(String email) {
		return emailIndex.get(email);
	}

	public User getUser(String id) {
		return userIndex.get(id);
	}

	public User deleteUser(String id) {
		User user = userIndex.remove(id);
		return emailIndex.remove(user.email);
	}
}