import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Floor {

    private int numOfRooms;
    private Room[][] roomGrid;
    private int spawnRow, spawnColumn;

    public Floor(int numOfRoomsMin, int numOfRoomsMax) throws FloorInitializationException {
        // every floor needs to have at least three rooms: a starting room, a midboss room, and an exit room
        if (numOfRoomsMin < 3) {
            throw new FloorInitializationException();
        }

        //TODO: unseed this
        Random rng = new Random();

        this.numOfRooms = 0;
        while (this.numOfRooms < numOfRoomsMin) {
            this.numOfRooms = rng.nextInt(numOfRoomsMax + 1);
        }

        roomGrid = new Room[numOfRooms][numOfRooms];

        spawnRow = roomGrid.length / 2;
        spawnColumn = roomGrid[0].length / 2;

        boolean floorGenerated = false;
        while (!floorGenerated) {
            System.out.println("floor generating...");
            //FIXME: fix this
            //floorGenerated = generateFloor();
            break;
        }
    }

    //TODO: debug to show what the floor looks like
    public void printFloor() {
        String[] toPrint = new String[roomGrid.length];
        for (int row = 0; row < roomGrid.length; row++) {
            StringBuilder sb = new StringBuilder();
            for (int col = 0; col < roomGrid[0].length; col++) {
                if (roomGrid[row][col] instanceof SimpleRoom) {
                    sb.append('O');
                } else {
                    sb.append(' ');
                }
            }
            toPrint[row] = sb.toString();
        }
        for (String i : toPrint) {
            System.out.println(i);
        }
    }

    /*
    private boolean generateFloor() {

        int roomSlotsCleared = 0;
        int[][] layoutGrid = new int[roomGrid.length][roomGrid[0].length];

        // set two crawlers to make slots where rooms will be
        //TODO: optimize this a bit more, make a method to use a given number of crawlers at once
        //TODO: on a similar note, make a class to manage the crawlers. this will be neccessary 
        //TODO: starts with two crawlers, but it's possible, however unlikely, that they'll run out of moves before the floor finishes
        //TODO: should this happen, spawn another crawler somewhere at the edge of the generated rooms
        //TODO: keep doing that until all rooms are generated
        RoomMakingCrawler crawler1 = new RoomMakingCrawler(spawnRow, spawnColumn, numOfRooms, layoutGrid);
        RoomMakingCrawler crawler2 = new RoomMakingCrawler(spawnRow, spawnColumn, numOfRooms, layoutGrid);
        while (roomSlotsCleared < numOfRooms) {
            boolean roomCreated = crawler1.attemptMove();
            if (roomCreated) {
                roomSlotsCleared++;
                if (roomSlotsCleared >= numOfRooms) {
                    break;
                }
            }
        }
        crawler1 = null;
        crawler2 = null; // might as well kill these now, not ever gonna be used again

        //TODO: room generation is alright, but crawlers like to double back on themselves a lot
        //TODO: redesign idea: keep track of the location of crawlers with -1 instead of 2
        //TODO: mark currently existing rooms with a number above 1, let's say 10
        //TODO: for every iteration of the crawlers' movement, decrement any rooms with numbers above 1 by one
        //TODO: these higher numbers are used by crawlers to avoid turning back on themselves too often
        //TODO: the crawlers, after generating a movement direction, check if the place they're moving to has a high number, which i'll start calling weight
        //TODO: they'll run an RNG check for if they really want to commit to turning back on themselves.
        //TODO: the higher the weight, the less likely they are to move there

        for (int row = 0; row < layoutGrid.length; row++) {
            for (int col = 0; col < layoutGrid[0].length; col++) {
                if (layoutGrid[row][col] > 0) {
                    //TODO: change this to a specific unfinishedRoom object
                    roomGrid[row][col] = new SimpleRoom();
                }
            }
        }

        return true;
    }
     */


    public int getSpawnRow() {
        return spawnRow;
    }

    public int getSpawnColumn() {
        return spawnColumn;
    }

    //TODO: this
    private int getNumAdjacentRooms(int row, int col) {
        return 4;
    }

    public static void main(String[] args) {
        RoomCrawlerController controller = new RoomCrawlerController(20, 2, 50);
        controller.attemptFloorCandidateGeneration();
        RoomCandidate[][] grud = controller.getCandidateGrid();
        for (RoomCandidate[] row : grud) {
            for (RoomCandidate can : row) {
                System.out.print( (can == null) ? " " : "0" );
            }
            System.out.println();
        }
    }
}

class RoomMakingCrawler {

    private int currentRow, currentColumn, numOfMoves, movesMade, id;
    private RoomCrawlerController controller;
    private Random rng;

    public RoomMakingCrawler(RoomCrawlerController controller, int spawnRow, int spawnColumn, int numOfMoves, int id) {
        this.currentRow = spawnRow;
        this.currentColumn = spawnColumn;
        this.numOfMoves = numOfMoves;
        this.controller = controller;
        this.movesMade = 0;
        this.id = id;
        rng = new Random();
    }

    /**
     * Tells this crawler to attempt a move in one of the four cardinal directions.
     *
     * @return True if the crawler successfully moved. False otherwise.
     */
    public boolean attemptMove() {
        if (movesMade >= numOfMoves)
            return false;
        // reworking this totally
        // first, figure out which moves are possible
        // this array is sorted, higher weight comes first
        CrawlerMove[] possibleMovementVectors = this.generatePossibleMovementVectors();

        // if no moves are possible, return false, and do not move
        if (possibleMovementVectors.length == 0) {
            return false;
        }
        //otherwise, the fun begins

        // select which move to do. the least weighty move is most likely to be chosen
        // if another crawler exists in the same direction of the move, the move is less likely to be chosen
        CrawlerMove determinedMove = null;
        for (CrawlerMove move : possibleMovementVectors) {
            int offset = 0;
            if (move.getDirection() == controller.getDirectionOfClosestCrawler(this)) {
                offset = 5;
            }
            if ((rng.nextInt(move.getWeight() + 1)) == move.getWeight() + offset) {
                determinedMove = move;
                break;
            }
        }
        if (determinedMove == null) determinedMove = possibleMovementVectors[possibleMovementVectors.length - 1];

        // finally, execute the move and return success
        move(determinedMove.getDirection());
        return true;
    }

    /**
     * Generates and returns a sorted array of possible moves.
     */
    private CrawlerMove[] generatePossibleMovementVectors() {
        ArrayList<CrawlerMove> outputArrayList = new ArrayList<>();
        for (Direction dir : Direction.allButCenter()) {
            if (this.controller.isMovePossible(this, dir)) {
                outputArrayList.add(new CrawlerMove(dir, this.controller.getWeightOfMove(this, dir)));
            }
        }
        CrawlerMove[] output = new CrawlerMove[outputArrayList.size()];
        for (int i = 0; i < output.length; i++) {
            output[i] = outputArrayList.get(i);
        }
        Arrays.sort(output);
        return output;
    }

    private void move(Direction dir) {
        movesMade++;
        this.currentColumn += dir.horizontalMovement;
        this.currentRow += dir.verticalMovement;
        System.out.println(dir);
    }

    public int getCurrentRow() {
        return currentRow;
    }

    public int getCurrentColumn() {
        return currentColumn;
    }


}

class RoomCrawlerController {
    // grid containing the weights of spots on the level
    private int[][] weightGrid;
    private RoomCandidate[][] candidateGrid;
    private ArrayList<RoomMakingCrawler> crawlers;
    private int gridSize, numOfDesiredRooms, numOfCurrentRooms;

    //TODO: delete this
    public ArrayList<RoomMakingCrawler> getCrawlers() {
        return crawlers;
    }

    public RoomCrawlerController(int numOfRooms, int numOfCrawlers, int gridSize) {
        crawlers = new ArrayList<>();
        weightGrid = new int[gridSize][gridSize];
        weightGrid[gridSize / 2][gridSize / 2] = -1;
        candidateGrid = new RoomCandidate[gridSize][gridSize];
        candidateGrid[gridSize / 2][gridSize / 2] = new RoomCandidate();
        this.gridSize = gridSize;
        this.numOfDesiredRooms = numOfRooms;
        this.numOfCurrentRooms = 0;
        for (int i = 0; i < numOfCrawlers; i++) {
            crawlers.add(new RoomMakingCrawler(this, gridSize / 2, gridSize / 2, numOfRooms+30, i));
        }
    }

    public boolean attemptFloorCandidateGeneration() {
        boolean floorGenerated = false;
        while (!floorGenerated) {
            for (RoomMakingCrawler crawler : crawlers) {
                // if a crawler has moved successfully, do this
                if (crawler.attemptMove()) {
                    // position of the current crawler, for convenience and efficiency
                    int crawlerPosVert = crawler.getCurrentRow();
                    int crawlerPosHor = crawler.getCurrentColumn();
                    // update the weight grid to reflect the current state of the board
                    this.decrementWeightGrid();
                    for (RoomMakingCrawler crawler1 : crawlers) {
                        weightGrid[crawler1.getCurrentRow()][crawler1.getCurrentColumn()] = -1;
                    }

                    if (candidateGrid[crawlerPosVert][crawlerPosHor] == null) {
                        candidateGrid[crawlerPosVert][crawlerPosHor] = new RoomCandidate();
                        numOfCurrentRooms++;
                    }
                }
                if (numOfCurrentRooms >= numOfDesiredRooms) {
                    floorGenerated = true;
                }
                if (floorGenerated) {
                    break;
                }
            }
        }
        return true;
    }

    private void decrementWeightGrid() {
        for (int row = 0; row < weightGrid.length; row++) {
            for (int col = 0; col < weightGrid.length; col++) {
                if (weightGrid[row][col] == 0) {
                } else if (weightGrid[row][col] == -1) {
                    weightGrid[row][col] = 10;
                } else {
                    weightGrid[row][col]--;
                }
            }
        }
    }

    /**
     * Gets the direction of the closest crawler.
     * If there is only one crawler, or the original crawler is under another, the method will return Direction.CENTER.
     */
    public Direction getDirectionOfClosestCrawler(RoomMakingCrawler originalCrawler) {
        for (RoomMakingCrawler currentCrawler : crawlers) {
            if (currentCrawler == originalCrawler) {
                continue;
            }
            // if positive, south
            // if negative, north
            int vertDistance = currentCrawler.getCurrentRow() - originalCrawler.getCurrentRow();
            // if positive, east
            // if negative, west
            int horDistance = currentCrawler.getCurrentColumn() - originalCrawler.getCurrentColumn();

            if (Math.abs(vertDistance) < Math.abs(horDistance)) {
                return (vertDistance > 0) ? Direction.SOUTH : Direction.NORTH;
            } else {
                return (horDistance > 0) ? Direction.EAST : Direction.WEST;
            }
        }
        return Direction.CENTER;
    }

    /**
     * @param crawler   The crawler doing the moving.
     * @param direction The direction it wants to move in.
     */
    public boolean isMovePossible(RoomMakingCrawler crawler, Direction direction) {
        int newCol = crawler.getCurrentColumn() + direction.horizontalMovement;
        int newRow = crawler.getCurrentRow() + direction.verticalMovement;
        try {
            return weightGrid[newRow][newCol] != -1;
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    /**
     * Returns the weight of a spot based on relative direction compared to a crawler's position.
     */
    public int getWeightOfMove(RoomMakingCrawler crawler, Direction direction) {
        int newCol = crawler.getCurrentColumn() + direction.horizontalMovement;
        int newRow = crawler.getCurrentRow() + direction.verticalMovement;
        try {
            return weightGrid[newRow][newCol];
        } catch (ArrayIndexOutOfBoundsException e) {
            // this shouldn't be possible, but if it does happen just return -1.
            return -1;
        }
    }

    public RoomCandidate[][] getCandidateGrid() {
        return candidateGrid;
    }
}

/**
 * A move that a crawler can make. Stores a direction and the move's weight (the lower the better).
 * Can be sorted against itself.
 */
class CrawlerMove implements Comparable {
    private Direction direction;
    private int weight;

    public CrawlerMove(Direction direction, int weight) {
        this.direction = direction;
        this.weight = weight;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public int compareTo(Object o) {
        CrawlerMove move = (CrawlerMove) o;
        if (move.weight < this.weight) {
            return -1;
        } else if (move.weight > this.weight) {
            return 1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "{" + this.direction + ", " + this.weight + "}";
    }
}

