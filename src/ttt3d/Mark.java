/* 
 * Created by Marek Kost and Lukas Bandura under no specific license.
 * Sharing is allowed only with written approval from one of creators.
 * (c) 2015-2016 - Marek Kost, Lukas Bandura
 */
package ttt3d;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

/**
 * Class contains mark object.
 * @author mkskelet
 */
public final class Mark {
    private final Sphere mark_object =  new Sphere();                     // object to appear on mouse hover
    private final PhongMaterial mark_material = new PhongMaterial();      // material used on visible hover object
    
    /**
     * Creates mark with given parameters
     * @param x X position
     * @param y Y position
     * @param z Z position
     * @param size size of mark
     * @param color mark color
     */
    public Mark(double x, double y, double z, double size, Color color) {
        initialize(x,y,z,size,color);
    }
    
    /*
    * Default constructor creates mark with 0 radius making it invisible.
    */
    public Mark() {
       mark_object.setRadius(0);        // sets radius to 0
    }
    
    /**
     * Sets parameters of mark
     * @param x X position
     * @param y Y position
     * @param z Z position
     * @param size size of mark
     * @param color mark color
     */
    public void initialize(double x, double y, double z, double size, Color color) {
        mark_object.setTranslateX(x);       // set X position
        mark_object.setTranslateY(y);       // set Y position
        mark_object.setTranslateZ(z);       // set Z position
        mark_object.setRadius(size);        // set size
        
        mark_material.setDiffuseColor(color);       // set color of material
        mark_object.setMaterial(mark_material);     // assign material
    }
    
    /**
     * Returns mark Node
     * @return mark object
     */
    public Node getMark() {
        return mark_object;
    }
    
    /**
     * Resets mark object by setting radius to 0, making it invisible.
     */
    public void reset() {
        mark_object.setRadius(0);        // sets radius to 0
    }
}