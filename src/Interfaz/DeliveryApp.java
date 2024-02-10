package Interfaz;

import javax.swing.*;

import Logica.Graph;
import Logica.Vertex;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DeliveryApp {
    private JFrame frame;
    private Graph<String> trophicGraph;
    private JComboBox<String> sourceCityComboBox;
    private JComboBox<String> destinationCityComboBox;
    private List<String> cityList;

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    DeliveryApp window = new DeliveryApp();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error al iniciar la aplicación: " + e.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public DeliveryApp() {
        initialize();
        trophicGraph = new Graph(Comparator.naturalOrder());
        cityList = new ArrayList<>();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 700, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(new GridBagLayout()); // Cambiado a GridBagLayout

        JTextField cityTextField = new JTextField();
        sourceCityComboBox = new JComboBox<>();
        destinationCityComboBox = new JComboBox<>();
        JButton addCityButton = new JButton("Agregar Ciudad");
        JButton addConnectionButton = new JButton("Agregar Conexión");
        JButton simulateDeliveryButton = new JButton("Simular Entrega");

        GridBagConstraints gbc = new GridBagConstraints(); // Configuración para GridBagLayout
    
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Nombre de la Ciudad:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        panel.add(cityTextField, gbc);

        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(addCityButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Ciudad de Origen:"), gbc);

        gbc.gridx = 3;
        gbc.gridy = 2;
        panel.add(sourceCityComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Ciudad de Destino:"), gbc);

        gbc.gridx = 3;
        gbc.gridy = 3;
        panel.add(destinationCityComboBox, gbc);

        gbc.gridx = 3;
        gbc.gridy = 4;
        panel.add(addConnectionButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 4;
        panel.add(simulateDeliveryButton, gbc);

        JTextArea resultTextArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(resultTextArea);
        frame.getContentPane().add(scrollPane, BorderLayout.SOUTH);

        addCityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cityName = cityTextField.getText();
                if (!cityName.isEmpty() && !cityList.contains(cityName)) {
                    trophicGraph.addVertex(cityName);
                    cityList.add(cityName);
                    cityTextField.setText("");
                    updateCityComboBoxes();
                    JOptionPane.showMessageDialog(null, "Ciudad agregada: " + cityName);
                } else {
                    JOptionPane.showMessageDialog(null, "Ingrese un nombre de ciudad válido y único");
                }
            }
        });

        addConnectionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sourceCity = sourceCityComboBox.getSelectedItem().toString();
                String destinationCity = destinationCityComboBox.getSelectedItem().toString();

                if (!sourceCity.isEmpty() && !destinationCity.isEmpty()) {
                    int travelTime = Integer.parseInt(JOptionPane.showInputDialog("Tiempo de Viaje (minutos):"));
                    double deliveryFee = Double.parseDouble(JOptionPane.showInputDialog("Tarifa de Entrega:"));

                    trophicGraph.addEdge(
                            trophicGraph.findVertex(sourceCity),
                            trophicGraph.findVertex(destinationCity),
                            Graph.INDIRECT,
                            travelTime,
                            deliveryFee
                    );
                    JOptionPane.showMessageDialog(null, "Conexión agregada entre " + sourceCity + " y " + destinationCity);
                } else {
                    JOptionPane.showMessageDialog(null, "Seleccione ciudades de origen y destino válidas");
                }
            }
        });

        simulateDeliveryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String startCity = sourceCityComboBox.getSelectedItem().toString();
                String destinationCity = destinationCityComboBox.getSelectedItem().toString();

                if (!startCity.isEmpty() && !destinationCity.isEmpty()) {
                    Vertex<String> startVertex = trophicGraph.findVertex(startCity);
                    Vertex<String> destinationVertex = trophicGraph.findVertex(destinationCity);

                    if (startVertex != null && destinationVertex != null) {
                        trophicGraph.simulatePackageDelivery(startVertex, destinationVertex);
                        updateResultTextArea(resultTextArea, String.join("\n", trophicGraph.listGraph()));
                    } else {
                        JOptionPane.showMessageDialog(null, "Las ciudades de inicio y destino deben existir en el grafo");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Seleccione ciudades de inicio y destino válidas");
                }
            }
        });
    }

    private void updateCityComboBoxes() {
        DefaultComboBoxModel<String> sourceModel = new DefaultComboBoxModel<>(cityList.toArray(new String[0]));
        DefaultComboBoxModel<String> destinationModel = new DefaultComboBoxModel<>(cityList.toArray(new String[0]));

        sourceCityComboBox.setModel(sourceModel);
        destinationCityComboBox.setModel(destinationModel);
    }

    private void updateResultTextArea(JTextArea resultTextArea, String text) {
        resultTextArea.setText(text);
    }
}

