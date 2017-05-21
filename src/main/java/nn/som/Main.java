package nn.som;

public class Main {
  public static void main(String[] args) {
    MatrixTopology topology = new MatrixTopology(20, 10, 1);
    double[][] minmaxWeights = {{-10, 10}, {-10, 10}};
    Network network = new Network(minmaxWeights, 0.80, topology);
    double learningFactor = 0.8;
    double neighbourhoodRadius = 0.8;
    double[][] data = FileUtils.loadTrainingData("data/dane_test.txt");

    Learning learning = new Learning(network, 1000, data, learningFactor, neighbourhoodRadius);
    learning.setDumpIteration(true).setDumpFilePrefix("out/iter_").setDumpFileExt(".txt");
    //learning.learnWTA();
    learning.learnWTM();
    //learning.learnGas();

    System.out.println(network.toString());
    FileUtils.saveNetworkToFile(network, "out/final.txt");
  }
}
