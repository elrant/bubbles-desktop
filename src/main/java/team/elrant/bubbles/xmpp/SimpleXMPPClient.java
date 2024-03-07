package team.elrant.bubbles.xmpp;

public class SimpleXMPPClient {
    public static void main(String[] args) {

        // XMPP connection details
        String username = "";
        String password = "";
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
