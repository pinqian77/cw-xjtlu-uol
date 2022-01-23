package cw3;

import java.awt.Color;


/**
 * CPT105 2020 Coursework 3 Sample Methods
 */
public class CW3_Example {
    
    // Returns a new picture that is an upside down version the given picture.
    public static Picture upsideDown(Picture picture) {
        
        int height = picture.height();
        int width = picture.width();
        Picture newPic = new Picture(width, height);

        for (int col = 0; col< width; col++){
            for (int row = 0; row< height; row++){
                newPic.setColor(col, height-1-row, picture.getColor(col, row));
            }
        }
        return newPic;
    }
    
    //Returns a new picture that is a grayscale version the given picture.
    public static Picture grayScale(Picture picture) {
        int height = picture.height();
        int width = picture.width();
        Picture newPic = new Picture(width, height);
        for (int col = 0; col< width; col++){
            for (int row = 0; row< height; row++){
                Color color = picture.getColor(col, row);
                double luminance = 0.299*color.getRed()+0.587*color.getGreen()+0.114*color.getBlue();
                int lum = (int)Math.round(luminance);
                Color gray = new Color(lum,lum,lum);
                newPic.setColor(col,row,gray);
            }
        }
        return newPic;
    }
    
    
    public static void main(String[] args) {
        Picture andrew = new Picture("C:\\qp_box\\Liverpool\\Y2\\CPT105\\CPT105\\cw3\\Andrew.png");
        //andrew.show();
        //upsideDown(andrew).show();
        //grayScale(andrew).show();
                
        //Picture udAndrew = new Picture("C:\\qp_box\\Liverpool\\Y2\\CPT105\\CPT105\\cw3\\Expected\\UpsideDownAndrew.png");
        //udAndrew.show();
        //System.out.println(udAndrew.equals(upsideDown(andrew)));
        
        Picture grayAndrew = new Picture("C:\\qp_box\\Liverpool\\Y2\\CPT105\\CPT105\\cw3\\Expected\\GrayAndrew.png");
        grayAndrew.show();
        System.out.println(grayAndrew.equals(grayScale(andrew)));
        
    }
    
}
