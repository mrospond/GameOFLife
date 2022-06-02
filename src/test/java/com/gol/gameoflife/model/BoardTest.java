package com.gol.gameoflife.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    Board board1 = new Board(3,3);
    Board board2 = new Board(25,25);
    Board board3 = new Board();
    Board board4 = new Board(13, 31);

    @Test
    void getWidth() {
        assertEquals(3, board1.getWidth());
        assertEquals(25, board2.getWidth());
        assertEquals(10, board3.getWidth());
        assertEquals(13, board4.getWidth());
    }

    @Test
    void getHeight() {
        assertEquals(3, board1.getHeight());
        assertEquals(25, board2.getHeight());
        assertEquals(10, board3.getHeight());
        assertEquals(31, board4.getHeight());
    }

    @Test
    void setCellState() {
        assertEquals(false, board1.getCell(1,1).isAlive());
        board1.setCellState(1,1,true);
        board2.setCellState(3,5,true);
        assertEquals(true, board1.getCell(1,1).isAlive());
        assertEquals(true, board2.getCell(3,5).isAlive());
        assertEquals(false, board2.getCell(13,3).isAlive());
    }
}