package byow.Core;

public class Rectangle {
    int xSize;
    int ySize;
    int xPos; // the lower left point of our rectangle;
    int yPos;

    Rectangle(int m, int n) {
        this.xSize = m;
        this.ySize = n;
    }

    void setPos(int x, int y) {
        this.xPos = x;
        this.yPos = y;
    }

}
