# ⚡ Real-Time Edge Detection Camera & Web Viewer

This project demonstrates a **high-performance, real-time edge detection pipeline** on **Android**, integrated with a **modern TypeScript-based web viewer** for visualization and statistics. It leverages the speed of **C++ (OpenCV)** and **GPU-accelerated rendering (OpenGL ES)** to achieve a smooth, responsive user experience.

---

## 📸 Screenshots

| Original Camera View | Edge Detection View |
| :------------------: | :-----------------: |
| ![Original Camera Feed](screenshots/realcamerafeed.jpeg) | ![Canny Edge Detection Feed](screenshots/edgecamerafeed.jpeg) |

---

## ✨ Features

### 📱 **Android Application (High-Performance Core)**

* **Live Camera Capture:** Captures high-framerate video efficiently using Android’s **CameraX API**.
* **Real-Time C++ Processing:** Utilizes **JNI (Java Native Interface)** to pass frames to a native C++ layer, bypassing Dalvik overhead for maximum speed.
* **OpenCV Edge Detection:** Implements the **Canny Edge Detection algorithm** using **OpenCV (C++)** on every frame.
* **GPU-Accelerated Rendering:** Ensures smooth display of processed video with minimal latency via **OpenGL ES 2.0**.
* **Interactive Toggle:** Allows users to switch instantly between the **original** and **edge-detected** views.

### 💻 **Web Viewer (Visualization & Stats)**

* **Modern Stack:** Built with **TypeScript** and **Vite** for a lightweight and fast development environment.
* **Frame Visualization:** Displays a processed frame, serving as a placeholder or future target for a live stream.
* **Dynamic Stats Overlay:** Uses DOM manipulation to display real-time metadata like **frame rate (FPS)** and **resolution**.

---
## 🚀 Getting Started

Follow these steps to set up and run both the Android application and the local Web Viewer.

### 1️⃣ Android App Setup

1.  Open the project in **Android Studio**.
2.  Ensure you have a device or emulator running. *Targeting **Android SDK 34+** is recommended.*
3.  Sync Gradle if prompted to ensure all dependencies are resolved.
4.  Build and run the app from the `app/` module.
5.  The app should launch and display the real-time edge detection camera feed.
    > **Note:** The **OpenCV library** is pre-packaged and included in the `openCV/` folder.

### 2️⃣ Web Viewer Setup

The web viewer requires **Node.js** and **npm** to be installed.

1.  Open your terminal and navigate into the web directory:
    ```bash
    cd web
    ```
2.  Install the necessary dependencies:
    ```bash
    npm install
    ```
3.  Start the local development server:
    ```bash
    npm run dev
    ```
4.  Open your browser to the URL displayed in the terminal (usually `http://localhost:5173`) to view the application and its statistics overlay.

---

## 🧠 Architecture Overview

The project employs a modular, high-performance data flow to minimize latency, utilizing the following sequence: **Java → C++ → OpenGL → Display**.

```mermaid
graph TD

A[📷 CameraX API] --> B(MainActivity.java)
B --> |"1️⃣ Sends ImageProxy"| C[ImageConverter.java]
C --> |"2️⃣ Converts to OpenCV Mat"| B
B --> |"3️⃣ JNI → processFrame(Mat)"| F[native-lib.cpp]
F --> |"4️⃣ Processes Frame (OpenCV Canny)"| B
B --> |"5️⃣ Converts Mat → Bitmap"| D[MyGLRenderer.java]
D --> |"6️⃣ Renders via OpenGL ES 2.0"| E[GLSurfaceView]
E --> |"7️⃣ Displays Frame"| G((📺 Screen Output))