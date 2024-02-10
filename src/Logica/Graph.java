package Logica;

import java.util.*;

public class Graph<T> {
    private Comparator<T> comparator;
    public static int DIRECT = 0;
    public static int INDIRECT = 1;

    private List<Vertex<T>> vertices;

    public Graph(Comparator<T> comparator) {
        this.comparator = comparator;
        this.vertices = new ArrayList<>();
    }

    public void addVertex(T info) {
        Vertex<T> newVertex = new Vertex<>(info);
        vertices.add(newVertex);
    }

    public boolean addEdge(Vertex<T> source, Vertex<T> destination, int type, int travelTime, double deliveryFee) {
        if (!vertices.contains(source) || !vertices.contains(destination)) {
            throw new IllegalArgumentException("Tanto el vértice de origen como el de destino deben existir");
        }

        if (type == DIRECT || type == INDIRECT) {
            if (!source.isAdjacent(destination)) {
                source.addEdge(destination, travelTime, deliveryFee);
                return true;
            }
        }

        return false;
    }

    public Vertex<T> findVertex(T info) {
        for (Vertex<T> vertex : vertices) {
            if (comparator.compare(vertex.getInfo(), info) == 0) {
                return vertex;
            }
        }
        return null;
    }

    public List<T> getAdjacents(Vertex<T> vertex) {
        List<T> adjacentInfos = new ArrayList<>();
        for (Edge<T> edge : vertex.getEdges()) {
            adjacentInfos.add(edge.getDestination().getInfo());
        }
        return adjacentInfos;
    }

    public boolean isAdyacent(Vertex<T> source, Vertex<T> destination) {
        return source.isAdjacent(destination);
    }

    public List<T> listGraph(Vertex<T> vertex) {
        List<T> graphInfo = new ArrayList<>();
        for (Vertex<T> v : vertices) {
            graphInfo.add(v.getInfo());
        }
        return graphInfo;
    }

    public List<T> listGraph() {
        List<T> graphInfo = new ArrayList<>();
        for (Vertex<T> vertex : vertices) {
            graphInfo.add(vertex.getInfo());
        }
        return graphInfo;
    }

    public void removeVertex(Vertex<T> vertex) {
        if (!vertices.contains(vertex)) {
            throw new IllegalArgumentException("El vértice no existe en el grafo");
        }

        vertices.remove(vertex);

        for (Vertex<T> v : vertices) {
            List<Edge<T>> edgesToRemove = new ArrayList<>();
            for (Edge<T> edge : v.getEdges()) {
                if (edge.getDestination().equals(vertex)) {
                    edgesToRemove.add(edge);
                }
            }
            v.getEdges().removeAll(edgesToRemove);
        }
    }

    public void simulatePackageDelivery(Vertex<T> start, Vertex<T> destination) {
        Map<Vertex<T>, Integer> distanceMap = new HashMap<>();
        Map<Vertex<T>, Double> costMap = new HashMap<>();
        PriorityQueue<Vertex<T>> priorityQueue = new PriorityQueue<>(Comparator.comparingDouble(costMap::get));

        for (Vertex<T> vertex : vertices) {
            distanceMap.put(vertex, Integer.MAX_VALUE);
            costMap.put(vertex, Double.MAX_VALUE);
        }

        distanceMap.put(start, 0);
        costMap.put(start, 0.0);
        priorityQueue.add(start);

        while (!priorityQueue.isEmpty()) {
            Vertex<T> current = priorityQueue.poll();

            for (Edge<T> edge : current.getEdges()) {
                Vertex<T> neighbor = edge.getDestination();
                int newDistance = distanceMap.get(current) + edge.getTravelTime();
                double newCost = costMap.get(current) + edge.getDeliveryFee();

                if (newDistance < distanceMap.get(neighbor)) {
                    distanceMap.put(neighbor, newDistance);
                    costMap.put(neighbor, newCost);
                    priorityQueue.add(neighbor);
                }
            }
        }

        int totalTime = distanceMap.get(destination);
        double totalCost = costMap.get(destination);

        System.out.println("Tiempo de viaje total: " + totalTime + " minutos");
        System.out.println("Costo total de entrega: $" + totalCost);
    }
}
