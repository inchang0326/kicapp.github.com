package com.example.me.myapplication;

public class Point {
    private int m_currR;
    private int m_currC;
    private int m_prevR;
    private int m_prevC;
    private char m_alpha;

    public Point() {}

    public Point(char alpha, int currR, int currC, int prevR, int prevC) {
        m_alpha = alpha;
        m_currR = currR;
        m_currC = currC;
        m_prevR = prevR;
        m_prevC = prevC;
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

    public int getPrevRow() {
        return m_prevR;
    }

    public void setPrevRow(int prevR) {
        m_prevR = prevR;
    }

    public int getPrevCol() {
        return m_prevC;
    }

    public void setPrevCol(int prevC) {
        m_prevC = prevC;
    }

    public char getAlpha() {
        return m_alpha;
    }

    public void setAlpha(char alpha) {
        this.m_alpha = alpha;
    }
}
