package team.elrant.bubbles.xmpp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jxmpp.jid.BareJid;
import org.jxmpp.jid.EntityBareJid;

import java.util.function.Consumer;

/**
 * The ChatListener class implements an incoming chat message listener.
 * It is responsible for processing incoming chat messages and updating the chat display accordingly.
 */
public class ChatListener implements IncomingChatMessageListener {
    private static final Logger logger = LogManager.getLogger(ChatListener.class);
    private final BareJid contactJid;
    private final Consumer<String> updateChatDisplay;

    /**
     * Constructs a ChatListener with the specified contact JID and chat display updater.
     *
     * @param contactJid        The JID of the contact with whom the chat is being conducted.
     * @param updateChatDisplay A consumer function to update the chat display with incoming messages.
     */
    public ChatListener(BareJid contactJid, Consumer<String> updateChatDisplay) {
        this.contactJid = contactJid;
        this.updateChatDisplay = updateChatDisplay;
    }

    /**
     * Processes incoming chat messages and updates the chat display if they are from the specified contact.
     *
     * @param from    The JID of the sender.
     * @param message The contents of the incoming message.
     * @param chat    The chat channel.
     */
    @Override
    public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
        try {
            if (from != null && from.equals(contactJid) && message.getBody() != null) {
                updateChatDisplay.accept(message.getBody());
                logger.info("Received message from {}: {}", from, message.getBody());
            }
        } catch (Exception e) {
            logger.error("Error updating chat display: {}", e.getMessage());
        }
    }
}
