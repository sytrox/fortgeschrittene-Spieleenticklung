/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author db-141205
 */
//
public class SpaceStationController extends AbstractControl{
    private float spawnCooldownCounter = 0;
    private float spawnCooldown = 5;
    
    @Override
    protected void controlUpdate(float tpf) {
        if(spawnCooldownCounter > spawnCooldown){
            new SpaceShipController();
            spawnCooldownCounter = 0f;
        }
        spawnCooldownCounter += tpf;
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
    
    
}
