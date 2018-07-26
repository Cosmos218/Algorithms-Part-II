import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Stack;

import java.awt.Color;

public class SeamCarver {
    private Picture picture;
    private double[][] energy;
    private double[][] distTo;
    private int[][] edgeTo;

    public SeamCarver(Picture picture) {               // create a seam carver object based on the given picture
        if (picture == null)
            throw new IllegalArgumentException("picture is null!");
        this.picture = new Picture(picture);    //mutate
        computeEnergy();
    }

    public Picture picture() {                         // current picture
        return new Picture(picture);     //mutate
    }

    public int width() {                           // width of current picture
        return energy.length;
    }

    public int height() {                          // height of current picture
        return energy[0].length;
    }

    public double energy(int x, int y) {              // energy of pixel at column x and row y
        validateXY(x, y);
        return energy[x][y];
    }

    private void computeEnergy() {
        energy = new double[picture.width()][picture.height()];
        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                if (x == 0 || y == 0 || x == width() - 1 || y == height() - 1) {
                    energy[x][y] = 1000;
                    continue;
                }
                Color xM1 = picture.get(x - 1, y);
                Color xP1 = picture.get(x + 1, y);
                Color yM1 = picture.get(x, y - 1);
                Color yP1 = picture.get(x, y + 1);
                double Dx = Math.pow(xM1.getRed() - xP1.getRed(), 2) + Math.pow(xM1.getGreen() - xP1.getGreen(), 2) + Math.pow(xM1.getBlue() - xP1.getBlue(), 2);
                double Dy = Math.pow(yM1.getRed() - yP1.getRed(), 2) + Math.pow(yM1.getGreen() - yP1.getGreen(), 2) + Math.pow(yM1.getBlue() - yP1.getBlue(), 2);
                energy[x][y] = Math.sqrt(Dx + Dy);
            }
        }
    }

    public int[] findVerticalSeam() {              // sequence of indices for horizontal seam
        distTo = new double[width()][height()];
        edgeTo = new int[width()][height()];
        for (int i = 0; i < width(); i++) {
            distTo[i][0] = energy[i][0];
            edgeTo[i][0] = -1;
        }
        for (int j = 1; j < height(); j++) {
            for (int i = 0; i < width(); i++) {
                double[] result = findMin(i, j);
                distTo[i][j] = result[0] + energy[i][j];
                edgeTo[i][j] = (int) result[1];
            }
        }
        Iterable<Integer> temp = findSeam();
        int[] result = new int[height()];
        int j = 0;
        for (int i : temp) {
            result[j++] = i;
        }
        return result;
    }

    private Iterable<Integer> findSeam() {
        double min = Double.POSITIVE_INFINITY;
        int temp = 0;
        for (int i = 0; i < width(); i++) {
            if (distTo[i][height() - 1] < min) {
                min = distTo[i][height() - 1];
                temp = i;
            }
        }
        return pathTo(temp);
    }

    private double[] findMin(int i, int j) {
        double[] result = new double[2];
        result[0] = Double.POSITIVE_INFINITY;
        for (int k = i - 1; k <= i + 1; k++) {
            if (k < 0 || k > width() - 1)
                continue;
            if (distTo[k][j - 1] < result[0]) {
                result[0] = distTo[k][j - 1];
                result[1] = k;
            }
        }
        return result;
    }

    private Iterable<Integer> pathTo(int i) {
        Stack<Integer> path = new Stack<Integer>();
        int j = height() - 1;
        for (int e = edgeTo[i][j]; e != -1; e = edgeTo[e][j], j--) {
            path.push(e);
        }
        return path;
    }

    public int[] findHorizontalSeam() {                // sequence of indices for vertical seam
        transposeEnergy();
        int[] result = findVerticalSeam();
        transposeEnergy();
        return result;
    }

    private void transposeEnergy() {
        double[][] newEnergy = new double[height()][width()];
        for (int i = 0; i < height(); i++) {
            for (int j = 0; j < width(); j++) {
                newEnergy[i][j] = energy[j][i];
            }
        }
        energy = newEnergy;
    }

    public void removeHorizontalSeam(int[] seam) {  // remove horizontal seam from current picture
        if (seam == null)
            throw new IllegalArgumentException("seam is null!");
        if (height() <= 1)
            throw new IllegalArgumentException("height less than 1");
        if (seam.length != width())
            throw new IllegalArgumentException("seam is wrong length!");
        for (int i = 0; i < seam.length - 1; i++) {
            if (Math.abs(seam[i] - seam[i+1]) > 1)
                throw new IllegalArgumentException("wrong differ in seam");
        }
        Picture newPic = new Picture(width(), height()-1);
        for (int i = 0; i < newPic.width(); i++) {
            for (int j = 0; j < newPic.height(); j++) {
                if (seam[i] < 0 || seam[i] > height()-1)
                    throw new IllegalArgumentException("invalid seam");
                if (seam[i] > j)
                    newPic.set(i, j, picture.get(i, j));
                else
                    newPic.set(i, j, picture.get(i, j + 1));
            }
        }
        picture = newPic;
        computeEnergy();
    }

    public void removeVerticalSeam(int[] seam) {    // remove vertical seam from current picture
        if (seam == null)
            throw new IllegalArgumentException("seam is null!");
        if (width() <= 1)
            throw new IllegalArgumentException("width less than 1");
        if (seam.length != height())
            throw new IllegalArgumentException("seam is wrong length!");
        for (int i = 0; i < seam.length - 1; i++) {
            if (Math.abs(seam[i] - seam[i+1]) > 1)
                throw new IllegalArgumentException("wrong differ in seam");
        }
        Picture newPic = new Picture(width()-1, height());
        for (int i = 0; i < newPic.width(); i++) {
            for (int j = 0; j < newPic.height(); j++) {
                if (seam[j] < 0 || seam[j] > width()-1)
                    throw new IllegalArgumentException("invalid seam");
                if (seam[j] > i)
                    newPic.set(i, j, picture.get(i, j));
                else
                    newPic.set(i, j, picture.get(i + 1, j));
            }
        }
        picture = newPic;
        computeEnergy();
    }

    private void validateXY(int x, int y) {
        if (x < 0 || x > width() - 1 || y < 0 || y > height() - 1)
            throw new IllegalArgumentException("invalid x or y");
    }
}
