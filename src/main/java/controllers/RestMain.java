package controllers;

import io.javalin.Javalin;

public class RestMain {

	public static void main(String[] args) throws Exception {
		Javalin app = Javalin.create();
		app.port(getAssignedPort());
		app.start();
		PacemakerRestService service = new PacemakerRestService();
		configRoutes(app, service);
	}

	static void configRoutes(Javalin app, PacemakerRestService service) {

		app.get("/users", ctx -> {
			service.listUsers(ctx);
		});

		app.post("/users", ctx -> {
			service.createUser(ctx);
		});

		app.get("/users/:id", ctx -> {
			service.listUser(ctx);
		});

		app.get("/users/:id/activities", ctx -> {
			service.getActivities(ctx);
		});

		app.get("/users/:id/activities/:type", ctx -> {
			service.listActivities(ctx);
		});
		
	    app.post("/users/:id/activities", ctx -> {
	      service.createActivity(ctx);
	    });

		 app.get("/users/:id/activities/:activityId", ctx -> {
		      service.getActivity(ctx);
		    });
		 
		app.get("/users/:id/activities/:activityId/locations", ctx -> {
			service.getActivityLocations(ctx);
		});

		app.get("/users/:id/friends", ctx -> {
			service.listFriends(ctx);
		});
		
		app.get("/users/:id/messages", ctx -> {
			service.listMessages(ctx);
		});
		
		app.post("/users/:id/friends/:email", ctx -> {
			service.followFriend(ctx);
		});
		
		app.post("/users/:email/messages/:message", ctx -> {
			service.messageFriend(ctx);
		});
		
		app.delete("/users/:id/friends/:email", ctx -> {
			service.deleteFriend(ctx);
		});
		
		app.post("/users/:id/activities/:activityId/locations", ctx -> {
			service.addLocation(ctx);
		});
		
		app.delete("/users", ctx -> {
			service.deleteUsers(ctx);
		});

		app.delete("/users/:id", ctx -> {
			service.deletetUser(ctx);
		});

		app.delete("/users/:id/activities", ctx -> {
			service.deleteActivities(ctx);
		});
	}

	private static int getAssignedPort() {
		ProcessBuilder processBuilder = new ProcessBuilder();
		if (processBuilder.environment().get("PORT") != null) {
			return Integer.parseInt(processBuilder.environment().get("PORT"));
		}
		return 7000;
	}
}