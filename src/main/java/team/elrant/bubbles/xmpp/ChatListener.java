package team.elrant.bubbles.xmpp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jxmpp.jid.BareJid;
import org.jxmpp.jid.EntityBareJid;

import java.util.function.Consumer;

public class ChatListener implements IncomingChatMessageListener {
    private static final Logger logger = LogManager.getLogger(ChatListener.class);
    public BareJid contactJid;
    Consumer<String> updateChatDisplay;

    public ChatListener(BareJid contactJid, Consumer<String> updateChatDisplay){
        this.contactJid = contactJid;
        this.updateChatDisplay = updateChatDisplay;
    }

    /**
     * @param from The JID of the sender
     * @param message The message contents
     * @param chat The chat channel
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
