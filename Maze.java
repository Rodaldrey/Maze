package Homework;

import java.util.*;

import tester.*;
import javalib.impworld.*;
import java.awt.Color;

import javalib.worldimages.*;

//represnets all the contants in the maze game
interface IConstants {
  int CELL_SIZE = 30;
}

// represents all the cells in the maze game
interface ICell extends IConstants {

  // updates the neighbors of the cell
  void updateNeighbors(int codeFrom, int codeTo);

  // checks if the cells have the same code
  boolean sameCode(ICell that);

  // helps check if the cells have the same code
  boolean sameCodeHelp(Cell that);

  // checks if the cell should draw a right edge
  boolean drawRightHuh(ArrayList<Edges> edges);

  // checks if the cell should draw a bottom edge
  boolean drawBottomHuh(ArrayList<Edges> edges);

  // adds the cell to the path
  ArrayList<Cell> addCellToPath(ArrayList<Cell> path, Cell that);


  // returns if the cell has been visited
  boolean returnVisitedHuh();

  // helps adds the cell to the path
  void addtoPathHelp(ArrayList<Cell> path);

  // adds the cell to the list
  void addCellToList(ArrayList<Cell> list);
}

// represents a cell in the maze game
class Cell implements ICell {
  int x;
  int y;
  ICell top;
  ICell right;
  ICell bottom;
  ICell left;
  boolean visited;
  int code;

  Cell(int x, int y, boolean visited, int code) {
    this.x = x;
    this.y = y;
    this.top = new MtCell();
    this.right = new MtCell();
    this.bottom = new MtCell();
    this.left = new MtCell();
    this.visited = false;
    this.code = code;
  }

  public boolean equals(Object that) {
    if (that instanceof Cell) {
      return this.x == ((Cell) that).x && this.y == ((Cell) that).y;
    } else {
      return false;
    }
  }

  // returns the hashcode of the cell
  public int hashCode() {
    return this.x * 100 + this.y;
  }

  // updates the neighbors of the cell
  public void updateNeighbors(int codeFrom, int codeTo) {
    if (this.code != codeFrom) {
      return;
    } else {
      this.code = codeTo;
      this.top.updateNeighbors(codeFrom, codeTo);
      this.right.updateNeighbors(codeFrom, codeTo);
      this.bottom.updateNeighbors(codeFrom, codeTo);
      this.left.updateNeighbors(codeFrom, codeTo);
    }
  }

  // checks if the cells have the same code
  public boolean sameCode(ICell that) {
    return that.sameCodeHelp(this);
  }

  // helps check if the cells have the same code
  public boolean sameCodeHelp(Cell that) {
    return this.code == that.code;
  }

  // checks if the cell should draw a right edge
  public boolean drawRightHuh(ArrayList<Edges> edges) {
    for (Edges e : edges) {
      if (e.hasConnections(this, this.right)) {
        return true;
      }
    }
    return false;
  }

  // checks if the cell should draw a bottom edge
  public boolean drawBottomHuh(ArrayList<Edges> edges) {
    for (Edges e : edges) {
      if (e.hasConnections(this, this.bottom)) {
        return true;
      }
    }
    return false;
  }



  // adds the cell to the path
  public ArrayList<Cell> addCellToPath(ArrayList<Cell> path, Cell that) {
    that.addtoPathHelp(path);
    return path;
  }


  // helps adds the cell to the path
  public void addtoPathHelp(ArrayList<Cell> path) {
    if (!this.equals(new MtCell())) {
      path.add(this);
    }
  }

  // adds the cell to the list
  public void addCellToList(ArrayList<Cell> list) {
    list.add(this);
  }


  // returns if the cell has been visited
  public boolean returnVisitedHuh() {
    return this.visited;
  }


}

// represents a cell in the maze game
class MtCell implements ICell {

  MtCell() {
  }

  // updates the neighbors of the cell
  public void updateNeighbors(int codeFrom, int codeTo) {
    // do nothing
  }

  // checks if the cells have the same code
  public boolean sameCode(ICell that) {
    return false;
  }

  // helps check if the cells have the same code
  public boolean sameCodeHelp(Cell that) {
    return false;
  }

  // checks if the cell should draw a right edge
  public boolean drawRightHuh(ArrayList<Edges> edges) {
    return false;
  }

  // checks if the cell should draw a bottom edge
  public boolean drawBottomHuh(ArrayList<Edges> edges) {
    return false;
  }


  // adds the cell to the path
  public ArrayList<Cell> addCellToPath(ArrayList<Cell> path, Cell that) {
    return path;
  }

  // returns if the cell has been visited
  public boolean returnVisitedHuh() {
    return true;
  }

  // helps adds the cell to the path
  public void addtoPathHelp(ArrayList<Cell> path) {

    // do nothing
  }


  // adds the cell to the list
  public void addCellToList(ArrayList<Cell> list) {

    // do nothing
  }

}

// represents all the edges in the maze game
class Edges {
  Cell cell1;
  Cell cell2;
  boolean visited;
  int weight;

  Edges(Cell cell1, Cell cell2, int weight) {
    this.cell1 = cell1;
    this.cell2 = cell2;
    this.visited = visited;
    this.weight = weight;
  }

  // checks if the edges have the same cells
  public boolean hasConnections(ICell cell1, ICell cell2) {
    return (this.cell1.equals(cell1) && this.cell2.equals(cell2)) ||
            (this.cell1.equals(cell2) && this.cell2.equals(cell1));
  }

}

// represents the maze game
class MazeWorld extends World implements IConstants {
  ArrayList<ArrayList<Cell>> cellBoard;
  ArrayList<Edges> edges;
  ArrayList<Edges> worklist;
  int mazeWidth;
  int mazeHeight;
  int totalVisitedEdges;
  Random rand = new Random();
  Cell startCell;
  Cell endCell;
  ArrayList<Cell> directNeighbors;
  boolean startDepth;

  // set as class field
  boolean startBreadth = false;
  boolean finishedBreadth = false;
  HashMap<Cell, Integer> queue = new HashMap<Cell, Integer>();
  ArrayList<Cell> visitedNodes = new ArrayList<Cell>();
  HashMap<Cell, Cell> connectedEdges; // set edges somewhere in your code

  ArrayList<Cell> path;

  MazeWorld(int mazeWidth, int mazeHeight, Random rand) {
    this.startCell = new Cell(0, 0, false, 0);
    this.endCell = new Cell(mazeWidth - 1, mazeHeight - 1, false, 0);
    this.directNeighbors = directNeighbors;
    queue.put(this.startCell, 0); // node, distance
    this.mazeWidth = mazeWidth;
    this.mazeHeight = mazeHeight;
    this.cellBoard = new ArrayList<ArrayList<Cell>>();
    this.edges = new ArrayList<Edges>();
    this.rand = rand;
    makeMaze();
    // System.out.println(makeMaze());
    this.totalVisitedEdges = 0;
    this.path = new ArrayList<Cell>();
    this.startDepth = false;
  }


//  // creates a graph of the maze
//  public HashMap<Cell, ArrayList<Cell>> buildGraph(HashMap<Cell, Cell> edges) {
//    HashMap<Cell, ArrayList<Cell>> graph = new HashMap<Cell, ArrayList<Cell>>();
//    Map<Cell, Cell> mappedEdges = edges; // converting to a Map so we can loop through elements
//
//    for (Map.Entry<Cell, Cell> entry : mappedEdges.entrySet()) {
//      if (graph.get(entry.getKey()) == null) {
//        graph.put(entry.getKey(), new ArrayList<Cell>());
//      }
//
//      if (graph.get(entry.getValue()) == null) {
//        graph.put(entry.getValue(), new ArrayList<Cell>());
//      }
//
//      graph.get(entry.getKey()).add(entry.getValue());
//      graph.get(entry.getValue()).add(entry.getKey());
//    }
//
//    return graph;
//  }
//
//  // returns the key of the last element in the queue, which is of type cell
//  public Cell getLastKey(HashMap<Cell, Integer> queue) {
//    Map<Cell, Integer> mappedQueue = queue;
//    ArrayList<Cell> listKeys = new ArrayList<Cell>();
//
//    for (Map.Entry<Cell, Integer> entry : mappedQueue.entrySet()) {
//      listKeys.add(entry.getKey());
//    }
//
//    return listKeys.get(listKeys.size() - 1);
//  }
//
//  // returns the value of the last element in the queue, which is of type int
//  public int getLastElementValue(HashMap<Cell, Integer> queue) {
//    Map<Cell, Integer> mappedQueue = queue;
//    ArrayList<Integer> listValues = new ArrayList<>();
//
//    for (Map.Entry<Cell, Integer> entry : mappedQueue.entrySet()) {
//      listValues.add(entry.getValue());
//    }
//
//    return listValues.get(listValues.size() - 1);
//  }
//
//  // removes the last element of hashmap and returns the modified hashmap
//  public HashMap<Cell, Integer> removeLastElement(HashMap<Cell, Integer> queue) {
//    Map<Cell, Integer> mappedQueue = queue;
//    HashMap<Cell, Integer> newQueue = new HashMap<Cell, Integer>();
//
//    int i = 1;
//    for (Map.Entry<Cell, Integer> entry : mappedQueue.entrySet()) {
//      if (i < mappedQueue.size() - 1) {
//        newQueue.put(entry.getKey(), entry.getValue());
//      }
//
//      i++;
//    }
//
//    return newQueue;
//  }
//
//  // returns the path from the start cell to the end cell
//  public HashMap<Cell, Integer> explore(HashMap<Cell, ArrayList<Cell>> graph,
//                                        Cell startCell, Cell endCell) {
//    if (getLastKey(queue) != endCell) {
//      ArrayList<Cell> neighbors = graph.get(getLastKey(queue));
//      for (int i = 0; i < neighbors.size(); i++) {
//        if (!visitedNodes.contains(neighbors.get(i))) {
//          visitedNodes.add(neighbors.get(i));
//          neighbors.get(i).visited = true;
//          // change color here, of neighbors.get(i), which is of type Cell
//
//          // copying all existing elements in queue to a new queue, and inserting a new
//          // element at the front.
//          // java does not allow us to insert a new element at the beginning of the
//          // hashmap, so we have to create a new one
//          Map<Cell, Integer> mappedQueue = queue;
//          HashMap<Cell, Integer> newQueue = new HashMap<Cell, Integer>();
//          newQueue.put(neighbors.get(i), getLastElementValue(queue) + 1);
//
//          for (Map.Entry<Cell, Integer> entry : mappedQueue.entrySet()) {
//            this.path.add(entry.getKey());
//            newQueue.put(entry.getKey(), entry.getValue());
//          }
//
//          queue = newQueue; // newQuee is the same as queue,
//          // except is has a new element at the beginning of
//          // the hashmap
//        }
//      }
//
//      queue = removeLastElement(queue);
//    } else {
//      this.finishedBreadth = true;
//    }
//    return queue;
//  }

  // creates the maze
  public ArrayList<ArrayList<Cell>> makeMaze() {
    for (int i = 0; i < this.mazeWidth; i++) {
      ArrayList<Cell> row = new ArrayList<Cell>();
      for (int j = 0; j < this.mazeHeight; j++) {
        row.add(new Cell(i, j, false, i * this.mazeWidth + j));
      }
      this.cellBoard.add(row);
    }

    // asigns the neighbors
    // assigns the neighbors
    for (int i = 0; i < this.mazeWidth; i++) {
      for (int j = 0; j < this.mazeHeight; j++) {
        Cell cell = this.cellBoard.get(i).get(j);
        if (cell.y > 0) {
          cell.top = this.cellBoard.get(i).get(j - 1);
        }
        if (cell.y < this.mazeHeight - 1) {
          cell.bottom = this.cellBoard.get(i).get(j + 1);
        }
        if (cell.x > 0) {
          cell.left = this.cellBoard.get(i - 1).get(j);
        }
        if (cell.x < this.mazeWidth - 1) {
          cell.right = this.cellBoard.get(i + 1).get(j);
        }
      }
    }

    // asigns the edges
    for (int i = 0; i < this.mazeWidth; i++) {
      for (int j = 0; j < this.mazeHeight; j++) {
        Cell cell = this.cellBoard.get(i).get(j);
        // if (cell.y > 0) {
        // this.edges.add(new Edges(cell,
        // this.cellBoard.get(i - 1).get(j),
        // rand.nextInt(mazeWidth * mazeHeight * 100)));
        // }

        if (cell.y < this.mazeHeight - 1) {
          this.edges.add(new Edges(cell,
                  this.cellBoard.get(i).get(j + 1),
                  rand.nextInt(mazeWidth * mazeHeight * 100)));
        }

        // if (cell.x > 0) {
        // this.edges.add(new Edges(cell,
        // this.cellBoard.get(i).get(j - 1),
        // rand.nextInt(mazeWidth * mazeHeight * 100)));
        // }

        if (cell.x < this.mazeWidth - 1) {
          this.edges.add(new Edges(cell,
                  this.cellBoard.get(i + 1).get(j),
                  rand.nextInt(mazeWidth * mazeHeight * 100)));
        }
      }
    }

    this.worklist = new ArrayList<Edges>(this.edges);
    HashMap<Cell, Cell> representatives = new HashMap<Cell, Cell>();
    for (ArrayList<Cell> c : this.cellBoard) {
      for (Cell c2 : c) {
        representatives.put(c2, c2);
      }
    }
    worklist.sort((e1, e2) -> e1.weight - e2.weight);

    ArrayList<Edges> mazeEdges = new ArrayList<Edges>();
    for (Edges e : this.worklist) {
      Cell cell1Result = find(e.cell1, representatives);
      Cell cell2Result = find(e.cell2, representatives);
      if (!cell1Result.equals(cell2Result)) {
        representatives.put(cell1Result, cell2Result);
        mazeEdges.add(e);
        edges.remove(e);
      }
    }



    this.connectedEdges = representatives;
    startBreadth = true;

    this.startCell = this.cellBoard.get(0).get(0);
    this.endCell = this.cellBoard.get(this.cellBoard.size() - 1)
            .get(this.cellBoard.get(this.cellBoard.size() - 1).size() - 1);

    return this.cellBoard;
  }




  //effects the variables of the graph every tick of an interval timer
  public void onTick() {
//    if (!finishedBreadth && startBreadth) {
//      explore(this.buildGraph(connectedEdges), startCell, endCell);
//    }

    if (startDepth) {
      if (path.contains(endCell)) {
        startDepth = false;
        return;
      }
      Cell currentCell = path.get(path.size() - 1);
      currentCell.visited = true;

      ArrayList<Cell> neighbors = new ArrayList<Cell>();
      for (Edges e : this.edges) {
        if (!currentCell.left.returnVisitedHuh()) {
          if (this.canMoveBetween(currentCell, currentCell.left)) {
            currentCell.left.addCellToList(neighbors);
          }
        }
        if (!currentCell.top.returnVisitedHuh()) {
          if (this.canMoveBetween(currentCell, currentCell.top)) {
            currentCell.top.addCellToList(neighbors);
          }
        }
        if (!currentCell.right.returnVisitedHuh()) {
          if (this.canMoveBetween(currentCell, currentCell.right)) {
            currentCell.right.addCellToList(neighbors);
          }
        }
        if (!currentCell.bottom.returnVisitedHuh()) {
          if (this.canMoveBetween(currentCell, currentCell.bottom)) {
            currentCell.bottom.addCellToList(neighbors);
          }
        }
        if (neighbors.size() == 0) {
          path.remove(path.size() - 1);
        }
        else {
          neighbors.get(0).addCellToList(path);
        }
        if (path.size() == 0) {
          path.add(startCell);
        }
      }
    }
  }

  // determines if the player can move between two cells
  boolean canMoveBetween(Cell c1, ICell c2) {
    for (Edges e: this.edges) {
      if (e.cell1.equals(c1) && e.cell2.equals(c2)) {
        return false;
      }
      if (e.cell1.equals(c2) && e.cell2.equals(c1)) {
        return false;
      }
    }
    return true;
  }

  // finds the representative of a cell
  public Cell find(Cell c, HashMap<Cell, Cell> representatives) {
    if (c.equals(representatives.get(c))) {
      return c;
    } else {
      return find(representatives.get(c), representatives);
    }
  }

  // draws the maze
  public WorldScene makeScene() {
    WorldScene scene = getEmptyScene();

    for (int i = 0; i < this.cellBoard.size(); i++) {
      for (int j = 0; j < this.cellBoard.get(i).size(); j++) {
        if (this.cellBoard.get(i).get(j).visited) {
          scene.placeImageXY(new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.SOLID,
                          Color.GRAY),
                  i * CELL_SIZE + (CELL_SIZE / 2 + 50),
                  j * CELL_SIZE + (CELL_SIZE / 2 + 50));
        }
      }
    }

    for (Cell c : this.path) {
      scene.placeImageXY(new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.SOLID,
                      Color.BLUE),
              c.x * CELL_SIZE + (CELL_SIZE / 2 + 50),
              c.y * CELL_SIZE + (CELL_SIZE / 2 + 50));
    }

    for (int i = 0; i < mazeWidth; i++) {
      for (int j = 0; j < mazeHeight; j++) {
        Cell c = this.cellBoard.get(i).get(j);

        if (this.cellBoard.get(i).get(j).equals(this.cellBoard.get(0).get(0))) {
          scene.placeImageXY(new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.SOLID,
                          Color.GREEN),
                  i * CELL_SIZE + (CELL_SIZE / 2 + 50),
                  j * CELL_SIZE + (CELL_SIZE / 2 + 50));

        }

        if (this.cellBoard.get(i).get(j).equals(this.cellBoard.get(mazeWidth
                - 1).get(mazeHeight - 1))) {
          scene.placeImageXY(new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.SOLID,
                          Color.RED),
                  i * CELL_SIZE + (CELL_SIZE / 2 + 50),
                  j * CELL_SIZE + (CELL_SIZE / 2 + 50));

        }

        // draw top edge
        if (c.top == new MtCell()) {
          scene.placeImageXY(new LineImage(new Posn(CELL_SIZE, 0), Color.BLACK),
                  j * CELL_SIZE + CELL_SIZE / 2,
                  i * CELL_SIZE);
        }

        // draw right edge
        if (c.right == new MtCell()) {
          scene.placeImageXY(new RectangleImage(2, CELL_SIZE, OutlineMode.SOLID, Color.RED),
                  i * CELL_SIZE +  50,
                  j * CELL_SIZE + 50);
        }


        if (c.drawBottomHuh(this.edges)) {
          scene.placeImageXY(new RectangleImage(CELL_SIZE, 2, OutlineMode.SOLID, Color.RED),
                  i * CELL_SIZE + (CELL_SIZE / 2 + 50),
                  j * CELL_SIZE + CELL_SIZE + 50);
          // scene.placeImageXY(new TextImage(c.code + " " + ((Cell)c.bottom).code, 10,
          // Color.BLACK),
          // i * CELL_SIZE + (CELL_SIZE / 2 + 50),
          // j * CELL_SIZE + CELL_SIZE + 50);

        }

        if (c.drawRightHuh(this.edges)) {
          scene.placeImageXY(new RectangleImage(2, CELL_SIZE, OutlineMode.SOLID, Color.RED),
                  i * CELL_SIZE + (CELL_SIZE + 50),
                  j * CELL_SIZE + CELL_SIZE / 2 + 50);
          // scene.placeImageXY(new TextImage(c.code + " " + ((Cell)c.right).code, 10,
          // Color.BLACK),
          // i * CELL_SIZE + (CELL_SIZE + 50),
          // j * CELL_SIZE + CELL_SIZE / 2 + 50);
        }

        // scene.placeImageXY(new TextImage(String.valueOf(c.code), 10, Color.BLACK),
        // i * CELL_SIZE + (this.mazeWidth * CELL_SIZE) / 2 -
        // (this.mazeWidth * CELL_SIZE / 2) + 5,
        // j * CELL_SIZE + (this.mazeHeight * CELL_SIZE) / 2 -
        // (this.mazeHeight * CELL_SIZE / 2) + 7);

      }

    }


    return scene;
  }

  // EFFECT: resets the game
  public void onKeyEvent(String key) {
    if (key.equals("r")) {
      makeMaze();
      this.mazeWidth = mazeWidth;
      this.mazeHeight = mazeHeight;
      this.cellBoard = new ArrayList<ArrayList<Cell>>();
      this.edges = new ArrayList<Edges>();
      this.rand = rand;
      makeMaze();
      this.totalVisitedEdges = 0;
      this.totalVisitedEdges = 0;
      this.path = new ArrayList<Cell>();
      boolean startBreadth = false;
      boolean finishedBreadth = false;
      this.startCell = new Cell(0, 0, false, 0);
      this.endCell = new Cell(mazeWidth - 1, mazeHeight - 1, false, 0);
      this.directNeighbors = directNeighbors;
      queue.put(this.startCell, 0); // node, distance
      this.cellBoard = new ArrayList<ArrayList<Cell>>();
      this.edges = new ArrayList<Edges>();
      makeMaze();
      // System.out.println(makeMaze());
      this.totalVisitedEdges = 0;
      this.path = new ArrayList<Cell>();
      this.startDepth = false;


    }


    if (key.equals("d")) {
      this.startDepth = true;
      this.startCell.visited = true;
      this.path.add(this.startCell);
    }
  }

}


// represents a cell in the maze
class ExamplesMaze implements IConstants {

  boolean startDepth;

  ExamplesMaze() {

    this.startDepth = false;

  }

  // tests the makeMaze method
  void testMazeWorld(Tester t) {
    MazeWorld maze = new MazeWorld(20, 20, new Random());

    maze.bigBang(800, 800, Math.pow(10, -5));

  }

  //tests the makeMaze method
  void testMakeMaze(Tester t) {

    MazeWorld maze = new MazeWorld(20, 20, new Random());

    //maze.bigBang(750, 750, 0.1);

    t.checkExpect(maze.makeMaze().size(), 20);
    t.checkExpect(maze.makeMaze().get(0).size(), 20);
    t.checkExpect(maze.makeMaze().get(0).get(0).code, 0);
    t.checkExpect(maze.makeMaze().get(0).get(0).visited, false);
    t.checkExpect(maze.makeMaze().get(0).get(0).top, new MtCell());
    t.checkExpect(maze.makeMaze().get(0).get(0).left, new MtCell());
    t.checkExpect(maze.makeMaze().get(0).get(0).right, new Cell(1, 0, false, 1));
    t.checkExpect(maze.makeMaze().get(0).get(0).bottom, new Cell(0, 1,
            false, 20));
    t.checkExpect(maze.makeMaze().get(0).get(0).x, 0);
    t.checkExpect(maze.makeMaze().get(0).get(0).y, 0);
    t.checkExpect(maze.makeMaze().get(0).get(1).code, 1);
    t.checkExpect(maze.makeMaze().get(0).get(1).visited, false);
    t.checkExpect(maze.makeMaze().get(0).get(1).top, new MtCell());
    t.checkExpect(maze.makeMaze().get(0).get(1).left, new Cell(0, 0,
            false, 0));
    t.checkExpect(maze.makeMaze().get(0).get(1).right, new Cell(2, 0,
            false, 2));
    t.checkExpect(maze.makeMaze().get(0).get(1).bottom, new Cell(1, 1,
            false, 21));
    t.checkExpect(maze.makeMaze().get(0).get(1).x, 1);
    t.checkExpect(maze.makeMaze().get(0).get(1).y, 0);
    t.checkExpect(maze.makeMaze().get(1).get(0).code, 20);
    t.checkExpect(maze.makeMaze().get(1).get(0).visited, false);
    t.checkExpect(maze.makeMaze().get(1).get(0).top, new Cell(0, 0,
            false, 0));
    t.checkExpect(maze.makeMaze().get(1).get(0).left, new MtCell());
    t.checkExpect(maze.makeMaze().get(1).get(0).right, new Cell(21, 0,
            false, 21));
    t.checkExpect(maze.makeMaze().get(1).get(0).bottom, new Cell(20, 1,
            false, 40));
    t.checkExpect(maze.makeMaze().get(1).get(0).x, 0);
    t.checkExpect(maze.makeMaze().get(1).get(0).y, 1);
    t.checkExpect(maze.makeMaze().get(1).get(1).code, 21);
    t.checkExpect(maze.makeMaze().get(1).get(1).visited, false);
    t.checkExpect(maze.makeMaze().get(1).get(1).top, new Cell(1, 0,
            false, 1));
    t.checkExpect(maze.makeMaze().get(1).get(1).left, new Cell(20, 0,
            false, 20));
    t.checkExpect(maze.makeMaze().get(1).get(1).right, new Cell(22, 0,
            false, 22));
    t.checkExpect(maze.makeMaze().get(1).get(1).bottom, new Cell(21, 1,
            false, 41));
    t.checkExpect(maze.makeMaze().get(1).get(1).x, 1);
    t.checkExpect(maze.makeMaze().get(1).get(1).y, 1);


  }

  // test the makeScene method
  void testMakeScene(Tester t) {
    MazeWorld maze = new MazeWorld(20, 20, new Random());
    //maze.bigBang(750, 750, 0.1);
    ArrayList<Edges> edges = new ArrayList<Edges>(Arrays.asList(new
                    Edges(maze.makeMaze().get(0).get(0),
                    maze.makeMaze().get(0).get(1), 2),
            new Edges(maze.makeMaze().get(0).get(0), maze.makeMaze().get(1).get(0), 3)));

    ArrayList<Cell> path = new ArrayList<Cell>(Arrays.asList(maze.makeMaze().get(0).get(0),
            maze.makeMaze().get(0).get(1), maze.makeMaze().get(1).get(1)));

    Cell visited1 = new Cell(0, 0, true, 0);
    Cell visited2 = new Cell(5, 6, true, 4);

    maze.edges = edges;
    maze.makeScene();
    t.checkExpect(edges.get(0).cell1, maze.makeMaze().get(0).get(0));
    t.checkExpect(edges.get(0).cell2, maze.makeMaze().get(0).get(1));
    t.checkExpect(edges.get(1).cell1, maze.makeMaze().get(0).get(0));
    t.checkExpect(edges.get(1).cell2, maze.makeMaze().get(1).get(0));
    javalib.funworld.WorldScene scene = new javalib.funworld.WorldScene(750, 750);
    t.checkExpect(edges.get(0), scene.placeImageXY(new RectangleImage(3,
                    20, OutlineMode.SOLID, Color.RED),
            maze.makeMaze().get(0).get(0).x * 10 + 10,
            maze.makeMaze().get(0).get(0).y * 10 + 10));
    t.checkExpect(edges.get(1), scene.placeImageXY(new RectangleImage(20,
                    3, OutlineMode.SOLID, Color.RED),
            maze.makeMaze().get(0).get(0).x * 10 + 10,
            maze.makeMaze().get(0).get(0).y * 10 + 10));


    t.checkExpect(maze.makeMaze().get(0).get(0), scene.placeImageXY(new
                    RectangleImage(20, 20, OutlineMode.SOLID, Color.GREEN),
            maze.makeMaze().get(0).get(0).x * 10 + 10,
            maze.makeMaze().get(0).get(0).y * 10 + 10));

    t.checkExpect(maze.makeMaze().get(scene.height - 1).get(scene.width - 1),
            scene.placeImageXY(new
                            RectangleImage(20, 20, OutlineMode.SOLID, Color.GREEN),
            maze.makeMaze().get(0).get(0).x * 10 + 10,
            maze.makeMaze().get(0).get(0).y * 10 + 10));

    maze.onKeyEvent("d");

    t.checkExpect(visited1, scene.placeImageXY(new RectangleImage(CELL_SIZE, CELL_SIZE,
                    OutlineMode.SOLID,
                    Color.GRAY),
            0 * CELL_SIZE + (CELL_SIZE / 2 + 50),
            0 * CELL_SIZE + (CELL_SIZE / 2 + 50)));

    t.checkExpect(visited2, scene.placeImageXY(new RectangleImage(CELL_SIZE, CELL_SIZE,
                    OutlineMode.SOLID,
                    Color.GRAY),
            5 * CELL_SIZE + (CELL_SIZE / 2 + 50),
            6 * CELL_SIZE + (CELL_SIZE / 2 + 50)));


    t.checkExpect(maze.makeMaze().get(0).get(0), scene.placeImageXY(new
                    RectangleImage(20, 20, OutlineMode.SOLID, Color.GREEN),
            maze.makeMaze().get(0).get(0).x * 10 + 10,
            maze.makeMaze().get(0).get(0).y * 10 + 10));

    t.checkExpect(edges.get(1), scene.placeImageXY(new RectangleImage(20,
                    3, OutlineMode.SOLID, Color.RED),
            maze.makeMaze().get(0).get(0).x * 10 + 10,
            maze.makeMaze().get(0).get(0).y * 10 + 10));


    t.checkExpect(edges.get(0), scene.placeImageXY(new RectangleImage(3,
                    20, OutlineMode.SOLID, Color.RED),
            maze.makeMaze().get(0).get(0).x * 10 + 10,
            maze.makeMaze().get(0).get(0).y * 10 + 10));
    t.checkExpect(edges.get(1), scene.placeImageXY(new RectangleImage(20,
                    3, OutlineMode.SOLID, Color.RED),
            maze.makeMaze().get(0).get(0).x * 10 + 10,
            maze.makeMaze().get(0).get(0).y * 10 + 10));

  }

  // test the onKeyEvent method
  void testOnKeyEvent(Tester t) {
    MazeWorld maze = new MazeWorld(20, 20, new Random());
    // maze.bigBang(750, 750, 0.1);

    ArrayList<Cell> path = new ArrayList<Cell>(Arrays.asList(maze.makeMaze().get(0).get(0),
            maze.makeMaze().get(0).get(1), maze.makeMaze().get(1).get(1)));


    maze.onKeyEvent("r");
    t.checkExpect(maze.makeMaze().get(0).get(0).code, 0);
    t.checkExpect(maze.makeMaze().get(1).get(1).code, 21);
    t.checkExpect(maze.makeMaze().get(2).get(2).code, 42);
    t.checkExpect(maze.makeMaze().get(3).get(3).code, 63);
    t.checkExpect(maze.makeMaze().get(4).get(4).code, 84);
    t.checkExpect(maze.edges.size(), 400);
    t.checkExpect(maze.edges.get(0).cell1, maze.makeMaze().get(0).get(0));
    t.checkExpect(maze.edges.get(0).cell2, maze.makeMaze().get(0).get(1));
    t.checkExpect(maze.edges.get(1).cell1, maze.makeMaze().get(0).get(0));
    t.checkExpect(maze.edges.get(1).cell2, maze.makeMaze().get(1).get(0));
    t.checkExpect(maze.edges.get(0).weight, 2);
    t.checkExpect(maze.edges.get(1).weight, 5);
    t.checkExpect(maze.edges.get(2).weight, 3);
    t.checkExpect(maze.edges.get(3).weight, 3);

    maze.onKeyEvent("r");

    t.checkExpect(maze.makeMaze().get(0).get(0).code, 0);
    t.checkExpect(maze.makeMaze().get(1).get(1).code, 21);
    t.checkExpect(maze.makeMaze().get(2).get(2).code, 42);
    t.checkExpect(maze.makeMaze().get(3).get(3).code, 63);
    t.checkExpect(maze.makeMaze().get(4).get(4).code, 84);
    t.checkExpect(maze.edges.size(), 400);
    t.checkExpect(maze.edges.get(0).cell1, maze.makeMaze().get(0).get(0));
    t.checkExpect(maze.edges.get(0).cell2, maze.makeMaze().get(0).get(1));
    t.checkExpect(maze.edges.get(1).cell1, maze.makeMaze().get(0).get(0));
    t.checkExpect(maze.edges.get(1).cell2, maze.makeMaze().get(1).get(0));
    t.checkExpect(maze.edges.get(0).weight, 4);
    t.checkExpect(maze.edges.get(1).weight, 5);
    t.checkExpect(maze.edges.get(2).weight, 7);
    t.checkExpect(maze.edges.get(3).weight, 6);

    maze.onKeyEvent("d");

    this.startDepth = true;
    maze.makeMaze().get(0).get(0).visited = true;
    path.add(maze.makeMaze().get(0).get(0));
  }

  // test the updateNeighbors method
  void testUpdateNeighbors(Tester t) {
    MazeWorld maze = new MazeWorld(20, 20, new Random());
    // maze.bigBang(750, 750, 0.1);

    t.checkExpect(maze.makeMaze().get(0).get(0).code, 0);
    t.checkExpect(maze.makeMaze().get(0).get(1).code, 1);
    t.checkExpect(maze.makeMaze().get(2).get(2).code, 42);
    t.checkExpect(maze.makeMaze().get(3).get(3).code, 63);
    t.checkExpect(maze.makeMaze().get(4).get(4).code, 84);

    maze.edges.get(0).cell1.updateNeighbors(maze.edges.get(0).cell1.code,
            maze.edges.get(1).cell2.code);

    t.checkExpect(maze.makeMaze().get(0).get(0).code, 0);
    t.checkExpect(maze.makeMaze().get(0).get(1).code, 0);
    t.checkExpect(maze.makeMaze().get(2).get(2).code, 42);
    t.checkExpect(maze.makeMaze().get(3).get(3).code, 63);
    t.checkExpect(maze.makeMaze().get(4).get(4).code, 84);

    maze.edges.get(1).cell1.updateNeighbors(maze.edges.get(0).cell1.code,
            maze.edges.get(1).cell2.code);

    t.checkExpect(maze.makeMaze().get(0).get(0).code, 0);
    t.checkExpect(maze.makeMaze().get(0).get(1).code, 0);
    t.checkExpect(maze.makeMaze().get(1).get(0).code, 0);
    t.checkExpect(maze.makeMaze().get(2).get(2).code, 42);
    t.checkExpect(maze.makeMaze().get(3).get(3).code, 63);
    t.checkExpect(maze.makeMaze().get(4).get(4).code, 84);


  }

  // test the sameCode method
  void testSameCode(Tester t) {
    MazeWorld maze = new MazeWorld(20, 20, new Random());
    // maze.bigBang(750, 750, 0.1);

    t.checkExpect(maze.edges.get(0).cell1.sameCode(maze.edges.get(0).cell2), false);
    t.checkExpect(maze.edges.get(1).cell1.sameCode(maze.edges.get(1).cell2), false);
    t.checkExpect(maze.edges.get(2).cell1.sameCode(maze.edges.get(2).cell2), false);
    t.checkExpect(maze.edges.get(3).cell1.sameCode(maze.edges.get(3).cell2), false);

    maze.edges.get(0).cell1.updateNeighbors(maze.edges.get(0).cell1.code,
            maze.edges.get(1).cell2.code);

    t.checkExpect(maze.edges.get(0).cell1.sameCode(maze.edges.get(0).cell2), true);
    t.checkExpect(maze.edges.get(1).cell1.sameCode(maze.edges.get(1).cell2), false);
    t.checkExpect(maze.edges.get(2).cell1.sameCode(maze.edges.get(2).cell2), false);
    t.checkExpect(maze.edges.get(3).cell1.sameCode(maze.edges.get(3).cell2), false);

    maze.edges.get(1).cell1.updateNeighbors(maze.edges.get(0).cell1.code,
            maze.edges.get(1).cell2.code);

    t.checkExpect(maze.edges.get(0).cell1.sameCode(maze.edges.get(0).cell2), true);
    t.checkExpect(maze.edges.get(1).cell1.sameCode(maze.edges.get(1).cell2), true);
    t.checkExpect(maze.edges.get(2).cell1.sameCode(maze.edges.get(2).cell2), false);
    t.checkExpect(maze.edges.get(3).cell1.sameCode(maze.edges.get(3).cell2), false);
  }

  // test the sameCodeHelp method
  void testSameCodeHelp(Tester t) {
    MazeWorld maze = new MazeWorld(20, 20, new Random());
    // maze.bigBang(750, 750, 0.1);

    t.checkExpect(maze.edges.get(0).cell1.sameCodeHelp(maze.edges.get(0).cell2), false);
    t.checkExpect(maze.edges.get(1).cell1.sameCodeHelp(maze.edges.get(1).cell2), false);
    t.checkExpect(maze.edges.get(2).cell1.sameCodeHelp(maze.edges.get(2).cell2), false);
    t.checkExpect(maze.edges.get(3).cell1.sameCodeHelp(maze.edges.get(3).cell2), false);

    maze.edges.get(0).cell1.updateNeighbors(maze.edges.get(0).cell1.code,
            maze.edges.get(1).cell2.code);

    t.checkExpect(maze.edges.get(0).cell1.sameCodeHelp(maze.edges.get(0).cell2), true);
    t.checkExpect(maze.edges.get(1).cell1.sameCodeHelp(maze.edges.get(1).cell2), false);
    t.checkExpect(maze.edges.get(2).cell1.sameCodeHelp(maze.edges.get(2).cell2), false);
    t.checkExpect(maze.edges.get(3).cell1.sameCodeHelp(maze.edges.get(3).cell2), false);

    maze.edges.get(1).cell1.updateNeighbors(maze.edges.get(0).cell1.code,
            maze.edges.get(1).cell2.code);

    t.checkExpect(maze.edges.get(0).cell1.sameCodeHelp(maze.edges.get(0).cell2), true);
    t.checkExpect(maze.edges.get(1).cell1.sameCodeHelp(maze.edges.get(1).cell2), true);
    t.checkExpect(maze.edges.get(2).cell1.sameCodeHelp(maze.edges.get(2).cell2), false);
    t.checkExpect(maze.edges.get(3).cell1.sameCodeHelp(maze.edges.get(3).cell2), false);
  }


  // test the drawRightHuh method
  void testDrawRightHuh(Tester t) {
    MazeWorld maze = new MazeWorld(20, 20, new Random());
    // maze.bigBang(750, 750, 0.1);
    ArrayList<Edges> edges = new ArrayList<Edges>(Arrays.asList(new
                    Edges(maze.makeMaze().get(0).get(0),
                    maze.makeMaze().get(0).get(1), 2),
            new Edges(maze.makeMaze().get(0).get(0), maze.makeMaze().get(1).get(0), 3),
            new Edges(maze.makeMaze().get(0).get(1), maze.makeMaze().get(1).get(1), 1),
            new Edges(maze.makeMaze().get(1).get(0), maze.makeMaze().get(1).get(1), 0),
            new Edges(maze.makeMaze().get(1).get(1), maze.makeMaze().get(1).get(2), 0),
            new Edges(maze.makeMaze().get(1).get(1), maze.makeMaze().get(2).get(1), 3),
            new Edges(maze.makeMaze().get(1).get(2), maze.makeMaze().get(2).get(2), 1),
            new Edges(maze.makeMaze().get(2).get(1), maze.makeMaze().get(2).get(2), 2)));

    t.checkExpect(maze.makeMaze().get(0).get(0).drawRightHuh(edges), true);
    t.checkExpect(maze.makeMaze().get(0).get(1).drawRightHuh(edges), false);
    t.checkExpect(maze.makeMaze().get(1).get(0).drawRightHuh(edges), false);
    t.checkExpect(maze.makeMaze().get(1).get(1).drawRightHuh(edges), false);
    t.checkExpect(maze.makeMaze().get(1).get(2).drawRightHuh(edges), true);
    t.checkExpect(maze.makeMaze().get(2).get(1).drawRightHuh(edges), false);
    t.checkExpect(maze.makeMaze().get(2).get(2).drawRightHuh(edges), false);


  }

  // test the drawBottomHuh method
  void testDrawBottomHuh(Tester t) {
    MazeWorld maze = new MazeWorld(20, 20, new Random());
    // maze.bigBang(750, 750, 0.1);
    ArrayList<Edges> edges = new ArrayList<Edges>(Arrays.asList(new
                    Edges(maze.makeMaze().get(0).get(0), maze.makeMaze().get(0).get(1), 2),
            new Edges(maze.makeMaze().get(0).get(0), maze.makeMaze().get(1).get(0), 3), new
                    Edges(maze.makeMaze().get(0).get(1), maze.makeMaze().get(1).get(1),
                    1), new Edges(maze.makeMaze().get(1).get(0),
                    maze.makeMaze().get(1).get(1), 0), new
                    Edges(maze.makeMaze().get(1).get(1),
                    maze.makeMaze().get(1).get(2), 0), new
                    Edges(maze.makeMaze().get(1).get(1),
                    maze.makeMaze().get(2).get(1), 3), new
                    Edges(maze.makeMaze().get(1).get(2),
                    maze.makeMaze().get(2).get(2), 1), new
                    Edges(maze.makeMaze().get(2).get(1),
                    maze.makeMaze().get(2).get(2), 2)));

    t.checkExpect(maze.makeMaze().get(0).get(0).drawBottomHuh(edges), false);
    t.checkExpect(maze.makeMaze().get(0).get(1).drawBottomHuh(edges), false);
    t.checkExpect(maze.makeMaze().get(1).get(0).drawBottomHuh(edges), true);
    t.checkExpect(maze.makeMaze().get(1).get(1).drawBottomHuh(edges), false);
    t.checkExpect(maze.makeMaze().get(1).get(2).drawBottomHuh(edges), false);
    t.checkExpect(maze.makeMaze().get(2).get(1).drawBottomHuh(edges), false);
    t.checkExpect(maze.makeMaze().get(2).get(2).drawBottomHuh(edges), true);
  }

  // test the addCellToPath method
  void testAddCellToPath(Tester t) {

    MazeWorld maze = new MazeWorld(20, 20, new Random());
    Cell c1 = new Cell(0,0, false, 0);
    MtCell mt = new MtCell();

    ArrayList<Cell> path = new ArrayList<Cell>(Arrays.asList(maze.makeMaze().get(0).get(0),
            maze.makeMaze().get(0).get(1), maze.makeMaze().get(1).get(1)));

    maze.bigBang(750, 750, 0.1);
    c1.addCellToPath(path, maze.makeMaze().get(0).get(0));
    t.checkExpect(maze.path.size(), 4);
    c1.addCellToPath(path, maze.makeMaze().get(0).get(1));
    t.checkExpect(maze.path.size(), 5);
    c1.addCellToPath(path, maze.makeMaze().get(1).get(1));
    t.checkExpect(maze.path.size(), 6);
    c1.addCellToPath(path, maze.makeMaze().get(1).get(2));
    t.checkExpect(maze.path.size(), 7);
    c1.addCellToPath(path, maze.makeMaze().get(2).get(2));
    t.checkExpect(maze.path.size(), 8);

  }

  // test the returnVisitedHuh method
  void testReturnVisitedHuh(Tester t) {
    Cell c1 = new Cell(0,0, false, 0);
    Cell c2 = new Cell(0,1, true, 2);
    Cell c3 = new Cell(1,1, false, 0222222222);
    Cell c4 = new Cell(1,2, true, 10000000);
    MtCell mt = new MtCell();

    t.checkExpect(c1.returnVisitedHuh(), false);
    t.checkExpect(c2.returnVisitedHuh(), true);
    t.checkExpect(c3.returnVisitedHuh(), false);
    t.checkExpect(c4.returnVisitedHuh(), true);
    t.checkExpect(mt.returnVisitedHuh(), false);
  }

//  // test the addToPathHelp method
//  void testAddToPathHelp(Tester t) {
//    MazeWorld maze = new MazeWorld(20, 20, new Random());
//    Cell c1 = new Cell(0,0, false, 0);
//    MtCell mt = new MtCell();
//
//    ArrayList<Cell> path = new ArrayList<Cell>(Arrays.asList(maze.makeMaze().get(0).get(0),
//            maze.makeMaze().get(0).get(1), maze.makeMaze().get(1).get(1)));
//
//    ArrayList<Cell> mtPath = new ArrayList<Cell>(Arrays.asList());
//
//    t.checkExpect(path.addToPathHelp(maze.makeMaze().get(2).get(0)),
//            new ArrayList<Cell>(Arrays.asList(maze.makeMaze().get(0).get(0),
//            maze.makeMaze().get(0).get(1), maze.makeMaze().get(1).get(1),
//                    maze.makeMaze().get(2).get(0))));
//
//    t.checkExpect(mtPath.addToPathHelp(maze.makeMaze().get(4).get(2)),
//            new ArrayList<Cell>(Arrays.asList(maze.makeMaze().get(4).get(2))));
//
//  }

  // test the addCellToList method
  void testaddCellToList(Tester t) {
    MazeWorld maze = new MazeWorld(20, 20, new Random());
    Cell c1 = new Cell(0,0, false, 0);
    ArrayList<Cell> path = new ArrayList<Cell>(Arrays.asList(maze.makeMaze().get(0).get(0),
            maze.makeMaze().get(0).get(1), maze.makeMaze().get(1).get(1)));

    ArrayList<Cell> mtPath = new ArrayList<Cell>(Arrays.asList());

    t.checkExpect(path.size(), 3);
    c1.addCellToList(path);
    t.checkExpect(path.size(), 4);

    t.checkExpect(mtPath.size(), 0);
    c1.addCellToList(mtPath);
    t.checkExpect(mtPath.size(), 0);
    t.checkExpect(path.size(), 4);
    c1.addCellToList(path);
    t.checkExpect(path.size(), 5);
  }

//  // test the removeLastElement method
//  boolean testRemoveLastElement(Tester t) {
//    HashMap<Cell, Integer> hm1 = new HashMap<Cell, Integer>();
//    HashMap<Cell, Integer> hm2 = new HashMap<Cell, Integer>();
//    HashMap<Cell, Integer> hm3 = new HashMap<Cell, Integer>();
//    HashMap<Cell, Integer> hm4 = new HashMap<Cell, Integer>();
//
//    hm1.put(new Cell(1, 1, false, 0), 1);
//    hm1.put(new Cell(1, 1, false, 0), 1);
//    hm2.put(new Cell(1, 1, false, 0), 1);
//
//    hm3.put(new Cell(1, 1, false, 0), 3);
//    hm3.put(new Cell(1, 1, false, 0), 2);
//    hm4.put(new Cell(1, 1, false, 0), 3);
//
//    return t.checkExpect(removeLastElement(hm1), hm2)
//            && t.checkExpect(removeLastElement(hm3), hm4);
//  }
//
//  // test the getlastElementValie method
//  boolean testGetLastElementValue(Tester t) {
//    HashMap<Cell, Integer> hm1 = new HashMap<Cell, Integer>();
//    HashMap<Cell, Integer> hm2 = new HashMap<Cell, Integer>();
//    HashMap<Cell, Integer> hm3 = new HashMap<Cell, Integer>();
//    HashMap<Cell, Integer> hm4 = new HashMap<Cell, Integer>();
//
//    hm1.put(new Cell(1, 1, false, 0), 1);
//    hm2.put(new Cell(1, 1, false, 0), 2);
//    hm3.put(new Cell(1, 1, false, 0), 3);
//
//    return t.checkExpect(getLastElementValue(hm1), 1)
//            && t.checkExpect(getLastElementValue(hm2), 2)
//            && t.checkExpect(getLastElementValue(hm3), 3);
//  }
//
//  // test the getLastKey method
//  boolean testGetLastKey(Tester t) {
//    HashMap<Cell, Integer> hm1 = new HashMap<Cell, Integer>();
//    HashMap<Cell, Integer> hm2 = new HashMap<Cell, Integer>();
//    HashMap<Cell, Integer> hm3 = new HashMap<Cell, Integer>();
//    HashMap<Cell, Integer> hm4 = new HashMap<Cell, Integer>();
//
//    hm1.put(new Cell(1, 1, false, 0), 1);
//    hm2.put(new Cell(1, 1, false, 0), 2);
//    hm3.put(new Cell(1, 1, false, 0), 3);
//
//    return t.checkExpect(getLastElementValue(hm1), new Cell(1, 1, false, 0))
//            && t.checkExpect(getLastElementValue(hm2), new Cell(1, 1, false, 0))
//            && t.checkExpect(getLastElementValue(hm3), new Cell(1, 1, false, 0));
//  }
//
//  // test the equals method
//  void testEquals(Tester t) {
//    Cell c1 = new Cell(0,0, false, 0);
//    Cell c2 = new Cell(0,1, true, 2);
//    Cell c3 = new Cell(1,1, false, 0222222222);
//    Cell c4 = new Cell(1,2, true, 10000000);
//    Cell c5 = new Cell(1,2, true, 10000000);
//    MtCell mt = new MtCell();
//
//    t.checkExpect(c1.equals(c1), true);
//    t.checkExpect(c1.equals(c2), false);
//    t.checkExpect(c1.equals(c3), false);
//    t.checkExpect(c1.equals(c4), false);
//    t.checkExpect(c1.equals(mt), false);
//    t.checkExpect(c2.equals(c1), false);
//    t.checkExpect(c2.equals(c2), true);
//    t.checkExpect(c2.equals(c3), false);
//    t.checkExpect(c2.equals(c4), false);
//    t.checkExpect(c2.equals(mt), false);
//    t.checkExpect(c3.equals(c1), false);
//    t.checkExpect(c3.equals(c2), false);
//    t.checkExpect(c3.equals(c3), true);
//    t.checkExpect(c3.equals(c4), false);
//    t.checkExpect(c3.equals(mt), false);
//    t.checkExpect(c4.equals(c1), false);
//    t.checkExpect(c4.equals(c2), false);
//    t.checkExpect(c4.equals(c3), false);
//    t.checkExpect(c4.equals(c4), true);
//    t.checkExpect(c4.equals(mt), false);
//    t.checkExpect(c4.equals(c5), true);
//    t.checkExpect(c5.equals(c4), true);
//  }
//
//  // test the hashCode method
//  void testHashCode(Tester t) {
//    Cell c1 = new Cell(0,0, false, 0);
//    Cell c2 = new Cell(0,1, true, 2);
//    Cell c3 = new Cell(1,1, false, 0222222222);
//    Cell c4 = new Cell(1,2, true, 10000000);
//    Cell c5 = new Cell(1,2, true, 10000000);
//    MtCell mt = new MtCell();
//
//    t.checkExpect(c1.hashCode(),0);
//    t.checkExpect(c2.hashCode(),1);
//    t.checkExpect(c3.hashCode(),101);
//    t.checkExpect(c4.hashCode(),102);
//
//  }
//
//  // test the find method
//  void testFind(Tester t) {
//    HashMap<Cell, Cell> representatives = new HashMap<Cell, Cell>();
//    Cell c1 = new Cell(0,0, false, 0);
//    Cell c2 = new Cell(0,1, true, 2);
//    Cell c3 = new Cell(1,1, false, 0222222222);
//    Cell c4 = new Cell(1,2, true, 10000000);
//    Cell c5 = new Cell(1,2, true, 10000000);
//    MtCell mt = new MtCell();
//    representatives.put(c1, c1);
//    representatives.put(c2, c2);
//    representatives.put(c3, c3);
//    representatives.put(c4, c4);
//    representatives.put(c5, c5);
//
//    t.checkExpect(find(c1, representatives), c1);
//    t.checkExpect(find(c2, representatives), c2);
//    t.checkExpect(find(c3, representatives), c3);
//    t.checkExpect(find(c4, representatives), c4);
//    t.checkExpect(find(c5, representatives), c5);
//  }
//
//
//  // test the ontick method
//  void testOnTick(Tester t) {
//    startDepth = true;
//    Cell c1 = new Cell(0,0, false, 0);
//    Cell c2 = new Cell(0,1, false, 2);
//    Cell c3 = new Cell(1,1, false, 0222222222);
//    Cell c4 = new Cell(1,2, false, 10000000);
//    Cell c5 = new Cell(1,2, false, 10000000);
//    Cell c6 = new Cell(1,0, false, 10000000);
//    Cell endCell = new Cell(2,2, false, 10000000);
//    MtCell mt = new MtCell();
//    ArrayList<Cell> path = new ArrayList<Cell>();
//
//    ArrayList<Cell> Neighbors = new ArrayList<Cell>();
//
//    t.checkExpect(path.size(), 0);
//    //tick
//    c1.visited = true;
//    path.add(c1);
//    Neighbors.add(c2);
//    Neighbors.add(c6);
//
//    //tick
//
//    c2.visited = true;
//    path.add(c2);
//    Neighbors.add(c3);
//    Neighbors.add(c4);
//
//    //tick
//
//    c3.visited = true;
//    path.add(c3);
//    Neighbors.add(c4);
//    Neighbors.add(c5);
//
//    //tick
//
//    c4.visited = true;
//    path.add(c4);
//    Neighbors.add(c5);
//    Neighbors.add(endCell);
//
//    //tick
//
//    c5.visited = true;
//    path.add(c5);
//    Neighbors.add(endCell);
//
//    //tick
//
//    endCell.visited = true;
//
//    t.checkExpect(path.size(), 6);
//
//  }
//
//  // test the buildGraph method
//  boolean testBuildGraph(Tester t) {
//    HashMap<Cell, Cell> edges = new HashMap<Cell, Cell>();
//
//    Cell cell1 = new Cell(1, 1, false, 1);
//    Cell cell2 = new Cell(2, 2, false, 2);
//    Cell cell3 = new Cell(3, 3, false, 3);
//    Cell cell4 = new Cell(4, 4, false, 4);
//
//    edges.put(cell1, cell2);
//    edges.put(cell2, cell1);
//    edges.put(cell2, cell3);
//    edges.put(cell2, cell4);
//    edges.put(cell4, cell3);
//    edges.put(cell4, cell1);
//
//    HashMap<Cell, ArrayList<Cell>> expectedValues = new HashMap<Cell, ArrayList<Cell>>();
//
//    ArrayList<Cell> val1 = new ArrayList<Cell>();
//    val1.add(cell2);
//    ArrayList<Cell> val2 = new ArrayList<Cell>();
//    val1.add(cell1);
//    val1.add(cell3);
//    val1.add(cell4);
//    ArrayList<Cell> val3 = new ArrayList<Cell>();
//    val3.add(cell2);
//    val3.add(cell4);
//    ArrayList<Cell> val4 = new ArrayList<Cell>();
//    val3.add(cell2);
//    val3.add(cell3);
//    val3.add(cell1);
//
//    expectedValues.put(cell1, val1);
//    expectedValues.put(cell2, val2);
//    expectedValues.put(cell3, val3);
//    expectedValues.put(cell4, val4);
//
//    return t.checkExpect(buildGraph(edges), expectedValues);
//  }
//
//  // test the exmplore method
//  boolean testExplore(Tester t) {
//    HashMap<Cell, Cell> edges = new HashMap<Cell, Cell>();
//
//    Cell cell1 = new Cell(1, 1, false, 1);
//    Cell cell2 = new Cell(2, 2, false, 2);
//    Cell cell3 = new Cell(3, 3, false, 3);
//    Cell cell4 = new Cell(4, 4, false, 4);
//
//    edges.put(cell1, cell2);
//    edges.put(cell2, cell1);
//    edges.put(cell2, cell3);
//    edges.put(cell2, cell4);
//    edges.put(cell4, cell3);
//    edges.put(cell4, cell1);
//
//    HashMap<Cell, ArrayList<Cell>> graph = new HashMap<Cell, ArrayList<Cell>>();
//
//    ArrayList<Cell> val1 = new ArrayList<Cell>();
//    val1.add(cell2);
//    ArrayList<Cell> val2 = new ArrayList<Cell>();
//    val1.add(cell1);
//    val1.add(cell3);
//    val1.add(cell4);
//    ArrayList<Cell> val3 = new ArrayList<Cell>();
//    val3.add(cell2);
//    val3.add(cell4);
//    ArrayList<Cell> val4 = new ArrayList<Cell>();
//    val3.add(cell2);
//    val3.add(cell3);
//    val3.add(cell1);
//
//    graph.put(cell1, val1);
//    graph.put(cell2, val2);
//    graph.put(cell3, val3);
//    graph.put(cell4, val4);
//
//    Cell startCell = cell1;
//    Cell endCell = cell4;
//
//    HashMap<Cell, Integer> queue = new HashMap<Cell, Integer>();
//
//    queue.put(cell1, cell2);
//    queue.put(cell2, cell3);
//    queue.put(cell2, cell4);
//    queue.put(cell4, cell3);
//    queue.put(cell4, cell1);
//
//    return t.checkExpect(explore(graph, startCell, endCell), queue);
//  }
//
//
//
//





}
