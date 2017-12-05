package com.pixeldot.ld40.Util;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import java.util.Collection;
import java.util.HashMap;

public class ContentManager {

    private HashMap<String, Texture> textures;
    private HashMap<String, BitmapFont> fonts;
    private HashMap<String, Sound> sounds;
    private HashMap<String, Music> music;

    public static final ContentManager Instance = new ContentManager();

    private ContentManager() {
        textures = new HashMap<String, Texture>();
        fonts = new HashMap<String, BitmapFont>();
        sounds = new HashMap<String, Sound>();
        music = new HashMap<String, Music>();
    }
    public boolean CheckTexture(String name) {
        return textures.containsKey(name);
    }

    public Texture GetTexture(String name) {
        if(!textures.containsKey(name))
            throw new IllegalArgumentException("Error: A Texture with the name \"" + name + "\" was not loaded.");

        return textures.get(name);
    }

    public BitmapFont GetFont(String name) {
        if(!fonts.containsKey(name))
            throw new IllegalArgumentException("Error: A Font with the name \"" + name + "\" was not loaded.");

        return fonts.get(name);
    }

    public Sound GetSound(String name) {
        if(!sounds.containsKey(name))
            throw new IllegalArgumentException("Error: A Sound with the name \"" + name + "\" was not loaded.");

        return sounds.get(name);
    }

    public Music GetMusic(String name) {
        if(!music.containsKey(name))
            throw new IllegalArgumentException("Error: Music with the name \"" + name + "\" was not loaded.");

        return music.get(name);
    }


    public Texture LoadTexture(String name, String path) {
        if(textures.containsKey(name)) {
            System.err.println("Warning: A texture with the name \"" + name + "\" was already loaded, not overwriting.");
            return textures.get(name);
        }

        Texture texture = new Texture("Textures/" + path);
        textures.put(name, texture);

        return texture;
    }

    public BitmapFont LoadFont(String name, String path, int size) {
        if(fonts.containsKey(name)) {
            System.err.println("Warning: A font with the name \"" + name + "\" was already loaded, not overwriting.");
            return fonts.get(name);
        }

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/" + path));

        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.flip = true;
        param.size = size;
        param.hinting = FreeTypeFontGenerator.Hinting.Full;

        BitmapFont font = generator.generateFont(param);

        fonts.put(name, font);

        generator.dispose();

        return font;
    }

    public Sound LoadSound(String name, String path) {
        if(sounds.containsKey(name)) {
            System.err.println("Warning: A sound with the name \"" + name + "\" was already loaded, not overwriting.");
            return sounds.get(name);
        }

        Sound sound = Gdx.audio.newSound(Gdx.files.internal("Sounds/" + path));
        sounds.put(name, sound);

        return sound;
    }

    public Music LoadMusic(String name, String path) {
        if(sounds.containsKey(name)) {
            System.err.println("Warning: Music with the name \"" + name + "\" was already loaded, not overwriting.");
            return music.get(name);
        }

        Music m = Gdx.audio.newMusic(Gdx.files.internal("Music/" + path));
        music.put(name, m);

        return m;
    }


    public void RemoveTexture(String name) {
        if(!textures.containsKey(name))
            throw new IllegalArgumentException("Error: A Texture with the name \"" + name + "\" was not loaded.");

        Texture texture = textures.get(name);
        texture.dispose();
    }

    public void RemoveFont(String name) {
        if(!fonts.containsKey(name))
            throw new IllegalArgumentException("Error: A Font with the name \"" + name + "\" was not loaded.");

        BitmapFont font = fonts.get(name);
        font.dispose();
    }

    public void RemoveSound(String name) {
        if(!sounds.containsKey(name))
            throw new IllegalArgumentException("Error: A Sound with the name \"" + name + "\" was not loaded.");

        Sound sound = sounds.get(name);
        sound.dispose();
    }

    public void RemoveMusic(String name) {
        if(!music.containsKey(name))
            throw new IllegalArgumentException("Error: Music with the name \"" + name + "\" was not loaded.");

        Music m =  music.get(name);
        m.dispose();
    }

    public void Dispose() {
        for(Texture t : textures.values()) {
            t.dispose();
        }
        textures.clear();

        for(BitmapFont f : fonts.values()) {
            f.dispose();
        }
        fonts.clear();

        for(Sound s : sounds.values()) {
            s.dispose();
        }
        sounds.clear();

        for(Music m : music.values()) {
            m.dispose();
        }
        music.clear();
    }
}
