// Create this new file: ImageConverter.java
package com.example.opencvintegrate;

import androidx.camera.core.ImageProxy;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import java.nio.ByteBuffer;

public class ImageConverter {

    public static void yuvToRgba(ImageProxy image, Mat output) {
        // This is a more robust way to convert YUV_420_888 to RGBA

        ImageProxy.PlaneProxy[] planes = image.getPlanes();

        ByteBuffer yBuffer = planes[0].getBuffer();
        ByteBuffer uBuffer = planes[1].getBuffer();
        ByteBuffer vBuffer = planes[2].getBuffer();

        int ySize = yBuffer.remaining();
        int uSize = uBuffer.remaining();
        int vSize = vBuffer.remaining();

        byte[] nv21 = new byte[ySize + uSize + vSize];

        //U and V are swapped
        yBuffer.get(nv21, 0, ySize);
        vBuffer.get(nv21, ySize, vSize);
        uBuffer.get(nv21, ySize + vSize, uSize);

        Mat yuv = new Mat(image.getHeight() + image.getHeight() / 2, image.getWidth(), CvType.CV_8UC1);
        yuv.put(0, 0, nv21);

        Imgproc.cvtColor(yuv, output, Imgproc.COLOR_YUV2RGBA_NV21, 4);

        yuv.release();
    }
}