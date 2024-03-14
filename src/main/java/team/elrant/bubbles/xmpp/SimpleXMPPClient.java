package team.elrant.bubbles.xmpp;

/**
 * The SimpleXMPPClient class is a simple command-line XMPP client.
 * It establishes a connection to the XMPP server, adds an incoming message listener,
 * sleeps for 10 seconds, and then disconnects from the server.
 */
public class SimpleXMPPClient {
    /**
     * The main method is the entry point of the SimpleXMPPClient application.
     * It establishes a connection to the XMPP server, adds an incoming message listener,
     * sleeps for 10 seconds, and then disconnects from the server.
     * @param args The command-line arguments (not used in this context).
     */
    public static void main(String[] args) {

        // XMPP connection details
        String username = ""; // Insert username here
        String password = ""; // Insert password here
        String serviceName = "elrant.team";

        ConnectedUser connectedUser = null;
        try {
            connectedUser = new ConnectedUser(username, password, serviceName);
            System.out.println("Connection established!");
        } catch (Exception e) {
            e.printStackTrace(); // Replace with more robust error handling in the future
        }

        try {
            if (connectedUser != null && connectedUser.isLoggedIn()) {
                connectedUser.addIncomingMessageListener(); /* Print any incoming messages */
                Thread.sleep(10000); /* Sleep for 10 seconds */
                connectedUser.disconnect();
            }
        } catch (InterruptedException e) {
            e.printStackTrace(); // Replace with more robust error handling in the future
        }
    }
}
