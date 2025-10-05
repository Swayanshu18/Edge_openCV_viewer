// Replace the ENTIRE content of your MyGLRenderer.java with this
package com.example.opencvintegrate;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyGLRenderer implements GLSurfaceView.Renderer {

    // ALL MEMBER VARIABLES ARE DECLARED INSIDE THE CLASS
    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "attribute vec2 a_texCoord;" +
                    "varying vec2 v_texCoord;" +
                    "void main() {" +
                    "  gl_Position = vPosition;" +
                    "  v_texCoord = a_texCoord;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform sampler2D s_texture;" +
                    "varying vec2 v_texCoord;" +
                    "void main() {" +
                    "  gl_FragColor = texture2D(s_texture, v_texCoord);" +
                    "}";

    private FloatBuffer vertexBuffer, textureBuffer;
    private int mProgram;
    private int textureId;
    private Bitmap currentFrame;
    private boolean frameAvailable = false;

    static float squareCoords[] = {
            -1.0f,  1.0f,
            -1.0f, -1.0f,
            1.0f, -1.0f,
            1.0f,  1.0f,
    };

    static float textureCoords[] = {
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,
    };

    private ShortBuffer drawListBuffer;
    private short drawOrder[] = { 0, 1, 2, 0, 2, 3 };

    public MyGLRenderer() {
        ByteBuffer bb = ByteBuffer.allocateDirect(squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);

        ByteBuffer tb = ByteBuffer.allocateDirect(textureCoords.length * 4);
        tb.order(ByteOrder.nativeOrder());
        textureBuffer = tb.asFloatBuffer();
        textureBuffer.put(textureCoords);
        textureBuffer.position(0);

        ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);
    }

    public synchronized void updateFrame(Bitmap frame) {
        if (currentFrame != null && !currentFrame.isRecycled()) {
            currentFrame.recycle();
        }
        currentFrame = frame;
        frameAvailable = true;
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glLinkProgram(mProgram);

        int[] textures = new int[1];
        GLES20.glGenTextures(1, textures, 0);
        textureId = textures[0];
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        synchronized (this) {
            if (frameAvailable && currentFrame != null && !currentFrame.isRecycled()) {
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
                GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, currentFrame, 0);
                frameAvailable = false;
            }
        }

        GLES20.glUseProgram(mProgram);
        int positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, 2, GLES20.GL_FLOAT, false, 8, vertexBuffer);
        int texCoordHandle = GLES20.glGetAttribLocation(mProgram, "a_texCoord");
        GLES20.glEnableVertexAttribArray(texCoordHandle);
        GLES20.glVertexAttribPointer(texCoordHandle, 2, GLES20.GL_FLOAT, false, 8, textureBuffer);
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);
        GLES20.glDisableVertexAttribArray(positionHandle);
        GLES20.glDisableVertexAttribArray(texCoordHandle);
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    public static int loadShader(int type, String shaderCode){
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }
}