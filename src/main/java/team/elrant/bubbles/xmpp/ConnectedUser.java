package team.elrant.bubbles.xmpp;

import javafx.scene.control.PasswordField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.jid.BareJid;
import org.jxmpp.jid.EntityBareJid;

import java.io.*;
import java.util.function.Consumer;

/**
 * The ConnectedUser class extends the User class.
 * It describes how the app's current user should connect, behave, send messages, etc.
 */
public class ConnectedUser extends User {
    private static final Logger logger = LogManager.getLogger(ConnectedUser.class);
    private static @Nullable Roster roster;
    private final @NotNull String password;
    private @Nullable XMPPTCPConnection connection;
    private @Nullable ChatManager chatManager;

    /**
     * Constructs a ConnectedUser object with the specified username, password, and service name.
     *
     * @param username    The username of the user.
     * @param password    The password of the user.
     * @param serviceName The service name of the XMPP server.
     */
    public ConnectedUser(@NotNull String username, @NotNull String password, @NotNull String serviceName) {
        super(username, serviceName);
        this.password = password;
    }


    /**
     * Load a new Connected user from a file.
     *
     * @param filename the filename
     * @throws IOException            the io exception
     * @throws ClassNotFoundException the class notfound exception
     */
    public ConnectedUser(@NotNull String filename) throws IOException, ClassNotFoundException {
        super("uninit", "uninit"); //initialize after read file
        try (FileInputStream fileIn = new FileInputStream(filename); ObjectInputStream objectIn = new ObjectInputStream(fileIn)) {
            ConnectedUser serializedUser = (ConnectedUser) objectIn.readObject();
            logger.info("User information loaded from {}", filename);
            super.username = serializedUser.getUsername();
            super.serviceName = serializedUser.getServiceName();
            this.password = serializedUser.getPassword();
        } catch (IOException | ClassNotFoundException e) {
            logger.error("Error loading user information from file: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Retrieves the roster of the connected user.
     *
     * @return The roster of the connected user.
     * @throws IllegalStateException if the roster is not initialized.
     */
    public static @NotNull Roster getRoster() {
        if (roster != null) {
            return roster;
        } else {
            throw new IllegalStateException("Roster is not initialized.");
        }
    }

    /**
     * Initializes the XMPP connection, logs in, sets up chat manager, and populates the roster.
     * After connecting, it sends a message to the test user.
     * This method assigns the connection and chatManager properties.
     *
     * @throws SmackException       If there is an issue with the XMPP protocol.
     * @throws InterruptedException If the operation is interrupted.
     * @throws XMPPException        If there is an XMPP related error.
     * @throws IOException          If an I/O error occurs.
     */
    public void initializeConnection() throws SmackException, InterruptedException, XMPPException, IOException {
        @NotNull XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder().setUsernameAndPassword(super.getUsername(), password).setXmppDomain(super.getServiceName()).setSecurityMode(ConnectionConfiguration.SecurityMode.required).build();

        connection = new XMPPTCPConnection(config);
        connection.connect();
        connection.login();

        chatManager = ChatManager.getInstanceFor(connection);

        roster = Roster.getInstanceFor(connection);
        roster.reloadAndWait();
    }

    /**
     * Sends a message to a contact.
     *
     * @param contactJid The JID of the contact to send the message to (user@service.name).
     * @param message    The message to send.
     */
    public void sendMessage(@NotNull BareJid contactJid, @NotNull String message) {
        try {
            if (chatManager != null) {
                Chat chat = chatManager.chatWith((EntityBareJid) contactJid);
                chat.send(message);
            }
        } catch (Exception e) {
            logger.error("Error sending XMPP message: {}", e.getMessage());
        }
    }

    /**
     * Saves the user information (excluding password) to a file.
     *
     * @param filename     The name of the file to save the user information to.
     * @param savePassword Flag indicating whether to save the password in the file.
     */
    public void saveUserToFile(@NotNull String filename, boolean savePassword) {
        File file = new File("user.dat");
        if (file.delete()) {
            logger.info("Old user information file deleted");
        }

        try (@NotNull FileOutputStream fileOut = new FileOutputStream(filename); @NotNull ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {

            if (savePassword)
                objectOut.writeObject(new ConnectedUser(this.getUsername(), this.getPassword(), this.getServiceName()));
            else {
                ConnectedUser user = new ConnectedUser(this.getUsername(), "uninit", this.getServiceName());
                objectOut.writeObject(user);
            }

            logger.info("User information (excluding password) saved to {}", filename);
        } catch (IOException e) {
            logger.error("Error saving user information to file: {}", e.getMessage());
        }
    }

    private @NotNull String getPassword() {
        return password;
    }


    /**
     * Sets password field.
     *
     * @param pwField the fxml password field
     */
    public void setPasswordField(PasswordField pwField) {
        pwField.setText(this.getPassword());
    }

    /**
     * Checks if the password is uninitialized
     *
     * @return true if the password in uninitialized, false if it isn't
     */
    public boolean passwordUnInit() {
        return this.getPassword().equals("uninit");
    }

    /**
     * Checks if the user is currently logged in.>>>>>>> main
     *
     * @return true if the user is logged in, otherwise false.
     */
    public boolean isLoggedIn() {
        if (connection != null) {
            return connection.isAuthenticated();
        } else {
            return false;
        }
    }

    /**
     * Adds an incoming message listener to the chat manager.
     *
     * @param contactJid        The JID of the contact for which to add the listener.
     * @param updateChatDisplay The consumer to handle incoming messages.
     */
    public void addIncomingMessageListener(BareJid contactJid, Consumer<String> updateChatDisplay) {
        if (chatManager != null) {
            ChatListener chatListener = new ChatListener(contactJid, updateChatDisplay);
            chatManager.addIncomingListener(chatListener);
        }
    }
}
