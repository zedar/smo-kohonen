package nn.som;

public class Network {
  private KohonenNeuron[] neurons;
  private MatrixTopology topology;

  public Network(double[][] minmaxWeights, double initNeuronPotential, MatrixTopology topology) {
    this.topology = topology;
    int numOfNeurons = topology.getNumbersOfNeurons();
    this.neurons = new KohonenNeuron[numOfNeurons];
    for (int i = 0; i < numOfNeurons; i++) {
      this.neurons[i] = new KohonenNeuron(minmaxWeights.length, minmaxWeights, initNeuronPotential);
    }
  }

  public KohonenNeuron getNeuron(int number) {
    return neurons[number];
  }

  public int getNumOfNeurons() {
    return neurons.length;
  }

  public MatrixTopology getTopology() {
    return this.topology;
  }

  public String toString(){
    String text = "";
    for (int i=0; i< neurons.length; i++ ){
      text +="Neuron number "+ (i + 1) + ": " +  neurons[i];
      if (i < neurons.length - 1){
        text += "\n";
      }
    }
    return text;
  }
}
