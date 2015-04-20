package mygame;

import mygame.gamestates.Menu;
import com.jme3.app.Application;
import com.jme3.app.DebugKeysAppState;
import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.app.state.AppState;
import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.FlyByCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.jme3.network.Network;
import com.jme3.network.Server;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.BatchNode;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;

import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.CameraControl.ControlDirection;

import com.jme3.scene.shape.Quad;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Torus;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext.Type;
import com.jme3.system.JmeSystem;
import com.jme3.texture.FrameBuffer;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import com.jme3.util.BufferUtils;
import com.jme3.util.NativeObject;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import mygame.common.Log;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

/**
 * test
 *
 * @author normenhansen
 */
public class ServerMain extends SimpleApplication {

    ParticleEmitter debris;
    private CameraControl cameraControl;
    private CameraNode cameraNode;
    private Node target;
    private Geometry geom;
    private Material blackMaterial; //for godray' first pass
    private HashMap<Spatial, Material> godrayMatarialBuffer = new HashMap<>();
    public static AssetManager assetManagerStatic;
    private Spatial elephant;
    private Node clickAbles;
    private MouseActionListener mouseActionListener;
    private ArrayList<GodrayLight> godrayLights = new ArrayList<>();
    private ArrayList<Geometry> geometries = new ArrayList<>();
    private Material combinedMaterial;
    private Geometry torus;

    public static void main(String[] args) {
        ServerMain app = new ServerMain();
        AppSettings settings = new AppSettings(true);
        app.setSettings(settings);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        clickAbles = new Node("clickables");
        rootNode.attachChild(clickAbles);
        assetManagerStatic = getAssetManager();
        //http://www.sharecg.com/v/70033/browse/2/YouTube-Video/Space-station-construction-free-3d-model
        Log.debug(
                "░░░░░░░░░▄░░░░░░░░░░░░░░▄░░░░\n"
                + "░░░░░░░░▌▒█░░░░░░░░░░░▄▀▒▌░░░\n"
                + "░░░░░░░░▌▒▒█░░░░░░░░▄▀▒▒▒▐░░░\n"
                + "░░░░░░░▐▄▀▒▒▀▀▀▀▄▄▄▀▒▒▒▒▒▐░░░\n"
                + "░░░░░▄▄▀▒░▒▒▒▒▒▒▒▒▒█▒▒▄█▒▐░░░\n"
                + "░░░▄▀▒▒▒░░░▒▒▒░░░▒▒▒▀██▀▒▌░░░\n"
                + "░░▐▒▒▒▄▄▒▒▒▒░░░▒▒▒▒▒▒▒▀▄▒▒▌░░\n"
                + "░░▌░░▌█▀▒▒▒▒▒▄▀█▄▒▒▒▒▒▒▒█▒▐░░\n"
                + "░▐░░░▒▒▒▒▒▒▒▒▌██▀▒▒░░░▒▒▒▀▄▌░\n"
                + "░▌░▒▄██▄▒▒▒▒▒▒▒▒▒░░░░░░▒▒▒▒▌░\n"
                + "▀▒▀▐▄█▄█▌▄░▀▒▒░░░░░░░░░░▒▒▒▐░\n"
                + "▐▒▒▐▀▐▀▒░▄▄▒▄▒▒▒▒▒▒░▒░▒░▒▒▒▒▌\n"
                + "▐▒▒▒▀▀▄▄▒▒▒▄▒▒▒▒▒▒▒▒░▒░▒░▒▒▐░\n"
                + "░▌▒▒▒▒▒▒▀▀▀▒▒▒▒▒▒░▒░▒░▒░▒▒▒▌░\n"
                + "░▐▒▒▒▒▒▒▒▒▒▒▒▒▒▒░▒░▒░▒▒▄▒▒▐░░\n"
                + "░░▀▄▒▒▒▒▒▒▒▒▒▒▒░▒░▒░▒▄▒▒▒▒▌░░\n"
                + "░░░░▀▄▒▒▒▒▒▒▒▒▒▒▄▄▄▀▒▒▒▒▄▀░░░\n"
                + "░░░░░░▀▄▄▄▄▄▄▀▀▀▒▒▒▒▒▄▄▀░░░░░\n"
                + "░░░░░░░░░▒▒▒▒▒▒▒▒▒▒▀▀░░░░░░░░\n");


        target = new Node();
        flyCam.setEnabled(false);
        cam.setFrustumFar(50000);

        cameraNode = new CameraNode("Camera Node", cam);
        cameraNode.setControlDir(ControlDirection.SpatialToCamera);
        target.attachChild(cameraNode);
        cameraNode.setLocalTranslation(new Vector3f(0, 0, 20));
        cameraNode.lookAt(target.getLocalTranslation(), Vector3f.UNIT_Y);
        cameraControl = new CameraControl(target);
        rootNode.addControl(cameraControl);
        rootNode.attachChild(target);

        inputManager.addMapping("moveUp", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addListener(cameraControl, "moveUp");
        inputManager.addMapping("moveLeft", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addListener(cameraControl, "moveLeft");
        inputManager.addMapping("moveDown", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addListener(cameraControl, "moveDown");
        inputManager.addMapping("moveRight", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addListener(cameraControl, "moveRight");

        mouseActionListener = new MouseActionListener(rootNode, guiNode, cam, clickAbles, inputManager, assetManager, settings);
        rootNode.addControl(mouseActionListener);
        inputManager.addMapping("Shoot",
                new KeyTrigger(KeyInput.KEY_SPACE), // trigger 1: spacebar
                new MouseButtonTrigger(MouseInput.BUTTON_LEFT)); // trigger 2: left-button click
        inputManager.addMapping("Move",
                new MouseButtonTrigger(MouseInput.BUTTON_RIGHT)); // trigger 2: left-button click
        inputManager.addListener(mouseActionListener, "Shoot");
        inputManager.addListener(mouseActionListener, "Move");

        //we dont need that "rendermanager", we define our own cylce in @see simpleRender

        renderManager.removeMainView(viewPort);
        renderManager.removePostView(guiViewPort);

        blackMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        blackMaterial.setColor("Color", ColorRGBA.Black);

        Geometry quad = new Geometry("box", new Quad(settings.getWidth(), settings.getHeight()));


        Torus mesh = new Torus(100, 100, 0.2f, 1.5f);
        torus = new Geometry("A shape", mesh); // wrap shape into geometry
        mesh.scaleTextureCoordinates(new Vector2f(0.5f, .5f));
        Material mat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        Texture cube1Tex = assetManager.loadTexture(
                "Textures/steel.jpg");
        cube1Tex.setWrap(Texture.WrapMode.Repeat);
        mat.setTexture("ColorMap", cube1Tex);

        torus.move(0, 0, 3f);
        torus.setMaterial(mat);                         // assign material to geometry
// if you want, transform (move, rotate, scale) the geometry.
        rootNode.attachChild(torus);




        GodrayLight l3 = new GodrayLight(settings, cam, assetManager, rootNode, guiNode);
        l3.move(0, 0, 3f);
        godrayLights.add(l3);
        rootNode.attachChild(l3);
        clickAbles.attachChild(l3);




        // attach geometry to a node



        quad.setMaterial(combinedMaterial);
        quad.setLocalTranslation(new Vector3f(0, 0, -1000));
        // guiNode.attachChild(quad);

        elephant = assetManager.loadModel("Models/Class II Gallactic Cruiser.j3o");
        elephant.setLocalScale(0.01f);
        elephant.move(new Vector3f(3, 3, 3));
        elephant.setUserData("ID", "fuckyou");
        elephant.addControl(new ShipController());
        clickAbles.attachChild(elephant);

        PointLight sun = new PointLight();
        sun.setPosition(new Vector3f(0, 0, 5));

        //sun.setDirection(new Vector3f(-0.1f, 0.7f, -1.0f).normalizeLocal());
        rootNode.addLight(sun);


        stateManager.attach(new Menu());
        emitterTransfer();

    }

    @Override
    public void simpleUpdate(float tpf) {
        torus.rotate(tpf, tpf, tpf);
        for (GodrayLight g : godrayLights) {
            g.update(tpf);
        }
        //debris.emitAllParticles();
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //render our scene normally (all attached stuff from the rootnode)
        renderManager.renderViewPort(viewPort, speed);
        geometries.clear();
        getGeometries(rootNode, geometries);
        for (GodrayLight godrayLight : godrayLights) {
            for (Geometry g : geometries) {
                godrayMatarialBuffer.put(g, g.getMaterial());
                g.setMaterial(blackMaterial);
            }
            godrayLight.getGeometry().setMaterial(godrayMatarialBuffer.get(godrayLight.getGeometry()));
            renderManager.renderViewPort(godrayLight.getViewPort(), speed);
            for (Geometry g : geometries) {
                g.setMaterial(godrayMatarialBuffer.get(g));
            }
            godrayMatarialBuffer.clear();
        }
        renderManager.renderViewPort(guiViewPort, speed);
    }

    private void getGeometries(Spatial spatial, ArrayList<Geometry> geometries) {

        if (spatial instanceof Geometry) {
            geometries.add((Geometry) spatial);
        } else {
            Node n = (Node) spatial;
            for (Spatial s : n.getChildren()) {
                getGeometries(s, geometries);
            }

        }
    }

    private void emitterTransfer() {
        // init colors



        debris =
                new ParticleEmitter("Debris", ParticleMesh.Type.Triangle, 10000);
        Material debris_mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        debris_mat.getAdditionalRenderState().setBlendMode(BlendMode.AlphaAdditive);
        debris_mat.setTexture("Texture", assetManager.loadTexture("Textures/lavatile.jpg"));
        debris.setMaterial(debris_mat);

        //debris.setImagesX(20);
        //debris.setImagesY(20); // 3x3 texture animation
        //debris.setRotateSpeed(4);
        debris.setStartSize(0.05f);
        debris.setEndSize(0.01f);
        debris.setLowLife(0.1f);
        debris.setHighLife(0.8f);
        debris.setFacingVelocity(true);
        debris.setSelectRandomImage(true);
        debris.getParticleInfluencer().setInitialVelocity(new Vector3f(2f, 0f, 0));
        debris.setStartColor(ColorRGBA.White);
        debris.setEndColor(ColorRGBA.White);
        //debris.setGravity(1f, 0.0f, 0);
        debris.setParticlesPerSec(0f);
        //node.addControl(new ParticelEmitterControl(800));
        debris.getParticleInfluencer().setVelocityVariation(1.0f);
        debris.setGravity(0, 0, 0);
        rootNode.attachChild(debris);

        rootNode.attachChild(rootNode);


        debris.setBatchHint(Spatial.BatchHint.Always);

        debris.emitAllParticles();

        debris.move(0, 8f, 0);
        //rootNode.detachChild(debris);
    }
}
