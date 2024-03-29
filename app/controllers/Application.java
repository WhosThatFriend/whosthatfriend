package controllers;

import play.*;
import play.modules.facebook.FbGraph;
import play.modules.facebook.FbGraphException;
import play.modules.facebook.Parameter;
import play.mvc.*;
import play.mvc.Scope.Session;
import wtf.WhosThatFriend;

import java.util.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Application extends Controller {

    public static void index() {
        try {
        	WhosThatFriend wtf = new WhosThatFriend();
        	ArrayList photoz = wtf.getPhotoz();
        	ArrayList friendz = wtf.getFriendz();
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