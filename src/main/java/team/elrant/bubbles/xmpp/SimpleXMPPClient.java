package team.elrant.bubbles.xmpp;

public class SimpleXMPPClient {
    public static void main(String[] args) {

        // XMPP connection details
        String username = "";
        String password = "";
        String serviceName = "elrant.team";

        ConnectedUser user = new ConnectedUser(username, password, serviceName);
        try {
            user.initializeConnection();
            System.out.println("Connection established!");
        } catch (Exception e) {
            e.printStackTrace(); // Replace with more robust error handling in the future
        }

        try {
            user.addIncomingMessageListener(); /* Print any incoming messages */
            Thread.sleep(10000); /* Sleep for 10 seconds */
        } catch (InterruptedException e) {
            e.printStackTrace(); // Replace with more robust error handling in the future
        }
        user.disconnect();
    }
}
