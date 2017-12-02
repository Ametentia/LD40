package com.pixeldot.ld40;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pixeldot.ld40.Util.GameStateManager;
import com.pixeldot.ld40.Util.StateType;

public class Metro extends ApplicationAdapter {

    // Window size
	public static final int W_WIDTH = 1280;
	public static final int W_HEIGHT = 720;

	// Game state manager
    private GameStateManager gsm;

	// Camera
	private OrthographicCamera camera;
	private Viewport viewport;

	// Rendering
	private SpriteBatch batch;
    private ShapeRenderer renderer;


	public void create () {

	    camera = new OrthographicCamera();
	    camera.setToOrtho(true, W_WIDTH, W_HEIGHT);

	    viewport = new ExtendViewport(W_WIDTH, W_HEIGHT, camera);
	    viewport.apply();

	    batch = new SpriteBatch();
	    renderer = new ShapeRenderer();



	    // Create Game state manager and add the first state
	    gsm = new GameStateManager(this);
	    gsm.AddState(StateType.Play);
	}

	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}

	public void dispose () {}

	// Getters
    public SpriteBatch getBatch() { return batch; }
    public ShapeRenderer getRenderer() { return renderer; }
    public OrthographicCamera getCamera() { return camera; }
}
