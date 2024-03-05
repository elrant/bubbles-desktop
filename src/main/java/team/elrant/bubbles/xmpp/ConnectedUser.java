package team.elrant.bubbles.xmpp;

import org.jivesoftware.smack.AbstractXMPPConnection;
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

import java.io.*;

/**
 * The ConnectedUser class extends the User class.
 * It describes how the app's current user should connect, behave, send messages, etc.
 */
public class ConnectedUser extends User {
    private final String password;
    private Roster roster;
    private AbstractXMPPConnection connection = null;    // When accessing this property, make sure it's not null
                                                        // (or just run InitializeConnection first)

    public ConnectedUser(String username, String password, String serviceName) {
        super(username, serviceName);
        this.password = password;
    }

    /**
     * The InitializeConnection method takes no parameters and connects to the server.
     * After connecting, it sends a message to the test user.
     */
    public void initializeConnection() throws SmackException, InterruptedException, XMPPException, IOException {
        // 1. Create the connection configuration
        XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                .setUsernameAndPassword(super.getUsername(), password)
                .setXmppDomain(super.getServiceName())
                .setSecurityMode(ConnectionConfiguration.SecurityMode.required)
                .build();

        // 2. Create the connection
        connection = new XMPPTCPConnection(config);
        connection.connect();
        connection.login();

        // 3. Create a chat manager
        ChatManager chatManager = ChatManager.getInstanceFor(connection);

        // 4. Get the roster
        roster = Roster.getInstanceFor(connection);
        roster.reloadAndWait(); // Initialize it

        if (!(super.getUsername().equals("dummy") && super.getServiceName().equals("elrant.team"))) {
            // 5. Add the test user to the roster as needed
            BareJid dummyJid = JidCreate.bareFrom("dummy@elrant.team");
            if (roster.getEntry(dummyJid) == null) {
                addContact(dummyJid.toString(), "Dummy");
            } else {
                sendMessage(dummyJid.toString(), "Hello, world!", chatManager);
            }
        }
    }

    private void addContact(String contactJid, String nickname) {
        try {
            BareJid bareJid = JidCreate.bareFrom(contactJid);
            if (!roster.contains(bareJid)) {
                roster.createItemAndRequestSubscription(bareJid, nickname, null);
            }
        } catch (Exception e) {
            System.err.println("Error adding contact: " + e);

        }
    }

    private void removeContact(String contactJid) {
        try {
            RosterEntry entry = roster.getEntry(JidCreate.entityBareFrom(contactJid));
            if (entry != null) {
                roster.removeEntry(entry);
            }
        } catch (Exception e) {
            System.err.println("Error removing contact: " + e);
        }
    }

    private void sendMessage(String contactJid, String message, ChatManager chatManager) {
        try {
            EntityBareJid recipientJid = JidCreate.entityBareFrom(contactJid);
            Chat chat = chatManager.chatWith(recipientJid);
            chat.send(message);
        } catch (Exception e) {
            System.err.println("Error sending XMPP message: " + e);
        }
    }

    public void disconnect() {
        connection.disconnect();
    }

    public void acceptSubscription(String contactJid, String nickname) {
        try {
            roster.createItemAndRequestSubscription(JidCreate.bareFrom(contactJid), nickname, null);
        } catch (Exception e) {
            System.err.println("Error accepting subscription: " + e);
        }
    }

    public Roster getRoster() {
        return roster;
    }

    public boolean isLoggedIn() {
        return connection.isAuthenticated();
    }

    /**
     * Saves the user information (excluding password) to a file.
     * @param filename The name of the file to save the user information to.
     */
    public void saveUserToFile(String filename) {
        try (FileOutputStream fileOut = new FileOutputStream(filename);
             ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {

            // Create a temporary user object without the password
            User userWithoutPassword = new User(super.getUsername(), super.getServiceName());

            // Write the user object (without password) to the file
            objectOut.writeObject(userWithoutPassword);

            System.out.println("User information (excluding password) saved to " + filename);
        } catch (IOException e) {
            System.err.println("Error saving user information to file: " + e);
        }
    }

    /**
     * Loads the user information from a file and initializes a ConnectedUser object.
     * @param filename The name of the file containing the serialized user information.
     * @return A ConnectedUser object initialized with the loaded user information.
     */
    public static ConnectedUser loadUserFromFile(String filename) {
        ConnectedUser connectedUser = null;
        try (FileInputStream fileIn = new FileInputStream(filename);
             ObjectInputStream objectIn = new ObjectInputStream(fileIn)) {

            // Read the serialized user object from the file
            User user = (User) objectIn.readObject();

            // Initialize a new ConnectedUser object with the loaded user information
            connectedUser = new ConnectedUser(user.getUsername(), "password", user.getServiceName());

            System.out.println("User information loaded from " + filename);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading user information from file: " + e);
        }
        return connectedUser;
    }
}
