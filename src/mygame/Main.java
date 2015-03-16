package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix4f;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Quad;
import com.jme3.scene.shape.Sphere;
import com.jme3.system.AppSettings;
import com.jme3.texture.FrameBuffer;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import com.sun.xml.internal.ws.api.config.management.policy.ManagementAssertion;

/**
 * test
 *
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    private ViewPort offView;
    private Texture offTex;
    private Material mat2;
    private Sphere b;
    private Geometry geom;
    private float tpfCounter = 0;
    private Material godrayMatarial;
    //the main

    public static void main(String[] args) {
        Main app = new Main();
        AppSettings settings = new AppSettings(true);
        app.setSettings(settings);
        app.start();
    }

    @Override
    public void simpleInitApp() {

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
        godrayMatarial.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Color);
        godrayMatarial.setTexture("firstPass", offTex);
        godrayMatarial.setFloat("exposure", 0.0094f);
        godrayMatarial.setFloat("decay", 0.95f);
        godrayMatarial.setFloat("density", 1.00f);
        godrayMatarial.setFloat("weight", 15.00f);


        quad.setMaterial(godrayMatarial);
        quad.setLocalTranslation(new Vector3f(0, 0, -1000));
        guiNode.attachChild(quad);





    }

    @Override
    public void simpleUpdate(float tpf) {
        Vector2f pos = new Vector2f();
        pos.x = cam.getScreenCoordinates(geom.getLocalTranslation()).x;
        pos.y = cam.getScreenCoordinates(geom.getLocalTranslation()).y;
        pos.x = (pos.x / settings.getWidth());
        pos.y = (pos.y / settings.getHeight());
        godrayMatarial.setVector2("lightPositionOnScreen", pos);
        geom.rotate(tpf, tpf, tpf);
        tpfCounter += tpf;
        mat2.setFloat("time", tpfCounter);
        System.out.println(cam.getScreenCoordinates(geom.getWorldTranslation()));
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    public Texture setupOffscreenView() {
        offView = getRenderManager().createPreView("Offscreen View", cam);
        offView.setClearFlags(true, true, true);
        offView.setBackgroundColor(ColorRGBA.Black);
        // create offscreen framebuffer
        FrameBuffer offBuffer = new FrameBuffer(settings.getWidth(), settings.getHeight(), 1);
        // setup framebuffer's texture
        Texture2D offTex = new Texture2D(settings.getWidth(), settings.getHeight(), Image.Format.RGBA8);
        offBuffer.setColorTexture(offTex);
        // set viewport to render to offscreen framebuffer
        offView.setOutputFrameBuffer(offBuffer);
        offView.attachScene(rootNode);
        return offTex;
    }
}
