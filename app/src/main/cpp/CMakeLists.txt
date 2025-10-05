cmake_minimum_required(VERSION 3.22.1)

project("opencvintegrate")

# <<< --- STEP 1: CORRECT THIS PATH ---
# Make sure this path is exactly correct for your system. Use forward slashes.
set(OpenCV_DIR "D:/opencv-4.12.0-android-sdk/OpenCV-android-sdk/sdk/native/jni")

# This command uses the path above to find all the necessary OpenCV files
find_package(OpenCV REQUIRED)

add_library(
        native-lib
        SHARED
        native-lib.cpp)

# <<< --- STEP 2: ADD THIS LINE ---
# This explicitly tells the compiler where to find the .hpp header files
target_include_directories(native-lib PRIVATE ${OpenCV_INCLUDE_DIRS})

find_library(
        log-lib
        log)

# This links your code against the actual OpenCV library files (.so)
target_link_libraries(
        native-lib
        ${OpenCV_LIBS}
        ${log-lib})