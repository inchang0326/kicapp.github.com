package com.example.me.codinggame;

public class MovingTaskParams {
    private Arrow m_arrow;
    private int m_dir;
    private int m_repeatNum;

    public MovingTaskParams() {
    }

    public MovingTaskParams(Arrow arrow, int dir) {
        this(arrow, dir, 1);
    }

    public MovingTaskParams(Arrow arrow, int dir, int repeatNum) {
        this.m_arrow = arrow;
        this.m_dir = dir;
        this.m_repeatNum = repeatNum;
    }

    public Arrow getArrow() {
        return this.m_arrow;
    }

    public void setArrow(Arrow arrow) {
        this.m_arrow = arrow;
    }

    public int getDir() {
        return this.m_dir;
    }

    public void setDir(int dir) {
        this.m_dir = dir;
    }

    public int getRepeatNum() {
        return this.m_repeatNum;
    }

    public void setRepeatNum(int repeatNum) {
        this.m_repeatNum = repeatNum;
    }
}
