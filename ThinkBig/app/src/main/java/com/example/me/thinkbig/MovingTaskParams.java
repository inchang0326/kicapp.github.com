package com.example.me.thinkbig;

import android.util.Log;

import java.util.Queue;

public class MovingTaskParams {
    private Queue<Arrow> m_queue;
    private int m_dir;
    private int m_size;

    public MovingTaskParams() {
    }

    public MovingTaskParams(Queue<Arrow> queue, int size, int dir) {
        m_queue = queue;
        m_size = size;
        m_dir = dir;
    }

    public Queue getQueue() {
        return m_queue;
    }

    public void setQueue(Queue<Arrow> queue) {
        m_queue = queue;
    }

    public int getDir() {
        return m_dir;
    }

    public void setDir(int dir) {
        m_dir = dir;
    }

    // 널포인트익셉션 예외 처리
    public Arrow getArrow() {
        Arrow arrow = m_queue.poll();
        if (arrow != null) return arrow;
        else return Arrow.dummy;
    }

    public void setArrow(Arrow arrow) {
        m_queue.add(arrow);
    }

    public int getSize() {
        return m_size;
    }

    public void setSize(int size) {
        m_size = size;
    }
}