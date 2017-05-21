package nn.som;

import java.util.Random;

/**
 * Neuron with bias and activation function
 */
public class KohonenNeuron {
  public static final double P_MIN = 0.75;
  private double[] weights;
  private double potential; // potential
  private boolean waiting = false; // needed for potential algorithm

  public KohonenNeuron(int weightNumber, double[][] minmaxWeights, double potential) {
    this.potential = potential;
    if (weightNumber != minmaxWeights.length) {
      throw new IllegalArgumentException("Wrong number of maxWeights");
    }
    Random rand = new Random();
    weights = new double[minmaxWeights.length];
    for(int i=0; i< weights.length; i++) {
      double[] weightsRange = minmaxWeights[i];
      if (weightsRange.length != 2) {
        weights[i] = rand.nextDouble();
      } else {
        // get random number from min - max range
        weights[i] = weightsRange[0] + (weightsRange[1] - weightsRange[0]) * rand.nextDouble();
      }
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

  public double getPotential() {
    return potential;
  }

  public void setPotential(double p) {
    this.potential = p;
  }

  public boolean isWaiting() {
    return waiting;
  }

  public void setWaiting(boolean b) {
    this.waiting = b;
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
