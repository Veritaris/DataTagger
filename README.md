# Data tagger
## What is it? 
This is a dataset markup & tagging tool  
Now supports the following markup types:

- [x] Text captcha, image classification markup
- [ ] Shapes on image (circle, rectangle, etc.) 
- [ ] Points on image  

## How to start? 
**Short summary**:  
Press `Enter` to save text and go next  
Use arrows (`←` or `→`) after typing `Tab` or `Esc` to switch between images  
Shortcuts:
- `Alt+O` to open metadata file in default text editor

### Extended guide:  
You need to select a folder with a dataset that contains numerically named directories with `image.png` file inside (temporary format, I'll add support of different formats later)

<img width="712" alt="Dataset structure" src="https://user-images.githubusercontent.com/37160602/145069814-56485619-53f0-406d-b987-ac6674b59335.png">

After that program will count folders amount and show this. It will also create `metadata.yml` file in the dataset directory (also there will be `.metadata.yml` file when you start marking up your dataset - this is a "backup"). If the directory already contains a metadata file, DataTagger will read all necessary information. You can also see markup progress here. Finally, click "Go to tagging"  

<img width="712" alt="Dataset selection" src="https://user-images.githubusercontent.com/37160602/145042760-593238af-4242-4072-acb6-a6e124ed28f1.png">

Markup interface is extremely simple. It shows you an image and you should enter text. You can click "Next" to save text and open the next picture. Also pressing Enter key does the same. If you need to go back or open another picture, press `Esc` or `Tab`, then you can use left and right arrows to open the previous or next image. There is also a shortcut `Alt+O` to open the dataset metadata file in your default text editor for `.yml` file  

<img width="712" alt="Marking the dataset up" src="https://user-images.githubusercontent.com/37160602/145042774-cf072daa-388f-4ce8-adb0-de6b0cc3c73b.png">

`metadata.yml` has the following syntax:
``` yml
!!me.veritaris.datatagger.Model.Metadata
datasetName: captchas
imagesAmount: 11259
lastTaggedImage: 1001
repeatedImages: [
  ]
taggedImages: {
  }
```
Note: there are buttons "Finish tagging" and "Repetition", but they don't work for now. They will have functionality in the next release  
## There is a lot of similar software for it, why another one? 
Yes, I already know about ImageTagger, LabelImg, Yolo, etc. I just wanted to create a very simple, lightweight d x-platform app for datasets annotation for my needs. Why not if I can? 
