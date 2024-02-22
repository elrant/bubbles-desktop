package team.elrant.bubbles.xmpp;

import org.jivesoftware.smack.roster.Roster;

public class DummyUser {
    public static void main(String[] args) {

        // XMPP connection details
        String username = "dummy";
        String password = "";
        String serviceName = "elrant.team";

        ConnectedUser user = new ConnectedUser(username, password, serviceName);
        user.initializeConnection();
        // Accept every incoming subscription request
        Roster roster = user.getRoster();
        roster.setSubscriptionMode(Roster.SubscriptionMode.accept_all);
    }
}
