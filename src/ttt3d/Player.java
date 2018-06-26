/* 
 * Created by Marek Kost and Lukas Bandura under no specific license.
 * Sharing is allowed only with written approval from one of creators.
 * (c) 2015-2016 - Marek Kost, Lukas Bandura
 */
package ttt3d;

import javafx.scene.paint.Color;

/**
 * Class used as Player class extending base Actor class.
 * @author mkskelet
 */
public class Player extends Actor {
    /**
     * Creates player with given mark color.
     * @param color mark color
     */
    public Player(Color color) {
        setColor(color);                    // sets actor color
        setActorType(ActorType.PLAYER);     // sets actor type
    }
}