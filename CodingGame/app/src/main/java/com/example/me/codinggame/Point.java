package com.example.me.codinggame;

public class Point {
    private int m_row;
    private int m_col;
    private char m_alpha;

    public Point() {}

    public Point(char alpha) {
        this(alpha, 0, 0);
    }

    public Point(char alpha, int row, int col) {
        this.m_alpha = alpha;
        this.m_row = row;
        this.m_col = col;
    }

    public int getRow() {
        return m_row;
    }

    public void setRow(int row) {
        this.m_row = row;
    }

    public int getCol() {
        return m_col;
    }

    public void setCol(int col) {
        this.m_col = col;
    }

    public char getAlpha() {
        return m_alpha;
    }

    public void setAlpha(char alpha) {
        this.m_alpha = alpha;
    }
}
