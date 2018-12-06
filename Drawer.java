import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;


/**
 * Created by ArtemBulkhak on 20.04.18.
 */
public class Drawer {

    private static final int[] YELLOW = {255,255,0};
    private static final int[] GREEN  = {0, 255, 0};
    private static final int[] RED  = {255, 0, 0};
    private static final int[] BLUE  = {0, 0, 255};
    private static final int[] PINK = {255, 105, 180};
    private static final int[] AQUA = {0, 255, 255};
    private static final int[] PURPLE = {255, 0, 255};
    private static final int[] MAROON = {128, 0, 0};


    private static final int[] WHITE = {220, 220, 220};
    private static final int[] GRAY1 = {220, 220, 220};
    private static final int[] GRAY2  = {211, 211, 211};
    private static final int[] GRAY3  = {192, 192, 192};
    private static final int[] GRAY4  = {169, 169, 169};
    private static final int[] GRAY5 = {128, 128, 128};
    private static final int[] GRAY6 = {105, 105, 105};
    private static final int[] GRAY7 = {64,64,64};
    private static final int[] GRAY8 = {0, 0, 0};


    private static final ArrayList<int[]> colors = new ArrayList<>();
    private CalcArrMatrix calcArrMatrix;
    private int grayLevel;
    private long window1;
    private int width;
    private int heigth;
    byte[] fire;
    private ByteBuffer pixels;


    Drawer(boolean pixelOrRectangle, boolean color, CalcArrMatrix calcArrMatrix, ArrayList<Cluster> clusters) {

        this.calcArrMatrix = calcArrMatrix;
        grayLevel = calcArrMatrix.getGrayLevel();
        width = calcArrMatrix.getGtm_width();
        heigth = calcArrMatrix.getGtm_heigth();



        if (!color){
            colors.add(GRAY8);
            colors.add(GRAY7);
            colors.add(GRAY6);
            colors.add(GRAY5);
            colors.add(GRAY4);
            colors.add(GRAY3);
            colors.add(GRAY2);
            colors.add(GRAY1);
            colors.add(WHITE);
        } else {
            colors.add(YELLOW);
            colors.add(RED);
            colors.add(GREEN);
            colors.add(BLUE);
            colors.add(PINK);
            colors.add(AQUA);
            colors.add(PURPLE);
            colors.add(MAROON);
            colors.add(WHITE);
        }



        fire = new byte[width * heigth * 3];


        int i = 0;
        for (Cluster cluster : clusters) {
            for (GLCM glcm : cluster.getPoints()) {
                if (pixelOrRectangle) {

                    fire[((glcm.getY() - 1) * width + glcm.getX()) * 3] = (byte) colors.get(i)[0];
                    fire[((glcm.getY() - 1) * width + glcm.getX()) * 3 + 1] = (byte) colors.get(i)[1];
                    fire[((glcm.getY() - 1) * width + glcm.getX()) * 3 + 2] = (byte) colors.get(i)[2];


                } else {
                    for (int k = glcm.getY() - 1 - grayLevel / 2; k < glcm.getY() - 1 + grayLevel / 2; k++) {
                        for (int j = glcm.getX() - grayLevel / 2; j < glcm.getX() + grayLevel / 2; j++) {

                            fire[(k * width + j) * 3] = (byte) colors.get(i)[0];
                            fire[(k * width + j) * 3 + 1] = (byte) colors.get(i)[1];
                            fire[(k * width + j) * 3 + 2] = (byte) colors.get(i)[2];
                        }
                    }
                }
            }
            i++;

        }

//        if (pixelOrRectangle) {
//            for (int j = (width * (grayLevel - 1) + grayLevel)*3; j < fire.length - (width * (grayLevel-1) + grayLevel)*3; j += 3) {
//                if (fire[j] != fire[j + width * 3]) {
//                    fire[j] = (byte) RED[0];
//                    fire[j + 1] = (byte) RED[1];
//                    fire[j + 2] = (byte) RED[2];
//                }
//            }
//        }


        pixels = ByteBuffer.allocateDirect(width*heigth*3).put(fire);
        pixels.flip();



//        GLFWErrorCallback.createPrint(System.err).set();

        if(!glfwInit()){
            throw new IllegalStateException();
        }

        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);

        window1 = glfwCreateWindow(width, heigth, "My program", 0, 0);


        if ( window1 == 0 )
            throw new RuntimeException("Failed to create the GLFW window1");

        glfwSetKeyCallback(window1, (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window1, true);
        });


        glfwMakeContextCurrent(window1);
        glfwShowWindow(window1);


        GL.createCapabilities();
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);



        while (!glfwWindowShouldClose(window1)){

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            glRasterPos2d(-1, -1);

            glPixelZoom(1, 1);

            glDrawPixels(width, heigth, GL_RGB, GL_UNSIGNED_BYTE, pixels);


            glfwPollEvents();
            glfwSwapBuffers(window1);
        }

        glfwTerminate();
    }



}
