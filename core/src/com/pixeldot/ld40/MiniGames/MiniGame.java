package com.pixeldot.ld40.MiniGames;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.pixeldot.ld40.Metro;
import com.pixeldot.ld40.Util.ContentManager;
import com.pixeldot.ld40.Util.GameStateManager;
import com.pixeldot.ld40.Util.State;

import static com.pixeldot.ld40.Metro.W_HEIGHT;
import static com.pixeldot.ld40.Metro.W_WIDTH;

/**
 * @author Matthew Threlfall
 */
public abstract class MiniGame extends State {
    protected State background;
    private int x = (Metro.W_WIDTH/4);
    private int y = (Metro.W_HEIGHT/8);
    private int boarderX = x + Metro.W_WIDTH/70;
    private int boarderY = y + Metro.W_HEIGHT/55;
    private int boarderWidth = Metro.W_WIDTH/2 - Metro.W_WIDTH/70*2;
    private int boarderHeight = (Metro.W_HEIGHT/8)*6 - Metro.W_HEIGHT/55*2;
    private static Sprite texture = new Sprite(ContentManager.Instance.GetTexture("minigameboarder"));

    protected int xStart = boarderX;
    protected int yStart = boarderY;
    protected int drawEndX = boarderX + boarderWidth;
    protected int drawEndY = boarderY + boarderHeight;

    public MiniGame(GameStateManager gsm, State background) {
        super(gsm);
        this.background = background;
        camera.setToOrtho(true, W_WIDTH, W_HEIGHT);
        viewport.apply();

        texture.setFlip(false, true);
    }
    public void update(float dt) {
        background.update(dt);
    }
    public void render() {
        batch.setProjectionMatrix(camera.combined);
        renderer.setProjectionMatrix(camera.combined);
        //255, 153, 0
        batch.begin();
        batch.draw(texture, boarderX - 134/2 - 1,boarderY - 117/2 + 4);
        batch.end();
    }
}
