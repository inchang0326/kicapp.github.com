package com.example.me.thinkbig;

public class Point {
    private int m_currR;
    private int m_currC;
    private char m_alpha;

    public Point() {}

    public Point(char alpha, int currR, int currC) {
        m_alpha = alpha;
        m_currR = currR;
        m_currC = currC;
    }

    public int getCurrRow() {
        return m_currR;
    }

    public void setCurrRow(int currR) {
        m_currR = currR;
    }

    public int getCurrCol() {
        return m_currC;
    }

    public void setCurrCol(int currC) {
        m_currC = currC;
    }

    public char getAlpha() {
        return m_alpha;
    }

    public void setAlpha(char alpha) {
        this.m_alpha = alpha;
    }
}
