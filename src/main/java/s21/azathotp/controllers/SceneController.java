package s21.azathotp.controllers;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.util.HashMap;

public class SceneController {
    private final HashMap<String, Pane> scenes = new HashMap<>();
    private final Scene mainScene;

    public SceneController(Scene mainScene) {
        this.mainScene = mainScene;
    }

    public void addPane(String name, Pane pane) {
        scenes.put(name, pane);
    }

    public void showScene(String name) {
        mainScene.setRoot(scenes.get(name));
    }
}
