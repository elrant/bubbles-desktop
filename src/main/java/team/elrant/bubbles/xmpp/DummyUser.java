package team.elrant.bubbles.xmpp;

import org.jivesoftware.smack.roster.Roster;

public class DummyUser {
    public static void main(String[] args) {

        // XMPP connection details
        String username = "dummy";
        String password = "";
        String serviceName = "elrant.team";

        ConnectedUser user = null;
        try {
            user = new ConnectedUser(username, password, serviceName);
        } catch (Exception e){
            e.printStackTrace(); // Replace with more robust error handling in the future
        }
        // Accept every incoming subscription request
        Roster roster = user.getRoster();
        roster.setSubscriptionMode(Roster.SubscriptionMode.accept_all);
    }
}
