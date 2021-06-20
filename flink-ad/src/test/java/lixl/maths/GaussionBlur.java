package lixl.maths;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2/24/2018.
 * 高斯模糊
 */
public class GaussionBlur {
    static float[][] gMatrix;        //计算高斯后的权重矩阵
    final static int r = 1;     //高斯模糊半径
    final static int size = 2 * r + 1;  //数组大小

    /**
     * 简单高斯模糊算法
     */
    public static void main(String[] args) throws IOException {
        gMatrix = GaussUtil.get2(GaussUtil.get2DKernalData(r, 1.5f));    //计算高斯权重
        BufferedImage img = ImageIO.read(new File("d:\\butterfly.jpg"));
        System.out.println("图片加载成功" + img);
        int height = img.getHeight();
        int width = img.getWidth();

        int[][] baseMatrix = new int[size][size];
        int[] values = new int[size*size];
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                readPixel(img, i, j, values);       //获取周边点的值
                fillMatrix(baseMatrix, values);     //将周边点 的值存到缓存矩阵中
                img.setRGB(i, j, avgMatrix(baseMatrix));
            }
        }
        ImageIO.write(img, "jpeg", new File("d:/test2.jpg"));

    }

    /**
     * 读取像素
     * @param img
     * @param x
     * @param y
     * @param pixels
     */
    private static void readPixel(BufferedImage img, int x, int y, int[] pixels){
        int xStart = x - r;
        int yStart = y - r;
        int current = 0;
        for(int i = xStart; i < size + xStart; i++){
            for(int j = yStart; j < size + yStart; j++){
                int tx = i;
                if(tx < 0){     //处理边界情况左溢出
                    tx = -tx;
                }else if(tx >= img.getWidth()){     //处理边界情况右溢出
                    tx = x;
                }
                int ty = j;
                if(ty < 0){
                    ty = -ty;
                }else if(ty >= img.getHeight()){
                    ty = y;
                }
                pixels[current++] = img.getRGB(tx, ty);     //获取
            }
        }
    }

    private static void fillMatrix(int[][] matrix, int... values){
        int filled = 0;
        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < size; j++){
                matrix[i][j] = values[filled++];
            }
        }
    }

    private static int avgMatrix(int[][] matrix){
        int r = 0;
        int g = 0;
        int b = 0;
        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix.length; j++){
                Color c = new Color(matrix[i][j]);
                r += c.getRed() * gMatrix[i][j];
                g += c.getGreen() * gMatrix[i][j];
                b += c.getBlue() * gMatrix[i][j];
            }
        }
        return new Color(r, g, b).getRGB();

    }


}
