package nn.som;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

public class MatrixTopology {
  private int rows;
  private int cols;
  private int radius = 0;

  public MatrixTopology(int rows, int cols) {
    this.rows = rows;
    this.cols = cols;
  }

  public MatrixTopology(int rows, int cols, int radius) {
    this(rows, cols);
    this.radius = radius;
  }

  public int getNumbersOfNeurons() {
    return rows * cols;
  }

  public int getNeuronNumber(final Position position) {
    if ((position.x < rows) && (position.y < cols)) {
      return (position.x - 1) * cols + position.y;
    }
    return -1;
  }

  public Position getNeuronPosition(int neuronNumber) {
    int x = ((neuronNumber - 1) / neuronNumber) + 1;
    int y = neuronNumber - ((x - 1) * neuronNumber);
    return new Position(x, y);
  }

  public List getConnectedNeurons(int neuronNumber) {
    List connectedNeurons = new ArrayList();

    if ((neuronNumber < cols * rows) && (neuronNumber > 0)){
      if (neuronNumber - cols > 0) {
        connectedNeurons.add(neuronNumber - cols);
      }

      if ((neuronNumber - 1 > 0) && ((neuronNumber % cols) != 1)) {
        connectedNeurons.add(neuronNumber - 1);
      }

      if ((neuronNumber + 1 <= cols * rows)
          && ((neuronNumber % cols) != 0)) {
        connectedNeurons.add(neuronNumber + 1);
      }

      if (neuronNumber + cols <= cols * rows) {
        connectedNeurons.add(neuronNumber + cols);
      }
    }
    return connectedNeurons;
  }

  public TreeMap getNeighbourhood(int neuronNumber) {
    TreeMap<java.lang.Integer, java.lang.Integer> neighbornhood = new TreeMap<java.lang.Integer, java.lang.Integer>();
    List tempConns   = new ArrayList();
    List neighborgoodConns = new ArrayList();

    tempConns.add(neuronNumber);

    int[] temp = null;
    int   key;

    for (int i = 0; i < radius; i++) {
      neighborgoodConns = getN(tempConns);

      for (int k = 0; k < neighborgoodConns.size(); k++) {
        key = (java.lang.Integer) neighborgoodConns.get(k);

        if (!neighbornhood.containsKey(key) && (key != neuronNumber)) {
          neighbornhood.put(key, i + 1);
        }
      }
      tempConns = new ArrayList<>(neighborgoodConns);
    }

    return neighbornhood;
  }

  private List getN(List tempConnection) {
    List neighborgoodConns = new ArrayList();
    List tempConns;

    for (int j = 0; j < tempConnection.size(); j++) {
      tempConns = getConnectedNeurons((java.lang.Integer)tempConnection.get(j));
      for (int i = 0; i < tempConns.size(); i++) {
        neighborgoodConns.add(tempConns.get(i));
      }
    }
    return neighborgoodConns;
  }

  public int getRows() {
    return rows;
  }

  public void setRows(int rows) {
    this.rows = rows;
  }

  public int getCols() {
    return cols;
  }

  public void setCols(int cols) {
    this.cols = cols;
  }

  public int getRadius() {
    return radius;
  }

  public void setRadius(int radius) {
    this.radius = radius;
  }

  public static class Position {
    public final int x;
    public final int y;

    public Position() {
      this(0,0);
    }

    public Position(final int x, final int y) {
      this.x = x;
      this.y = y;
    }
  }
}
