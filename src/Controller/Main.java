package Controller;

import GUI.GUILaunch;
import javafx.application.Application;

public class Main {
    static void main(String[] args) throws Exception {
        Application.launch(GUILaunch.class, args); //Controller skapas per automatik av FX och Controllern skapar databasen.
    }
}
