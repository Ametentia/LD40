package com.pixeldot.ld40.Entities.Tiles;

public class TileParam {
    public float powerIntake;
    public float powerOutput;

    public float waterIntake;
    public float waterOutput;

    public float pollution;

    public int population;

    public float moneyIntake;
    public float moneyOutput;

    public TileParam(TileType type) {
        powerIntake = type.powerIntake;
        powerOutput = type.powerOutput;

        waterIntake = type.waterIntake;
        waterOutput = type.waterOutput;

        pollution = type.pollution;

        moneyIntake = type.moneyIntake;
        moneyOutput = type.moneyOutput;
    }

    public TileParam() {}
}
