package com.gol.gameoflife.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CellTest {
    Board board = new Board();
    Cell cell = new Cell();

    @Test
    void isAlive() {
        assertEquals(false, board.getCell(2,3).isAlive());
        assertEquals(false, board.getCell(9,9).isAlive());
        board.getCell(2,3).changeState();
        assertEquals(true, board.getCell(2,3).isAlive());
    }

    @Test
    void changeState() {
        assertEquals(false, cell.isAlive());
        cell.changeState(true);
        assertEquals(true, cell.isAlive());
        assertEquals(false, board.getCell(2,3).isAlive());
        assertEquals(false, board.getCell(9,9).isAlive());
        board.getCell(2,3).changeState();
        board.getCell(9,9).changeState();
        assertEquals(true, board.getCell(2,3).isAlive());
        assertEquals(true, board.getCell(9,9).isAlive());
    }

    @Test
    void testChangeState() {
        assertEquals(false, cell.isAlive());
        cell.changeState();
        assertEquals(true, cell.isAlive());
        cell.changeState();
        assertEquals(false, cell.isAlive());
        assertEquals(false, board.getCell(6,9).isAlive());
        board.getCell(6,9).changeState();
        assertEquals(true, board.getCell(6,9).isAlive());
        board.getCell(6,9).changeState();
        assertEquals(false,board.getCell(6,9).isAlive());

    }
}