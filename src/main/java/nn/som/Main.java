package nn.som;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.rmi.dgc.Lease;
import java.util.ArrayList;
import java.util.List;

public class Main {
  public static void main(String[] args) {
    MatrixTopology topology = new MatrixTopology(20, 10);
    double[] maxWeights = {5, 4};
    Network network = new Network(maxWeights, topology);
    double learningFactor = 0.08;
    double[][] data = FileUtils.loadTrainingData("data/dane_test.txt");

    Learning learning = new Learning(network, 20, data, learningFactor);
    learning.setDumpIteration(true).setDumpFilePrefix("out/iter_").setDumpFileExt(".txt");
    learning.learn();

    System.out.println(network.toString());
    FileUtils.saveNetworkToFile(network, "out/final.txt");
  }
}
