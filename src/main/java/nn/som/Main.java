package nn.som;

public class Main {
  public static void main(String[] args) {
    MatrixTopology topology = new MatrixTopology(20, 10, 1);
    double[] maxWeights = {5, 4};
    Network network = new Network(maxWeights, topology);
    double learningFactor = 0.08;
    double neighbourhoodRadius = 0.8;
    double[][] data = FileUtils.loadTrainingData("data/dane_test.txt");

    Learning learning = new Learning(network, 200, data, learningFactor, neighbourhoodRadius);
    learning.setDumpIteration(false).setDumpFilePrefix("out/iter_").setDumpFileExt(".txt");
    //learning.learnWTA();
    //learning.learnWTM();
    learning.learnGas();

    System.out.println(network.toString());
    FileUtils.saveNetworkToFile(network, "out/final.txt");
  }
}
