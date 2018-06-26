/* 
 * Created by Marek Kost and Lukas Bandura under no specific license.
 * Sharing is allowed only with written approval from one of creators.
 * (c) 2015-2016 - Marek Kost, Lukas Bandura
 */
package ttt3d;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.scene.input.KeyCode;
import javafx.scene.input.ScrollEvent;
import static javafx.application.Application.launch;
import javafx.scene.SceneAntialiasing;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

/**
 * Main game class
 * Sets up scene, creates grid and sets up event handlers assocated with keyboard.
 * @author mkskelet
 */

public class TTT3D extends Application {
    private Grid grid;                          // playing grid
    private static Label winNotification;       // label that shows end game text

    private void init(Stage primaryStage) {
        winNotification = new Label("Player X won.\nCongratulations!");     // create label with sample text
        winNotification.setTranslateX(-104);                                // set X position to show label in vertical center of the screen
        winNotification.setTranslateY(-20);                                 // set Y position
        winNotification.setTranslateZ(-320);                                // set Z position to show label in front of grid
        winNotification.textAlignmentProperty().setValue(TextAlignment.CENTER);     // set text alignment to center
        winNotification.setStyle("-fx-font: 128 arial; -fx-font-size: 22pt;"        // set style
                + "-fx-text-fill: #57FF5B; -fx-background-color: #8E42FF; "
                + "-fx-border-width: 2 2 2 2; -fx-border-color: #57FF5B;");
        winNotification.setDisable(true);                                   // disable label

        Label label = new Label("Use A, D to turn grid left or right respectively.\n"       // set instructions label text
                + "Use mouse scroll wheel or W, S to move cursor in depth.\n"
                + "Select block by moving and clicking on it with left mouse button.\n"
                + "\n"
                + "Press ESC to reset the game.");
        label.setTranslateX(-190);      // set label X position
        label.setTranslateY(150);       // set label Y position
        label.setTranslateZ(-200);      // set label Z position
        label.setTextAlignment(TextAlignment.CENTER);                       // set text alignment to center
        label.setStyle("-fx-font: 14 arial; -fx-text-fill: #57FF5B;");      // set style
        
        PerspectiveCamera cam = new PerspectiveCamera();        // create perspective camera
        cam.setFieldOfView(75);                                 // preciously set camera field of view so it looks cool
        cam.setTranslateZ(-200);                                // fine tune camera position
        
        Group root = new Group();               // create root group used to hold all scene objects
        
        primaryStage.setResizable(false);                           // make window not resizable
        primaryStage.setScene(new Scene(root, 400, 400, true, SceneAntialiasing.BALANCED));     // set up a scene
        primaryStage.getScene().setFill(Color.GREY);               // set scene color
        primaryStage.getScene().setCamera(cam);                     // set camera
        
        root.getTransforms().addAll(                                // set root group transform
            new Translate(400 / 2, 160),                            // set position
            new Rotate(0, Rotate.X_AXIS)                            // rotate 0 degrees on X axis (because of reasons)
        );
        
        grid = new Grid();                          // create Grid
        root.getChildren().add(grid.getGrid());     // add grid to root
        root.getChildren().add(label);              // add label to root
        root.getChildren().add(winNotification);    // add win notification to root
        
        // register KEY_PRESSED event handlers
        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, (final KeyEvent e) -> {
            switch (e.getCode().toString()) {
                case "D":
                    // if D was pressed
                    grid.rotateRight();                         // rotate grid right
                    e.consume();                                // consume event
                    break;
                case "A":
                    // if A was pressed
                    grid.rotateLeft();                          // rotate grid left
                    e.consume();                                // consume event
                    break;
                case "W":
                    // if W was pressed
                    grid.scrollIn();                            // scroll in grid
                    e.consume();                                // consume event
                    break;
                case "S":
                    // if S was pressed
                    grid.scrollOut();                           // scroll out grid
                    e.consume();                                // consume event
                    break;
                default:
                    if(e.getCode().toString() == KeyCode.ESCAPE.toString()) {       // if ESC was pressed
                        winNotification.setDisable(true);                           // hide end game label
                        grid.resetGrid();                                           // reset game
                    }
                    break;
            }
        });
        
        // register mouse scroll event handler
        root.setOnScroll((ScrollEvent event) -> {
            if(event.getDeltaY() > 0) {         // if scrolling in
                grid.scrollIn();                // call ScrollIn() function of grid
                event.consume();
            }
            else {                              // if scrolling out
                grid.scrollOut();               // call ScrollOut()
                event.consume();                
            }
        });
    }

    @Override public void start(Stage primaryStage) throws Exception {
        init(primaryStage);
        primaryStage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX 
     * application. main() serves only as fallback in case the 
     * application can not be launched through deployment artifacts,
     * e.g., in IDEs with limited FX support. NetBeans ignores main().
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    /**
     * Function will display end game screen with message.
     * @param message message to show on end game
     */
    public static void ShowEndScreen(String message) {
        winNotification.setText(message);       // set text
        winNotification.setDisable(false);      // enable label
    }
}