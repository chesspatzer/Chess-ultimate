package com.company;

/**
 * Direction enum, used to map out directions
 */
public enum Direction {
    NORTH(0,1), SOUTH(0,-1), EAST(1,0), WEST(-1,0),
    NORTH_EAST(1,1), NORTH_WEST(-1,1), SOUTH_EAST(-1,-1), SOUTH_WEST(1,-1);

    final int x; //x coordinate
    final int y; //y coordinate

    /**
     * Enum constructor that constructs the enum based on the x and y coordinates
     * @param x
     * @param y
     */
    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * getter, gets the Y position
     * @return Y position offset
     */
    public int getY() { return y; }

    /**
     * getter, gets the X position
     * @return X position offset
     */
    public int getX(){
        return x;
    }
}
