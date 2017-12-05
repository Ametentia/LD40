package com.pixeldot.ld40.Util;

import com.pixeldot.ld40.Metro;
import com.pixeldot.ld40.MiniGames.*;
import com.pixeldot.ld40.State.Play;

import java.util.Stack;

public class GameStateManager {

    public final Metro metro;
    private Stack<State> states;

    public GameStateManager(Metro metro) {
        this.metro = metro;

        states = new Stack<State>();
    }

    private State GetState(StateType type) {
        switch (type) {
            case Play:
                return new Play(this);
            case MINIGAME_CONNECTDOTS:
                return new ConnectTheDots(this, states.peek());
            case MINIGAME_SCHOOL:
                return new SchoolQuiz(this, states.peek());
            case MINIGAME_HOSPITALHELP:
                return new HospitalHelp(this, states.peek());
            case MINIGAME_POLICE:
                return new PoliceChase(this, states.peek());
            case MINIGAME_SHEEPJUMP:
                return new Sheep(this, states.peek());
            case MINIGAME_PIPEGAME:
                return new PipeGame(this, states.peek());
            // TODO(Pixel): Implement
            case Menu:
            case EndScreen:
            default:
                throw new IllegalArgumentException("Error: This is implemented yet!");
        }
    }

    public void AddState(StateType type) {
        states.push(GetState(type));
    }

    public void RemoveState() {
        State s = states.pop();
        if(s != null) s.dispose();
    }

    public void SetState(StateType type) {
        RemoveState();
        AddState(type);
    }

    public void Update(float dt) { states.peek().update(dt); }
    public void Render() { states.peek().render(); }
}
