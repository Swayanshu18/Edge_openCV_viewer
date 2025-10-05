# Real-Time Edge Detection Camera & Web Viewer

This project is a comprehensive technical demonstration of a high-performance, real-time video processing pipeline on Android, coupled with a modern TypeScript-based web viewer for displaying the output.

---

## 📷 Screenshots

Here are screenshots of the application in action, showing both the normal camera feed and the live edge-detected feed.
| Normal Camera View | Edge Detection View |
| :---: | :---: |
| ![Normal Camera Feed](screenshhots/edgecamerafeed.jpeg) | ![Edge Detection Feed](screenshhots/realcamerafeed.jpeg) |

*(**Note:** For these images to display correctly on GitHub, you must create a `screenshots` folder in your project's root directory, add your image files (e.g., `normal_feed.png`, `edge_feed.png`) to it, and ensure the paths above are correct.)*

---

## ✅ Features Implemented

### **Android Application:**

* **Live Camera Feed:** Captures a high-framerate video stream using Android's modern **CameraX API**.
* **Real-Time C++ Processing:** Sends every camera frame to a native C++ layer via the **JNI (Java Native Interface)** for maximum performance.
* **OpenCV Edge Detection:** Utilizes the powerful **OpenCV** library in C++ to apply a Canny edge detection filter to each frame.
* **GPU-Powered Rendering:** Displays the final processed video smoothly using a custom **OpenGL ES 2.0** renderer, ensuring the UI remains fast and responsive.
* **Interactive Toggle:** A simple on-screen button to switch between the original camera feed and the live edge-detected view.

### **Web Viewer:**

* **TypeScript Project:** A modern web front-end built with **TypeScript** and the **Vite** toolchain.
* **Static Frame Display:** Showcases a sample processed frame saved from the Android application.
* **Stats Overlay:** Displays basic frame statistics (resolution, FPS) using TypeScript to manipulate the DOM.

---

## 🧠 Architecture Overview

The application is built on a multi-layered pipeline that passes data from the high-level Java layer to low-level C++ for efficient processing and back for rendering.

#### **Android App Frame Flow**

graph TD
A[CameraX API]

    subgraph Android App (Java Layer)
        B(MainActivity.java)
        C(ImageConverter.java)
        D(MyGLRenderer.java)
        E[GLSurfaceView]
    end

    subgraph Native (C++ Layer)
        F[native-lib.cpp]
    end

    subgraph Final Output
        G((Screen Display))
    end

    A -- "Provides 'ImageProxy'" --> B
    B -- "1. Calls with 'ImageProxy'" --> C
    C -- "2. Returns processed 'Mat' object" --> B
    B -- "3. JNI Call: 'processFrame(Mat, ...)'" --> F
    F -- "4. Modifies 'Mat' in place" --> B
    B -- "5. Converts 'Mat' to 'Bitmap' & passes to" --> D
    D -- "6. Renders Bitmap as texture onto" --> E
    E -- "7. Displays pixels on" --> G