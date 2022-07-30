module willhero.willhero {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.graphics;
    requires javafx.media;

//    requires org.controlsfx.controlsfx;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens WillHero to javafx.fxml;
    exports WillHero;
    exports Exceptions;
}


