package nn.som;

public class TransientNeuron {
  // reference neuron
  private final KohonenNeuron neuron;
  // temporary distance from the vector
  private final double distance;

  public TransientNeuron(final KohonenNeuron neuron, final double distance) {
    this.neuron = neuron;
    this.distance = distance;
  }

  public KohonenNeuron getNeuron() {
    return neuron;
  }

  public double getDistance() {
    return distance;
  }
}
