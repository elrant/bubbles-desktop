package team.elrant.bubbles.xmpp;

import org.jivesoftware.smack.roster.Roster;

public class DummyUser {
    public static void main(String[] args) {

        // XMPP connection details
        String username = "dummy";
        String password = "";
        String serviceName = "elrant.team";
        Roster roster;
        ConnectedUser user;
        try {
            user = new ConnectedUser(username, password, serviceName);
            // Accept every incoming subscription request
            roster = user.getRoster();
            roster.setSubscriptionMode(Roster.SubscriptionMode.accept_all);
        } catch (Exception ignored){
            // Replace with more robust error handling in the future
        }
    }
}
