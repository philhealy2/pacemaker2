package controllers;

import io.javalin.Context;
import models.Activity;
import models.Location;
import models.User;

import static models.Fixtures.users;

import java.util.Collection;

public class PacemakerRestService {

	PacemakerAPI pacemaker = new PacemakerAPI();

	PacemakerRestService() {
		users.forEach(user -> pacemaker.createUser(user.firstname, user.lastname, user.email, user.password));
	}

	public void listUsers(Context ctx) {
		ctx.json(pacemaker.getUsers());
		System.out.println("list users requested");
	}

	public void listUser(Context ctx) {
		String id = ctx.param("id");
		ctx.json(pacemaker.getUser(id));
	}

	public void createUser(Context ctx) {
		User user = ctx.bodyAsClass(User.class);
		User newUser = pacemaker.createUser(user.firstname, user.lastname, user.email, user.password);
		ctx.json(newUser);
	}

	public void deletetUser(Context ctx) {
		String id = ctx.param("id");
		ctx.json(pacemaker.deleteUser(id));
	}

	public void deleteUsers(Context ctx) {
		pacemaker.deleteUsers();
		ctx.json(204);
	}
	
	public void followFriend(Context ctx) {
		String id = ctx.param("id");
		String email = ctx.param("email");
		User user = pacemaker.followFriend(id, email);
		if (user != null) {
			ctx.json(user);
		} else {
			ctx.status(404);
		}
	}
	
	public void deleteFriend(Context ctx) {
		String id = ctx.param("id");
		String email = ctx.param("email");
		User user = pacemaker.deleteFriend(id, email);
		if (user != null) {
			ctx.json(user);
		} else {
			ctx.status(404);
		}
	}

	public void getActivities(Context ctx) {
		String id = ctx.param("id");
		Collection<Activity> activities = pacemaker.getActivities(id);
		if (activities != null) {
			ctx.json(activities);
		} else {
			ctx.status(404);
		}
	}

	public void listActivities(Context ctx) {
		String id = ctx.param("userId");
		String sortBy = ctx.param("sortBy");
		Collection<Activity> activities = pacemaker.listActivities(id, sortBy);
		if (activities != null) {
			ctx.json(activities);
		} else {
			ctx.status(404);
		}
	}

	public void createActivity(Context ctx) {
		String id = ctx.param("id");
		User user = pacemaker.getUser(id);
		if (user != null) {
			Activity activity = ctx.bodyAsClass(Activity.class);
			Activity newActivity = pacemaker.createActivity(id, activity.type, activity.location, activity.distance);
			ctx.json(newActivity);
		} else {
			ctx.status(404);
		}
	}

	public void deleteActivities(Context ctx) {
		String id = ctx.param("id");
		pacemaker.deleteActivities(id);
		ctx.json(204);
	}

	public void getActivity(Context ctx) {
		String id = ctx.param("activityId");
		Activity activity = pacemaker.getActivity(id);
		if (activity != null) {
			ctx.json(activity);
		} else {
			ctx.status(404);
		}
	}

	public void getActivityLocations(Context ctx) {
		String id = ctx.param("activityId");
		Activity activity = pacemaker.getActivity(id);
		if (activity != null) {
			ctx.json(activity.route);
		} else {
			ctx.status(404);
		}
	}

	public void addLocation(Context ctx) {
		String id = ctx.param("activityId");
		Activity activity = pacemaker.getActivity(id);
		if (activity != null) {
			Location location = ctx.bodyAsClass(Location.class);
			activity.route.add(location);
			ctx.json(location);
		} else {
			ctx.status(404);
		}
	}
}