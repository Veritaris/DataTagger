package me.veritaris.datatagger.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import me.veritaris.datatagger.Model.Model;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class ImageTaggingController extends Controller implements Initializable {

    @FXML protected Button repeatedImageButton;
    @FXML protected Button finishTaggingButton;
    @FXML protected TextField captchaTextField;
    @FXML protected ProgressBar datasetTaggingProgress;
    @FXML protected Button goToNextImageButton;
    @FXML protected ImageView captchaImage;
    @FXML protected AnchorPane textTaggingPane;
    @FXML protected Label currentFilePath;

    private final KeyCodeCombination openMetadataFileCombination = new KeyCodeCombination(KeyCode.O, KeyCombination.ALT_DOWN);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        int lastTaggedImage = Model.getMetadata().getLastTaggedImage();
        datasetTaggingProgress.setProgress(Model.getMetadata().getLastTaggedImage() / (double)Model.getMetadata().getImagesAmount());
        datasetTaggingProgress.setTooltip(new Tooltip(String.format("%s/%s", Model.getMetadata().getLastTaggedImage(), Model.getMetadata().getImagesAmount())));

        if (Model.getMetadata().getTaggedImages().containsKey(lastTaggedImage)) {
            captchaTextField.setText(Model.getMetadata().getTaggedImages().get(lastTaggedImage));
        }

        textTaggingPane.setOnKeyPressed(
            (event) -> {
                if (openMetadataFileCombination.match(event)) {
                    openMetadataFile();
                    return;
                }
                switch (event.getCode()) {
                    case LEFT:
                    case KP_LEFT:
                        if (captchaTextField.isFocused()) {
                            return;
                        }
                        handleChangeImage(-1);
                        break;
                    case RIGHT:
                    case KP_RIGHT:
                        if (captchaTextField.isFocused()) {
                            return;
                        }
                        handleChangeImage(+1);
                        break;
                    case ESCAPE:
                        textTaggingPane.requestFocus();
                        break;
                    case ENTER:
                        handleTagAndGoToNextImage();
                        break;
                    case TAB:
                        switchFocus();
                        break;
                }
                event.consume();
            }
        );

        captchaTextField.setOnKeyPressed(
            (event) -> {
                switch (event.getCode()) {
                    case ENTER:
                        handleTagAndGoToNextImage();
                        break;
                    case TAB:
                        switchFocus();
                        break;
                }
            }
        );
        openImage();
    }

    private void openMetadataFile() {
        try {
            Desktop.getDesktop().edit(Model.getMetadataFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void switchFocus() {
        if (captchaTextField.isFocused()) {
            textTaggingPane.requestFocus();
        } else {
            captchaTextField.requestFocus();
            captchaTextField.deselect();
            captchaTextField.positionCaret(captchaTextField.getText().length());
        }
    }

    @FXML
    protected void goToNextImage(ActionEvent actionEvent) {
        handleTagAndGoToNextImage();
    }

    @FXML
    protected void checkForRepetition(ActionEvent actionEvent) {
    }

    @FXML
    protected void finishTagging(ActionEvent actionEvent) {
    }

    @FXML
    protected void markAsRepeated(ActionEvent actionEvent) {
    }

    private void handleChangeImage(int direction) {
        int newImageId = Model.getMetadata().getLastTaggedImage() + direction;
        if (newImageId < 0 || newImageId > Model.getMetadata().getImagesAmount()) {
            return;
        }

        Model.getMetadata().setLastTaggedImage(newImageId);
        Model.getInstance().saveProgress();
        openImage();

        captchaTextField.setText(Model.getMetadata().getTaggedImages().getOrDefault(newImageId, ""));
    }

    private void handleTagAndGoToNextImage() {
        String tag = captchaTextField.getText();
        if (!Model.getMetadata().getTaggedImages().containsValue(tag)) {
            Model.writeImageTag(tag);
            captchaTextField.clear();
            openImage();
            datasetTaggingProgress.setProgress(Model.getMetadata().getLastTaggedImage() / (double)Model.getMetadata().getImagesAmount());
            datasetTaggingProgress.setTooltip(new Tooltip(String.format("%s/%s", Model.getMetadata().getLastTaggedImage(), Model.getMetadata().getImagesAmount())));
            captchaTextField.requestFocus();
        }
    }

    private void openImage() {
        try {
            File file = new File(Model.getLastTaggedImagePath());
            this.captchaImage.setImage(new Image(new FileInputStream(Model.getLastTaggedImagePath())));
            this.currentFilePath.setText(
                file.getParentFile().getName() + "/" + file.getName()
            );
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
