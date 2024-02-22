module team.elrant.bubbles {

    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires jxmpp.jid;
    requires smack.im;
    requires smack.extensions;
    requires smack.tcp;
    requires smack.core;
    requires org.jetbrains.annotations;

    opens team.elrant.bubbles.gui to javafx.fxml;
    exports team.elrant.bubbles.gui;
    exports team.elrant.bubbles.xmpp;
}