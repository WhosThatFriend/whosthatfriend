package wtf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import play.modules.facebook.FbGraph;
import play.modules.facebook.FbGraphException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public class WhosThatFriend {
	private static final int numRandomFriends = 4;

	private ArrayList friendz = new ArrayList();
	private ArrayList photoz = new ArrayList();

    public WhosThatFriend() throws FbGraphException{
    	JsonArray friends = FbGraph.getConnection("me/friends");

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
    }

    public ArrayList getFriendz(){
    	return friendz;
    }

    public ArrayList getPhotoz(){
    	return photoz;
    }
}
