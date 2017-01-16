// The GameStateManager does exactly what its
// name says. It contains a list of GameStates.
// It decides which GameState to update() and
// draw() and handles switching between different
// GameStates.

package com.neet.DiamondHunter.Manager;

import java.awt.Graphics2D;

import com.neet.DiamondHunter.GameState.GameOverState;
import com.neet.DiamondHunter.GameState.GameState;
import com.neet.DiamondHunter.GameState.IntroState;
import com.neet.DiamondHunter.GameState.MenuState;
import com.neet.DiamondHunter.GameState.PauseState;
import com.neet.DiamondHunter.GameState.PlayState;
import com.neet.DiamondHunter.GameState.WaitState;


public class GameStateManager {
	
	private boolean paused;
	// ����� �������� ��ٸ����� �ϴ� �÷���
	private boolean wait;
	private PauseState pauseState;
	// ����� ���� ���� ��ٸ����� �ϴ� ȭ��
	private WaitState waitState;
	
	public static GameState[] gameStates;
	private int currentState;
	private int previousState;
	
	public static final int NUM_STATES = 4;
	public static final int INTRO = 0;
	public static final int MENU = 1;
	public static final int PLAY = 2;
	public static final int GAMEOVER = 3;
	
	public GameStateManager() {
		
		JukeBox.init();
		
		paused = false;
		pauseState = new PauseState(this);
		
		// ���ȭ�� �ʱ�ȭ
		wait = false;
		waitState = new WaitState(this);
		
		gameStates = new GameState[NUM_STATES];
		setState(INTRO);
		
	}
	
	public void setState(int i) {
		previousState = currentState;
		unloadState(previousState);
		currentState = i;
		if(i == INTRO) {
			gameStates[i] = new IntroState(this);
			gameStates[i].init();
		}
		else if(i == MENU) {
			gameStates[i] = new MenuState(this);
			gameStates[i].init();
		}
		else if(i == PLAY) {
			gameStates[i] = new PlayState(this);
			gameStates[i].init();
		}
		else if(i == GAMEOVER) {
			gameStates[i] = new GameOverState(this);
			gameStates[i].init();
		}
	}
	
	public void unloadState(int i) {
		gameStates[i] = null;
	}
	
	public void setPaused(boolean b) {
		paused = b;
	}
	// ����
	public void setWait(boolean b){
		wait = b;
	}
	public void update() {
		if(paused) {
			pauseState.update();
		}
		// ���ȭ�� ������Ʈ
		else if(wait){
			waitState.update();
		}
		else if(gameStates[currentState] != null) {
			gameStates[currentState].update();
		}
	}
	
	public void draw(Graphics2D g) {
		if(paused) {
			pauseState.draw(g);
		}
		else if(wait){
			waitState.draw(g);
		}
		else if(gameStates[currentState] != null) {
			gameStates[currentState].draw(g);
		}
	}
	
}
