package blog.javamastery.library.ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class MainController {
    @FXML
    private TextField searchField;

    @FXML
    private TableView<?> bookTable;

    @FXML
    private TableColumn<?, ?> titleColumn;

    @FXML
    private TableColumn<?, ?> authorColumn;

    @FXML
    private TableColumn<?, ?> statusColumn;

    @FXML
    private void initialize() {
        // Initialize the controller
        // We'll add table initialization here later
    }

    @FXML
    private void handleSearch() {
        String searchText = searchField.getText();
        System.out.println("Searching for: " + searchText);
        // We'll implement search logic later
    }

    @FXML
    private void handleAddBook() {
        System.out.println("Add book clicked");
        // We'll implement book addition later
    }

    @FXML
    private void handleBorrow() {
        System.out.println("Borrow clicked");
        // We'll implement borrowing later
    }

    @FXML
    private void handleReturn() {
        System.out.println("Return clicked");
        // We'll implement returning later
    }

    @FXML
    private void handleExit() {
        Platform.exit();
    }
}
