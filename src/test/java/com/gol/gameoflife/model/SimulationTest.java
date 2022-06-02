package com.gol.gameoflife.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimulationTest {

    Simulation s = new Simulation();

    @Test
    void nextCellState() {
        assertEquals(true, s.nextCellState(true, 3));
        assertEquals(true, s.nextCellState(false, 3));
        assertEquals(false, s.nextCellState(true, 4));
        assertEquals(false, s.nextCellState(false, 4));
        assertEquals(false, s.nextCellState(true, 1));
        assertEquals(false, s.nextCellState(false, 1));
        assertEquals(true, s.nextCellState(true, 2));
        assertEquals(false, s.nextCellState(false, 2));
        assertEquals(false, s.nextCellState(true, 8));
        assertEquals(false, s.nextCellState(false, 8));
        assertEquals(false, s.nextCellState(true, 0));
    }

    @Test
    void nextStep() {
        Simulation sim = new Simulation();
        sim.getBoard().getCell(5,6).changeState();
        sim.getBoard().getCell(5,7).changeState();
        sim.getBoard().getCell(5,8).changeState();
//        sim.getBoard().printBoard();
        s.getBoard().getCell(4,7).changeState();
        s.getBoard().getCell(5,7).changeState();
        s.getBoard().getCell(6,7).changeState();
        sim.nextStep();
        assertEquals(s.getBoard().getCell(4,7).isAlive(), sim.getBoard().getCell(4,7).isAlive());
        assertEquals(s.getBoard().getCell(5,7).isAlive(), sim.getBoard().getCell(5,7).isAlive());
        assertEquals(s.getBoard().getCell(6,7).isAlive(), sim.getBoard().getCell(6,7).isAlive());
        assertEquals(s.getBoard().getCell(7,7).isAlive(), sim.getBoard().getCell(7,7).isAlive());

    }

    @Test
    void countAliveNeighbours() {
        assertEquals(0, s.countAliveNeighbours(3,3));
        assertEquals(0, s.countAliveNeighbours(9,7));
//        s.getBoard().printBoard();
        s.getBoard().getCell(4,7).changeState();
        s.getBoard().getCell(5,7).changeState();
        s.getBoard().getCell(6,7).changeState();
        assertEquals(2,s.countAliveNeighbours(5,7));
    }

    @Test
    void getNeighbourState() {
        assertEquals(0, s.getNeighbourState(0,0));
        s.getBoard().getCell(4,7).changeState();
        s.getBoard().getCell(5,7).changeState();
        s.getBoard().getCell(0,0).changeState();
        assertEquals(1, s.getNeighbourState(4,7));
        assertEquals(1, s.getNeighbourState(5,7));
        assertEquals(1, s.getNeighbourState(0,0));
        assertEquals(0, s.getNeighbourState(0,11));
        assertEquals(0, s.getNeighbourState(15,71));
    }
}