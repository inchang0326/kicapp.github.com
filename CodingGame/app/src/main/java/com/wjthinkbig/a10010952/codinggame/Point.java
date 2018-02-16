package com.wjthinkbig.a10010952.codinggame;

public class Point {
    private int m_currR;
    private int m_currC;

    public Point() {
    }

    public Point(int currR, int currC) {
        m_currR = currR;
        m_currC = currC;
    }

    public int getRow() {
        return m_currR;
    }

    public void setRow(int currR) {
        m_currR = currR;
    }

    public int getCol() {
        return m_currC;
    }

    public void setCol(int currC) {
        m_currC = currC;
    }
}
