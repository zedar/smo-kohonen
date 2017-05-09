package nn.som;

public class EuclidesDistance {
  public static final double calcDistance(double[] fromVector, double[] toVector) {
    if(fromVector.length != toVector.length)
      return -1;

    double sum = 0;

    for(int i=0; i< fromVector.length; i++){
      double w = fromVector[i];
      double x = toVector[i];
      sum += (x - w) *( x - w);
    }

    return Math.sqrt(sum);
  }
}
