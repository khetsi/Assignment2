package mediaplayer;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class MediaPlayerController implements Initializable {
    
    private String path;
    private MediaPlayer mediaPlayer;
    
    @FXML
    private MediaView mediaView;
    
    @FXML
    private Slider volumeSlider;
    
    @FXML
    private Slider progressBar;
    
    
    @FXML
    private StackPane pane;
    
    @FXML
    private void OpenFileMethod(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        path = file.toURI().toString();

        if(path != null){
            Media media = new Media(path);
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);
            
            //creating bindings
            DoubleProperty widthProp = mediaView.fitWidthProperty();
            DoubleProperty heightProp = mediaView.fitHeightProperty();
            
            widthProp.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
            heightProp.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));
            
            volumeSlider.setValue(mediaPlayer.getVolume()*100);
            volumeSlider.valueProperty().addListener((Observable observable) -> {
                mediaPlayer.setVolume(volumeSlider.getValue()/100);
            });
            
            mediaPlayer.currentTimeProperty().addListener((ObservableValue<? extends javafx.util.Duration> observable, javafx.util.Duration oldValue, javafx.util.Duration newValue) -> {
                progressBar.setValue(newValue.toSeconds());
            });
            
            progressBar.setOnMousePressed((MouseEvent event1) -> {
                mediaPlayer.seek(javafx.util.Duration.seconds(progressBar.getValue()));
            });
            
            progressBar.setOnMouseDragged((MouseEvent event1) -> {
                mediaPlayer.seek(javafx.util.Duration.seconds(progressBar.getValue()));
            });
            
            mediaPlayer.setOnReady(() -> {
                javafx.util.Duration total = media.getDuration();
                progressBar.setMax(total.toSeconds());
            });
            
            mediaPlayer.play();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
    public void pauseVideo(ActionEvent event){
        mediaPlayer.pause();
    }
    
    @FXML
    public void stopVideo(ActionEvent event){
        mediaPlayer.stop();
    }
    
    @FXML
    public void playVideo(ActionEvent event){
        mediaPlayer.play();
        mediaPlayer.setRate(1);
    }
}
