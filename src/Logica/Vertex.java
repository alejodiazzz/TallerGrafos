package Logica;



import java.util.ArrayList;
import java.util.List;

public class Vertex<T> {
    private T info;
    private boolean visited;
    private List<Edge<T>> adjacents;

    public Vertex(T info) {
        this.info = info;
        this.visited = false;
        this.adjacents = new ArrayList<>();
    }

    public T getInfo() {
        return info;
    }

    // Otros métodos de la clase...

    public boolean isAdjacent(Vertex<T> otherVertex) {
        for (Edge<T> edge : adjacents) {
            if (edge.getDestination().equals(otherVertex)) {
                return true;
            }
        }
        return false;
    }

    public boolean addEdge(Vertex<T> destination, int travelTime, double deliveryFee) {
        Edge<T> edge = new Edge<>(destination, travelTime, deliveryFee);
        return adjacents.add(edge);
    }

    public List<Edge<T>> getEdges() {
        return adjacents;
    }
}
