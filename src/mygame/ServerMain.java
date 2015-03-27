package mygame;

import mygame.gamestates.Menu;
import com.jme3.app.Application;
import com.jme3.app.DebugKeysAppState;
import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.app.state.AppState;
import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.FlyByCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.jme3.network.Network;
import com.jme3.network.Server;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;

import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.control.CameraControl.ControlDirection;

import com.jme3.scene.shape.Quad;
import com.jme3.scene.shape.Sphere;
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

    private CameraControl cameraControl;
    private CameraNode cameraNode;
    private Node target;
    private long tick = 0;
    private ViewPort godrayViewPort;
    private Texture offTex;
    private Material mat2;
    private Sphere b;
    private Geometry geom;
    private float tpfCounter = 0;
    private Material godrayMatarial;
    private Material blackMaterial; //for godray' first pass
    private HashMap<Spatial, Material> godrayMatarialBuffer = new HashMap<>();
    //the main
    public static AssetManager assetManagerStatic;
    private IntBuffer pixelBuf;
    private BufferedImage img;
    Spatial elephant;

    public static void main(String[] args) {
	ServerMain app = new ServerMain();
	AppSettings settings = new AppSettings(true);
	app.setSettings(settings);
	app.start();
    }

    @Override
    public void simpleInitApp() {
	assetManagerStatic = getAssetManager();

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
	cameraNode.setLocalTranslation(new Vector3f(0, 20, 0));
	cameraNode.lookAt(target.getLocalTranslation(), Vector3f.UNIT_Z);
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

	//we dont need that "rendermanager", we define our own cylce in @see simpleRender
	renderManager.removeMainView(viewPort);
	renderManager.removePostView(guiViewPort);

	blackMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
	blackMaterial.setColor("Color", ColorRGBA.Black);

	pixelBuf = ByteBuffer.allocateDirect((settings.getWidth() * settings.getHeight()) << 2).order(ByteOrder.nativeOrder()).asIntBuffer();
	img = new BufferedImage(settings.getWidth(), settings.getHeight(), BufferedImage.TYPE_INT_RGB);
	Geometry quad = new Geometry("box", new Quad(settings.getWidth(), settings.getHeight()));
	offTex = setupOffscreenView();
	b = new Sphere(20, 20, 1);
	geom = new Geometry("Sphere", b);


	mat2 = new Material(assetManager, "MatDefs/lava.j3md");
	Texture tex1 = assetManager.loadTexture("Textures/cloud.png");
	tex1.setWrap(Texture.WrapMode.Repeat);
	mat2.setTexture("texture1", tex1);
	Texture tex2 = assetManager.loadTexture("Textures/lavatile.jpg");
	tex2.setWrap(Texture.WrapMode.Repeat);
	mat2.setTexture("texture2", tex2);
	mat2.setVector2("uvScale", new Vector2f(1.0f, 1.0f));
	mat2.setVector2("resolution", new Vector2f(settings.getWidth(), settings.getHeight()));
	mat2.setFloat("time", 1.0f);
	mat2.setFloat("fogDensity", 0.0f);
	mat2.setVector3("fogColor", new Vector3f(0, 0, 0));
	mat2.setVector4("test", new Vector4f(1, 0, 0, 1));
	//mat2.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off); // turn culling off
	//mat2.preload(renderManager);
	geom.setMaterial(mat2);

	rootNode.attachChild(geom);


	Material sceneMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
	sceneMaterial.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Color);
	sceneMaterial.setTexture("ColorMap", offTex);


	godrayMatarial = new Material(assetManager, "MatDefs/godray.j3md");
	godrayMatarial.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Additive);
	godrayMatarial.setTexture("firstPass", offTex);
	godrayMatarial.setFloat("exposure", 0.0094f);
	godrayMatarial.setFloat("decay", 0.95f);
	godrayMatarial.setFloat("density", 1.00f);
	godrayMatarial.setFloat("weight", 15.00f);


	quad.setMaterial(godrayMatarial);
	quad.setLocalTranslation(new Vector3f(0, 0, -1000));
	guiNode.attachChild(quad);

	elephant = assetManager.loadModel("Models/Class II Gallactic Cruiser.j3o");
	elephant.setLocalScale(0.01f);
	elephant.move(new Vector3f(0, 3, 0));

	rootNode.attachChild(elephant);

	DirectionalLight sun = new DirectionalLight();
	sun.setDirection(new Vector3f(-0.1f, 0.7f, -1.0f).normalizeLocal());
	rootNode.addLight(sun);


	stateManager.attach(new Menu());
    }

    @Override
    public void simpleUpdate(float tpf) {
	tick++;

	Vector2f pos = new Vector2f();
	pos.x = cam.getScreenCoordinates(geom.getLocalTranslation()).x;
	pos.y = cam.getScreenCoordinates(geom.getLocalTranslation()).y;
	pos.x = (pos.x / settings.getWidth());
	pos.y = (pos.y / settings.getHeight());
	godrayMatarial.setVector2("lightPositionOnScreen", pos);
	geom.rotate(tpf, tpf, tpf);
	tpfCounter += tpf;
	mat2.setFloat("time", tpfCounter);

	/*GL11.glReadPixels(0, 0, settings.getWidth(), settings.getHeight(), GL12.GL_BGRA, GL11.GL_UNSIGNED_BYTE, pixelBuf);
	 for (int x = 0; x < settings.getWidth(); x++) {

	 for (int y = 0; y < settings.getHeight(); y++) {

	 img.setRGB(x, y, pixelBuf.get((settings.getHeight() - y - 1) * settings.getWidth() + x));

	 }

	 }
	 try {



	 OutputStream out = new BufferedOutputStream(new FileOutputStream("C:\\Users\\db-141205\\Desktop\\Spiele2\\img_" + tick + ".png"));


	 ImageIO.write(img, "png", out);

	 } catch (IOException e) {

	 System.out.println("Fehler beim schreiben der Pattern-Datei: " + "img_" + tick + ".png");

	 }*/
    }

    @Override
    public void simpleRender(RenderManager rm) {
	//render our scene normally (all attached stuff from the rootnode)
	renderManager.renderViewPort(viewPort, speed);
	for (Spatial rootNodeChild_Spatial : rootNode.getChildren()) {
	    try {
		Node rootNodeChild_Node = (Node) rootNodeChild_Spatial;
		for (Spatial n : rootNodeChild_Node.getChildren()) {          //objekte bestehen aus mehreren einzelteilen mit unterschiedlichen materialien, deswegen das gecaste und das einzelne wegspeichern vor dem firstpass für die godrays
		    Geometry g = (Geometry) n;
		    godrayMatarialBuffer.put(g, g.getMaterial());
		}
		rootNodeChild_Spatial.setMaterial(blackMaterial);
	    } catch (Exception e) {
//                Geometry g = (Geometry) rootNodeChild_Spatial;
		//              godrayMatarialBuffer.put(g, g.getMaterial());
		//rootNodeChild_Spatial.setMaterial(blackMaterial);
	    }
	}

	/*Node node = (Node)(elephant);
	 for(Spatial n : node.getChildren()){
	 Geometry g = (Geometry) n;
	 godrayMatarialBuffer.put(g, g.getMaterial());
	 }  */
	//elephant.setMaterial(blackMaterial);
	renderManager.renderViewPort(godrayViewPort, speed);

	for (Spatial rootNodeChild_Spatial : rootNode.getChildren()) {
	    try {
		Node rootNodeChild_Node = (Node) rootNodeChild_Spatial;
		for (Spatial n : rootNodeChild_Node.getChildren()) {          //objekte bestehen aus mehreren einzelteilen mit unterschiedlichen materialien, deswegen das gecaste und das einzelne wegspeichern vor dem firstpass für die godrays
		    Geometry g = (Geometry) n;
		    g.setMaterial(godrayMatarialBuffer.get(g));
		}
	    } catch (Exception e) {
		//            Geometry g = (Geometry) rootNodeChild_Spatial;
		//g.setMaterial(godrayMatarialBuffer.get(g));
	    }
	}

	/* for(Spatial n : node.getChildren()){         //selbe wie vor dem rendern, nur umgekehrt
	 Geometry g = (Geometry) n;
	 g.setMaterial(godrayMatarialBuffer.get(g));
	 }  */
	//guiViewport at very last (guiNode)
	renderManager.renderViewPort(guiViewPort, speed);
    }

    public Texture setupOffscreenView() {

	godrayViewPort = new ViewPort("godray viewport", cam);
	//offView = getRenderManager().createPostView("Offscreen View", cam);
	godrayViewPort.setClearFlags(true, true, true);
	godrayViewPort.setBackgroundColor(ColorRGBA.Black);
	// create offscreen framebuffer
	FrameBuffer offBuffer = new FrameBuffer(settings.getWidth(), settings.getHeight(), 1);
	// setup framebuffer's texture
	Texture2D offTex = new Texture2D(settings.getWidth(), settings.getHeight(), Image.Format.RGBA8);
	offBuffer.setColorTexture(offTex);
	// set viewport to render to offscreen framebuffer
	godrayViewPort.setOutputFrameBuffer(offBuffer);
	godrayViewPort.attachScene(rootNode);

	return offTex;
    }
}
