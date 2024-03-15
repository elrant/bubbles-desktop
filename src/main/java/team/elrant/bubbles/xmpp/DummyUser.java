package team.elrant.bubbles.xmpp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.roster.Roster;

/**
 * The DummyUser class is a simple utility for simulating a user in an XMPP application.
 * It sets up a connected user with test double credentials and accepts all incoming subscription requests.
 */
public class DummyUser {
    private static final Logger logger = LogManager.getLogger(DummyUser.class);

    /**
     * The main method creates a test double user and sets up the XMPP connection.
     * It accepts every incoming subscription request.
     *
     * @param args The command-line arguments (not used in this context).
     */
    public static void main(String[] args) {
        // XMPP connection details
        String username = "dummy";
        String password = ""; // Test double password
        String serviceName = "elrant.team";

        ConnectedUser user = new ConnectedUser(username, password, serviceName);
        logger.info("Connected user created successfully.");

        // Accept every incoming subscription request
        Roster roster = user.getRoster();
        if (roster != null) {
            roster.setSubscriptionMode(Roster.SubscriptionMode.accept_all);
            logger.info("Subscription mode set to accept all.");
        } else {
            logger.error("Roster is null. Unable to set subscription mode.");
        }
    }
}
