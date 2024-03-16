module team.elrant.bubbles {

    requires javafx.controls;
    requires javafx.fxml;

    requires javafx.graphics;
    requires jxmpp.jid;
    requires smack.im;
    requires smack.extensions;
    requires smack.tcp;
    requires smack.core;
    requires org.jetbrains.annotations;
    requires org.apache.logging.log4j;

    opens team.elrant.bubbles.gui to javafx.fxml;
    exports team.elrant.bubbles.gui;
    exports team.elrant.bubbles.xmpp;
}