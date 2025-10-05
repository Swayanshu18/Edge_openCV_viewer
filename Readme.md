## **Android Application:**

* Live Camera Feed: Captures a high-framerate video stream using Android's modern CameraX API.
* Real-Time C++ Processing: Sends every camera frame to a native C++ layer via the JNI (Java Native Interface) for maximum performance.
* OpenCV Edge Detection: Utilizes the powerful OpenCV library in C++ to apply a Canny edge detection filter to each frame.
* GPU-Powered Rendering: Displays the final processed video smoothly using a custom OpenGL ES 2.0 renderer, ensuring the UI remains fast and responsive.
* Interactive Toggle: A simple on-screen button to switch between the original camera feed and the live edge-detected view.


# **Web Viewer:**

* TypeScript Project: A modern web front-end built with TypeScript and the Vite toolchain.
* Static Frame Display: Showcases a sample processed frame saved from the Android application.
* Stats Overlay: Displays basic frame statistics (resolution, FPS) using TypeScript to manipulate the DOM.
  ![Alt text for image 1](D:\OPENCVintegrate\edgecamerafeed.jpeg)
  ![Alt text for image 2](D:\OPENCVintegrate\edgecamerafeed.jpeg)