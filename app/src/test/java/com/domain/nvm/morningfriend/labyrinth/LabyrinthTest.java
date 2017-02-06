package com.domain.nvm.morningfriend.labyrinth;

import com.domain.nvm.morningfriend.features.puzzle.labyrinth.data.Labyrinth;
import com.domain.nvm.morningfriend.features.puzzle.labyrinth.data.Labyrinth.Direction;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.List;


public class LabyrinthTest {

    private Labyrinth l;

    @Before
    public void setUp() {
        l = new Labyrinth(4);
    }

    @Test
    public void getNextTileTopRow() {
        assertEquals(Labyrinth.INVALID_TILE, l.getNextTile(1, Direction.UP));
    }

    @Test
    public void getNextTileBottomRow() {
        assertEquals(Labyrinth.INVALID_TILE, l.getNextTile(13, Direction.DOWN));
    }

    @Test
    public void getNextTileLeftCol() {
        assertEquals(Labyrinth.INVALID_TILE, l.getNextTile(4, Direction.LEFT));
    }

    @Test
    public void getNextTileRightCol() {
        assertEquals(Labyrinth.INVALID_TILE, l.getNextTile(7, Direction.RIGHT));
    }

    @Test
    public void getNextTileMiddle() {
        assertEquals(6, l.getNextTile(5, Direction.RIGHT));
        assertEquals(4, l.getNextTile(5, Direction.LEFT));
        assertEquals(1, l.getNextTile(5, Direction.UP));
        assertEquals(9, l.getNextTile(5, Direction.DOWN));
    }

    @Test
    public void getNeighborsLeftTopTest() {
        List<Integer> neighbors = l.getNeighborTiles(0);
        assertTrue(neighbors.contains(1));
        assertTrue(neighbors.contains(4));
        assertEquals(2, neighbors.size());
    }

    @Test
    public void getNeighborsRightTopTest() {
        List<Integer> neighbors = l.getNeighborTiles(3);
        assertTrue(neighbors.contains(2));
        assertTrue(neighbors.contains(7));
        assertEquals(2, neighbors.size());
    }

    @Test
    public void getNeighborsRightBottomTest() {
        List<Integer> neighbors = l.getNeighborTiles(15);
        assertTrue(neighbors.contains(14));
        assertTrue(neighbors.contains(11));
        assertEquals(2, neighbors.size());
    }

    @Test
    public void getNeighborsLeftBottomTest() {
        List<Integer> neighbors = l.getNeighborTiles(12);
        assertTrue(neighbors.contains(13));
        assertTrue(neighbors.contains(8));
        assertEquals(2, neighbors.size());
    }

    @Test
    public void getNeighborsLeftColTest() {
        List<Integer> neighbors = l.getNeighborTiles(4);
        assertTrue(neighbors.contains(0));
        assertTrue(neighbors.contains(5));
        assertTrue(neighbors.contains(8));
        assertEquals(3, neighbors.size());
    }

    @Test
    public void getNeighborsRightColTest() {
        List<Integer> neighbors = l.getNeighborTiles(7);
        assertTrue(neighbors.contains(3));
        assertTrue(neighbors.contains(6));
        assertTrue(neighbors.contains(11));
        assertEquals(3, neighbors.size());
    }

    @Test
    public void getNeighborsTopRowTest() {
        List<Integer> neighbors = l.getNeighborTiles(1);
        assertTrue(neighbors.contains(0));
        assertTrue(neighbors.contains(2));
        assertTrue(neighbors.contains(5));
        assertEquals(3, neighbors.size());
    }

    @Test
    public void getNeighborsBottomRowTest() {
        List<Integer> neighbors = l.getNeighborTiles(13);
        assertTrue(neighbors.contains(12));
        assertTrue(neighbors.contains(9));
        assertTrue(neighbors.contains(14));
        assertEquals(3, neighbors.size());
    }

    @Test
    public void getNeighborsMiddleTest() {
        List<Integer> neighbors = l.getNeighborTiles(5);
        assertTrue(neighbors.contains(1));
        assertTrue(neighbors.contains(4));
        assertTrue(neighbors.contains(6));
        assertTrue(neighbors.contains(9));
        assertEquals(4, neighbors.size());
    }
}
