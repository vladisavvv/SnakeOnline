package com.vladik_company;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;

import java.net.URISyntaxException;

public class Main extends Game implements ApplicationListener {
	static public int width = 960;
	static public int height = 540;

	@Override
	public void create () {
		WaitingScreen waitingScreen = null;
		try {
			waitingScreen = new WaitingScreen(this);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		setScreen(waitingScreen);


//		MainScreen mainScreen = new MainScreen(this);
//		setScreen(mainScreen);
	}

	public void startGame(ServerHandler serverHandler) {
		AreaScreen areaScreen = new AreaScreen(this, serverHandler);
		setScreen(areaScreen);
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		super.dispose();
	}

	@Override
	public void resize(int width, int height) {

		super.resize(width, height);
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}
}
