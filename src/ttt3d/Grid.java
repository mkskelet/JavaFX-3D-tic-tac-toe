/* 
 * Created by Marek Kost and Lukas Bandura under no specific license.
 * Sharing is allowed only with written approval from one of creators.
 * (c) 2015-2016 - Marek Kost, Lukas Bandura
 */
package ttt3d;

import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.event.ActionEvent;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * Class creates playing grid, plays animations, handles mouse events associated with grid and 
 * alternates between player turns.
 * @author mkskelet
 */
public class Grid extends Group {
    private final GridBlock[] blocks = new GridBlock[27];       // array of GridBlocks used as playing grid
    private Node grid;                                          // Node containing all of created objects
    private final static double[] PLANE_DISTANCE = {-100,0,100};        // array of plane distances
    private double currentScrollLevel = -100;                           // distance of clickable plane
    
    private RotateTransition rotate_right;                      // animation for right rotation
    private RotateTransition rotate_left;                       // animation for left rotation
    private RotateTransition rotate_up;                         // animation for up rotation
    private RotateTransition rotate_down;                       // animation for down rotation
    
    private Player p1;       // first player
    private Player p2;       // second player
    private boolean player1turn = true;             // boolean variable that indicates whose turn it is
    
    private Color checkColor;           // last checked color
    private int consecutiveCount;       // number of same consecutive colors in current checking enviroment(row, collumn, diagonals)

    /**
     * Default constructor.
     * Creates grid, players and gets game ready to play.
     */
    public Grid() {
        createGrid();           
        createAnimations();
        setupEventHandlers();
        setEnableMouseForPlane();
        setupPlayers();
    }
    
    /**
     * Function returns playing grid Node containing all created objects
     * @return grid
     */
    public Node getGrid() {
        return grid;
    }
    
    private void createGrid() {
        Group group = new Group();      // create local group
        
        for(int i=0; i<3; ++i) {                // 3 planes
            for(int j=0; j<3; ++j) {            // 3 collumns
                for(int k=0; k<3; ++k) {        // 3 rows
                    blocks[9*i+3*j+k] = new GridBlock(50);          //create GridBlocks with size of 50
                    blocks[9*i+3*j+k].setTranslateX(100*j-100);     // set X position
                    blocks[9*i+3*j+k].setTranslateY(100*k-100);     // set Y position
                    blocks[9*i+3*j+k].setTranslateZ(100*i-100);     // set Z position
                    group.getChildren().add(blocks[9*i+3*j+k]);     // add block as child
                }
            }
        }
        
        grid = group;       // assign whole group to grid Node
    }
    
    private void createAnimations() {
        rotate_right = new RotateTransition(Duration.millis(1000), grid);       // create 1 second long animation
        rotate_right.setAxis(new Point3D(0,1,0));                               // turning around Y axis
        rotate_right.setByAngle(90);                                            // by 90 degree angle
        rotate_right.setCycleCount(1);                                          // 1 cycle
        
        rotate_left = new RotateTransition(Duration.millis(1000), grid);
        rotate_left.setAxis(new Point3D(0,1,0));
        rotate_left.setByAngle(-90);
        rotate_left.setCycleCount(1);
        
        rotate_up = new RotateTransition(Duration.millis(1000), grid);
        rotate_up.setAxis(new Point3D(1,0,0));
        rotate_up.setByAngle(-90);
        rotate_up.setCycleCount(1);
        
        rotate_down = new RotateTransition(Duration.millis(1000), grid);
        rotate_down.setAxis(new Point3D(1,0,0));
        rotate_down.setByAngle(90);
        rotate_down.setCycleCount(1);
    }
    
    private void setupEventHandlers() {
        for(int i=0; i<27; ++i) {
            // for each block register MOUSE_PRESSED event handler
            blocks[i].getBox().addEventHandler(MouseEvent.MOUSE_PRESSED, (final MouseEvent e) -> {
                for(int j=0; j<27; ++j) {
                    if(e.getTarget().equals(blocks[j].getBox()))        // if we found target index
                        setMark(j);                                     // call setMark(index)
                }
                e.consume();
            });
        }
    }
    
    private void setupPlayers() {
        p1 = new Player(Color.VIOLET);          // create player 1
        p2 = new Player(Color.GREENYELLOW);     // create player 2
    }
    
    private void setMark(int index) {
        if(player1turn)
            blocks[index].mark(p1.getColor());       // create mark in selected block
        else
            blocks[index].mark(p2.getColor());       // create mark in selected block
        
        player1turn = !player1turn;     // alternate player turn
        
        if(checkWin()) {     // if check for end of game is positive
            setDisableMouse(true);       // disable mouse for grid
            endGame();                   // end game
        }
    }
    
    private boolean checkWin() {
        // <editor-fold defaultstate="collapsed" desc="Check all collumns">
        for(int i=0; i<3; ++i) {                // 3 planes
            for(int j=0; j<3; ++j) {            // 3 collumns
                consecutiveCount = 0;
                for(int k=0; k<3; ++k) {        // 3 rows
                    if(blocks[9*i+3*j+k].isMarked()) {
                        if(k==0) {
                            checkColor = blocks[9*i+3*j+k].getColor();
                            consecutiveCount = 1;
                            continue;
                        }
                        else if(k > 0 && !blocks[9*i+3*j+k].compareColor(checkColor)) {
                            consecutiveCount = 0;
                            break;
                        }
                        
                        if(p1.getColor() == checkColor && blocks[9*i+3*j+k].compareColor(p1.getColor())) {
                            consecutiveCount++;
                        }
                        else if(p2.getColor() == checkColor && blocks[9*i+3*j+k].compareColor(p2.getColor())) {
                            consecutiveCount++;
                        }
                        
                        if(consecutiveCount == 3) {
                            return true;
                        }
                    }
                }
            }
        }
        // </editor-fold>
        
        // <editor-fold defaultstate="collapsed" desc="Check rows">
        for(int i=0; i<3; ++i) {                // 3 planes
            for(int j=0; j<3; ++j) {            // 3 collumns
                consecutiveCount = 0;
                for(int k=0; k<3; ++k) {        // 3 rows
                    if(blocks[9*i+3*k+j].isMarked()) {
                        if(k==0) {
                            checkColor = blocks[9*i+3*k+j].getColor();
                            consecutiveCount = 1;
                            continue;
                        }
                        else if(k > 0 && !blocks[9*i+3*k+j].compareColor(checkColor)) {
                            consecutiveCount = 0;
                            break;
                        }
                        
                        if(p1.getColor() == checkColor && blocks[9*i+3*k+j].compareColor(p1.getColor())) {
                            consecutiveCount++;
                        }
                        else if(p2.getColor() == checkColor && blocks[9*i+3*k+j].compareColor(p2.getColor())) {
                            consecutiveCount++;
                        }
                        
                        if(consecutiveCount == 3) {
                            return true;
                        }
                    }
                }
            }
        }
        // </editor-fold>
        
        // <editor-fold defaultstate="collapsed" desc="Check side rows">
        for(int i=0; i<3; ++i) {                // 3 planes
            for(int j=0; j<3; ++j) {            // 3 collumns
                consecutiveCount = 0;
                for(int k=0; k<3; ++k) {        // 3 rows
                    if(blocks[9*k+3*j+i].isMarked()) {
                        if(k==0) {
                            checkColor = blocks[9*k+3*j+i].getColor();
                            consecutiveCount = 1;
                            continue;
                        }
                        else if(k > 0 && !blocks[9*k+3*j+i].compareColor(checkColor)) {
                            consecutiveCount = 0;
                            break;
                        }
                        
                        if(p1.getColor() == checkColor && blocks[9*k+3*j+i].compareColor(p1.getColor())) {
                            consecutiveCount++;
                        }
                        else if(p2.getColor() == checkColor && blocks[9*k+3*j+i].compareColor(p2.getColor())) {
                            consecutiveCount++;
                        }
                        
                        if(consecutiveCount == 3) {
                            return true;
                        }
                    }
                }
            }
        }
        // </editor-fold>
        
        // <editor-fold defaultstate="collapsed" desc="Check diagonals 1-3">
        for(int i=0; i<3; ++i) {                // 3 planes
            consecutiveCount = 0;
            for(int j=0; j<3; ++j) {            // 3 collumns
                int k = j;
                if(blocks[9*i+3*j+k].isMarked()) {
                    if(k==0) {
                        checkColor = blocks[9*i+3*j+k].getColor();
                        consecutiveCount = 1;
                        continue;
                    }
                    else if(k > 0 && !blocks[9*i+3*j+k].compareColor(checkColor)) {
                        consecutiveCount = 0;
                        break;
                    }

                    if(p1.getColor() == checkColor && blocks[9*i+3*j+k].compareColor(p1.getColor())) {
                        consecutiveCount++;
                    }
                    else if(p2.getColor() == checkColor && blocks[9*i+3*j+k].compareColor(p2.getColor())) {
                        consecutiveCount++;
                    }

                    if(consecutiveCount == 3) {
                        return true;
                    }
                }
            }
        }
        // </editor-fold>
        
        // <editor-fold defaultstate="collapsed" desc="Check diagonals 4-6">
        for(int i=0; i<3; ++i) {                // 3 planes
            consecutiveCount = 0;
            for(int j=0; j<3; ++j) {            // 3 collumns
                int k = 2-j;
                if(blocks[9*i+3*j+k].isMarked()) {
                    if(j==0) {
                        checkColor = blocks[9*i+3*j+k].getColor();
                        consecutiveCount = 1;
                        continue;
                    }
                    else if(j > 0 && !blocks[9*i+3*j+k].compareColor(checkColor)) {
                        consecutiveCount = 0;
                        break;
                    }

                    if(p1.getColor() == checkColor && blocks[9*i+3*j+k].compareColor(p1.getColor())) {
                        consecutiveCount++;
                    }
                    else if(p2.getColor() == checkColor && blocks[9*i+3*j+k].compareColor(p2.getColor())) {
                        consecutiveCount++;
                    }

                    if(consecutiveCount == 3) {
                        return true;
                    }
                }
            }
        }
        // </editor-fold>
        
        // <editor-fold defaultstate="collapsed" desc="Check diagonals 7-9">
        for(int j=0; j<3; ++j) {                // 3 planes
            consecutiveCount = 0;
            for(int i=0; i<3; ++i) {            // 3 collumns
                int k = i;
                if(blocks[9*i+3*j+k].isMarked()) {
                    if(k==0) {
                        checkColor = blocks[9*i+3*j+k].getColor();
                        consecutiveCount = 1;
                        continue;
                    }
                    else if(k > 0 && !blocks[9*i+3*j+k].compareColor(checkColor)) {
                        consecutiveCount = 0;
                        break;
                    }

                    if(p1.getColor() == checkColor && blocks[9*i+3*j+k].compareColor(p1.getColor())) {
                        consecutiveCount++;
                    }
                    else if(p2.getColor() == checkColor && blocks[9*i+3*j+k].compareColor(p2.getColor())) {
                        consecutiveCount++;
                    }

                    if(consecutiveCount == 3) {
                        return true;
                    }
                }
            }
        }
        // </editor-fold>
        
        // <editor-fold defaultstate="collapsed" desc="Check diagonals 10-12">
        for(int j=0; j<3; ++j) {                // 3 planes
            consecutiveCount = 0;
            for(int i=0; i<3; ++i) {            // 3 collumns
                int k = 2-i;
                if(blocks[9*i+3*j+k].isMarked()) {
                    if(i==0) {
                        checkColor = blocks[9*i+3*j+k].getColor();
                        consecutiveCount = 1;
                        continue;
                    }
                    else if(i > 0 && !blocks[9*i+3*j+k].compareColor(checkColor)) {
                        consecutiveCount = 0;
                        break;
                    }

                    if(p1.getColor() == checkColor && blocks[9*i+3*j+k].compareColor(p1.getColor())) {
                        consecutiveCount++;
                    }
                    else if(p2.getColor() == checkColor && blocks[9*i+3*j+k].compareColor(p2.getColor())) {
                        consecutiveCount++;
                    }

                    if(consecutiveCount == 3) {
                        return true;
                    }
                }
            }
        }
        // </editor-fold>
        
        // <editor-fold defaultstate="collapsed" desc="Check diagonals 13-15">
        for(int k=0; k<3; ++k) {                // 3 planes
            consecutiveCount = 0;
            for(int i=0; i<3; ++i) {            // 3 collumns
                int j = i;
                if(blocks[9*i+3*j+k].isMarked()) {
                    if(i==0) {
                        checkColor = blocks[9*i+3*j+k].getColor();
                        consecutiveCount = 1;
                        continue;
                    }
                    else if(i > 0 && !blocks[9*i+3*j+k].compareColor(checkColor)) {
                        consecutiveCount = 0;
                        break;
                    }

                    if(p1.getColor() == checkColor && blocks[9*i+3*j+k].compareColor(p1.getColor())) {
                        consecutiveCount++;
                    }
                    else if(p2.getColor() == checkColor && blocks[9*i+3*j+k].compareColor(p2.getColor())) {
                        consecutiveCount++;
                    }

                    if(consecutiveCount == 3) {
                        return true;
                    }
                }
            }
        }
        // </editor-fold>
        
        // <editor-fold defaultstate="collapsed" desc="Check diagonals 16-18">
        for(int k=0; k<3; ++k) {                // 3 planes
            consecutiveCount = 0;
            for(int i=0; i<3; ++i) {            // 3 collumns
                int j = 2-i;
                if(blocks[9*i+3*j+k].isMarked()) {
                    if(i==0) {
                        checkColor = blocks[9*i+3*j+k].getColor();
                        consecutiveCount = 1;
                        continue;
                    }
                    else if(i > 0 && !blocks[9*i+3*j+k].compareColor(checkColor)) {
                        consecutiveCount = 0;
                        break;
                    }

                    if(p1.getColor() == checkColor && blocks[9*i+3*j+k].compareColor(p1.getColor())) {
                        consecutiveCount++;
                    }
                    else if(p2.getColor() == checkColor && blocks[9*i+3*j+k].compareColor(p2.getColor())) {
                        consecutiveCount++;
                    }

                    if(consecutiveCount == 3) {
                        return true;
                    }
                }
            }
        }
        // </editor-fold>
        
        // <editor-fold defaultstate="collapsed" desc="Check diagonal 19">
        consecutiveCount = 0;
        for(int k=0; k<3; ++k) {                // 3 planes
            int i = k, j = k;
            if(blocks[9*i+3*j+k].isMarked()) {
                if(i==0) {
                    checkColor = blocks[9*i+3*j+k].getColor();
                    consecutiveCount = 1;
                    continue;
                }
                else if(i > 0 && !blocks[9*i+3*j+k].compareColor(checkColor)) {
                    consecutiveCount = 0;
                    break;
                }

                if(p1.getColor() == checkColor && blocks[9*i+3*j+k].compareColor(p1.getColor())) {
                    consecutiveCount++;
                }
                else if(p2.getColor() == checkColor && blocks[9*i+3*j+k].compareColor(p2.getColor())) {
                    consecutiveCount++;
                }

                if(consecutiveCount == 3) {
                    return true;
                }
            }
        }
        // </editor-fold>
        
        // <editor-fold defaultstate="collapsed" desc="Check diagonal 20">
        consecutiveCount = 0;
        for(int k=0; k<3; ++k) {                // 3 planes
            int i = k, j = 2-k;
            if(blocks[9*i+3*j+k].isMarked()) {
                if(i==0) {
                    checkColor = blocks[9*i+3*j+k].getColor();
                    consecutiveCount = 1;
                    continue;
                }
                else if(i > 0 && !blocks[9*i+3*j+k].compareColor(checkColor)) {
                    consecutiveCount = 0;
                    break;
                }

                if(p1.getColor() == checkColor && blocks[9*i+3*j+k].compareColor(p1.getColor())) {
                    consecutiveCount++;
                }
                else if(p2.getColor() == checkColor && blocks[9*i+3*j+k].compareColor(p2.getColor())) {
                    consecutiveCount++;
                }

                if(consecutiveCount == 3) {
                    return true;
                }
            }
        }
        // </editor-fold>
        
        // <editor-fold defaultstate="collapsed" desc="Check diagonal 21">
        consecutiveCount = 0;
        for(int k=0; k<3; ++k) {                // 3 planes
            int i = 2-k, j = k;
            if(blocks[9*i+3*j+k].isMarked()) {
                if(k==0) {
                    checkColor = blocks[9*i+3*j+k].getColor();
                    consecutiveCount = 1;
                    continue;
                }
                else if(k > 0 && !blocks[9*i+3*j+k].compareColor(checkColor)) {
                    consecutiveCount = 0;
                    break;
                }

                if(p1.getColor() == checkColor && blocks[9*i+3*j+k].compareColor(p1.getColor())) {
                    consecutiveCount++;
                }
                else if(p2.getColor() == checkColor && blocks[9*i+3*j+k].compareColor(p2.getColor())) {
                    consecutiveCount++;
                }

                if(consecutiveCount == 3) {
                    return true;
                }
            }
        }
        // </editor-fold>
        
        // <editor-fold defaultstate="collapsed" desc="Check diagonal 22">
        consecutiveCount = 0;
        for(int k=0; k<3; ++k) {                // 3 planes
            int i = 2-k, j = i;
            if(blocks[9*i+3*j+k].isMarked()) {
                if(k==0) {
                    checkColor = blocks[9*i+3*j+k].getColor();
                    consecutiveCount = 1;
                    continue;
                }
                else if(k > 0 && !blocks[9*i+3*j+k].compareColor(checkColor)) {
                    consecutiveCount = 0;
                    break;
                }

                if(p1.getColor() == checkColor && blocks[9*i+3*j+k].compareColor(p1.getColor())) {
                    consecutiveCount++;
                }
                else if(p2.getColor() == checkColor && blocks[9*i+3*j+k].compareColor(p2.getColor())) {
                    consecutiveCount++;
                }

                if(consecutiveCount == 3) {
                    return true;
                }
            }
        }
        // </editor-fold>
        
        return false;
    }
    
    private void endGame() {
        String win = "Player " + (player1turn ? "2" : "1") + " won!\nCongratulations!";     // set string
        TTT3D.ShowEndScreen(win);       // show end screen
    }
    
    /**
     * Resets grid.
     */
    public void resetGrid() {
        for(int i = 0; i < 27; ++i)
            blocks[i].reset();          // reset each block
        
        player1turn = true;             // sets player 1 turn
        setDisableMouse(true);          // disable mouse for grid
        setEnableMouseForPlane();       // enable mouse for current clickable plane (topmost)
    }
    
    /**
     * Function responsible for rotating grid right
     */
    public void rotateRight() {
        if(!animationIsPlaying()) {     // rotate only if no animation is playing
            setDisableMouse(true);      // disable mouse
            rotate_right.play();        // start animation
            rotate_right.setOnFinished((ActionEvent event) -> {     // when animation ends
                setEnableMouseForPlane();                           // enable mouse only for clickable plane
                event.consume();
            });
        }
    }
    
    /**
     * Function responsible for rotating grid left
     */
    public void rotateLeft() {
        if(!animationIsPlaying()) {
            setDisableMouse(true);
            rotate_left.play();
            rotate_left.setOnFinished((ActionEvent event) -> {
                setEnableMouseForPlane();
                event.consume();
            });
        }
    }
    
    /**
     * Function responsible for rotating grid up
     */
    public void rotateUp() {
        if(!animationIsPlaying()) {
            setDisableMouse(true);
            rotate_up.play();
            rotate_up.setOnFinished((ActionEvent event) -> {
                setEnableMouseForPlane();
                event.consume();
            });
        }
    }
    
    /**
     * Function responsible for rotating grid down
     */
    public void rotateDown() {
        if(!animationIsPlaying()) {
            setDisableMouse(true);
            rotate_down.play();
            rotate_down.setOnFinished((ActionEvent event) -> {
                setEnableMouseForPlane();
                event.consume();
            });
        }
    }
    
    private boolean animationIsPlaying() {
        // returns true if any of animations are being played
        // returns false otherwise
        return !(rotate_right.getStatus() != Animation.Status.RUNNING &&
                rotate_left.getStatus() != Animation.Status.RUNNING &&
                rotate_up.getStatus() != Animation.Status.RUNNING &&
                rotate_down.getStatus() != Animation.Status.RUNNING);
    }
    
    private void setDisableMouse(boolean enable) {
        for(int i=0; i<27; ++i) {                   // for each block
            blocks[i].passMouseEvents(enable);      // call passMouseEvents with parameter enable
        }
        currentScrollLevel = PLANE_DISTANCE[0];     // reset currentScrollLevel
    }
    
    /**
     * Function sets clickable plane to one step away from camera if current clickable plane isnt last.
     */
    public void scrollIn() {
        for(int i=0; i<3; ++i) {
            if(currentScrollLevel == PLANE_DISTANCE[i] && i<2){     // if current clickable plane depth isn't equal to last element in PLANE_DISTANCE array
                currentScrollLevel = PLANE_DISTANCE[i+1];           // set currentScrollLevel to next array element
                break;
            }
        }
        setEnableMouseForPlane();       // enable mouse for current clickable plane
    }
    
    /**
     * Function sets clickable plane to one step closer to camera if current clickable plane isnt first.
     */
    public void scrollOut() {
        for(int i=0; i<3; ++i) {
            if(currentScrollLevel == PLANE_DISTANCE[i] && i>0){     // if current clickable plane depth isn't equal to first element in PLANE_DISTANCE array
                currentScrollLevel = PLANE_DISTANCE[i-1];           // set currentScrollLevel to previous array element
                break;
            }
        }
        setEnableMouseForPlane();       // enable mouse for current clickable plane
    }
    
    private void setEnableMouseForPlane() {
        // for each block
        for(int i=0; i<27; ++i) {
            // if global Z position of block is equal(or within 1% offset range) to currentScrollLevel
            if(grid.localToScene(blocks[i].getTranslateX(), blocks[i].getTranslateY(), blocks[i].getTranslateZ()).getZ()+1 > currentScrollLevel &&
                    grid.localToScene(blocks[i].getTranslateX(), blocks[i].getTranslateY(), blocks[i].getTranslateZ()).getZ()-1 < currentScrollLevel) {
                blocks[i].passMouseEvents(false);       // dont pass mouse events (make block clickable)
            }
            else blocks[i].passMouseEvents(true);       // pass mouse events (make block not clickable)
        }
    }
}