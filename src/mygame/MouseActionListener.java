/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import mygame.common.Log;

/**
 *
 * @author io
 */
public class MouseActionListener implements ActionListener {

    private Node rootNode;
    private Node guiNode;
    private Camera cam;
    private Node clickAbles;
    private InputManager inputManager;

    public MouseActionListener(Node rootNode, Node guiNode, Camera cam, Node clickAbles, InputManager inputManager) {
        this.rootNode = rootNode;
        this.guiNode = guiNode;
        this.cam = cam;
        this.clickAbles = clickAbles;
        this.inputManager = inputManager;
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals("Shoot") && !isPressed) {
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
                Log.debug("läuft");
            } else {
            }
        }

    }
}
