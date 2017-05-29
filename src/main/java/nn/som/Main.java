package nn.som;

public class Main {
  public static void main(String[] args) {
    MatrixTopology topology = new MatrixTopology(20, 10, 1);
    double[][] minmaxWeights = {{-5, 5}, {-5, 5}};
    Network network = new Network(minmaxWeights, 0.8, topology);
    // SETTINGS: WTA/WTM
//    double learningFactor = 0.08;
//    double neighbourhoodRadius = 0.08;
    // SETTINGS: GAS
    double learningFactor = 0.8;
    double neighbourhoodRadius = 2.0;

    double[][] data = FileUtils.loadTrainingData("data/dane_test.txt");

    Learning learning = new Learning(network, 1000, data, learningFactor, neighbourhoodRadius);
    learning.setDumpIteration(true).setDumpFilePrefix("out/iter_").setDumpFileExt(".txt");
    //learning.learnWTA();
    //learning.learnWTM();
    learning.learnGas();

    System.out.println(network.toString());
    FileUtils.saveNetworkToFile(network, "out/final.txt");
  }
}
