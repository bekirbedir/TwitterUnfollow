import twitter4j.IDs;
import twitter4j.Relationship;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterUnfollowBot {

	private Twitter twitter;

	private void initConfiguration() {
		
		ConfigurationBuilder builder = new ConfigurationBuilder();
		builder.setOAuthConsumerKey("");
		builder.setOAuthConsumerSecret("");
		builder.setOAuthAccessToken("");
		builder.setOAuthAccessTokenSecret("");
		Configuration configuration = builder.build();
		TwitterFactory factory = new TwitterFactory(configuration);
		twitter = factory.getInstance();
		removeNotFollowed();

	}
	
	private void removeNotFollowed() {
		System.out.println("function started.");
		long[] longIds = null;
		int unfollowedPeople = 0;
		
		try {
			IDs ids = twitter.getFriendsIDs(-1);
			longIds = ids.getIDs();
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		
		System.out.println("Friendships: " + longIds.length);
		
		for (long l : longIds) {
			try {
				Relationship r = twitter.showFriendship(twitter.getId(), l);
				if (!r.isTargetFollowingSource()) {
					twitter.destroyFriendship(l);
					unfollowedPeople++;

					System.out.println("Tweeters unfollowed " + unfollowedPeople);
				}
				if (r.isTargetFollowingSource() && !r.isSourceFollowingTarget()) {
					twitter.createFriendship(l);
				}
			} catch (IllegalStateException | TwitterException e) {
				e.printStackTrace();
			}			
		}
	}

	

	public static void main(String[] args) {
		new TwitterUnfollowBot().initConfiguration();
	}
}