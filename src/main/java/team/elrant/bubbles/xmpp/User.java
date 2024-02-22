package team.elrant.bubbles.xmpp;

public class User {
    private final String username;
    private final String serviceName;

    public User(String username, String serviceName) {
        this.username = username;
        this.serviceName = serviceName;
    }

    public String getUsername() {
        return username;
    }

    public String getServiceName() {
        return serviceName;
    }
}
