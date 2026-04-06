package Controller;

import GUI.DemoGUI;
import Model.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Controller for the Main Scene (main window after logging in). All actions relating to Main
 * Scene will be called here.
 */

public class MainSceneController implements Initializable {
    private DemoGUI gui = new DemoGUI();
    private Database database;


    ArrayList<String> temporaryList = new ArrayList<>(
            Arrays.asList("Chicken pasta", "Beef tacos",
                    "Chicken Alfredo", "Pasta tata" )
    );

    @FXML
    private TextField searchBarField;

    @FXML
    private ListView<String> searchListView;

    /**
     * method called when search button is clicked
     * @param event
     */
    @FXML
    public void search(ActionEvent event) {
        searchListView.getItems().clear();
        searchListView.getItems().addAll(
                searchList(searchBarField.getText(), temporaryList));
    }

    public void switchToMainMenu(ActionEvent event ) throws IOException {
        gui.createMainWindow(event);
    }

    /**
     * initializes the search listview, listens to when an item is selected from the
     * listview and calls recipeSelected() method when something is picked
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        searchListView.getItems().addAll(temporaryList);

        searchListView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> recipeSelected()
        );
    }

    public void setDatabase(Database database){
        this.database = database;
    }

    /**
     * method called when recipe from the list is selected
     */
    public void recipeSelected() {
        System.out.println(searchListView.getSelectionModel().getSelectedItem());
    }


    private List<String> searchList(String searchWords, List<String> listOfStrings) {

        List<String> searchWordsArray = Arrays.asList(searchWords.trim().split(" "));

        return listOfStrings.stream().filter(input -> {
            return searchWordsArray.stream().allMatch(word ->
                    input.toLowerCase().contains(word.toLowerCase()));
        }).collect(Collectors.toList());
    }

}
