package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Classe principale dell'applicazione grafica <b>QTMiner FX</b>.
 * <p>
 * Estende {@link javafx.application.Application} e costituisce
 * il punto d'ingresso dell'interfaccia utente JavaFX del progetto QTMiner.
 * Si occupa di caricare la vista principale definita nel file FXML
 * e di inizializzare la finestra principale dell'applicazione.
 *
 * <p><b>Ruolo:</b> rappresenta il frontend del sistema di clustering
 * <i>Quality Threshold</i>, fornendo un'interfaccia interattiva per
 * l'esecuzione dei comandi e la visualizzazione dei risultati.
 * 
 * @author Mirco Catalano
 * @author Lorenzo Amato
 */
public class QTMinerFxApp extends Application {

    /**
     * Costruttore predefinito dell'applicazione QTMiner FX.
     * <p>
     * Necessario per il corretto avvio del runtime JavaFX.
     */
    public QTMinerFxApp() {
        super();
    }

    /**
     * Avvia l'interfaccia grafica JavaFX e visualizza la scena principale.
     * <p>
     * Questo metodo carica il file {@code MainView.fxml} dalla cartella
     * delle risorse e imposta la finestra con titolo e dimensioni predefinite.
     *
     * @param stage finestra principale fornita dal runtime JavaFX
     * @throws Exception se il file FXML non è accessibile o non può essere caricato
     */
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/gui/view/MainView.fxml"));
        stage.setTitle("QTMiner FX");
        stage.setScene(new Scene(root, 1280, 720));
        stage.show();
    }

    /**
     * Punto di ingresso dell'applicazione QTMiner FX.
     * <p>
     * Inizializza il framework JavaFX e richiama il metodo {@link #start(Stage)}.
     *
     * @param args argomenti della linea di comando
     */
    public static void main(String[] args) {
        launch(args);
    }
}