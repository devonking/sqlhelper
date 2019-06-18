package com.devonking.sql.pie.core;

/**
 * description in here
 *
 * @author Devon King
 * @since 1.0
 */
public class Page {

    private int num;

    private int size;

    public Page(int num, int size) {
        this.num = num;
        this.size = size;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
