package controllers;

import play.*;
import play.modules.facebook.FbGraph;
import play.modules.facebook.FbGraphException;
import play.modules.facebook.Parameter;
import play.mvc.*;
import play.mvc.Scope.Session;

import java.util.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Application extends Controller {

	private static final int numRandomFriends = 4;

    public static void index() {
        try {
        	JsonArray friends = FbGraph.getConnection("me/friends");
        	ArrayList friendz = new ArrayList();
        	ArrayList photoz = new ArrayList();

        	ArrayList randomFriends = new ArrayList();
            Random randomFriendGenerator = new Random();
            Random selectedFriendGenerator = new Random();
            Integer selectedFriend = selectedFriendGenerator.nextInt(numRandomFriends-1);

            while(randomFriends.size() < numRandomFriends){
            	Integer randomFriend = randomFriendGenerator.nextInt(friends.size());

            	if(!randomFriends.contains(randomFriend)){
            		JsonElement id = friends.get(randomFriend).getAsJsonObject().get("id");
            		JsonElement name = friends.get(randomFriend).getAsJsonObject().get("name");
            		String profilePicUrl = FbGraph.getPicture(id.getAsString());

            		HashMap hmFriendInfo = new HashMap<String, String>();
            		hmFriendInfo.put("id", id.getAsString());
            		hmFriendInfo.put("name", name.getAsString());
            		hmFriendInfo.put("profilePicUrl", profilePicUrl);
            		if(randomFriends.size() == selectedFriend){
                		JsonArray photos = FbGraph.getConnection(id.getAsString() + "/photos");
                        Random randomPicGenerator = new Random();
                        if(photos.size() > 0){
                        	Integer randomPhoto = randomPicGenerator.nextInt(photos.size());
                        	JsonElement source = photos.get(randomPhoto).getAsJsonObject().get("source");
                        	photoz.add(source.getAsString());
                        	friendz.add(hmFriendInfo);
                        	randomFriends.add(randomFriend);
                        } else {
                        	// selected random friend must be re-chosen because they do not have any shared pictures
                        }
            		} else {
            			friendz.add(hmFriendInfo);
            			randomFriends.add(randomFriend);
            		}
            	}
            }

            render(friendz, photoz);
		} catch (Exception e) {
			e.printStackTrace();
			render();
		}
    }



    public static void facebookLogin() {
        try {
            JsonObject profile = FbGraph.getObject("me"); // fetch the logged in user
            String email = profile.get("email").getAsString(); // retrieve the email
            // do useful things
            Session.current().put("username", email); // put the email into the session (for the Secure module)
        } catch (FbGraphException fbge) {
            flash.error(fbge.getMessage());
            if (fbge.getType() != null && fbge.getType().equals("OAuthException")) {
                Session.current().remove("username");
            }
        }
        redirect("/");
    }

    public static void logout() {
    	index();
    }

}