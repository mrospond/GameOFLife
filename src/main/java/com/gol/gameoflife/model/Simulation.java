/*
    Klasa całej symulacji. Zawiera planszę oraz zasady gry.
 */

package com.gol.gameoflife.model;


public class Simulation {
    private Board board;

    public Simulation(int height, int width){
        this.board = new Board(height, width);
    }

    public Simulation(){
        this(10,10);
    }


    // Definiuje następny stan komórki w zależności od stanów jej sąsiadów
    public boolean nextCellState(boolean initialState, int aliveNeighbours){
        if(initialState && aliveNeighbours < 2) return false;
        if(!initialState && aliveNeighbours == 3) return true;
        if(initialState && aliveNeighbours > 3) return false;
        else return initialState;
    }

    public Board getBoard(){
        return board;
    }

    // Definiuje następny stan całej planszy
    public void nextStep(){
        Board afterStep = new Board(board.getWidth(), board.getHeight());

        for(int w = 0; w < board.getWidth(); w++){

            for(int h = 0; h < board.getHeight(); h++){

                //int neighbourCount = countAliveNeighbours(w, h);
                int neighbourCount = countAliveNeighboursInfinite(w,h);
                boolean nextState = nextCellState(board.getCell(w,h).isAlive(), neighbourCount);
                afterStep.getCell(w,h).changeState(nextState);
            }
        }

        this.board = afterStep;
    }

    // Zwraca liczbę żywych sąsiadów
    public int countAliveNeighbours(int row, int col){
        int count = 0;

        count += getNeighbourState(row - 1, col - 1);
        count += getNeighbourState(row, col - 1);
        count += getNeighbourState(row + 1, col - 1);

        count += getNeighbourState(row - 1, col);
        count += getNeighbourState(row + 1, col);

        count += getNeighbourState(row - 1, col + 1);
        count += getNeighbourState(row, col + 1);
        count += getNeighbourState(row + 1, col + 1);

        return count;
    }

    public int countAliveNeighboursInfinite(int row, int col){
        int count = 0;
        for(int r = -1; r < 2; r++){
            for(int c = -1; c < 2; c++){
                if(!(c==0 && r ==0)){
                    int neighbourRow = (row + r + board.getWidth()) % board.getWidth();
                    int neighbourCol = (col + c + board.getWidth()) % board.getWidth();
                    if (board.getCell(neighbourRow, neighbourCol).isAlive()) count++;
                }
            }
        }
        if(count == 3) System.out.println("Row " +  row + " COl " +  col);
        return count;
    }


    // Zwraca stan sąsiada, jeżeli z pól row i column wynika, że sąsiad znajdowałby się poza planszą zwraca 0 (martwy)
    public int getNeighbourState(int row, int col){
        if (row < 0 || row >= board.getWidth()) {
            return 0;
        }

        if (col < 0 || col >= board.getHeight()) {
            return 0;
        }

        if(this.board.getCell(row,col).isAlive()){
            return 1;
        }
        else return 0;
    }


}
