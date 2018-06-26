/* 
 * Created by Marek Kost and Lukas Bandura under no specific license.
 * Sharing is allowed only with written approval from one of creators.
 * (c) 2015-2016 - Marek Kost, Lukas Bandura
 */
package ttt3d;

import javafx.scene.paint.Color;

/**
 * Base class for player and potentially computer controlled AI.
 * @author mkskelet
 */
public class Actor {
    private Color mark_color;       // player color
    ActorType type;                 // type of actor
    
    /**
     * Function returns actor mark color.
     * @return mark color
     */
    public final Color getColor() {
        return mark_color;
    }
    
    /**
     * Function used for setting actor mark color.
     * @param color mark color
     */
    public final void setColor(Color color) {
        mark_color = color;
    }
    
    /**
     * Function returns actor type.
     * @return type of actor
     */
    public final ActorType getActorType() {
        return type;
    }
    
    /**
     * Function sets actor type.
     * @param type type of actor
     */
    public final void setActorType(ActorType type) {
        this.type = type;
    }
}