package team.elrant.bubbles.xmpp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

/**
 * The User class represents a user in the XMPP system.
 * It stores the username and service name of the user.
 */
public class User implements Serializable {
    private static final Logger logger = LogManager.getLogger(User.class);

    private final @NotNull String username;
    private final @NotNull String serviceName;
    private final @Nullable String password;

    /**
     * Constructs a User object with the specified username and service name.
     *
     * @param username    The username of the user.
     * @param serviceName The service name of the XMPP server.
     */

    public User(@NotNull String username, @NotNull String serviceName, @NotNull String password) {
        this.username = username;
        this.serviceName = serviceName;
        this.password = password;
    }

    /**
     * Loads the user information from a file and initializes a User object.
     *
     * @param filename The name of the file containing the serialized user information.
     * @throws IOException            If an I/O error occurs while reading the file.
     * @throws ClassNotFoundException If the class of a serialized object cannot be found.
     */
    public User(@NotNull String filename, @NotNull String password) throws IOException, ClassNotFoundException {
        this.password = password;
        try (FileInputStream fileIn = new FileInputStream(filename);
             ObjectInputStream objectIn = new ObjectInputStream(fileIn)) {
            User serializedUser = (User) objectIn.readObject();
            logger.info("User information loaded from {}", filename);
            this.username = serializedUser.getUsername();
            this.serviceName = serializedUser.getServiceName();
        } catch (IOException | ClassNotFoundException e) {
            logger.error("Error loading user information from file: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Retrieves the username of the user.
     *
     * @return The username of the user.
     */
    public @NotNull String getUsername() {
        return username;
    }

    /**
     * Retrieves the service name of the XMPP server.
     *
     * @return The service name of the XMPP server.
     */
    public @NotNull String getServiceName() {
        return serviceName;
    }
}
