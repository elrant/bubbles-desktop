package team.elrant.bubbles.xmpp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

/**
 * The User class represents a user in the XMPP system.
 * It stores the username and service name of the user.
 */
public class User implements Serializable {
    private String username;
    private String serviceName;

    /**
     * Constructs a User object with the specified username and service name.
     * @param username The username of the user.
     * @param serviceName The service name of the XMPP server.
     */
    public User(String username, String serviceName) {
        this.username = username;
        this.serviceName = serviceName;
    }

    /**
     * Loads the user information from a file and initializes a User object.
     * @param filename The name of the file containing the serialized user information.
     */
    public User(String filename) {

        // This try syntax is a try-with-resources statement.
        // It automatically closes the resources
        // (in this case, the FileInputStream and ObjectInputStream)
        // after the try block is executed.
        try (FileInputStream fileIn = new FileInputStream(filename); ObjectInputStream objectIn = new ObjectInputStream(fileIn)) {
            User serializedUser = (User) objectIn.readObject();
            System.out.println("User information loaded from " + filename);
            this.username = serializedUser.getUsername();
            this.serviceName = serializedUser.getServiceName();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading user information from file: " + e);
            this.username = null;
            this.serviceName = null;
        }
    }

    /**
     * Retrieves the username of the user.
     * @return The username of the user.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Retrieves the service name of the XMPP server.
     * @return The service name of the XMPP server.
     */
    public String getServiceName() {
        return serviceName;
    }
}
