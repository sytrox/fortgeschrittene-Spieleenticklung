/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.material.RenderState;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;
import com.jme3.system.AppSettings;
import com.jme3.ui.Picture;
import mygame.common.Log;

/**
 *
 * @author io
 */
public class MouseActionListener extends AbstractControl implements ActionListener {

    private Node rootNode;
    private Node guiNode;
    private Camera cam;
    private Node clickAbles;
    private InputManager inputManager;
    private ShipControler shipControler;
    private boolean select;
    private AssetManager assetManager;
    private final AppSettings settings;
    private Picture pic;

    public MouseActionListener(Node rootNode, Node guiNode, Camera cam, Node clickAbles, InputManager inputManager, AssetManager assetManager, AppSettings settings) {
        this.rootNode = rootNode;
        this.guiNode = guiNode;
        this.cam = cam;
        this.clickAbles = clickAbles;
        this.inputManager = inputManager;
        this.assetManager = assetManager;
        this.settings = settings;
        pic = new Picture("HUD Picture");
        pic.setImage(assetManager, "Textures/selectBox.png", true);
        pic.getMaterial().getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals("Shoot") && isPressed) {
            select = true;

            pic.setPosition(inputManager.getCursorPosition().x, inputManager.getCursorPosition().y);


            guiNode.attachChild(pic);
            Log.debug("click");
            // 1. Reset results list.
            CollisionResults results = new CollisionResults();
            Vector2f click2d = inputManager.getCursorPosition();
            Vector3f click3d = cam.getWorldCoordinates(
                    new Vector2f(click2d.x, click2d.y), 0f).clone();
            Vector3f dir = cam.getWorldCoordinates(
                    new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
            Ray ray = new Ray(click3d, dir);
            clickAbles.collideWith(ray, results);
            // 4. Print the results
            System.out.println("----- Collisions? " + results.size() + "-----");
            for (int i = 0; i < results.size(); i++) {
                // For each hit, we know distance, impact point, name of geometry.
                float dist = results.getCollision(i).getDistance();
                Vector3f pt = results.getCollision(i).getContactPoint();
                String hit = results.getCollision(i).getGeometry().getName();
                System.out.println("* Collision #" + i);
                System.out.println("  You shot " + hit + " at " + pt + ", " + dist + " wu away.");
            }
            // 5. Use the results (we mark the hit object)
            if (results.size() > 0) {
                CollisionResult closest = results.getClosestCollision();
                Log.debug(closest.getGeometry().getParent().getUserData("ID"));
                shipControler = closest.getGeometry().getParent().getControl(ShipControler.class);
            } else {
            }
        } else if (name.equals("Shoot") && !isPressed) {
            select = false;
            pic.removeFromParent();
        }
        if (name.equals("Move") && !isPressed) {
            Vector2f click2d = inputManager.getCursorPosition();
            Vector3f click3d = cam.getWorldCoordinates(
                    new Vector2f(click2d.x, click2d.y), 0.0f).clone();
            Vector3f dir = cam.getWorldCoordinates(
                    new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
            dir = dir.mult(17.0f / dir.z);
            dir = dir.mult(-1);
            dir = dir.add(cam.getLocation());
            dir.z = 3.0f;

            shipControler.setNewPosition(dir);
            Log.debug(dir);

        }
    }

    @Override
    protected void controlUpdate(float tpf) {
        if (select) {
            pic.setWidth(inputManager.getCursorPosition().x - pic.getLocalTranslation().x);
            pic.setHeight(inputManager.getCursorPosition().y - pic.getLocalTranslation().y);
            float x = Math.abs(inputManager.getCursorPosition().x - pic.getLocalTranslation().x);
            float y = Math.abs(inputManager.getCursorPosition().y - pic.getLocalTranslation().y);
            Log.debug(x + "::" + y);

        }

    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
}
