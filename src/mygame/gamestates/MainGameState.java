/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.gamestates;

import mygame.gamestates.GameState;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.input.InputManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;
import mygame.common.Log;

/**
 *
 * @author db-141205
 */
public class MainGameState extends GameState {

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
	super.initialize(stateManager, app);
	getScreen().parseLayout("Interface/Game.gui.xml", this);
	AmbientLight al = new AmbientLight();
	al.setColor(ColorRGBA.Green.mult(50));
	rootNode.addLight(al);

	DirectionalLight sun = new DirectionalLight();
	sun.setDirection(new Vector3f(0, -20, 0).normalizeLocal());
	sun.setColor(ColorRGBA.White.mult(0.3f));
	rootNode.addLight(sun);

	PointLight pl = new PointLight();
	//pl.setPosition(new Vector3f(0, 20, 0));
	rootNode.addLight(pl);

	initStars();



	initialized = true;
    }

    @Override
    public void cleanup() {
	super.cleanup();
	// unregister all my listeners, detach all my nodes, etc...

    }

    @Override
    public void setEnabled(boolean enabled) {
	// Pause and unpause
    }

    // Note that update is only called while the state is both attached and enabled.
    @Override
    public void update(float tpf) {
	// do the following while game is RUNNING
    }

    private void initStars() {
	for (int j = 0; j < 20; j++) {
	    Vector3f[] lineVerticies = new Vector3f[5000 / 20];
	    for (int i = 0; i < 5000 / 20; i++) {
		lineVerticies[i] = new Vector3f((float) (i * (Math.random() - 0.5f)), (float) (-i * (Math.random())), (float) (i * (Math.random() - 0.5f)));
	    }
	    plotPoints(lineVerticies, ColorRGBA.White, (float) j / 20 + 1);
	}
    }

    public void plotPoints(Vector3f[] lineVerticies, ColorRGBA pointColor, float size) {
	Mesh mesh = new Mesh();
	mesh.setMode(Mesh.Mode.Points);
	mesh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(lineVerticies));
	mesh.setPointSize(size);
	mesh.updateBound();
	mesh.updateCounts();
	Geometry geo = new Geometry("line", mesh);
	Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
	mat.setColor("Color", pointColor);
	geo.setMaterial(mat);
	rootNode.attachChild(geo);
    }
}
