# Customize JavaFX Stage on Windows 11

A small collection of utility methods to customize a JavaFX stage. Targets Windows 11+, won't show any effect on unsupported OSes.

```java
public class Example extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        final var button = new Button("A Button");

        final var root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.getChildren().add(button);
        root.setStyle("-fx-background-color: rgb(2, 48, 71);");

        Scene scene = new Scene(root, 400, 300);
        stage.setScene(scene);

        stage.setTitle("Customized Window Example");

        Platform.runLater(() -> {
            final var handle = StageOps.findWindowHandle(stage);
            StageOps.setCaptionColor(handle, Color.rgb(2, 48, 71));
            StageOps.setTextColor(handle, Color.rgb(142, 202, 230));
            StageOps.setBorderColor(handle, Color.rgb(251, 133, 0));
        });

        stage.show();
    }
}
```
