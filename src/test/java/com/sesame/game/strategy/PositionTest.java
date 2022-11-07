package com.sesame.game.strategy;

import java.util.ArrayList;
import java.util.Collections;

import com.sesame.game.strategy.model.Position;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author mike
 * @date 2022/10/22
 */
public class PositionTest {

    @Test
    public void testCompare() {

        ArrayList<Position> positions = new ArrayList<>();
        positions.add(new Position(0, 1));
        positions.add(new Position(0, 0));
        Collections.sort(positions);
        Assert.assertEquals(new Position(0, 0), positions.get(0));

        positions = new ArrayList<>();
        positions.add(new Position(1, 1));
        positions.add(new Position(1, 7));
        Collections.sort(positions);
        Assert.assertEquals(new Position(1, 1), positions.get(0));

    }
}