package team.elrant.bubbles.xmpp;

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
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.jid.BareJid;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.function.Consumer;

/**
 * The ConnectedUser class extends the User class.
 * It describes how the app's current user should connect, behave, send messages, etc.
 */
public class ConnectedUser extends User {
    private static final Logger logger = LogManager.getLogger(ConnectedUser.class);
    private final @NotNull String password;
    private @Nullable Roster roster;
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
        @NotNull XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                .setUsernameAndPassword(super.getUsername(), password)
                .setXmppDomain(super.getServiceName())
                .setSecurityMode(ConnectionConfiguration.SecurityMode.required)
                .build();

        connection = new XMPPTCPConnection(config);
        connection.connect();
        connection.login();

        chatManager = ChatManager.getInstanceFor(connection);

        roster = Roster.getInstanceFor(connection);
        roster.reloadAndWait();
    }

    /**
     * Adds a contact to the user's roster.
     *
     * @param contactJid The JID of the contact to add (user@service.name).
     * @param nickname   The user-defined nickname of the contact, defaults to the contact's username.
     */
    public void addContact(@NotNull BareJid contactJid, @Nullable String nickname) {
        try {
            if (roster != null && !roster.contains(contactJid)) {
                roster.createItemAndRequestSubscription(contactJid, nickname, null);
            }
        } catch (Exception e) {
            logger.error("Error adding contact: {}", e.getMessage());
        }
    }

    /**
     * Removes a contact from the user's roster.
     *
     * @param contactJid The JID of the contact to remove (user@service.name).
     */
    public void removeContact(@NotNull String contactJid) {
        try {
            if (roster != null) {
                RosterEntry entry = roster.getEntry(JidCreate.entityBareFrom(contactJid));
                if (entry != null) {
                    roster.removeEntry(entry);
                }
            }
        } catch (Exception e) {
            logger.error("Error removing contact: {}", e.getMessage());
        }
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
     * Accepts a subscription request from a contact.
     *
     * @param contactJid The JID of the contact to accept the subscription from (user@service.name).
     * @param nickname   The user-defined nickname of the contact, defaults to the contact's username.
     */
    public void acceptSubscription(@NotNull BareJid contactJid, @Nullable String nickname) {
        if (nickname == null || nickname.isEmpty()) {
            nickname = contactJid.toString().split("@")[0];
        }
        try {
            if (roster != null) {
                roster.createItemAndRequestSubscription(JidCreate.bareFrom(contactJid), nickname, null);
            }
        } catch (Exception e) {
            logger.error("Error accepting subscription: {}", e.getMessage());
        }
    }

    /**
     * Saves the user information (excluding password) to a file.
     *
     * @param filename The name of the file to save the user information to.
     */
    public void saveUserToFile(@NotNull String filename, boolean savePassword) {
        try (@NotNull FileOutputStream fileOut = new FileOutputStream(filename);
             @NotNull ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {

            if (savePassword)
                objectOut.writeObject(this);
            else {
                @NotNull User userToFile = new User(super.getUsername(), super.getServiceName());
                objectOut.writeObject(userToFile);
            }

            logger.info("User information (excluding password) saved to {}", filename);
        } catch (IOException e) {
            logger.error("Error saving user information to file: {}", e.getMessage());
        }
    }

    /**
     * Retrieves the roster of the connected user.
     *
     * @return The roster of the connected user.
     * @throws IllegalStateException if the roster is not initialized.
     */
    public @NotNull Roster getRoster() {
        if (roster != null) {
            return roster;
        } else {
            throw new IllegalStateException("Roster is not initialized.");
        }
    }

    /**
     * Checks if the user is currently logged in.
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
     * Disconnects the user from the XMPP server.
     */
    public void disconnect() {
        if (connection != null) {
            connection.disconnect();
        }
    }

    /**
     * Adds an incoming message listener to the chat manager.
     */
    public void addIncomingMessageListener(BareJid contactJid, Consumer<String> updateChatDisplay) {
        if (chatManager != null) {
            ChatListener chatListener = new ChatListener(contactJid, updateChatDisplay);
            chatManager.addIncomingListener(chatListener);
        }
    }
}
