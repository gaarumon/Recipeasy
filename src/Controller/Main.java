package Controller;

import GUI.DemoGUI;
import Model.Database;
import javafx.application.Application;

public class Main {
    static void main(String[] args) throws Exception {
        Application.launch(DemoGUI.class, args); //Controller skapas per automatik av FX och Controllern skapar databasen.
    }
}
