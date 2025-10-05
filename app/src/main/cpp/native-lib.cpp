#include <jni.h>
#include <opencv2/core.hpp>
#include <opencv2/imgproc.hpp>

// Note the new "jboolean apply_edge_detection" parameter
extern "C"
JNIEXPORT void JNICALL
Java_com_example_opencvintegrate_MainActivity_processFrame(
        JNIEnv *env,
        jobject thiz,
        jlong mat_addr_rgba,
        jint rotation,
        jboolean apply_edge_detection) {

    // Cast the long address back to a cv::Mat pointer
    cv::Mat& rgba = *(cv::Mat*)mat_addr_rgba;

    // Rotation logic will be applied in both modes
    if (rotation == 90) {
        cv::rotate(rgba, rgba, cv::ROTATE_90_CLOCKWISE);
    } else if (rotation == 180) {
        cv::rotate(rgba, rgba, cv::ROTATE_180);
    } else if (rotation == 270) {
        cv::rotate(rgba, rgba, cv::ROTATE_90_COUNTERCLOCKWISE);
    }

    // --- NEW CONDITIONAL LOGIC ---
    // Check if edge detection should be applied
    if (apply_edge_detection) {
        // This is the edge detection processing block
        cv::Mat gray;
        cv::cvtColor(rgba, gray, cv::COLOR_RGBA2GRAY);
        cv::Canny(gray, gray, 80, 100);
        cv::bitwise_not(gray, gray);
        cv::cvtColor(gray, rgba, cv::COLOR_GRAY2RGBA);
    }
    // If apply_edge_detection is false, this block is skipped,
    // and the original rotated RGBA frame is shown.
}