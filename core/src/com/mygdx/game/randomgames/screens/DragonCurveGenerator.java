package com.mygdx.game.randomgames.screens;

import com.badlogic.gdx.math.Vector2;

import java.util.Iterator;
import java.util.LinkedList;

import static com.mygdx.game.randomgames.screens.DragonCurveGenerator.Direction.*;

public class DragonCurveGenerator {

    enum Direction {
        LEFT,
        RIGHT;

        public static Vector2 turn(Vector2 heading, Direction turn){
            Vector2 newHeading = new Vector2();
            switch (turn){
                case LEFT:
                    newHeading.x = -heading.y;
                    newHeading.y = heading.x;
                    break;
                case RIGHT:
                    newHeading.x = heading.y;
                    newHeading.y = -heading.x;
            }
            return newHeading;
        }
    }

    static LinkedList<Direction> dragonTurns(int recursions) {
        LinkedList<Direction> turns = new LinkedList<Direction>();
        turns.add(LEFT);

        for (int i = 0; i < recursions; i++){
            // TODO: Create a reversed copy of turns
            LinkedList<Direction> cpyTurns = new LinkedList<Direction>();
            Iterator it = turns.descendingIterator();
            while(it.hasNext())
                cpyTurns.add((Direction) it.next());

            // TODO: Add a left turn to turns
            turns.add(LEFT);

            // TODO: Add reflected version of reversed to turns
            for(Direction dir : cpyTurns) {
                try {
                    turns.add(reflect(dir));
                } catch(Exception e) {
                    System.out.println("impossibruuu the direction provided not exists");
                    e.printStackTrace();
                }
            }

        }
        System.out.print(turns);
        return turns;
    }

    static Direction reflect(Direction dir) throws Exception {
        switch (dir) {
            case LEFT:
                return RIGHT;
            case RIGHT:
                return LEFT;
            default:
                throw new Exception();
        }
    }

    public static float[] generateDragonCurve(int width, int height, int recursions){

        LinkedList<Direction> turns = DragonCurveGenerator.dragonTurns(recursions);

        Vector2 head = new Vector2(width/2, height/2);
        Vector2 heading = new Vector2(5, 0);

        float[] curve = new float[(turns.size() + 1) * 2];

        int i = 0;
        curve[i++] = head.x;
        curve[i++] = head.y;

        //TODO: Convert the list of turns into the actual path
        for(Direction dir : turns) {
            Vector2 vect = Direction.turn(heading, dir);
            curve[i++] = vect.x;
            curve[i++] = vect.y;
        }


        return curve;

    }


}
