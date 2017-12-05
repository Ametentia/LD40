package com.pixeldot.ld40.Util;

import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class Vars {

    public static final Random Random = new Random();

    public static Vector2 PosToIso(float x, float y) {
        return new Vector2(x - y, (x + y) / 2f);
    }

}
