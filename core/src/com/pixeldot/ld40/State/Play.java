package com.pixeldot.ld40.State;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.MathUtils;
import com.pixeldot.ld40.MiniGames.ConnectTheDots;
import com.pixeldot.ld40.Util.ContentManager;
import com.pixeldot.ld40.Util.GameStateManager;
import com.pixeldot.ld40.Util.State;
import com.pixeldot.ld40.Util.StateType;

public class Play extends State {

    private boolean openState;
    private Music[] songs;
    private int songPlay;

    public Play(GameStateManager gsm) {
        super(gsm);
        loadContent();
        openState = false;

    }

    public void update(float dt) {
        if(!songs[songPlay].isPlaying()) {
            songPlay=(songPlay+1)%5;
            songs[songPlay].setVolume(0.4f);
            songs[songPlay].play();
        }
    }

    public void render() {

    }
    public void loadContent() {
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
