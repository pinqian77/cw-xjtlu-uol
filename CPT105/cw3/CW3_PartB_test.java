package cw3;

import java.awt.Color;
/**
 * CPT105 2020 Coursework 3 Part B
 */
public class CW3_PartB_test {
    
    /****************
     * CW3 Part B.1 *
     ****************/
    
    // Original picture and positionally tranformed picture.
    public static Picture positionalTransform(Picture picture) {
        int width = picture.width();
        int height = picture. height();
        Picture pic_res = new Picture(width*2,height);

        //********** right-tranformed **********
        //////swirl effect//////
        Picture pic_swirl = new Picture(width,height);
        double swirl_degree = 0.0358; //数字越小变化越不明显
        int mid_col = width/2;
        int mid_row = height/2;
        
		for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int col_offset = col - mid_col;
                int row_offset = row - mid_row;

                double radian = Math.atan2((double)row_offset, (double)col_offset);
                double radius = Math.sqrt(col_offset*col_offset+row_offset*row_offset);

                int res_col = (int)(radius*Math.cos(radian+radius*swirl_degree)+mid_col);
                int res_row = (int)(radius*Math.sin(radian+radius*swirl_degree)+mid_row);
                
                if(res_col>=width) res_col=width-1;
                if(res_col<0) res_col=0;
                if(res_row>=height) res_row = height-1;
                if(res_row<0) res_row = 0;

                Color color = picture.getColor(res_col, res_row);
                pic_swirl.setColor(col, row, color);
            }
        }

        //////wave effect//////
        Picture pic_wave1 = new Picture(width,height);
        for (int col = 0; col < picture.width(); col++) {
            for (int row = 0; row < picture.height(); row++) {
                int bias_1 = (int)(Math.sin(Math.PI/15*row)*5);
                //int bias_2 = (int)(Math.sin(Math.PI/15*col)*5);
                Color color = pic_swirl.getColor(col, row);
                if(col+bias_1<width && col+bias_1>=0 && row<height && row>=0){
                    pic_wave1.setColor(col+bias_1, row, color);
                }                
            }
        }
        Picture pic_wave2 = new Picture(width,height);
        for (int col = 0; col < picture.width(); col++) {
            for (int row = 0; row < picture.height(); row++) {
                //int bias_1 = (int)(Math.sin(Math.PI/15*row)*5);
                int bias_2 = (int)(Math.sin(Math.PI/15*col)*5);
                Color color = pic_wave1.getColor(col, row);
                if(col<width && col>=0 && row+bias_2<height && row+bias_2>=0){
                    pic_wave2.setColor(col, row+bias_2, color);
                }                
            }
        }

        // ********** left-normal **********
        for (int row=0; row<height; row++){
            for(int col=0; col<width; col++){
                Color color_1 = picture.getColor(col, row);
                Color color_2 = pic_wave2.getColor(col, row);
                pic_res.setColor(col, row, color_1);
                pic_res.setColor(col+width, row, color_2);
            }
        }

        picture = pic_res;
        return picture;
    }
    
	
    /****************
     * CW3 Part B.2 *
     ****************/
    
    // Original picture and color tranformed picture.
    public static Picture colorTransform(Picture picture) {
        int t_width = 75;
        int t_height = 200;
        Picture target = new Picture(300,400);
        for (int t_col=0; t_col<t_width; t_col++){
            for(int t_row=0; t_row<t_height; t_row++){
                int src_col = t_col*picture.width()/(t_width+20);
                int src_row = t_row*picture.height()/t_height;
                target.setColor(t_col+picture.width(), t_row, picture.getColor(src_col, src_row));
            }
        }
        return target;
    }
    
	
    public static void main(String[] args) {
        Picture andrew = new Picture("C:\\qp_box\\Liverpool\\Y2\\CPT105\\CPT105\\cw3\\Andrew.png");
        //andrew.show();
        
        positionalTransform(andrew).show();
        
        //colorTransform(andrew).show();
        
    }
}