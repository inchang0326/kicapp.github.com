package com.wjthinkbig.a10010952.codinggame;

import android.util.Log;

import java.util.ArrayDeque;
import java.util.Queue;

public class MovingTaskParams {
    private ArrayDeque<Arrow> m_queue;
    private int m_size;

    public MovingTaskParams() {
    }

    public MovingTaskParams(ArrayDeque<Arrow> queue, int size) {
        m_queue = queue;
        m_size = size;
    }

    public Queue getQueue() {
        return m_queue;
    }

    public void setQueue(ArrayDeque<Arrow> queue) {
        m_queue = queue;
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
