package com.pixeldot.ld40.State;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.pixeldot.ld40.Entities.Player;
import com.pixeldot.ld40.Entities.Tiles.TileParam;
import com.pixeldot.ld40.Tiles.BaseTile;
import com.pixeldot.ld40.Tiles.Map;
import com.pixeldot.ld40.Util.ContentManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.MathUtils;
import com.pixeldot.ld40.MiniGames.ConnectTheDots;
import com.pixeldot.ld40.Util.ContentManager;
import com.pixeldot.ld40.Util.GameStateManager;
import com.pixeldot.ld40.Util.InputHandler;
import com.pixeldot.ld40.Util.State;
import com.pixeldot.ld40.Util.StateType;

import static com.pixeldot.ld40.Metro.W_HEIGHT;

public class Play extends State {

    private int GridSize = 60;
    private Map map;

    // Camera movement
    private Vector2 start, end;
    private boolean moving;

    private Player player;

    private BitmapFont font;

    private InputHandler input;

    private boolean openState;
    private Music[] songs;
    private int songPlay;

    public Play(GameStateManager gsm) {
        super(gsm);

        loadContent();

        map = new Map(GridSize, GridSize);

        input = new InputHandler();
        Gdx.input.setInputProcessor(input);

        player = new Player();

        loadContent();
        openState = false;

    }

    public void update(float dt) {
        mouse.set(camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0)));

        boolean canMove = Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT)
                && Gdx.input.isButtonPressed(Input.Buttons.LEFT);

        // Camera panning with mouse
        if(canMove && !moving) {
            moving = true;
            start = new Vector2(mouse.x, mouse.y);
        }
        else if(canMove || moving) {
            end = new Vector2(mouse.x, mouse.y);
            camera.translate(start.sub(end));
            start.set(end);
            moving = false;
        }


        camera.zoom += (0.1 * input.getScrollValue());
        camera.zoom = Math.max(Math.min(camera.zoom, 5), 0.25f);
        camera.update();

        if(!canMove && Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            int y = (int) (((2 * mouse.y - mouse.x) / 2f) / BaseTile.Size + 0.5f);
            int x = (int) (((2 * mouse.y + mouse.x) / 2f) / BaseTile.Size);

            //map.selectTile(x, y);
            map.placeTile(player, input.getCurrent(), x, y);
        }

        if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            int y = (int) (((2 * mouse.y - mouse.x) / 2f) / BaseTile.Size + 0.5f);
            int x = (int) (((2 * mouse.y + mouse.x) / 2f) / BaseTile.Size);

            map.removeTile(x, y);
        }

        TileParam params = new TileParam();
        map.getParams(params);

        player.updateParams(params);
        player.update(dt);

        map.setCurrent(input.getCurrent());


        input.resetScroll();

        if(!openState) {
            gsm.AddState(StateType.MINIGAME_SHEEPJUMP);
            openState = true;
        }
        if(!songs[songPlay].isPlaying()) {
            songPlay=(songPlay+1)%5;
            songs[songPlay].setVolume(0.4f);
            songs[songPlay].play();
        }
    }

    public void render() {

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        int y = (int) (((2 * mouse.y - mouse.x) / 2f) / BaseTile.Size + 0.5f);
        int x = (int) (((2 * mouse.y + mouse.x) / 2f ) / BaseTile.Size);

        map.render(batch, x, y);

        batch.end();

        renderer.setProjectionMatrix(hudCamera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Filled);


        renderer.setColor(Color.WHITE);
        renderer.rect(33, 33, 220, 20);
        renderer.rect(20, 90, 215, 20);
        renderer.rect(24, 155, 208, 20);
        renderer.rect(380, 30, 465, 50);

        renderer.setColor(new Color(77 / 255f, 1, 77 / 255f, 1));
        float width = Math.min((player.getPollution() / 7500f) * 220, 220);
        renderer.rect(33, 33, width, 20);

        renderer.setColor(Color.YELLOW);
        width = Math.max(Math.min((player.getExcessPower() / 3000f) * 215, 215), 0);
        renderer.rect(20, 90, width, 20);

        renderer.setColor(new Color((100 / 255f), (149 / 255f), (237 / 255f), 1f));
        width = Math.max(Math.min((player.getExcessWater() / 3000f) * 210, 210), 0);
        renderer.rect(24, 155, width, 20);

        float clamp = Math.max(Math.min(player.getSatisfaction(), 1), 0);

        Color c = new Color(Color.RED);
        renderer.setColor(c.lerp(Color.GREEN, clamp));
        width = clamp * 465;
        renderer.rect(380, 30, width, 50);


        renderer.end();

        batch.setProjectionMatrix(hudCamera.combined);
        batch.begin();
        Texture ui = ContentManager.Instance.GetTexture("UI_Pollution");
        batch.draw(ui, 5, 5, ui.getWidth() / 2f, ui.getHeight() / 2f, 0, 0, ui.getWidth(), ui.getHeight(), false, true);

        ui = ContentManager.Instance.GetTexture("UI_Power");
        batch.draw(ui, 5, 75, ui.getWidth() / 2f, ui.getHeight() / 2f, 0, 0, ui.getWidth(), ui.getHeight(), false, true);

        ui = ContentManager.Instance.GetTexture("UI_Water");
        batch.draw(ui, 5, 135, ui.getWidth() / 2f, ui.getHeight() / 2f, 0, 0, ui.getWidth(), ui.getHeight(), false, true);

        ui = ContentManager.Instance.GetTexture("UI_Rating");
        batch.draw(ui, 350, 5, ui.getWidth() / 2f, ui.getHeight() / 2f, 0, 0, ui.getWidth(), ui.getHeight(), false, true);

        if(clamp > 0.75f) {
            ui = ContentManager.Instance.GetTexture("Face_Happy");
        }
        else if(clamp < 0.75f && clamp > 0.25f) {
            ui = ContentManager.Instance.GetTexture("Face_Medium");
        }
        else if(clamp < 0.5f && clamp > 0.1f) {
            ui = ContentManager.Instance.GetTexture("Face_Angry");
        }
        else {
            ui = ContentManager.Instance.GetTexture("Face_Skull");
        }

        batch.draw(ui, 350, 5, ui.getWidth() / 2f, ui.getHeight() / 2f, 0, 0, ui.getWidth(), ui.getHeight(), false, true);

        ui = ContentManager.Instance.GetTexture("UI_Foundation");
        batch.draw(ui, 1030, 5, ui.getWidth() / 1.8f, ui.getHeight() / 1.8f, 0, 0, ui.getWidth(), ui.getHeight(), false, true);


        float prevX = 30;
        for(int i = 1; i < 8; i++) {

            ui = ContentManager.Instance.GetTexture("Tooltip_" + i);
            batch.draw(ui, prevX, W_HEIGHT - 100, ui.getWidth() / 3f, ui.getHeight() / 3f,
                    0, 0, ui.getWidth(), ui.getHeight(), false, true);
            prevX = prevX + ui.getWidth() / 3f;

        }

        batch.end();

    }

    private void loadContent() {
        // Grass
        ContentManager.Instance.LoadTexture("Tile_Grass0", "Tiles/Grass3D.png");
        ContentManager.Instance.LoadTexture("Tile_Grass1", "Tiles/Grass3D2.png");

        // Road
        ContentManager.Instance.LoadTexture("Tile_Road", "Tiles/Path-01.png");

        // Houses
        ContentManager.Instance.LoadTexture("Tile_House", "Tiles/House.png");

        // Reactor
        ContentManager.Instance.LoadTexture("Tile_Reactor0", "Tiles/Reactor-L.png");
        ContentManager.Instance.LoadTexture("Tile_Reactor1", "Tiles/Reactor-R.png");

        // Town Hall
        ContentManager.Instance.LoadTexture("Tile_TownHall0", "Tiles/TownHall-L.png");
        ContentManager.Instance.LoadTexture("Tile_TownHall1", "Tiles/TownHall-R.png");

        // School
        ContentManager.Instance.LoadTexture("Tile_School0", "Tiles/School-L.png");
        ContentManager.Instance.LoadTexture("Tile_School1", "Tiles/School-R.png");

        // Hospital
        ContentManager.Instance.LoadTexture("Tile_Hospital0", "Tiles/Hospital-L.png");
        ContentManager.Instance.LoadTexture("Tile_Hospital1", "Tiles/Hospital-M.png");
        ContentManager.Instance.LoadTexture("Tile_Hospital2", "Tiles/Hospital-R.png");

        // Well
        ContentManager.Instance.LoadTexture("Tile_Well", "Tiles/Well.png");

        // UI
        ContentManager.Instance.LoadTexture("UI_Foundation", "UI/LevelFoundation.png");
        ContentManager.Instance.LoadTexture("UI_Pollution", "UI/PollutionBar-1.png");
        ContentManager.Instance.LoadTexture("UI_Power", "UI/PowerBar-1.png");
        ContentManager.Instance.LoadTexture("UI_Water", "UI/WaterBar.png");
        ContentManager.Instance.LoadTexture("UI_Rating", "UI/SatisfactionBar.png");

        // Tooltip
        ContentManager.Instance.LoadTexture("Tooltip_1", "UI/Tooltip/Road.png");
        ContentManager.Instance.LoadTexture("Tooltip_2", "UI/Tooltip/House.png");
        ContentManager.Instance.LoadTexture("Tooltip_3", "UI/Tooltip/TownHall.png");
        ContentManager.Instance.LoadTexture("Tooltip_4", "UI/Tooltip/Reactor.png");
        ContentManager.Instance.LoadTexture("Tooltip_5", "UI/Tooltip/School.png");
        ContentManager.Instance.LoadTexture("Tooltip_6", "UI/Tooltip/Well.png");
        ContentManager.Instance.LoadTexture("Tooltip_7", "UI/Tooltip/Hospital.png");

        // Faces
        ContentManager.Instance.LoadTexture("Face_Happy", "UI/HappyFace.png");
        ContentManager.Instance.LoadTexture("Face_Medium", "UI/MediumFace.png");
        ContentManager.Instance.LoadTexture("Face_Angry", "UI/AngryFace.png");
        ContentManager.Instance.LoadTexture("Face_Skull", "UI/SkullFace.png");


        font = ContentManager.Instance.LoadFont("Teko", "Teko-Regular.ttf", 20);

        ContentManager.Instance.LoadFont("ubuntu24","fonts/Ubuntu-R.ttf", 24);
        ContentManager.Instance.LoadFont("ubuntu84","fonts/Ubuntu-R.ttf", 84);
        ContentManager.Instance.LoadFont("ubuntu130","fonts/Ubuntu-R.ttf", 130);
        ContentManager.Instance.LoadFont("OhMaria64","fonts/OhMaria.ttf", 64);
        ContentManager.Instance.LoadFont("OhMaria84","fonts/OhMaria.ttf", 114);
        ContentManager.Instance.LoadTexture("PixelGrass", "textures/grass.png");
        ContentManager.Instance.LoadTexture("PixelGrassEnd", "textures/grassEnd.png");
        ContentManager.Instance.LoadTexture("minigameboarder","textures/miniGameBoarder.png");
        ContentManager.Instance.LoadTexture("SheepPixel", "textures/SpriteSheets/sheep.png");
        ContentManager.Instance.LoadTexture("SheepPixelRain", "textures/SpriteSheets/rainsheep.png");
        ContentManager.Instance.LoadTexture("SheepFence", "textures/fence.png");
        ContentManager.Instance.LoadTexture("PoliceCar", "textures/policecarpng.png");
        ContentManager.Instance.LoadTexture("Car", "textures/car.png");
        ContentManager.Instance.LoadTexture("roadMinigame", "textures/redRoad.png");
        ContentManager.Instance.LoadTexture("pipeTurnEmp", "textures/pipeturnREmpty.png");
        ContentManager.Instance.LoadTexture("pipeTurnFull", "textures/pipeturnRFull.png");
        ContentManager.Instance.LoadTexture("pipeCrossFull", "textures/pipecrossfulll.png");
        ContentManager.Instance.LoadTexture("pipeCrossEmp", "textures/pipecrossempty.png");
        ContentManager.Instance.LoadTexture("pipeStraightEmp", "textures/pipestraightemppng.png");
        ContentManager.Instance.LoadTexture("pipeStraightFull", "textures/pipestraightfull.png");
        ContentManager.Instance.LoadTexture("pipeArrow", "textures/PipeArrow.png");
        ContentManager.Instance.LoadSound("sheepJump", "sounds/sheepJump.wav");
        ContentManager.Instance.LoadSound("pipe1", "sounds/PipeTurn1.ogg");
        ContentManager.Instance.LoadSound("pipe2", "sounds/PipeTurn2.ogg");
        ContentManager.Instance.LoadSound("getDot1", "sounds/Hit.ogg");
        ContentManager.Instance.LoadSound("correct", "sounds/Correct.ogg");
        ContentManager.Instance.LoadSound("wrong", "sounds/Wrong.wav");
        ContentManager.Instance.LoadSound("DIE_SHEEP", "sounds/DeadSheep.ogg");
        ContentManager.Instance.LoadMusic("GoneCountry", "music/Anttis instrumentals - Gone Country.mp3");
        ContentManager.Instance.LoadMusic("KotoIsHome", "music/Anttis instrumentals - Koto is home in Finnish.mp3");
        ContentManager.Instance.LoadMusic("MorningSong", "music/Anttis instrumentals - Morning Song.mp3");
        ContentManager.Instance.LoadMusic("PianoMan", "music/Anttis instrumentals - Pianoman cometh.mp3");
        ContentManager.Instance.LoadMusic("Refugee", "music/Anttis instrumentals - Refugee with PHD.mp3");
        songs = new Music[5];
        songs[0] = ContentManager.Instance.GetMusic("GoneCountry");
        songs[1] = ContentManager.Instance.GetMusic("KotoIsHome");
        songs[2] = ContentManager.Instance.GetMusic("MorningSong");
        songs[3] = ContentManager.Instance.GetMusic("PianoMan");
        songs[4] = ContentManager.Instance.GetMusic("Refugee");
        songPlay = MathUtils.random(0,4);
        songs[songPlay].setVolume(0.2f);
        songs[songPlay].setLooping(false);
        songs[songPlay].play();
    }
}
