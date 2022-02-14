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
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import me.veritaris.datatagger.Main;
import me.veritaris.datatagger.Model.Model;
import me.veritaris.datatagger.pojo.Annotation;
import me.veritaris.datatagger.pojo.ImageTextAnnotation;
import org.controlsfx.control.ToggleSwitch;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.IntStream;


public class ImageTaggingController extends Controller implements Initializable {

    @FXML protected Button repeatedImageButton;
    @FXML protected Button finishTaggingButton;
    @FXML protected TextField annotationTextField;
    @FXML protected ProgressBar datasetTaggingProgress;
    @FXML protected Button goToNextImageButton;
    @FXML protected ImageView imageToAnnotate;
    @FXML protected AnchorPane textTaggingPane;
    @FXML protected Label currentFilePath;
    @FXML protected ListView<Annotation> imagesList;
    @FXML protected ToggleSwitch alwaysOnTopSwitch;

    private final KeyCodeCombination openMetadataFileCombination = new KeyCodeCombination(KeyCode.O, KeyCombination.ALT_DOWN);

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        int lastTaggedImage = Model.getMetadata().getLastTaggedImage();

        datasetTaggingProgress.setProgress(Model.getMetadata().getLastTaggedImage() / (double)Model.getMetadata().getImagesAmount());
        datasetTaggingProgress.setTooltip(new Tooltip(String.format("%s/%s", Model.getMetadata().getLastTaggedImage(), Model.getMetadata().getImagesAmount())));

        updateAnnotationList();
        imagesList.scrollTo(Model.getMetadata().getLastTaggedImage() - 4);

        if (Model.getMetadata().getTaggedImages().containsKey(lastTaggedImage)) {
            annotationTextField.setText(Model.getMetadata().getTaggedImages().get(lastTaggedImage));
        }

        setupListViewEvents();
        setupTextFieldEvents();
        setupCommonEvents();

        openImage();
    }


    private void setupTextFieldEvents() {
        annotationTextField.setOnKeyPressed(
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
    }

    private void setupListViewEvents() {
        imagesList.setOnMouseClicked(
            (event) -> {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    if (event.getClickCount() == 2) {
                        Annotation selectedAnnotation = imagesList.getSelectionModel().getSelectedItems().get(0);
                        openImage(selectedAnnotation.getId());
                        annotationTextField.setText(selectedAnnotation.getAnnotation());
                    }
                }
                event.consume();
            }
        );
        imagesList.setOnKeyPressed(
            (event) -> {
                switch (event.getCode()) {
                    case ENTER:
                        Annotation selectedAnnotation = imagesList.getSelectionModel().getSelectedItems().get(0);
                        openImage(selectedAnnotation.getId());
                        annotationTextField.setText(selectedAnnotation.getAnnotation());
                        break;
                }
            }
        );
    }

    private void setupCommonEvents() {
        textTaggingPane.setOnKeyPressed(
            (event) -> {
                if (openMetadataFileCombination.match(event)) {
                    openMetadataFile();
                    return;
                }
                switch (event.getCode()) {
                    case LEFT:
                    case KP_LEFT:
                        if (annotationTextField.isFocused()) {
                            return;
                        }
                        handleChangeImage(-1);
                        break;
                    case RIGHT:
                    case KP_RIGHT:
                        if (annotationTextField.isFocused()) {
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

        alwaysOnTopSwitch.setOnMouseClicked(
            (event) -> {
                Main.getPrimaryStage().setAlwaysOnTop(alwaysOnTopSwitch.isSelected());
            }
        );
    }

    private void openMetadataFile() {
        try {
            Desktop.getDesktop().edit(Model.getMetadataFile());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void updateAnnotationList() {
        imagesList.getItems().clear();
        IntStream.rangeClosed(0, Model.getMetadata().getImagesAmount()-1).forEach(
            (id) -> imagesList.getItems()
                .add(new ImageTextAnnotation(id, Model.getMetadata().getTaggedImages().get(id)))
        );
    }

    private void switchFocus() {
        String textFieldData = annotationTextField.getText();
        if (annotationTextField.isFocused()) {
            textTaggingPane.requestFocus();
        } else {
            annotationTextField.requestFocus();
            annotationTextField.deselect();

            if (textFieldData != null) {
                annotationTextField.positionCaret(textFieldData.length());
            } else {
                annotationTextField.positionCaret(0);
            }
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

        imagesList.getSelectionModel().select(imagesList.getSelectionModel().getSelectedIndex() + direction);
        imagesList.scrollTo(imagesList.getSelectionModel().getSelectedIndex());

        Model.getMetadata().setLastTaggedImage(newImageId);
        Model.getInstance().saveProgress();

        openImage();

        annotationTextField.setText(Model.getMetadata().getTaggedImages().getOrDefault(newImageId, ""));
    }

    private void handleTagAndGoToNextImage() {
        String annotation = annotationTextField.getText();

        if (!Model.getMetadata().getTaggedImages().containsValue(annotation)) {
            Model.writeImageTag(annotation);

            saveAnnotation(annotation);
            return;
        }

        repeatedImageButton.setDisable(false);
    }

    private void saveAnnotation(String annotation) {
        if (Model.getMetadata().isGitSavingEnabled()) {
            Model.getGitHandler().handleChanges(new ImageTextAnnotation(Model.getMetadata().getLastTaggedImage(), annotation));
        }

        annotationTextField.clear();
        openImage();
        updateAnnotationList();
        datasetTaggingProgress.setProgress(Model.getMetadata().getLastTaggedImage() / (double)Model.getMetadata().getImagesAmount());
        datasetTaggingProgress.setTooltip(new Tooltip(String.format("%s/%s", Model.getMetadata().getLastTaggedImage(), Model.getMetadata().getImagesAmount())));
        annotationTextField.requestFocus();
    }

    private void openImage() {
        try {
            File file = new File(Model.getLastTaggedImagePath());

            this.imageToAnnotate.setImage(new Image(new FileInputStream(Model.getLastTaggedImagePath())));
            this.currentFilePath.setText(
                file.getParentFile().getName() + "/" + file.getName()
            );
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    private void openImage(int imageId) {
        try {
            File file = new File(Model.getDatasetPath() + "/" + imageId);
            Model.getMetadata().setLastTaggedImage(imageId);

            this.imageToAnnotate.setImage(new Image(new FileInputStream(Model.getLastTaggedImagePath())));
            this.currentFilePath.setText(
                file.getParentFile().getName() + "/" + file.getName()
            );
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        }
    }
}
