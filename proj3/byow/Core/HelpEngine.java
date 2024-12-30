package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.*;

public class HelpEngine {

    static Random rd;

    static int canvasX;
    static int canvasY;
    static TETile[][] curWorld;
    private static List<Rectangle> recList;
    private static boolean[][] occuMatrix; // this helps to check a certain Rec should be added to recList;



    static void setSeed(long seed) {
        rd = new Random(seed);
    }

    static void setCanvas(int minCanvasSize, int maxCanvasSize, int margin) {
        if (rd == null) {
            throw new IllegalStateException("Random object not initialized. Call setSeed first.");
        }



        canvasX = rd.nextInt(maxCanvasSize - minCanvasSize) + minCanvasSize;
        canvasY = rd.nextInt(maxCanvasSize - minCanvasSize) + minCanvasSize;
        curWorld = new TETile[canvasX][canvasY];
        for (int x = 0; x < canvasX; x += 1) {
            for (int y = 0; y < canvasY; y += 1) {
                curWorld[x][y] = Tileset.NOTHING;
            }
        }

        // initialize recList;
        recList = new ArrayList<>();



        // modify occuMatrix here so that margin area is marked as occupied;
        occuMatrix = new boolean[canvasX][canvasY];
        for (int x = 0; x < canvasX; x++) {
            for (int y = 0; y < canvasY; y++) {
                if (x < margin || x >= canvasX - margin || y < margin || y >= canvasY - margin) {
                    occuMatrix[x][y] = true; // Mark margin as occupied
                } else {
                    occuMatrix[x][y] = false;
                }
            }
        }

    }

    static void tryAddRec(int minRecSize, int maxRecSize) {


        // everytime we call this function, one Rec is obtained, and given no conflict, it will be added to the both OccuMtrix & recList;
        int xSize = rd.nextInt(maxRecSize - minRecSize) + minRecSize;
        int ySize = rd.nextInt(maxRecSize - minRecSize) + minRecSize;
        Rectangle rec = new Rectangle(xSize, ySize);
        int xPos = rd.nextInt(canvasX);
        int yPos = rd.nextInt(canvasY);
        rec.setPos(xPos, yPos);

        if (canBeAdded(rec)) {
            addRecToOM(rec);
            addRecToRL(rec);
            addRecToCW(rec);
        }

    }

    private static boolean canBeAdded(Rectangle rec) {
        for (int i = rec.xPos - 1; i <= rec.xPos + rec.xSize; i++) {
            for (int j = rec.yPos - 1; j <= rec.yPos + rec.ySize; j++) {
                if (i < 0 || i >= canvasX || j < 0 || j >= canvasY || occuMatrix[i][j]) {
                    return false;
                }
            }
        }
        return true;

    }



    private static void addRecToOM(Rectangle rec) {
        // add rec to occuMatrix;
        for (int i = rec.xPos - 1; i <= rec.xPos + rec.xSize; i++) {
            for (int j = rec.yPos - 1; j <= rec.yPos + rec.ySize; j++) {
                occuMatrix[i][j] = true;
            }
        }

    }

    private static void addRecToRL(Rectangle rec) {
        recList.add(rec);
    }

    private static void addRecToCW(Rectangle rec) {
        // add rec to curWorld;
        for (int i = rec.xPos; i <= rec.xPos + rec.xSize - 1; i++) {
            for (int j = rec.yPos; j <= rec.yPos + rec.ySize - 1; j++) {
                curWorld[i][j] = Tileset.FLOOR;
            }
        }
    }

    static void connectRooms() {
        // sort recList from left to right according to their position;
        recList.sort((r1, r2) -> Integer.compare(r1.xPos, r2.xPos));


        // then connect;
        for (int i = 0; i <= recList.size() - 2; i++) {
            Rectangle r1 = recList.get(i);
            Rectangle r2 = recList.get(i + 1);
            // now, connect them;
            for (int j = Math.min(r1.yPos, r2.yPos); j <= Math.max(r1.yPos, r2.yPos); j++) {
                curWorld[r1.xPos][j] = Tileset.FLOOR;
            }
            for (int k = r1.xPos; k <= r2.xPos; k++) {
                curWorld[k][r2.yPos] = Tileset.FLOOR;
            }
        }

    }


    static void buildWalls() {
        // build walls in 4 different directions;

        // left;
        for (int i = 0; i < curWorld.length - 1; i++) {
            for (int j = 0; j < curWorld[0].length; j++) {
                if (curWorld[i][j].equals(Tileset.NOTHING) && curWorld[i + 1][j].equals(Tileset.FLOOR)) {
                    curWorld[i][j] = Tileset.WALL;
                }
            }
        }

        // right;
        for (int i = 1; i < curWorld.length; i++) {
            for (int j = 0; j < curWorld[0].length; j++) {
                if (curWorld[i][j].equals(Tileset.NOTHING) && curWorld[i - 1][j].equals(Tileset.FLOOR)) {
                    curWorld[i][j] = Tileset.WALL;
                }
            }
        }

        // bottom;
        for (int i = 0; i < curWorld.length; i++) {
            for (int j = 0; j < curWorld[0].length - 1; j++) {
                if (curWorld[i][j].equals(Tileset.NOTHING) && curWorld[i][j + 1].equals(Tileset.FLOOR)) {
                    curWorld[i][j] = Tileset.WALL;
                }
            }
        }

        // up;
        for (int i = 0; i < curWorld.length; i++) {
            for (int j = 1; j < curWorld[0].length; j++) {
                if (curWorld[i][j].equals(Tileset.NOTHING) && curWorld[i][j - 1].equals(Tileset.FLOOR)) {
                    curWorld[i][j] = Tileset.WALL;
                }
            }
        }





    }





    static TETile[][] returnCurWorld() {
        return curWorld;
    }













}
