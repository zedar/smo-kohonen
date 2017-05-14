package nn.som;

import java.util.Iterator;
import java.util.TreeMap;

public class Learning {
  private Network network;
  private int maxIterations=20;
  private double[][] learningData;
  private double learningFactor = 0.8;
  private double neighbourhoodRadius = 0.8;
  private String dumpFilePrefix;
  private String dumpFileExt;
  private boolean dumpIteration;


  public Learning(final Network network, final int maxIterations, final double[][] learningData, final double learningFactor) {
    this.network = network;
    this.maxIterations = maxIterations;
    this.learningData = learningData;
    this.learningFactor = learningFactor;
  }

  public Learning(final Network network, final int maxIterations, final double[][] learningData, final double learningFactor, final double neighbourhoodRadius) {
    this(network, maxIterations, learningData, learningFactor);
    this.neighbourhoodRadius = neighbourhoodRadius;
  }

  public Learning setDumpFilePrefix(String dumpFilePrefix) {
    this.dumpFilePrefix = dumpFilePrefix;
    return this;
  }

  public Learning setDumpFileExt(String dumpFileExt) {
    this.dumpFileExt = dumpFileExt;
    return this;
  }


  public Learning setDumpIteration(boolean dumpIteration) {
    this.dumpIteration = dumpIteration;
    return this;
  }

  public void learnWTA(){
    int bestNeuron = 0;
    double[] vector;
    int dataSize = learningData.length;
    for (int i = 0; i < maxIterations; i++){
      for(int j= 0; j < dataSize; j++){
        vector = learningData[j];
        bestNeuron = getBestNeuron(vector);
        changeNeuronWeightWTA(bestNeuron, vector, i);
      }
      if (dumpIteration && dumpFilePrefix != null) {
        FileUtils.saveNetworkToFile(network, dumpFilePrefix+i+(dumpFileExt != null ? dumpFileExt : ""));
      }
    }
  }

  public void learnWTM(){
    int bestNeuron = 0;
    double[] vector;
    int dataSize = learningData.length;
    for (int i = 0; i < maxIterations; i++){
      for(int j= 0; j < dataSize; j++){
        vector = learningData[j];
        bestNeuron = getBestNeuron(vector)+1;
        changeWeightWTM(bestNeuron, vector, i);
      }
      if (dumpIteration && dumpFilePrefix != null) {
        FileUtils.saveNetworkToFile(network, dumpFilePrefix+i+(dumpFileExt != null ? dumpFileExt : ""));
      }
    }
  }

  private int getBestNeuron(double[] vector){
    KohonenNeuron tempNeuron;
    double distance, bestDistance = -1;
    int networkSize = network.getNumOfNeurons();
    int bestNeuron = 0;
    for(int i = 0; i < networkSize; i++){
      tempNeuron = network.getNeuron(i);
      distance = EuclidesDistance.calcDistance(tempNeuron.getWeights(), vector);
      if((distance < bestDistance) || (bestDistance == -1)){
        bestDistance = distance;
        bestNeuron = i;
      }
    }
    return bestNeuron;
  }

  private void changeNeuronWeightWTA(int neuronNumber, double[] vector, int iteration) {
    double[] weights = network.getNeuron(neuronNumber).getWeights();
    int weightNumber = weights.length;
    double weight;
    for (int i = 0; i < weightNumber; i++) {
      weight = weights[i];
      weights[i] += learningFactor * (vector[i] - weight);
    }
  }

  private void changeWeightWTM(int neuronNumber,double[] vector, int iteration) {
    TreeMap neighboorhood = network.getTopology().getNeighbourhood(neuronNumber);
    Iterator it = neighboorhood.keySet().iterator();
    int neuronNr;
    while(it.hasNext()){
      neuronNr = (Integer)it.next();
      changeNeuronWeightWTM(neuronNr,vector,iteration,(Integer)neighboorhood.get(neuronNr));
    }
  }

  private void changeNeuronWeightWTM(int neuronNumber, double[] vector, int iteration, int distance) {
    double[] weights = network.getNeuron(neuronNumber-1).getWeights();
    int weightNumber = weights.length;
    double weight;
    for (int i=0; i < weightNumber; i++) {
      weight = weights[i];
      weights[i] += learningFactor * GaussNeighbourhood.calc(neighbourhoodRadius, distance) * (vector[i] - weight);
    }
    //network.getNeuron(neuronNumber).setWeights(weights);
  }

  private void changeWeightGas(double[] vector, int iteration) {
    
  }
}
