package team.elrant.bubbles.xmpp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The SimpleXMPPClient class is a simple command-line XMPP client.
 * It establishes a connection to the XMPP server, adds an incoming message listener,
 * sleeps for 10 seconds, and then disconnects from the server.
 */
public class SimpleXMPPClient {
    private static final Logger logger = LogManager.getLogger(SimpleXMPPClient.class);

    /**
     * The main method is the entry point of the SimpleXMPPClient application.
     * It establishes a connection to the XMPP server, adds an incoming message listener,
     * sleeps for 10 seconds, and then disconnects from the server.
     *
     * @param args The command-line arguments (not used in this context).
     */
    public static void main(String[] args) {
        // XMPP connection details
        String username = ""; // Insert username here
        String password = ""; // Insert password here
        String serviceName = "elrant.team";

        try {
            ConnectedUser connectedUser = new ConnectedUser(username, password, serviceName);
            logger.info("Connection established!");

            if (connectedUser.isLoggedIn()) {
                connectedUser.addIncomingMessageListener(); // Print any incoming messages
                Thread.sleep(10000); // Sleep for 10 seconds
                connectedUser.disconnect();
                logger.info("Disconnected from XMPP server.");
            } else {
                logger.error("Failed to log in. Check credentials.");
            }
        } catch (InterruptedException e) {
            logger.error("Interrupted while sleeping: " + e.getMessage());
            Thread.currentThread().interrupt(); // Restore interrupted status
        } catch (Exception e) {
            logger.error("Error occurred: " + e.getMessage());
        }
    }
}
