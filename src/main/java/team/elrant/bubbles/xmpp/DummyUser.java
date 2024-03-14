package team.elrant.bubbles.xmpp;

import org.jivesoftware.smack.roster.Roster;

/**
 * The DummyUser class is a simple utility for simulating a user in an XMPP application.
 * It sets up a connected user with test double credentials and accepts all incoming subscription requests.
 */
public class DummyUser {
    /**
     * The main method creates a test double user and sets up the XMPP connection.
     * It accepts every incoming subscription request.
     * @param args The command-line arguments (not used in this context).
     */
    public static void main(String[] args) {
        // XMPP connection details
        String username = "dummy";
        String password = ""; // Test double password
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
