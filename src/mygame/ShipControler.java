/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import mygame.common.Log;

/**
 *
 * @author io
 */
public class ShipControler extends AbstractControl {

    private Vector3f newPosition;

    public ShipControler() {
        super();
        newPosition = new Vector3f();
        //Log.debug(spatial);
        //newPosition = spatial.getLocalTranslation();
    }

    @Override
    protected void controlUpdate(float tpf) {
        Vector3f moveVector = getNewPosition().subtract(this.spatial.getLocalTranslation());
        moveVector = moveVector.normalize();
        this.spatial.move(moveVector.mult(tpf));
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    /**
     * @return the newPosition
     */
    public Vector3f getNewPosition() {
        return newPosition;
    }

    /**
     * @param newPosition the newPosition to set
     */
    public void setNewPosition(Vector3f newPosition) {
        this.newPosition = newPosition;
    }
}
