package nn.som;

public class GaussNeighbourhood {
  public static final double calc(double radius, double distance) {
    return java.lang.Math.exp(-(java.lang.Math.pow(distance,2))/ (2 * radius * radius ));
  }
}
