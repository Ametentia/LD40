package com.pixeldot.ld40.Entities;

import com.pixeldot.ld40.Entities.Tiles.TileParam;

public class Player {

    private static final float PowerWeight = 10.4f;
    private static final float WaterWeight = 3.2f;
    private static final float PollutionWeight = 6.4f;

    private float money;
    private float income;
    private float outcome;

    // Good parameters (max these)
    private float excessPower;
    private float excessWater;

    // Bad parameters (min these)
    private float pollution;
    private int population;

    // Between 0 and 1
    private float satisfaction;

    private float accumulator;

    public Player() {
        money = 10000;
        outcome = income = 0;

        excessPower = 0;
        excessWater = 0;

        satisfaction = 1;

        population = 0;

        accumulator = 0;
    }

    public void update(float dt) {
        accumulator += dt;
        if(accumulator < 1) return;

        money += (income + excessPower + excessWater);
        money -= outcome;

        satisfaction = (money + (PowerWeight * excessPower) + (WaterWeight * excessWater))
                / (population + (PollutionWeight * pollution) + 1);
        satisfaction = 1 - (float) Math.exp(-satisfaction);


        accumulator = 0;
    }

    public void updateParams(TileParam params) {
        population = params.population;

        pollution = params.pollution;

        outcome = params.moneyIntake;
        income = params.moneyOutput;

        excessPower = params.powerOutput - params.powerIntake;
        excessWater = params.waterOutput - params.waterIntake;
    }

    public float getExcessPower() {
        return excessPower;
    }

    public float getExcessWater() {
        return excessWater;
    }

    public float getPollution() {
        return pollution;
    }

    public int getPopulation() {
        return population;
    }

    public float getSatisfaction() {
        return satisfaction;
    }

    public void setPollution(float pollution) {
        this.pollution = pollution;
    }

    public void setOutcome(float outcome) {
        this.outcome = outcome;
    }

    public void setIncome(float income) {
        this.income = income;
    }

    public void setExcessPower(float excessPower) {
        this.excessPower = excessPower;
    }

    public void setExcessWater(float excessWater) {
        this.excessWater = excessWater;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public boolean takeMoney(int amount) {
        if(money < amount) return false;

        money -= amount;
        return true;
    }

    public String toString() {
        return "City Stats: [\n" +
                "Money: " + money + "\n" +
                "Power: " + excessPower + "\n" +
                "Water: " + excessWater + "\n" +
                "Pollution: " + pollution + "\n" +
                "Satisfaction: " + satisfaction + "\n" +
                "Income: " + income + "\n" +
                "Outcome: " + outcome + "\n" +
                "Population: " + population + "\n" +
                "\n]";
    }
}
