package nn.som;

import java.util.*;

public class Learning {
  private Network network;
  private int maxIterations=20;
  private double[][] learningData;

  private double learningFactor = 0.8;
  private double minLearningFactpr = 0.01;
  private double maxLearningFactor = learningFactor;

  private double neighbourhoodRadius = 0.8;
  private double minNeighbourhoodRadius = 0.1;
  private double maxNeighbourhoodRadius = neighbourhoodRadius;

  private String dumpFilePrefix;
  private String dumpFileExt;
  private boolean dumpIteration;


  public Learning(final Network network, final int maxIterations, final double[][] learningData, final double learningFactor) {
    this.network = network;
    this.maxIterations = maxIterations;
    this.learningData = learningData;
    this.learningFactor = learningFactor;
    this.maxLearningFactor = learningFactor;
  }

  public Learning(final Network network, final int maxIterations, final double[][] learningData, final double learningFactor, final double neighbourhoodRadius) {
    this(network, maxIterations, learningData, learningFactor);
    this.neighbourhoodRadius = neighbourhoodRadius;
    this.maxNeighbourhoodRadius = neighbourhoodRadius;
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
        //if (i <= 20) {
          changeNeuronPotential(bestNeuron);
        //}
//        else if (i == 21) {
//          for (int k = 0; k < network.getNumOfNeurons(); k++) {
//            network.getNeuron(k).setWaiting(false);
//          }
//        }
      }
      if (dumpIteration && dumpFilePrefix != null) {
        FileUtils.saveNetworkToFile(network, dumpFilePrefix+(i+1)+(dumpFileExt != null ? dumpFileExt : ""));
      }
      changeLearningFactor(i+1);
    }
  }

  public void learnWTM(){
    int bestNeuron = 0;
    double[] vector;
    int dataSize = learningData.length;
    for (int i = 0; i < maxIterations; i++){
      for(int j= 0; j < dataSize; j++){
        vector = learningData[j];
        bestNeuron = getBestNeuron(vector);
        changeWeightWTM(bestNeuron, vector, i);
        changeNeuronPotential(bestNeuron);
      }
      if (dumpIteration && dumpFilePrefix != null) {
        FileUtils.saveNetworkToFile(network, dumpFilePrefix+(i+1)+(dumpFileExt != null ? dumpFileExt : ""));
      }
      changeNeighbourhoodRadius(i);
      changeLearningFactor(i);
    }
  }

  public void learnGas() {
    int dataSize = learningData.length;
    for (int i = 0; i < maxIterations; i++) {
      for (int j = 0; j < dataSize; j++) {
        double[] vector = learningData[j];
        changeWeightGas(vector, i);
      }
      if (dumpIteration && dumpFilePrefix != null) {
        FileUtils.saveNetworkToFile(network, dumpFilePrefix+(i+1)+(dumpFileExt != null ? dumpFileExt : ""));
      }
      changeNeighbourhoodRadius(i);
      changeLearningFactor(i);
    }
  }

  private int getBestNeuron(double[] vector){
    KohonenNeuron tempNeuron;
    double distance, bestDistance = -1;
    int networkSize = network.getNumOfNeurons();
    int bestNeuron = 0;
    for(int i = 0; i < networkSize; i++){
      tempNeuron = network.getNeuron(i);
      if (tempNeuron.isWaiting()) {
        continue;
      }
      distance = EuclidesDistance.calcDistance(tempNeuron.getWeights(), vector);
      if((distance < bestDistance) || (bestDistance == -1)){
        bestDistance = distance;
        bestNeuron = i;
      }
    }
    return bestNeuron;
  }

  private void changeNeuronWeightWTA(int neuronNumber, double[] vector, int iteration) {
    KohonenNeuron neuron = network.getNeuron(neuronNumber);
    double[] weights = neuron.getWeights();
    int weightNumber = weights.length;
    double weight;
    for (int i = 0; i < weightNumber; i++) {
      weight = weights[i];
      weights[i] += learningFactor * (vector[i] - weight);
    }
  }

  private void changeWeightWTM(int neuronNumber,double[] vector, int iteration) {
    TreeMap neighboorhood = network.getTopology().getNeighbourhood(neuronNumber);
    // IMPORTANT: change weight of the best neuron
    changeNeuronWeightWTM(neuronNumber, vector, iteration, 0);
    // IMPORTANT: change weights of the neurons from the neighbourhood
    Iterator it = neighboorhood.keySet().iterator();
    while(it.hasNext()){
      int neuronNr = (Integer)it.next();
      changeNeuronWeightWTM(neuronNr,vector,iteration,(Integer)neighboorhood.get(neuronNr));
    }
  }

  private void changeNeuronWeightWTM(int neuronNumber, double[] vector, int iteration, int distance) {
    KohonenNeuron neuron = network.getNeuron(neuronNumber);
    double[] weights = neuron.getWeights();
    int weightNumber = weights.length;
    double weight;
    for (int i=0; i < weightNumber; i++) {
      weight = weights[i];
      weights[i] += learningFactor * GaussNeighbourhood.calc(neighbourhoodRadius, distance) * (vector[i] - weight);
    }
  }

  private void changeNeuronPotential(int bestNeuron) {
    for (int i = 0; i < network.getNumOfNeurons(); i++) {
      KohonenNeuron neuron = network.getNeuron(i);
      neuron.setWaiting(false);
      if (i == bestNeuron) {
        neuron.setPotential(neuron.getPotential() - KohonenNeuron.P_MIN);
      } else {
        neuron.setPotential(neuron.getPotential() + 1.0/(double)network.getNumOfNeurons());
      }
      if (neuron.getPotential() > 1.0) {
        neuron.setPotential(1.0);
      }
      if (neuron.getPotential() < KohonenNeuron.P_MIN) {
        neuron.setWaiting(true);
      }
    }
  }

  private void changeWeightGas(double[] vector, int iteration) {
    // calculate distance of every neuron from the vector
    List<TransientNeuron> transientNeurons = new ArrayList<>();
    for (int i = 0; i < network.getNumOfNeurons(); i++) {
      KohonenNeuron neuron = network.getNeuron(i);
      double distance = EuclidesDistance.calcDistance(neuron.getWeights(), vector);
      transientNeurons.add(new TransientNeuron(neuron, distance));
    }

    // sort neurons by the distance from input vector
    TransientNeuron[] ordered = transientNeurons.stream()
        .sorted((n1, n2) -> {
          if (n1.getDistance() > n2.getDistance()) {
            return 1;
          } else if (n1.getDistance() < n2.getDistance()) {
            return -1;
          } else {
            return 0;
          }
        })
        .toArray(TransientNeuron[]::new);

    for (int i = 0; i < ordered.length; i++) {
      double[] weights = ordered[i].getNeuron().getWeights();
      double factor = Math.exp(-i/neighbourhoodRadius);
      for (int j = 0; j < weights.length; j++) {
        weights[j] += learningFactor * factor * (vector[j] - weights[j]);
      }
    }
  }

  private void changeNeighbourhoodRadius(int iteration) {
    neighbourhoodRadius = maxNeighbourhoodRadius * Math.pow(minNeighbourhoodRadius / maxNeighbourhoodRadius, iteration / maxIterations);
  }

  private void changeLearningFactor(int iteration) {
    learningFactor = maxLearningFactor * Math.pow(minLearningFactpr / maxLearningFactor, iteration / maxIterations);
  }
}
