/* 
 * Created by Marek Kost and Lukas Bandura under no specific license.
 * Sharing is allowed only with written approval from one of creators.
 * (c) 2015-2016 - Marek Kost, Lukas Bandura
 */
package ttt3d;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

/**
 * Class containing block box, controlling hover object and block mark.
 * @author mkskelet
 */
public final class GridBlock extends Group {
    private final Box box =  new Box();                                     // block
    
    private final Box hover =  new Box();                                   // object to appear on mouse hover
    private final PhongMaterial hover_invisible = new PhongMaterial();      // material used on visible hover object
    private final PhongMaterial hover_visible = new PhongMaterial();        // material used on invisible hover object
    
    private final Mark mark = new Mark();           // mark object
    private boolean marked = false;                 // indicates if block was marked
    private Color marked_color;                     // color of mark
    
    /**
     * Function returns box object of GridBlock.
     * @return box object
     */
    public Box getBox() {
        return box;         // returns current instance of block
    }
    
    /**
     * Sets object mouse transparent property to whatever passed parameter is.
     * @param pass if set to true, object won't receive mouse events
     */
    public void passMouseEvents(boolean pass) {
        if(pass || marked) setMouseTransparent(true);
        else setMouseTransparent(false);
    }

    private void setHoverActive(boolean active) {
        if(active) {                                // if hover object should be active
            hover.setMaterial(hover_visible);       // assign visible material to object
        }
        else {                                      // otherwise
            hover.setMaterial(hover_invisible);     // assign invisible material to object
        }
    }

    /**
     * Create GridBlock with selected size.
     * @param size size of box
     */
    public GridBlock(double size) {
        box.setScaleX(size);        // set X scale
        box.setScaleY(size);        // set Y scale
        box.setScaleZ(size);        // set Z scale
        
        final PhongMaterial boxMaterial = new PhongMaterial();      // create material for block
        Image img = new Image("file:images/tex3.png");              // load image file
        boxMaterial.setDiffuseMap(img);                             // set texture to material
        boxMaterial.setDiffuseColor(Color.AQUA);                    // set texture color
        box.setMaterial(boxMaterial);                               // assign material to block

        hover.setScaleX(size/10*9);     // set X scale
        hover.setScaleY(size/10*9);     // set Y scale
        hover.setScaleZ(size/10*9);     // set Z scale

        // set up hover object visible material
        img = new Image("file:images/hover.png");           // load image file
        hover_visible.setDiffuseMap(img);                   // set texture
        hover_visible.setDiffuseColor(Color.FUCHSIA);       // set color
        
        // set up hover object invisible material
        hover_invisible.setDiffuseColor(Color.TRANSPARENT);     // set color to transparent
        img = new Image("file:images/transparent.png");         // load image file
        hover_invisible.setDiffuseMap(img);                     // set texture to material
        
        setHoverActive(false);           // make hover object inactive
        
        getChildren().add(box);                                     // add block as child to local group
        getChildren().add(hover);                                   // add hover object as child to local group
        getChildren().add(mark.getMark());                          // add mark object as child to local group
        
        box.addEventHandler(MouseEvent.MOUSE_ENTERED, (final MouseEvent e) -> {
            setHoverActive(true);       // if mouse entered the box, activate hover object
        });
        box.addEventHandler(MouseEvent.MOUSE_EXITED, (final MouseEvent e) -> { 
            setHoverActive(false);      // if mouse exited the box, deactivate hover object
        });
    }
    
    /**
     * Function creates mark in this GridBlock.
     * @param color current player mark color
     */
    public void mark(Color color) {
        mark.initialize(box.getTranslateX(), box.getTranslateY(), box.getTranslateZ(), 25, color);      // create mark
        marked_color = color;           // set mark color
        marked = true;                  // set block marked
        passMouseEvents(marked);        // pass mouse events
    }
    
    /**
     * Function checks if block is marked.
     * @return true if block is marked, false otherwise
     */
    public boolean isMarked() {
        return marked;
    }
    
    /**
     * Function compares color passed as parameter to marked color.
     * @param color color to compare
     * @return true if colors are the same, false otherwise
     */
    public boolean compareColor(Color color) {
        return marked_color == color;
    }
    
    /**
     * Functions returns color of mark.
     * @return mark color
     */
    public Color getColor() {
        return marked_color;
    }
    
    /**
     * Resets grid block and mark.
     */
    public void reset() {
        marked = false;             // set marked to false
        mark.reset();               // reset mark
        passMouseEvents(false);     // enable mouse events
    }
}