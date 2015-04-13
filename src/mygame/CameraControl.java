/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.input.controls.ActionListener;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;
import mygame.common.Log;

/**
 *
 * @author db-141205
 */
public class CameraControl extends AbstractControl implements ActionListener {

    private final float speed = 3.0f;
    boolean left, right, up, down;
    Node cameraNode;

    CameraControl(Node cameraNode) {
        this.cameraNode = cameraNode;
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals("moveLeft") && isPressed) {
            left = true;
        } else if (name.equals("moveLeft") && !isPressed) {
            left = false;
        }
        if (name.equals("moveUp") && isPressed) {
            up = true;
        } else if (name.equals("moveUp") && !isPressed) {
            up = false;
        }
        if (name.equals("moveRight") && isPressed) {
            right = true;
        } else if (name.equals("moveRight") && !isPressed) {
            right = false;
        }
        if (name.equals("moveDown") && isPressed) {
            down = true;
        } else if (name.equals("moveDown") && !isPressed) {
            down = false;
        }
    }

    @Override
    protected void controlUpdate(float tpf) {
        if (left) {
            cameraNode.move(-tpf * speed, 0f, 0f);
        }
        if (right) {
            cameraNode.move(tpf * speed, 0f, 0f);
        }
        if (up) {
            cameraNode.move(0f, tpf * speed, 0f);
        }
        if (down) {
            cameraNode.move(0f, -tpf * speed, 0f);
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
}
