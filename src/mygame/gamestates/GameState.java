package mygame.gamestates;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;



import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.input.InputManager;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.jme3.system.AppSettings;
import mygame.ServerMain;
import mygame.common.Log;
import tonegod.gui.core.Element;
import tonegod.gui.core.Screen;
import tonegod.gui.core.utils.BitmapTextUtil;

public abstract class GameState extends AbstractAppState {

    protected RenderManager renderManager;
    protected Camera cam;
    protected ServerMain clientMain;
    protected Node rootNode;
    protected Node guiNode;
    protected AssetManager assetManager;
    protected AppStateManager stateManager;
    protected InputManager inputManager;
    protected ViewPort viewPort;
    protected AppSettings settings;
    protected BulletAppState physics;
    private static Screen screen = null;
    private List<Element> screenElements = new ArrayList<>();
    private List<Spatial> guiNodeSpatials = new ArrayList<>();
    private List<Control> guiNodeControls = new ArrayList<>();

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
	super.initialize(stateManager, app);
	clientMain = (ServerMain) app;
	rootNode = clientMain.getRootNode();
	assetManager = clientMain.getAssetManager();
	this.stateManager = clientMain.getStateManager();
	inputManager = clientMain.getInputManager();
	viewPort = clientMain.getViewPort();
	guiNode = clientMain.getGuiNode();
	physics = this.stateManager.getState(BulletAppState.class);
	cam = clientMain.getCamera();
	renderManager = clientMain.getRenderManager();

	if (screen == null) {
	    createScreen();
	}

    }

    /**
     * Resizes the text in the given element to fit in it's bounds
     *
     * @param ref
     */
    void textSize(Element ref) {
	float size = 10;
	ref.setFontSize(size);
	String text = ref.getText();
	float textWidth = BitmapTextUtil.getTextWidth(ref, text);
	float textHeight = BitmapTextUtil.getTextLineHeight(ref, text);
	while (textWidth < ref.getWidth() && textHeight < ref.getHeight()) {
	    size += 1;
	    ref.setFontSize(size);
	    textWidth = BitmapTextUtil.getTextWidth(ref, text);
	    textHeight = BitmapTextUtil.getTextLineHeight(ref, text);
	}
	size -= 2;
	Log.trace("%s - %s - %s - %s - %s - %s", ref.getWidth(), textWidth, ref.getHeight(), textHeight, text, size);
	ref.setFontSize(size);
    }

    private void createScreen() {
	screen = new Screen(clientMain, "tonegod/gui/style/atlasdef/style_map.gui.xml");
	screen.setUseTextureAtlas(true, "tonegod/gui/style/atlasdef/atlas.png");
	guiNode.addControl(screen);
    }

    public void changeState(GameState newState) {
	Log.debug("Changing state from '%s' to '%s'", this.getClass().getSimpleName(), newState.getClass().getSimpleName());
	stateManager.attach(newState);
	stateManager.detach(this);
    }

    @Override
    public void cleanup() {
	super.cleanup();
	Log.debug("Cleaning scene");

	for (int i = 0; i < guiNodeSpatials.size(); i++) {
	    guiNode.detachChild(guiNodeSpatials.get(i));
	}

	for (int i = 0; i < screenElements.size(); i++) {
	    screen.removeElement(screenElements.get(i));
	}

	for (int i = 0; i < guiNodeControls.size(); i++) {
	    guiNode.removeControl(guiNodeControls.get(i));
	}

    }

    /**
     * Reference is only to be used to create Elements such as Buttons
     *
     * @return The screen element
     */
    public Screen getScreen() {
	if (screen == null) {
	    createScreen();
	}
	return screen;
    }

    public void addScreenElement(Element element) {
	screen.addElement(element);
	screenElements.add(element);
    }

    public void removeScreenElement(Element element) {
	screen.removeElement(element);
	screenElements.remove(element);
    }

    public void guiNodeAttachChild(Spatial spatial) {
	guiNode.attachChild(spatial);
	guiNodeSpatials.add(spatial);
    }

    public void guiNodeDetachChild(Spatial spatial) {
	guiNode.detachChild(spatial);
	guiNodeSpatials.remove(spatial);
    }

    public void guiNodeAddControl(Control control) {
	guiNode.addControl(control);
	guiNodeControls.add(control);
    }

    public void guiNodeRemoveControl(Control control) {
	guiNode.removeControl(control);
	guiNodeControls.remove(control);
    }
}
