import static org.junit.Assert.*;

import java.awt.Color;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

public class SeamCarverTests {

    @Test
    public void SeamCarver_Construct_RetunsSamePicture() {
        String pathToPicture = "Data\\HJocean.png";
        Picture picture = new Picture(pathToPicture);
        SeamCarver carver = new SeamCarver(picture);
        assertEquals(picture, carver.picture());
    }

    @Test
    public void SeamCarver_Width_Validation() {
        String pathToPicture = "Data\\HJocean.png";
        Picture picture = new Picture(pathToPicture);
        SeamCarver carver = new SeamCarver(picture);
        assertEquals(768, carver.width());
    }
    @Test
    public void SeamCarver_Height_Validation() {
        String pathToPicture = "Data\\HJocean.png";
        Picture picture = new Picture(pathToPicture);
        SeamCarver carver = new SeamCarver(picture);
        assertEquals(432, carver.height());
    }

    @SuppressWarnings("deprecation")
    @Test
    public void SeamCarver_Energy_Computation_3x7_Validation() {
        String expectedMatrix = "195075, 195075, 195075\n"
                + "195075, 86627, 195075\n" + "195075, 55775, 195075\n"
                + "195075, 105720, 195075\n" + "195075, 63180, 195075\n"
                + "195075, 78196, 195075\n" + "195075, 195075, 195075\n";
        double[][] getMatrix = GetMatrix(expectedMatrix);
        String pathToPicture = "Data\\3x7.png";
        Picture picture = new Picture(pathToPicture);
        SeamCarver carver = new SeamCarver(picture);
        for (int y = 0; y < picture.height(); y++) {
            for (int x = 0; x < picture.width(); x++) {
                assertEquals(getMatrix[y][x], carver.energy(x, y), 0.0);
            }
        }
    }

    
    
    @SuppressWarnings("deprecation")
    @Test
    public void SeamCarver_Horizontal_Rip_3x7_Validation() {
        String expectedMatrix = "195075, 195075, 195075\n"
                + "195075, 21935, 195075\n"  + "195075, 116422, 195075\n" 
                + "195075, 63180, 195075\n" + "195075, 78196, 195075\n" +
                "195075, 195075, 195075\n";
        double[][] getMatrix = GetMatrix(expectedMatrix);
        String pathToPicture = "Data\\3x7.png";
        Picture picture = new Picture(pathToPicture);
        SeamCarver carver = new SeamCarver(picture);
        carver.removeHorizontalSeam(carver.findHorizontalSeam());
        this.DumpPicture(carver.picture());
        for (int y = 0; y < getMatrix.length; y++) {
            for (int x = 0; x < getMatrix[0].length; x++) {
                
                assertEquals(getMatrix[y][x], carver.energy(x, y), 0.0);
            }
        }
        assertEquals(picture.height() - 1, carver.picture().height());
    }
    
    @SuppressWarnings("deprecation")
    @Test
    public void SeamCarver_Vertical_Rip_3x7_Validation() {
        String expectedMatrix = "195075, 195075\n"
                + "195075, 195075\n" + "195075, 195075\n"
                + "195075, 195075\n" + "195075, 195075\n"
                + "195075, 195075\n" + "195075, 195075\n";
        double[][] getMatrix = GetMatrix(expectedMatrix);
        String pathToPicture = "Data\\3x7.png";
        Picture picture = new Picture(pathToPicture);
        SeamCarver carver = new SeamCarver(picture);
        carver.removeVerticalSeam(carver.findVerticalSeam());
        for (int y = 0; y < getMatrix.length; y++) {
            for (int x = 0; x < getMatrix[0].length; x++) {
                assertEquals(getMatrix[y][x], carver.energy(x, y), 0.0);
            }
        }
        assertEquals(picture.width() - 1, carver.picture().width());
    }
    
    @SuppressWarnings("deprecation")
    @Test
    public void SeamCarver_Vertical_Rip_Error_Validation() {
        String expectedMatrix = "195075, 195075\n"
                + "195075, 195075\n" + "195075, 195075\n"
                + "195075, 195075\n" + "195075, 195075\n"
                + "195075, 195075\n" + "195075, 195075\n";
        double[][] getMatrix = GetMatrix(expectedMatrix);
        String pathToPicture = "Data\\3x7.png";
        Picture picture = new Picture(pathToPicture);
        SeamCarver carver = new SeamCarver(picture);
        carver.removeVerticalSeam(carver.findVerticalSeam());
        for (int y = 0; y < getMatrix.length; y++) {
            for (int x = 0; x < getMatrix[0].length; x++) {
                assertEquals(getMatrix[y][x], carver.energy(x, y), 0.0);
            }
        }
        assertEquals(picture.width() - 1, carver.picture().width());
        
        try{
            carver.removeVerticalSeam(new int[] {1, 3});
        } catch (Exception ex)
        {
            assertEquals(ex.toString(), "java.lang.IllegalArgumentException");    
        }
    }
    
    @SuppressWarnings("deprecation")
    @Test
    public void SeamCarver_Horizontal_Rip_Exception_Pass() throws Exception {
        String expectedMatrix = "195075, 195075\n"
                + "195075, 195075\n" + "195075, 195075\n"
                + "195075, 195075\n" + "195075, 195075\n"
                + "195075, 195075\n" + "195075, 195075\n";
        double[][] getMatrix = GetMatrix(expectedMatrix);
        String pathToPicture = "Data\\3x7.png";
        Picture picture = new Picture(pathToPicture);
        SeamCarver carver = new SeamCarver(picture);
        carver.removeVerticalSeam(carver.findVerticalSeam());
        for (int y = 0; y < getMatrix.length; y++) {
            for (int x = 0; x < getMatrix[0].length; x++) {
                assertEquals(getMatrix[y][x], carver.energy(x, y), 0.0);
            }
        }
        
        assertEquals(picture.width() - 1, carver.picture().width());
        try{
            carver.removeHorizontalSeam(new int[] {1, 3, 4, 7});
        } catch (Exception ex)
        {
            assertEquals(ex.toString(), "java.lang.IllegalArgumentException");
            return;
        }
        throw new Exception();
    }
    
    @SuppressWarnings("deprecation")
    @Test
    public void SeamCarver_Vertical_Rip_3x7_Validation_Multiple_Pass() {
        String expectedMatrix = "195075, 195075\n"
                + "195075, 195075\n" + "195075, 195075\n"
                + "195075, 195075\n" + "195075, 195075\n"
                + "195075, 195075\n" + "195075, 195075\n";
        double[][] getMatrix = GetMatrix(expectedMatrix);
        String pathToPicture = "Data\\3x7.png";
        Picture picture = new Picture(pathToPicture);
        SeamCarver carver = new SeamCarver(picture);
        carver.removeVerticalSeam(carver.findVerticalSeam());
        for (int y = 0; y < getMatrix.length; y++) {
            for (int x = 0; x < getMatrix[0].length; x++) {
                assertEquals(getMatrix[y][x], carver.energy(x, y), 0.0);
            }
        }
        
        assertEquals(picture.width() - 1, carver.picture().width());
        expectedMatrix = 
                  "195075, 195075\n" + "195075, 195075\n"
                + "195075, 195075\n" + "195075, 195075\n"
                + "195075, 195075\n" + "195075, 195075\n";
        getMatrix = GetMatrix(expectedMatrix);
        carver.removeHorizontalSeam(carver.findHorizontalSeam());
        for (int y = 0; y < getMatrix.length; y++) {
            for (int x = 0; x < getMatrix[0].length; x++) {
                assertEquals(getMatrix[y][x], carver.energy(x, y), 0.0);
            }
        }
        
        assertEquals(picture.height() - 1, carver.picture().height());
        
        expectedMatrix = "195075\n"
                + "195075\n" + "195075\n"
                + "195075\n" + "195075\n";
        getMatrix = GetMatrix(expectedMatrix);
        carver.removeVerticalSeam(carver.findVerticalSeam());
        for (int y = 0; y < getMatrix.length; y++) {
            for (int x = 0; x < getMatrix[0].length; x++) {
                assertEquals(getMatrix[y][x], carver.energy(x, y), 0.0);
            }
        }
        assertEquals(picture.width() - 2, carver.picture().width());
    }
    
    @SuppressWarnings("deprecation")
    @Test
    public void SeamCarver_Vertical_Rip_10x12_Validation() {
        String expectedMatrix = "195075,195075,195075,195075,195075,195075,195075,195075,195075\n" +
                                "195075,57896,85720,102741,34430,59999,37600,134498,195075\n" +
                                "195075,61287,57183,38233,156907,40535,8039,38807,195075\n" +
                                "195075,54629,107054,60764,88253,26352,98179,37931,195075\n" +
                                "195075,90253,82713,25654,40974,33631,22333,81645,195075\n" +
                                "195075,29595,110853,96663,68427,65745,133143,45680,195075\n" +
                                "195075,50893,93407,65043,84261,90198,63419,95309,195075\n" +
                                "195075,36515,92404,127938,106258,36155,89490,70296,195075\n" +
                                "195075,49322,87505,90079,36873,93488,113431,93055,195075\n" +
                                "195075,49404,43495,76943,94544,43829,91557,22531,195075\n" +
                                "195075,52430,26203,25559,67144,33075,50430,67579,195075\n" +
                                "195075,195075,195075,195075,195075,195075,195075,195075,195075\n";
        double[][] getMatrix = GetMatrix(expectedMatrix);
        String pathToPicture = "Data\\10x12.png";
        Picture picture = new Picture(pathToPicture);
        SeamCarver carver = new SeamCarver(picture);
        carver.removeVerticalSeam(carver.findVerticalSeam());
        
        this.DumpPicture(carver.picture());
        
        for (int y = 0; y < getMatrix.length; y++) {
            for (int x = 0; x < getMatrix[0].length; x++) {
                assertEquals(getMatrix[y][x], carver.energy(x, y), 0.0);
            }
        }
        assertEquals(picture.width() - 1, carver.picture().width());
    }
    
    @SuppressWarnings("deprecation")
    @Test
    public void SeamCarver_Vertical_Rip_10x12_Validation_2_Pass() {
        String expectedMatrix = "195075,195075,195075,195075,195075,195075,195075,195075,195075\n" +
                                "195075,57896,85720,102741,34430,59999,37600,134498,195075\n" +
                                "195075,61287,57183,38233,156907,40535,8039,38807,195075\n" +
                                "195075,54629,107054,60764,88253,26352,98179,37931,195075\n" +
                                "195075,90253,82713,25654,40974,33631,22333,81645,195075\n" +
                                "195075,29595,110853,96663,68427,65745,133143,45680,195075\n" +
                                "195075,50893,93407,65043,84261,90198,63419,95309,195075\n" +
                                "195075,36515,92404,127938,106258,36155,89490,70296,195075\n" +
                                "195075,49322,87505,90079,36873,93488,113431,93055,195075\n" +
                                "195075,49404,43495,76943,94544,43829,91557,22531,195075\n" +
                                "195075,52430,26203,25559,67144,33075,50430,67579,195075\n" +
                                "195075,195075,195075,195075,195075,195075,195075,195075,195075\n";
        double[][] getMatrix = GetMatrix(expectedMatrix);
        String pathToPicture = "Data\\10x12.png";
        Picture picture = new Picture(pathToPicture);
        SeamCarver carver = new SeamCarver(picture);
        carver.removeVerticalSeam(carver.findVerticalSeam());
        
        this.DumpPicture(carver.picture());
        
        for (int y = 0; y < getMatrix.length; y++) {
            for (int x = 0; x < getMatrix[0].length; x++) {
                assertEquals(getMatrix[y][x], carver.energy(x, y), 0.0);
            }
        }
        assertEquals(picture.width() - 1, carver.picture().width());
        
        this.DumpLinearMatix(carver.findVerticalSeam());
        this.DumpLinearMatix(carver.findHorizontalSeam());
    }
    
    private void DumpLinearMatix(int[] linMat){
        for(int i=0;i<linMat.length;i++)
        {
            System.out.printf("%d => %d,", i, linMat[i]);
        }
        System.out.println();
    }
    
    private void DumpPicture(Picture picture)
    {
        for (int y = 0; y < picture.height(); y++) {
            for (int x = 0; x < picture.width(); x++) {
                Color color = picture.get(x, y);
                System.out.printf("(%s,%s) => %s, ", y, x, color.toString().replace("java.awt.Color", ""));
            }
            
            System.out.println();
        }
    }
    
    @SuppressWarnings("deprecation")
    @Test
    public void SeamCarver_Border_EnergyValidation() {
        String pathToPicture = "Data\\3x7.png";
        Picture picture = new Picture(pathToPicture);
        SeamCarver carver = new SeamCarver(picture);
        assertEquals(195075.0, carver.energy(1, 0), 0.0);
        assertEquals(195075.0, carver.energy(1, 6), 0.0);
        assertEquals(195075.0, carver.energy(0, 3), 0.0);
        assertEquals(195075.0, carver.energy(2, 3), 0.0);
        assertEquals(105720.0, carver.energy(1, 3), 0.0);
    }

    @SuppressWarnings("deprecation")
    @Test
    public void SeamCarver_Border_Vertical_Seam_Validation() {
        String pathToPicture = "Data\\3x7.png";
        Picture picture = new Picture(pathToPicture);
        SeamCarver carver = new SeamCarver(picture);
        int[] expectedSeam = new int[] { 0, 1, 1, 1, 1, 1, 0 };
        assertArrayEquals(expectedSeam, carver.findVerticalSeam());
    }

    @SuppressWarnings("deprecation")
    @Test
    public void SeamCarver_Border_Vertical_Seam_Validation_1() {
        String pathToPicture = "Data\\10x12.png";
        Picture picture = new Picture(pathToPicture);
        SeamCarver carver = new SeamCarver(picture);
        int[] expectedSeam = new int[] { 5, 6, 7, 8, 7, 7, 6, 7, 6, 5, 6, 5 };
        assertArrayEquals(expectedSeam, carver.findVerticalSeam());
    }
    
    @SuppressWarnings("deprecation")
    @Test
    public void SeamCarver_Border_Vertical_Seam_Validation_2_pass() {
        String pathToPicture = "Data\\10x12.png";
        Picture picture = new Picture(pathToPicture);
        SeamCarver carver = new SeamCarver(picture);
        int[] expectedSeam = new int[] { 5, 6, 7, 8, 7, 7, 6, 7, 6, 5, 6, 5 };
        assertArrayEquals(expectedSeam, carver.findVerticalSeam());
        
        expectedSeam = new int[] { 5, 6, 7, 8, 7, 7, 6, 7, 6, 5, 6, 5 };
        assertArrayEquals(expectedSeam, carver.findVerticalSeam());
    }
    
    @SuppressWarnings("deprecation")
    @Test
    public void SeamCarver_Border_Vertical_Seam_Validation_2_pass_mixed() {
        String pathToPicture = "Data\\10x12.png";
        Picture picture = new Picture(pathToPicture);
        SeamCarver carver = new SeamCarver(picture);
        int[] expectedSeam = new int[] { 5, 6, 7, 8, 7, 7, 6, 7, 6, 5, 6, 5 };
        assertArrayEquals(expectedSeam, carver.findVerticalSeam());
        
        int[] expectedSeam2 = new int[] { 8, 9, 10, 10, 10, 9, 10, 10, 9, 8 };
        assertArrayEquals(expectedSeam2, carver.findHorizontalSeam());
        
        expectedSeam = new int[] { 5, 6, 7, 8, 7, 7, 6, 7, 6, 5, 6, 5 };
        assertArrayEquals(expectedSeam, carver.findVerticalSeam());
    }
    
    @SuppressWarnings("deprecation")
    @Test
    public void SeamCarver_Border_Vertical_Rip() {
        String pathToPicture = "Data\\10x12.png";
        Picture picture = new Picture(pathToPicture);
        SeamCarver carver = new SeamCarver(picture);
        carver.removeVerticalSeam(carver.findVerticalSeam());
    }
    
    @SuppressWarnings("deprecation")
    @Test
    public void SeamCarver_Find_Horizontal_Rip() {
        String pathToPicture = "Data\\3x7.png";
        Picture picture = new Picture(pathToPicture);
        SeamCarver carver = new SeamCarver(picture);
        int[] expectedSeam = new int[] { 1, 2, 1 };
        
//        for(int i=0;i<carver.findHorizontalSeam().length;i++){
//            System.out.print(carver.findHorizontalSeam()[i]);
//        }
        
        int[] result = carver.findHorizontalSeam();
        // carver.DumpMatrix();
        assertArrayEquals(expectedSeam, result);
    }
    
    @SuppressWarnings("deprecation")
    @Test
    public void SeamCarver_Find_Horizontal_Rip_2_pass() {
        String pathToPicture = "Data\\3x7.png";
        Picture picture = new Picture(pathToPicture);
        SeamCarver carver = new SeamCarver(picture);
        int[] expectedSeam = new int[] { 1, 2, 1 };
        
        int[] result = carver.findHorizontalSeam();
        // carver.DumpMatrix();
        assertArrayEquals(expectedSeam, result);
        
        result = carver.findHorizontalSeam();
        // carver.DumpMatrix();
        assertArrayEquals(expectedSeam, result);
    }
    
    @SuppressWarnings("deprecation")
    @Test
    public void SeamCarver_Find_Horizontal_Rip_1() {
        String pathToPicture = "Data\\10x12.png";
        Picture picture = new Picture(pathToPicture);
        SeamCarver carver = new SeamCarver(picture);
        int[] expectedSeam = new int[] { 8, 9, 10, 10, 10, 9, 10, 10, 9, 8 };
        assertArrayEquals(expectedSeam, carver.findHorizontalSeam());
    }
    
    @SuppressWarnings("deprecation")
    @Test
    public void SeamCarver_Border_Vertical_Seam_Validation_2() throws Exception {
        
        String pathToPicture = "Data\\HJocean.png";
        Picture picture = new Picture(pathToPicture);
        SeamCarver carver = new SeamCarver(picture);
        //carver.findHorizontalSeam();
        throw new Exception();
        //int[] expectedSeam = new int[] { 5, 6, 7, 8, 7, 7, 6, 7, 6, 5, 6, 5 };
        //assertArrayEquals(expectedSeam, carver.findVerticalSeam());
    }
    
    @SuppressWarnings("deprecation")
    @Test
    public void SeamCarver_Border_Center_EnergyValidation() {
        String pathToPicture = "Data\\3x7.png";
        Picture picture = new Picture(pathToPicture);
        SeamCarver carver = new SeamCarver(picture);
        assertEquals(105720.0, carver.energy(1, 3), 0.0);

    }

//    @SuppressWarnings("deprecation")
//    @Test
//    public void SeamCarver_GetAdjList_UpperLeft() {
//        String pathToPicture = "Data\\3x7.png";
//        Picture picture = new Picture(pathToPicture);
//        SeamCarver carver = new SeamCarver(picture);
//        int y = 0, x = 0;
//        List<Position> adj = carver.getAdjListNorthSouth(x, y);
//        assertEquals(adj.size(), 2);
//        
//        Position south= adj.get(0);
//        Position southEast = adj.get(1);
//        
//        assertEquals(0, south.getX());
//        assertEquals(1, south.getY());
//        
//        assertEquals(1, southEast.getX());
//        assertEquals(1, southEast.getY());
//    }
    
//    @SuppressWarnings("deprecation")
//    @Test
//    public void SeamCarver_GetAdjList_UpperRight() {
//        String pathToPicture = "Data\\3x7.png";
//        Picture picture = new Picture(pathToPicture);
//        SeamCarver carver = new SeamCarver(picture);
//        int x = 2, y = 0;
//        List<Position> adj = carver.getAdjListNorthSouth(x, y);
//        assertEquals(adj.size(), 2);
//        
//        Position southWest = adj.get(0);
//        Position south = adj.get(1);
//        
//        assertEquals(1, southWest.getX());
//        assertEquals(1, southWest.getY());
//        
//        assertEquals(2, south.getX());
//        assertEquals(1, south.getY());
//    }
    
//    @SuppressWarnings("deprecation")
//    @Test
//    public void SeamCarver_GetAdjList_BottomLeft() {
//        String pathToPicture = "Data\\3x7.png";
//        Picture picture = new Picture(pathToPicture);
//        SeamCarver carver = new SeamCarver(picture);
//        int x = 0, y = 6;
//        List<Position> adj = carver.getAdjListNorthSouth(x, y);
//        assertEquals(adj.size(), 0);
//    }
//    
//    @SuppressWarnings("deprecation")
//    @Test
//    public void SeamCarver_GetAdjList_Centered() {
//        String pathToPicture = "Data\\3x7.png";
//        Picture picture = new Picture(pathToPicture);
//        SeamCarver carver = new SeamCarver(picture);
//        int x = 1, y = 1;
//        List<Position> adj = carver.getAdjListNorthSouth(x, y);
//        assertEquals(adj.size(), 3);
//        
//        Position southWest = adj.get(0);
//        Position south = adj.get(1);
//        Position southEast = adj.get(2);
//        
//        assertEquals(0, southWest.getX());
//        assertEquals(2, southWest.getY());
//        
//        assertEquals(1, south.getX());
//        assertEquals(2, south.getY());
//        
//        assertEquals(2, southEast.getX());
//        assertEquals(2, southEast.getY());
//    }

    private double[][] GetMatrix(String expectedMatrix) {
        String[] rows = expectedMatrix.split("\n");
        int height = rows.length;
        int width = rows[0].split(",").length;
        double[][] imageMatrix = new double[height][];
        for (int row = 0; row < height; row++) {
            imageMatrix[row] = new double[width];
            String[] cols = rows[row].split(",");
            for (int colVal = 0; colVal < width; colVal++) {
                imageMatrix[row][colVal] = Double.parseDouble(cols[colVal]);
            }
        }
        return imageMatrix;
    }
}
