/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import mygame.common.Log;

/**
 *
 * @author io
 */
public class ShipController extends AbstractControl {

    private Vector3f oldPosition;
    private Vector3f newPosition;
    private Vector3f diffVector;
    private Vector3f diffVectorBuff;
    private Vector3f lookAtVectorBuff;
    private float rotationBuffer;
    private float rotation;
    private boolean firstrun = true;

    public ShipController() {
	super();
	newPosition = new Vector3f(0, 0, 3);
	oldPosition = new Vector3f();
	diffVector = new Vector3f();
	lookAtVectorBuff = new Vector3f();

	//Log.debug(spatial);
	//newPosition = spatial.getLocalTranslation();
    }

    @Override
    protected void controlUpdate(float tpf) {
	if (firstrun) {
	    this.spatial.rotateUpTo(Vector3f.UNIT_Z);

	    firstrun = false;
	}
	//this.spatial.move(getVectorFromAngle(rotationBuffer).mult(-tpf));
	Log.debug("v: " + getVectorFromAngle(rotationBuffer) + " w : " + rotationBuffer);
	diffVectorBuff = newPosition.subtract(this.spatial.getLocalTranslation());
	diffVectorBuff.z = 3;



	diffVectorBuff.z = 0;
	Vector3f moveVector = newPosition.subtract(this.spatial.getLocalTranslation());
	moveVector.z = 0;
	if (!(moveVector.length() < 0.1)) {
	    moveVector = moveVector.normalize();
	    this.spatial.move(moveVector.mult(tpf));
	}

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
	this.oldPosition = spatial.getLocalTranslation().clone();
	this.newPosition = newPosition;
	this.diffVector = newPosition.subtract(oldPosition);
	this.diffVector.z = 3;
	//this.perpVector = this.perpVector.normalize();
	float buff = calcRotationAngleInDegrees(oldPosition, newPosition);
	rotation = rotationBuffer - buff;



	Log.debug("Winkel: " + (rotation) + "VEC1: " + oldPosition + " VEC2 " + newPosition);


	rotationBuffer = calcRotationAngleInDegrees(oldPosition, newPosition);
	this.spatial.rotate(0.0f, -(float) Math.toRadians(rotation), 0.0f);
    }

    /**
     * Calculates the angle from centerPt to targetPt in degrees. The return
     * should range from [0,360), rotating CLOCKWISE, 0 and 360 degrees
     * represents NORTH, 90 degrees represents EAST, etc...
     *
     * Assumes all points are in the same coordinate space. If they are not, you
     * will need to call SwingUtilities.convertPointToScreen or equivalent on
     * all arguments before passing them to this function.
     *
     * @param centerPt Point we are rotating around.
     * @param targetPt Point we want to calcuate the angle to.
     * @return angle in degrees. This is the angle from centerPt to targetPt.
     */
    public static float calcRotationAngleInDegrees(Vector3f centerPt, Vector3f targetPt) {
	// calculate the angle theta from the deltaY and deltaX values
	// (atan2 returns radians values from [-PI,PI])
	// 0 currently points EAST.
	// NOTE: By preserving Y and X param order to atan2,  we are expecting
	// a CLOCKWISE angle direction.

	centerPt.dot(targetPt);

	centerPt.length();
	targetPt.length();

	double theta = Math.atan2(targetPt.y - centerPt.y, targetPt.x - centerPt.x);

	// rotate the theta angle clockwise by 90 degrees
	// (this makes 0 point NORTH)
	// NOTE: adding to an angle rotates it clockwise.
	// subtracting would rotate it counter-clockwise
	theta += Math.PI / 2.0;

	// convert from radians to degrees
	// this will give you an angle from [0->270],[-180,0]
	float angle = (float) Math.toDegrees(theta);

	// convert to positive range [0-360)
	// since we want to prevent negative angles, adjust them now.
	// we can assume that atan2 will not return a negative value
	// greater than one partial rotation
	if (angle < 0) {
	    angle += 360;
	}

	return angle;
    }

    public static Vector3f getVectorFromAngle(float angle) {
	return new Vector3f(FastMath.cos(angle), FastMath.sin(angle), 0);
    }
}
