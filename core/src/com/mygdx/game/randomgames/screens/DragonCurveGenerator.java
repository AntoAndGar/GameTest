package com.mygdx.game.randomgames.screens;

import static com.mygdx.game.randomgames.screens.DragonCurveGenerator.Direction.LEFT;
import static com.mygdx.game.randomgames.screens.DragonCurveGenerator.Direction.RIGHT;

import java.util.Iterator;
import java.util.LinkedList;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.randomgames.customexceptions.DragonGeneratorException;

public class DragonCurveGenerator {

    enum Direction {
        LEFT,
        RIGHT;

        public static Vector2 turn(Vector2 heading, Direction turn) throws DragonGeneratorException{
            Vector2 newHeading = new Vector2();
            switch (turn){
                case LEFT:
                    newHeading.x = -heading.y;
                    newHeading.y = heading.x;
                    break;
                case RIGHT:
                    newHeading.x = heading.y;
                    newHeading.y = -heading.x;
                    break;
                default:
                	throw new DragonGeneratorException();
            }
            return newHeading;
        }
    }

    private static LinkedList<Direction> dragonTurns(int recursions) {
        LinkedList<Direction> turns = new LinkedList<Direction>();
        turns.add(LEFT);

        for (int i = 0; i < recursions; i++){
            // TODO: Create a reversed copy of turns
            LinkedList<Direction> cpyTurns = new LinkedList<Direction>();
            Iterator<Direction> it = turns.descendingIterator();
            while(it.hasNext())
                cpyTurns.add((Direction) it.next());

            turns.add(LEFT);

            for(Direction dir : cpyTurns) {
                try {
                    turns.add(reflect(dir));
                } catch(DragonGeneratorException e) {
                    System.out.println("impossibruuu the direction provided not exists");
                    e.printStackTrace();
                }
            }

        }
        return turns;
    }

    private static Direction reflect(Direction dir) throws DragonGeneratorException {
        switch (dir) {
            case LEFT:
                return RIGHT;
            case RIGHT:
                return LEFT;
            default:
                throw new DragonGeneratorException();
        }
    }

    public static float[] generateDragonCurve(int width, int height, int recursions) throws DragonGeneratorException{

        LinkedList<Direction> turns = DragonCurveGenerator.dragonTurns(recursions);
        System.out.println(turns);

        Vector2 head = new Vector2(width/2, height/2);
        Vector2 heading = new Vector2(10, 0);

        float[] curve = new float[(turns.size() + 1) * 2];

        int i = 0;
        curve[i++] = head.x;
        curve[i++] = head.y;

        for (Direction turn : turns){
            heading = Direction.turn(heading, turn);
            head.x += heading.x;
            head.y += heading.y;
            curve[i++] = head.x;
            curve[i++] = head.y;
        }


        return curve;

    }


}
