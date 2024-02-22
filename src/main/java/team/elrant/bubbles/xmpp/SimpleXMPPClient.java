package team.elrant.bubbles.xmpp;

public class SimpleXMPPClient {
    public static void main(String[] args) {

        // XMPP connection details
        String username = "";
        String password = "";
        String serviceName = "elrant.team";

        ConnectedUser user = new ConnectedUser(username, password, serviceName);
        user.initializeConnection();
        user.disconnect();
    }
}
