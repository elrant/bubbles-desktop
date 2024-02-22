package team.elrant.bubbles.xmpp;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.jid.BareJid;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;

import java.io.IOException;

public class ConnectedUser extends User {
    private Roster roster;
    private final String password;
    private AbstractXMPPConnection connection = null;
    public ConnectedUser(String username, String password, String serviceName) {
        super(username, serviceName);
        this.password = password;
    }

    public void initializeConnection() {
        try {
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
            roster.reloadAndWait(); // Ensure roster is up-to-date

            if (super.getUsername() != "dummy") {
                // 5. Add or remove contacts as needed
                BareJid dummyJid = JidCreate.bareFrom("dummy@elrant.team");
                if(roster.getEntry(dummyJid) == null) {
                    addContact(dummyJid.toString() , "Dummy");
                } else {
                    sendMessage(dummyJid.toString(), "Hello, world!", chatManager);
                }
            }
        } catch (SmackException | InterruptedException | XMPPException | IOException e) {
            System.err.println("Error sending XMPP message: " + e);
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

    public void acceptSubscription(String contactJid) {
        try {
            roster.createEntry(JidCreate.bareFrom(contactJid), null, null);
        } catch (Exception e) {
            System.err.println("Error accepting subscription: " + e);
        }
    }

    public Roster getRoster() {
        return roster;
    }
}
