package nn.som;

import java.util.Random;

/**
 * Neuron with bias and activation function
 */
public class KohonenNeuron {
  private double[] weights;

  public KohonenNeuron(int weightNumber, double[] maxWeights) {
    if (weightNumber != maxWeights.length) {
      throw new IllegalArgumentException("Wrong number of maxWeights");
    }
    Random rand = new Random();
    weights = new double[maxWeights.length];
    for(int i=0; i< weights.length; i++){
      weights[i] = rand.nextDouble() * maxWeights[i];
    }
  }

  public KohonenNeuron(double[] weights){
    this.weights = new double[weights.length];
    setWeights(weights);
  }

  public double[] getWeights() {
    return weights;
  }

  public void setWeights(double[] weights) {
    if (this.weights.length != weights.length) {
      throw new IllegalArgumentException("Wrong input vector length");
    }
    for(int i=0; i< this.weights.length; i++){
      this.weights[i] = weights[i];
    }
  }

  public double getValue(double[] vector) {
    int inputSize = vector.length;
    return EuclidesDistance.calcDistance(weights, vector);
//    if( activationFunction != null)
//      return activationFunction.getValue(value);
//    else
//      return value;
  }

  public String toString(){
    String text="";
    text += "[ ";
    int weightSize = weights.length;
    for (int i = 0; i < weightSize; i++){
      text += weights[i];
      if(i < weightSize - 1) {
        text += ", ";
      }
    }
    text += " ]";
    return text;
  }

}
