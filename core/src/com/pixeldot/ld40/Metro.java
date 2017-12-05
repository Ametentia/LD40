package com.pixeldot.ld40;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pixeldot.ld40.Util.ContentManager;
import com.pixeldot.ld40.Util.GameStateManager;
import com.pixeldot.ld40.Util.StateType;

public class Metro extends ApplicationAdapter {

    // Window size
	public static final int W_WIDTH = 1280;
	public static final int W_HEIGHT = 720;

	private static final float DELTA = 1.0f / 60.0f;
	private float accumulator;

	// Game state manager
    private GameStateManager gsm;

	// Camera
	private OrthographicCamera camera;
	private OrthographicCamera hudCamera;
	private Viewport viewport;

	// Rendering
	private SpriteBatch batch;
    private ShapeRenderer renderer;


	public void create () {

	    camera = new OrthographicCamera();
	    camera.setToOrtho(true, W_WIDTH, W_HEIGHT);

	    hudCamera = new OrthographicCamera();
	    hudCamera.setToOrtho(true, W_WIDTH, W_HEIGHT);

	    viewport = new ExtendViewport(1920, 1080, camera);
	    viewport.apply();

	    batch = new SpriteBatch();
	    renderer = new ShapeRenderer();

	    // Create Game state manager and add the first state
	    gsm = new GameStateManager(this);
	    gsm.AddState(StateType.Testing);
	}

    public void render () {
		// Gdx.gl.glClearColor((100.f / 255.f), (149.f / 255.f), (237.f / 255.f), 1); // Cornflower Blue
		Gdx.gl.glClearColor(128.f / 255.f, 128.f / 255.f, 128.f / 255.f, 1f);
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		accumulator += Gdx.graphics.getDeltaTime();
		while(accumulator >= DELTA) {
		    accumulator -= DELTA;
		    gsm.Update(DELTA);
        }

        gsm.Render();
	}

    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

	public void dispose () {
        ContentManager.Instance.Dispose();
    }

	// Getters

    public Viewport GetViewport() { return viewport; }

    public SpriteBatch GetBatch() { return batch; }
    public ShapeRenderer GetRenderer() { return renderer; }
    public OrthographicCamera GetCamera() { return camera; }
    public OrthographicCamera getHudCamera() { return hudCamera; }
}
