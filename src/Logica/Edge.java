package Logica;

public class Edge<T> {
    private Vertex<T> destination;
    private int travelTime;
    private double deliveryFee;

    public Edge(Vertex<T> destination, int travelTime, double deliveryFee) {
        this.destination = destination;
        this.travelTime = travelTime;
        this.deliveryFee = deliveryFee;
    }

    public Vertex<T> getDestination() {
        return destination;
    }

    public int getTravelTime() {
        return travelTime;
    }

    public double getDeliveryFee() {
        return deliveryFee;
    }
}

