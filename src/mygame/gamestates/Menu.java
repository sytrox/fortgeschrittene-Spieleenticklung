/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.gamestates;

import mygame.gamestates.GameState;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.event.MouseButtonEvent;
import mygame.common.Log;

/**
 *
 * @author db-141205
 */
public class Menu extends GameState {

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
	super.initialize(stateManager, app);
	getScreen().parseLayout("Interface/MainMenu.gui.xml", this);
	initialized = true;
    }

    public void startClick(MouseButtonEvent evt, boolean toggled) {
	changeState(new MainGameState());
	//cleanup();
    }
}
