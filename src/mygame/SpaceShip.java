/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author db-141205
 */
public class SpaceShip {
    private SpaceShipController spaceshipcontroller;
    private Spatial spatial;
    private AssetManager assetManager;
    private Node clickAbles;
    
    SpaceShip(Node clickAbles){
        this.clickAbles = clickAbles;
        this.assetManager = ServerMain.assetManagerStatic;
        spaceshipcontroller = new SpaceShipController();
        
        spatial = assetManager.loadModel("Models/Class II Gallactic Cruiser.j3o");
        spatial.setLocalScale(0.01f);
        spatial.move(new Vector3f(3, 3, 3));
        spatial.setUserData("ID", "fuckyou");
        spatial.addControl(new SpaceShipController());
        clickAbles.attachChild(spatial);
    }
    
    
}
