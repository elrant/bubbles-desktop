package team.elrant.bubbles.xmpp;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
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

/**
 * The ConnectedUser class extends the User class.
 * It describes how the app's current user should connect, behave, send messages, etc.
 */
public class ConnectedUser extends User {
    private static final Logger logger = LogManager.getLogger(ConnectedUser.class);
    private final String password;
    private Roster roster;
    private XMPPTCPConnection connection;
    private ChatManager chatManager;

    /**
     * Constructs a ConnectedUser object with the specified username, password, and service name.
     *
     * @param username    The username of the user.
     * @param password    The password of the user.
     * @param serviceName The service name of the XMPP server.
     */
    public ConnectedUser(String username, String password, String serviceName) {
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
        XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
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

        if (!(super.getUsername().equals("dummy") && super.getServiceName().equals("elrant.team"))) {
            BareJid dummyJid = JidCreate.bareFrom("dummy@elrant.team");
            if (roster != null && roster.getEntry(dummyJid) == null) {
                addContact(dummyJid.toString(), "Dummy");
            } else {
                sendMessage(dummyJid.toString(), "Hello, world!");
            }
        }
    }

    /**
     * Adds a contact to the user's roster.
     *
     * @param contactJid The JID of the contact to add (user@service.name).
     * @param nickname   The user-defined nickname of the contact, defaults to the contact's username.
     */
    private void addContact(String contactJid, String nickname) {
        try {
            BareJid bareJid = JidCreate.bareFrom(contactJid);
            if (roster != null && !roster.contains(bareJid)) {
                roster.createItemAndRequestSubscription(bareJid, nickname, null);
            }
        } catch (Exception e) {
            logger.error("Error adding contact: " + e.getMessage());
        }
    }

    /**
     * Removes a contact from the user's roster.
     *
     * @param contactJid The JID of the contact to remove (user@service.name).
     */
    private void removeContact(String contactJid) {
        try {
            if (roster != null) {
                RosterEntry entry = roster.getEntry(JidCreate.entityBareFrom(contactJid));
                if (entry != null) {
                    roster.removeEntry(entry);
                }
            }
        } catch (Exception e) {
            logger.error("Error removing contact: " + e.getMessage());
        }
    }

    /**
     * Sends a message to a contact.
     *
     * @param contactJid The JID of the contact to send the message to (user@service.name).
     * @param message    The message to send.
     */
    public void sendMessage(String contactJid, String message) {
        try {
            EntityBareJid recipientJid = JidCreate.entityBareFrom(contactJid);
            if (chatManager != null) {
                Chat chat = chatManager.chatWith(recipientJid);
                chat.send(message);
            }
        } catch (Exception e) {
            logger.error("Error sending XMPP message: " + e.getMessage());
        }
    }

    /**
     * Accepts a subscription request from a contact.
     *
     * @param contactJid The JID of the contact to accept the subscription from (user@service.name).
     * @param nickname   The user-defined nickname of the contact, defaults to the contact's username.
     */
    public void acceptSubscription(String contactJid, String nickname) {
        if (nickname == null || nickname.isEmpty()) {
            nickname = contactJid.split("@")[0];
        }
        try {
            if (roster != null) {
                roster.createItemAndRequestSubscription(JidCreate.bareFrom(contactJid), nickname, null);
            }
        } catch (Exception e) {
            logger.error("Error accepting subscription: " + e.getMessage());
        }
    }

    /**
     * Saves the user information (excluding password) to a file.
     *
     * @param filename The name of the file to save the user information to.
     */
    public void saveUserToFile(String filename) {
        try (FileOutputStream fileOut = new FileOutputStream(filename);
             ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {

            User userWithoutPassword = new User(super.getUsername(), super.getServiceName());
            objectOut.writeObject(userWithoutPassword);

            logger.info("User information (excluding password) saved to " + filename);
        } catch (IOException e) {
            logger.error("Error saving user information to file: " + e.getMessage());
        }
    }

    /**
     * Retrieves the roster of the connected user.
     *
     * @return The roster of the connected user.
     * @throws IllegalStateException if the roster is not initialized.
     */
    public Roster getRoster() {
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
    public void addIncomingMessageListener() {
        if (chatManager != null) {
            chatManager.addIncomingListener((from, message, chat) ->
                    logger.info("Received message from " + from + ": " + message.getBody()));
        }
    }
}
