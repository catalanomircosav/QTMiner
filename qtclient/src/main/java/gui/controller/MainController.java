package gui.controller;

import gui.net.QTProtocolClient;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.converter.NumberStringConverter;
import javafx.util.Duration;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Controller principale che gestisce la GUI del progetto.
 * 
 * @version 2.0
 * @author Mirco Catalano
 * @author Lorenzo Amato 
 */
public class MainController {

    /**
     * Costruisce il controller per la gestione della GUI.
     */
    public MainController() { }

    // ===== Connessione =====
    @FXML private TextField hostField;
    @FXML private TextField portField;
    @FXML private Label statusLabel;

    // ===== Database =====
    @FXML private TextField tableNameField;
    @FXML private Slider radiusSlider;
    @FXML private TextField radiusField;

    // ===== File =====
    @FXML private TextField fileTableField;
    @FXML private Slider fileRadiusSlider;
    @FXML private TextField fileRadiusField;

    // ===== Dataset =====
    @FXML private TableView<DataRow> dataTable;

    // ===== Clustering =====
    @FXML private BarChart<String, Number> clusterBarChart;
    @FXML private ScatterChart<Number, Number> clusterScatterChart;

    @FXML private TableView<ClusterRow> clustersTable;
    @FXML private TableColumn<ClusterRow, String> clusterNameCol;
    @FXML private TableColumn<ClusterRow, String> centroidCol;
    @FXML private TableColumn<ClusterRow, Number> sizeCol;
    @FXML private TableColumn<ClusterRow, Number> avgDistanceCol;

    @FXML private TableView<ExampleRow> examplesTable;
    @FXML private TableColumn<ExampleRow, String> exClusterCol;
    @FXML private TableColumn<ExampleRow, String> exExampleCol;
    @FXML private TableColumn<ExampleRow, Number> exDistCol;

    @FXML private ChoiceBox<String> xAxisChoice;
    @FXML private ChoiceBox<String> yAxisChoice;

    @FXML private Label statusFileLabel;

    // ===== Log =====
    @FXML private TextArea logArea;

    // cache header dataset per lo scatter (se caricato)
    private String[] lastHeaders = null;

    private final QTProtocolClient client = new QTProtocolClient();

    // mapping per selezione bidirezionale (tabella <-> grafico)
    private final Map<ExampleRow, XYChart.Data<Number, Number>> row2Point = new HashMap<>();
    private final Map<XYChart.Data<Number, Number>, ExampleRow> point2Row = new HashMap<>();

    /**
     * Metodo che inizializza a valori di default le strutture iterative della GUI.
     */
    @FXML
    public void initialize() {
        hostField.setText("127.0.0.1");
        portField.setText("8080");

        radiusField.textProperty().bindBidirectional(radiusSlider.valueProperty(), new NumberStringConverter());
        fileRadiusField.textProperty().bindBidirectional(fileRadiusSlider.valueProperty(), new NumberStringConverter());

        // Colonne tabelle clustering
        if (clusterNameCol != null) clusterNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        if (centroidCol   != null) centroidCol.setCellValueFactory(new PropertyValueFactory<>("centroid"));
        if (sizeCol       != null) sizeCol.setCellValueFactory(new PropertyValueFactory<>("size"));
        if (avgDistanceCol!= null) avgDistanceCol.setCellValueFactory(new PropertyValueFactory<>("avgDistance"));

        if (exClusterCol  != null) exClusterCol.setCellValueFactory(new PropertyValueFactory<>("cluster"));
        if (exExampleCol  != null) exExampleCol.setCellValueFactory(new PropertyValueFactory<>("example"));
        if (exDistCol     != null) exDistCol.setCellValueFactory(new PropertyValueFactory<>("distance"));

        // selezione dalla tabella -> punto nel grafico
        if (examplesTable != null) {
            examplesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
                if (newV == null) return;
                XYChart.Data<Number, Number> p = row2Point.get(newV);
                if (p != null) highlightPoint(p);
            });
        }

        if (xAxisChoice != null && yAxisChoice != null) {
            xAxisChoice.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> refreshScatter());
            yAxisChoice.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> refreshScatter());
        }

        updateStatus();
    }

    // === CONNESSIONE ===
    /**
     * Metodo che comunica con il server e permette la connessione al server.
     */
    @FXML private void onConnect() {
        try {
            client.connect(hostField.getText().trim(), Integer.parseInt(portField.getText().trim()));
            append("Connesso al server: " + hostField.getText() + ":" + portField.getText());
        } catch (Exception e) { alert("Connessione fallita", e.getMessage()); }
        updateStatus();
    }

    /**
     * Metodo che comunica con il server e permette la disconnessione dal server.
     */
    @FXML private void onDisconnect() {
        try { client.disconnect(); append("Disconnesso."); }
        catch (Exception e) { alert("Errore disconnessione", e.getMessage()); }
        updateStatus();
    }

    /**
     * Metodo che permette di pulire la console del log
     */
    @FXML private void onClearLog() {
        logArea.clear();
    }

    // === CARICAMENTO TABELLA ===
    /**
     * Metodo che permette di  visualizzare la tabella del dataset nella GUI.
     */
    @FXML private void onLoadTable() {
        if (!ensureConnected()) return;
        try {
            String table = tableNameField.getText().trim();
            String resp = client.loadTableFromDb(table);
            append(resp);

            String[] lines = resp.strip().split("\\r?\\n");
            if (lines.length < 2) return;

            // headers: togli [] e normalizza
            String headerLine = lines[0].replaceAll("\\[[^]]*\\]", "").replaceAll("\\s+", " ").trim();
            String[] headers = Arrays.stream(headerLine.split(",\\s*")).map(String::trim).toArray(String[]::new);

            // crea colonne dinamiche
            dataTable.getColumns().clear();
            for (int colIndex = 0; colIndex < headers.length; colIndex++) {
                final int ci = colIndex;
                TableColumn<DataRow, String> col = new TableColumn<>(headers[colIndex]);
                col.setCellValueFactory(cell -> cell.getValue().getValueAt(ci));
                dataTable.getColumns().add(col);
            }

            // righe
            ObservableList<DataRow> rows = FXCollections.observableArrayList();
            for (int i = 1; i < lines.length; i++) {
                String line = lines[i].replaceFirst("^\\d+:\\s*", "").trim();
                if (line.isEmpty()) continue;
                rows.add(new DataRow(line.split(",\\s*")));
            }
            dataTable.setItems(rows);
            lastHeaders = headers;

            append("Tabella caricata con " + rows.size() + " righe e " + headers.length + " colonne.");
        } catch (Exception e) { alert("Errore caricamento tabella", e.getMessage()); }
    }

    // === CLUSTERING ===
    @FXML private void onClusterFromDB() {
        if (!ensureConnected()) return;
        try {
            String resp = client.clusterFromDb(radiusSlider.getValue());
            append(resp);
            ChartsData cd = ChartsData.fromServerText(resp);
            renderCharts(cd);
        } catch (Exception e) { alert("Errore clustering DB", e.getMessage()); }
    }

    @FXML private void onClusterFromFile() {
        if (!ensureConnected()) return;
        try {
            String resp = client.clusterFromFile(fileTableField.getText().trim(), fileRadiusSlider.getValue());
            append(resp);
            ChartsData cd = ChartsData.fromServerText(resp);
            renderCharts(cd);
        } catch (Exception e) { alert("Errore clustering file", e.getMessage()); }
    }

    @FXML
    private void onSaveClusters() {
        if (!ensureConnected()) return;
        try {
            String resp = client.saveClustersToFile();
            append(resp);
            showStatus("Clustering salvato correttamente!", "green");
        } catch (Exception e) {
            alert("Errore salvataggio", e.getMessage());
            showStatus("Errore durante il salvataggio: " + e.getMessage(), "red");
        }
    }

    private void showStatus(String message, String color) {
        statusFileLabel.setText(message);
        statusFileLabel.setStyle("-fx-text-fill: " + color + ";");

        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(e -> statusFileLabel.setText(""));
        pause.play();
    }



    // === VISUALIZZAZIONE ===
    private static class ChartsData {
        List<String> clusterNames   = new ArrayList<>();
        List<Integer> clusterSizes  = new ArrayList<>();
        List<List<String[]>> members= new ArrayList<>();
        List<String> centroidsText  = new ArrayList<>();
        List<Double> avgDistances   = new ArrayList<>();
        List<ExampleRow> examples   = new ArrayList<>();

        static ChartsData fromServerText(String text) {
            ChartsData cd = new ChartsData();
            if (text == null || text.isEmpty()) return cd;

            // blocchi:
            // 0: Centroid=(...)\nExamples:\n[tuple] dist=...\n...\nAvgDistance=...
            Pattern block = Pattern.compile(
                "(?ms)^\\s*(\\d+)\\s*:\\s*Centroid=\\(([^)]+)\\)\\s*\\R+Examples:\\s*(.*?)\\R+AvgDistance=([0-9\\.]+)",
                Pattern.MULTILINE | Pattern.DOTALL
            );
            Matcher mb = block.matcher(text);

            while (mb.find()) {
                String id            = mb.group(1).trim();
                String centroidVals  = mb.group(2).trim();
                String examplesBlock = mb.group(3).trim();
                double avgDist       = Double.parseDouble(mb.group(4).trim());

                cd.clusterNames.add("C" + id);
                cd.centroidsText.add(centroidVals);
                cd.avgDistances.add(avgDist);

                Pattern ex = Pattern.compile("\\[([^\\]]+)\\]\\s*dist=([0-9\\.]+)");
                Matcher me = ex.matcher(examplesBlock);

                List<String[]> rows = new ArrayList<>();
                while (me.find()) {
                    String tuple = me.group(1).trim();                 // es: sunny 30.3 high weak yes
                    String[] vals = tuple.split("\\s*,\\s*|\\s+");     // supporto sia “,” che spazi
                    rows.add(vals);
                    cd.examples.add(new ExampleRow("C" + id, tuple, Double.parseDouble(me.group(2))));
                }
                cd.members.add(rows);
                cd.clusterSizes.add(rows.size());
            }
            return cd;
        }
    }

    private void renderCharts(ChartsData cd) {
        // reset map selezioni
        row2Point.clear();
        point2Row.clear();

        // --- BarChart ---
        clusterBarChart.getData().clear();
        XYChart.Series<String, Number> bar = new XYChart.Series<>();
        bar.setName("Dimensione cluster");
        for (int i = 0; i < cd.clusterNames.size(); i++) {
            bar.getData().add(new XYChart.Data<>(cd.clusterNames.get(i), cd.clusterSizes.get(i)));
        }
        clusterBarChart.getData().add(bar);

        // --- Scatter ---
        clusterScatterChart.getData().clear();

        // decidi colonne x,y:
        String[] headersForPlot = (lastHeaders != null && lastHeaders.length > 0)
                ? lastHeaders
                : inferHeadersFromAny(cd);

        int[] xy = chooseXYColumns(cd, headersForPlot.length);
        final int xCol = xy[0];
        final int yCol = xy[1];

        Map<Integer, Map<String, Integer>> encoders = new HashMap<>();

        for (int i = 0; i < cd.members.size(); i++) {
            XYChart.Series<Number, Number> s = new XYChart.Series<>();
            s.setName(cd.clusterNames.get(i));
            List<String[]> rows = cd.members.get(i);
            for (int r = 0; r < rows.size(); r++) {
                String[] vals = rows.get(r);
                double x = encode(vals, xCol, encoders);
                double y = encode(vals, yCol, encoders);

                XYChart.Data<Number, Number> point = new XYChart.Data<>(x, y);
                s.getData().add(point);

                // collega ExampleRow → point
                ExampleRow er = findExampleRow(cd.examples, cd.clusterNames.get(i), vals);
                if (er != null) {
                    row2Point.put(er, point);
                    point2Row.put(point, er);
                }
            }

            if (lastHeaders != null && xAxisChoice != null && yAxisChoice != null) {
                ObservableList<String> headersList = FXCollections.observableArrayList(lastHeaders);
                xAxisChoice.setItems(headersList);
                yAxisChoice.setItems(headersList);

                if (xAxisChoice.getValue() == null && headersList.size() > 0)
                    xAxisChoice.setValue(headersList.get(0));
                if (yAxisChoice.getValue() == null && headersList.size() > 1)
                    yAxisChoice.setValue(headersList.get(1));
            }

            clusterScatterChart.getData().add(s);
        }

        // applica nodi interattivi quando i Node sono creati
        Platform.runLater(this::wireScatterPoints);

        // --- Tabelle ---
        clustersTable.getItems().clear();
        examplesTable.getItems().clear();

        ObservableList<ClusterRow> clusters = FXCollections.observableArrayList();
        for (int i = 0; i < cd.clusterNames.size(); i++) {
            clusters.add(new ClusterRow(
                cd.clusterNames.get(i),
                cd.centroidsText.size() > i ? cd.centroidsText.get(i) : cd.clusterNames.get(i),
                cd.clusterSizes.get(i),
                cd.avgDistances.size() > i ? cd.avgDistances.get(i) : null
            ));
        }
        
        clustersTable.setItems(clusters);
        examplesTable.setItems(FXCollections.observableArrayList(cd.examples));
    }

    /** Ridisegna solo lo scatter chart quando cambiano gli assi */
    private void refreshScatter() {
        if (xAxisChoice == null || yAxisChoice == null || clusterScatterChart == null) return;
        if (lastHeaders == null || lastHeaders.length == 0) return;
        if (clusterScatterChart.getData().isEmpty()) return;

        String xLabel = xAxisChoice.getValue();
        String yLabel = yAxisChoice.getValue();
        if (xLabel == null || yLabel == null) return;

        int xCol = Arrays.asList(lastHeaders).indexOf(xLabel);
        int yCol = Arrays.asList(lastHeaders).indexOf(yLabel);
        if (xCol < 0 || yCol < 0) return;

        // Recupera l’ultima struttura dati dai punti già presenti
        // (rende l’interfaccia reattiva senza nuovo clustering)
        Map<Integer, Map<String, Integer>> encoders = new HashMap<>();
        clusterScatterChart.getData().clear();

        // rigenera i punti usando gli ExampleRow correnti
        for (ClusterRow cluster : clustersTable.getItems()) {
            XYChart.Series<Number, Number> s = new XYChart.Series<>();
            s.setName(cluster.getName());

            for (ExampleRow row : examplesTable.getItems()) {
                if (!row.getCluster().equals(cluster.getName())) continue;
                String[] vals = row.getExample().split("\\s*,\\s*|\\s+");
                double x = encode(vals, xCol, encoders);
                double y = encode(vals, yCol, encoders);
                s.getData().add(new XYChart.Data<>(x, y));
            }
            clusterScatterChart.getData().add(s);
        }
        Platform.runLater(this::wireScatterPoints);
    }

    /** crea i Node dei punti e aggiunge click + hover */
    private void wireScatterPoints() {
        for (XYChart.Series<Number, Number> s : clusterScatterChart.getData()) {
            for (XYChart.Data<Number, Number> d : s.getData()) {
                if (d.getNode() == null) continue;
                d.getNode().setCursor(Cursor.HAND);
                d.getNode().getStyleClass().add("scatter-point");

                d.getNode().setOnMouseClicked(ev -> {
                    ExampleRow row = point2Row.get(d);
                    if (row != null) {
                        examplesTable.getSelectionModel().select(row);
                        examplesTable.scrollTo(row);
                    }
                    highlightPoint(d);
                });
            }
        }
    }

    /** evidenzia un punto (e attenua gli altri) */
    private void highlightPoint(XYChart.Data<Number, Number> target) {
        for (XYChart.Series<Number, Number> s : clusterScatterChart.getData()) {
            for (XYChart.Data<Number, Number> d : s.getData()) {
                if (d.getNode() == null) continue;
                if (d == target) {
                    d.getNode().setStyle("-fx-scale-x: 1.6; -fx-scale-y: 1.6; -fx-effect: dropshadow(gaussian, rgba(20,152,255,0.8), 10, 0, 0, 0);");
                } else {
                    d.getNode().setStyle("-fx-scale-x: 1.0; -fx-scale-y: 1.0; -fx-effect: null;");
                }
            }
        }
    }

    /** tenta di trovare l’ExampleRow corrispondente ai valori vals in quel cluster */
    private ExampleRow findExampleRow(List<ExampleRow> all, String clusterName, String[] vals) {
        String flat = String.join(" ", vals).trim();
        for (ExampleRow er : all) {
            if (er.getCluster().equals(clusterName) && er.getExample().equals(flat)) return er;
        }
        return null;
    }

    /** se non ho headers, li inferisco dalla lunghezza della prima tupla disponibile */
    private String[] inferHeadersFromAny(ChartsData cd) {
        for (List<String[]> rows : cd.members) {
            if (!rows.isEmpty()) {
                int n = rows.get(0).length;
                String[] h = new String[n];
                for (int i = 0; i < n; i++) h[i] = "Col" + (i + 1);
                lastHeaders = h; // salva per usi futuri
                return h;
            }
        }
        lastHeaders = new String[] {"Col1", "Col2"};
        return lastHeaders;
    }

    /** sceglie due colonne per lo scatter: prima prova due numeriche, altrimenti 0 e 1 */
    private int[] chooseXYColumns(ChartsData cd, int cols) {
        boolean[] isNumeric = new boolean[cols];
        Arrays.fill(isNumeric, true);

        // controlla i valori presenti
        for (List<String[]> clusterRows : cd.members) {
            for (String[] tuple : clusterRows) {
                for (int c = 0; c < Math.min(cols, tuple.length); c++) {
                    if (isNumeric[c]) {
                        try { Double.parseDouble(tuple[c].replace(',', '.')); }
                        catch (Exception e) { isNumeric[c] = false; }
                    }
                }
            }
        }

        // trova prime due colonne numeriche
        List<Integer> numIdx = new ArrayList<>();
        for (int i = 0; i < cols; i++) if (isNumeric[i]) numIdx.add(i);

        if (numIdx.size() >= 2) return new int[]{ numIdx.get(0), numIdx.get(1) };
        if (cols >= 2) return new int[]{ 0, 1 };
        return new int[]{ 0, 0 };
    }

    /** encoding: numerico -> double; categorico -> indice per colonna */
    private double encode(String[] vals, int col, Map<Integer, Map<String, Integer>> encoders) {
        if (col >= vals.length) return 0.0;
        String raw = vals[col].trim();
        try { return Double.parseDouble(raw.replace(',', '.')); }
        catch (Exception ignored) {
            Map<String, Integer> map = encoders.computeIfAbsent(col, k -> new LinkedHashMap<>());
            return map.computeIfAbsent(raw, k2 -> map.size());
        }
    }

    // === UTILS ===
    private boolean ensureConnected() {
        if (!client.isConnected()) {
            alert("Non connesso", "Connettiti prima al server.");
            return false;
        }
        return true;
    }

    private void updateStatus() {
        boolean connected = client.isConnected();
        statusLabel.setText(connected ? "Connesso" : "Non connesso");
        statusLabel.getStyleClass().removeAll("connected", "disconnected");
        statusLabel.getStyleClass().add(connected ? "connected" : "disconnected");
    }

    private void append(String msg) { if (logArea != null) logArea.appendText(msg + "\n"); }

    private void alert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        a.setHeaderText(title);
        a.showAndWait();
        append("! " + title + ": " + msg);
    }

    // === CLASSI DI SUPPORTO ===

    /**
     * Rappresenta una riga generica di dati da visualizzare in una tabella JavaFX.
     * <p>
     * È una classe di supporto utilizzata per gestire in modo dinamico i valori
     * associati alle colonne di una tabella, rendendoli compatibili con il modello
     * osservabile di JavaFX.
     */
    public static class DataRow {
        /** Array contenente i valori della riga. */
        private final String[] values;

        /**
         * Costruisce una riga dati a partire da un insieme di valori testuali.
         *
         * @param v array di stringhe che rappresenta i valori delle colonne
         */
        public DataRow(String[] v) { values = v; }

        /**
         * Restituisce un valore osservabile associato all'indice specificato.
         * <p>
         * Se l'indice non è valido, viene restituita una stringa vuota come valore
         * predefinito.
         *
         * @param i indice del valore da restituire
         * @return un {@link ObservableValue} contenente il valore richiesto
         */
        public ObservableValue<String> getValueAt(int i) {
            String val = (i >= 0 && i < values.length) ? values[i] : "";
            return new SimpleStringProperty(val);
        }
    }

    /**
     * Modello di riga per la tabella che visualizza le informazioni sui cluster
     * generati dall'algoritmo di Quality Threshold (QT).
     * <p>
     * Ogni riga rappresenta un singolo cluster, mostrando il suo nome,
     * il centroide, la dimensione e la distanza media dei punti rispetto al centro.
     */
    public static class ClusterRow {
        /** Nome identificativo del cluster. */
        private final String name;

        /** Rappresentazione testuale del centroide del cluster. */
        private final String centroid;

        /** Numero di elementi contenuti nel cluster. */
        private final Number size;

        /** Distanza media dei punti del cluster dal centroide. */
        private final Number avgDistance;

        /**
         * Costruisce una riga di tabella che rappresenta un cluster.
         *
         * @param n nome del cluster
         * @param c rappresentazione del centroide
         * @param s dimensione del cluster
         * @param avg distanza media dal centroide
         */
        public ClusterRow(String n, String c, Number s, Number avg) {
            name = n;
            centroid = c;
            size = s;
            avgDistance = avg;
        }

        /** 
         * Restituisce il nome del cluster.
         * 
         * @return nome del cluster */
        public String getName() { return name; }

        /** 
         * Restituisce il centroide del cluster.
         * 
         * @return centroide del cluster */
        public String getCentroid() { return centroid; }

        /** 
         * Restituisce il numero di elementi nel cluster.
         * 
         * @return numero di elementi nel cluster */
        public Number getSize() { return size; }

        /**
         * Restituisce la distanza media dei punti dal centroide
         * 
         * @return distanza media dei punti dal centroide */
        public Number getAvgDistance() { return avgDistance; }
    }

    /**
     * Modello di riga per la tabella che visualizza i singoli esempi
     * (o punti) appartenenti a un determinato cluster.
     * <p>
     * È utilizzata per rappresentare i risultati dell’analisi di clustering,
     * consentendo di mostrare l’appartenenza di ciascun esempio e la sua distanza
     * dal centro del cluster.
     */
    public static class ExampleRow {
        /** Nome del cluster a cui appartiene l'esempio. */
        private final String cluster;

        /** Rappresentazione testuale dell'esempio. */
        private final String example;

        /** Distanza dell'esempio dal centroide del cluster. */
        private final Number distance;

        /**
         * Costruisce una riga contenente le informazioni su un esempio.
         *
         * @param c nome del cluster di appartenenza
         * @param e rappresentazione testuale dell’esempio
         * @param d distanza dell’esempio dal centroide
         */
        public ExampleRow(String c, String e, Number d) {
            cluster = c;
            example = e;
            distance = d;
        }

        /** 
         * Restituisce il nome del cluster di appartenenza.
         * 
         * @return nome del cluster di appartenenza */
        public String getCluster() { return cluster; }

        /** 
         * Restituisce la rappresentazione testuale dell'esempio
         * 
         * @return rappresentazione testuale dell’esempio */
        public String getExample() { return example; }

        /** 
         * Restituisce la distanza dal centroide del cluster.
         * 
         * @return distanza dal centroide del cluster */
        public Number getDistance() { return distance; }
    }

}
