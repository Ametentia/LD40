package com.pixeldot.ld40.State;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.pixeldot.ld40.Entities.Player;
import com.pixeldot.ld40.Entities.Tiles.Tile;
import com.pixeldot.ld40.Entities.Tiles.TileParam;
import com.pixeldot.ld40.Entities.Tiles.TileType;
import com.pixeldot.ld40.Util.ContentManager;
import com.pixeldot.ld40.Util.GameStateManager;
import com.pixeldot.ld40.Util.State;

import java.util.Random;

import static com.pixeldot.ld40.Metro.W_WIDTH;

public class Play extends State {

    private static final int GridSize = 20;

    private Vector2 start, end;
    private boolean moving;

    private BitmapFont font;

    private Tile[][] grid;
    private Player player;

    private Tile prev;

    private float accumulator;

    public Play(GameStateManager gsm) {
        super(gsm);

        // Load Content
        LoadContent();
        generateGrid();

        player = new Player();
        player.setExcessPower(2003);
        player.setExcessWater(219);
        player.setPopulation(20000);

        accumulator = 0;
    }

    public void Update(float dt) {
        accumulator += dt;

        mouse.set(camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0)));

        // Camera panning with mouse
        if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT) && !moving) {
            moving = true;
            start = new Vector2(mouse.x, mouse.y);
        }
        else if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT) || moving) {
            end = new Vector2(mouse.x, mouse.y);
            camera.translate(start.sub(end));
            start.set(end);
            moving = false;
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            generateGrid();
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT_BRACKET)) {
            camera.zoom *= 0.5f;
        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT_BRACKET)) {
            camera.zoom /= 0.5f;
        }


        float pollution = 0;
        float power = 0, rqPower = 0;
        float water = 0, rqWater = 0;
        float income = 0, outcome = 0;

        for(int i = 0; i < GridSize; ++i) {
            for(int j = 0; j < GridSize; ++j) {
                final TileParam params = grid[i][j].getParams();
                power += params.powerOutput;
                rqPower += params.powerIntake;

                pollution += params.pollution;

                water += params.waterOutput;
                rqWater += params.waterIntake;

                income += params.moneyOutput;
                outcome += params.moneyIntake;
            }
        }

        int x = (int) ((2 * mouse.y + mouse.x) / 2f) / Tile.Size;
        int y = (int) ((2 * mouse.y - mouse.x) / 2f) / Tile.Size;

        if(x < 20 && x > 0 && y < 20 && y > 0) {
            if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                if(prev != null) prev.debugOverlay = null;
                prev = grid[Math.abs(x)][Math.abs(y)];
                prev.debugOverlay = Color.GRAY;
            }
        }

        if(prev != null) {
            if(Gdx.input.isKeyJustPressed(Input.Keys.A)) {

                int ordinal = prev.getType().ordinal() - 1 < 0 ?
                        TileType.values()[TileType.values().length - 1].ordinal() :
                        prev.getType().ordinal() - 1;

                TileType type = TileType.values()[ordinal];
                prev.setType(type);
            }
            else if(Gdx.input.isKeyJustPressed(Input.Keys.D)) {
                int len = TileType.values().length;
                int ordinal = prev.getType().ordinal() + 1 >= len ?
                        0 :
                        prev.getType().ordinal() + 1;

                TileType type = TileType.values()[ordinal];
                prev.setType(type);
            }
        }

        player.setPollution(pollution);

        player.setExcessPower(power - rqPower);
        player.setExcessWater(water - rqWater);

        player.setOutcome(outcome);
        player.setIncome(income);

        player.update(dt);

        camera.update();
    }

    public void Render() {

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        for(int i = 0; i < GridSize; ++i) {
            for(int j = 0; j < GridSize; j++) {
                grid[i][j].render(batch);
            }
        }

        batch.setProjectionMatrix(hudCamera.combined);
        mouse.set(camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0)));
        font.draw(batch, player.toString(), W_WIDTH - 200, 20);

        batch.end();
    }

    @Override
    public void Dispose() {

    }


    private void generateGrid() {
        Random random = new Random();

        grid = new Tile[GridSize][GridSize];
        for(int i = 0; i < GridSize; ++i) {
            for(int j = 0; j < GridSize; ++j) {
                TileType type = TileType.Blank;
                if(random.nextInt() % 10 == 0) {
                    type = TileType.Housing;
                }
                else if(random.nextInt() % 4 == 0) {
                    type = TileType.Road;
                }
                grid[i][j] = new Tile(type, i, j);
            }
        }
    }

    private void LoadContent() {
        ContentManager.Instance.LoadTexture("Blank1", "Tiles/Grass-01.png");
        ContentManager.Instance.LoadTexture("Blank2", "Tiles/Grass-02.png");
        ContentManager.Instance.LoadTexture("Housing", "Tiles/Shack-01.png");
        ContentManager.Instance.LoadTexture("Road", "Tiles/Path-01.png");

        font = ContentManager.Instance.LoadFont("TekoR", "Teko-Regular.ttf", 30);
    }
}
