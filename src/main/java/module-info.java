module com.gobblets.gobbletgobblers {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens com.game.gobbletgobblers to javafx.fxml;
    exports com.game.gobbletgobblers;
    exports com.game.gobbletgobblers.board;
    opens com.game.gobbletgobblers.board to javafx.fxml;
}