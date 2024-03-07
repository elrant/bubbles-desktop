package team.elrant.bubbles.xmpp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String serviceName;

    public User(String username, String serviceName) {
        this.username = username;
        this.serviceName = serviceName;
    }

    /**
     * Loads the user information from a file and initializes a User object.
     * @param filename The name of the file containing the serialized user information.
     */
    public User (String filename) {
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
    public String getUsername() {
        return username;
    }

    public String getServiceName() {
        return serviceName;
    }
}
