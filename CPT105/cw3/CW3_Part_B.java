package cw3;

import java.awt.Color;

/**
 * CPT105 2020 Coursework 3 Part B
 */
public class CW3_Part_B {
    
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
        double swirl_degree = 0.0368; //数字越小变化越不明显
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
        int height = picture.height();
        int width = picture.width();
        Picture pic_res = new Picture(width*2,height);

        // frosted glass
        Picture pic_glass = new Picture(width, height);
        for (int row = 0; row < height ; row++) {
            for (int col = 0; col < width ; col++) {
                Color color = picture.getColor(col, row);
                pic_glass.setColor(col, row, color);
            }   
        }
        
        for (int row = 4; row < height-4 ; row++) {
            for (int col = 4; col < width-4 ; col++) {
            	
            	int range = (int)(Math.random()*4)+1;
            	int sub = (range +1)/2;
            	int a = (int)(Math.random()*range)+1;
                int b = (int)(Math.random()*range)+1;
                int randX = col+a-sub;
                int randY = row+b-sub;
                
                Color c = picture.getColor(randX, randY);
                pic_glass.setColor(col, row, c);  
            }
        }

        // grayscale
        Picture pic_grey = new Picture(width,height);
        for (int col = 0; col< width; col++){
            for (int row = 0; row< height; row++){
                Color color = pic_glass.getColor(col, row);
                double luminance = 0.299*color.getRed()+0.587*color.getGreen()+0.114*color.getBlue();
                int lum = (int)Math.round(luminance);
                Color gray = new Color(lum,lum,lum);
                pic_grey.setColor(col,row,gray);
            }
        }
        
        // reverse
        Picture pic_reverse = new Picture(width,height);
        for (int row=0; row<height; row++){
            for(int col=0; col<width; col++){
                Color color = pic_grey.getColor(col, row);
                int r = 255-color.getRed();
                int g = 255-color.getGreen();
                int b = 255-color.getBlue();
                Color res_color = new Color(r,g,b);

                pic_reverse.setColor(col, row, res_color);
            }
        }

        // blur
        Picture pic_padding = new Picture(width+8,height+8);
        Picture pic_blur = new Picture(width, height);

        // right-down->left-up
        int r_1 = -1;
        for(int r = height-4; r<height; r++){
            r_1++;
            int c_1 = -1;
            for(int c = width-4; c<width; c++){
                c_1++;
                Color edge1 = pic_reverse.getColor(c, r);
                pic_padding.setColor(c_1, r_1, edge1);
                
            }
        }
        // left-up->right-down
        int r_2 = pic_padding.height()-4-1;
        for(int r = 0; r<4; r++){
            r_2++;
            int c_2 = pic_padding.width()-4-1;
            for(int c = 0; c<4; c++){
                c_2++;
                Color edge2 = pic_reverse.getColor(c, r);
                pic_padding.setColor(c_2, r_2, edge2);
                
            }
        }
        // right-up -> left-down
        int r_3 = pic_padding.height()-4-1;
        for(int r = 0; r<4; r++){
            r_3++;
            int c_3 = -1;
            for(int c = width-4; c<width; c++){
                c_3++;
                Color edge3 = pic_reverse.getColor(c, r);
                pic_padding.setColor(c_3, r_3, edge3);
                
            }
        }
        // left-down->right-up
        int r_4 = -1;
        for(int r = height-4; r<height; r++){
            r_4++;
            int c_4 = pic_padding.width()-4-1;
            for(int c = 0; c<4; c++){
                c_4++;
                Color edge4 = pic_reverse.getColor(c, r);
                pic_padding.setColor(c_4, r_4, edge4);
                
            }
        }
        // up->down
        int r_p1 = pic_padding.height()-4-1;
        for(int r = 0; r<4;r++){
            r_p1++;
            for(int c = 0; c<width; c++){
                Color up = pic_reverse.getColor(c, r);
                pic_padding.setColor(c+4, r_p1, up);
            }
        }
        // down->up
        int r_p2 = -1;
        for(int r = height-4; r<height;r++){
            r_p2++;
            for(int c = 0; c<width; c++){
                Color down = pic_reverse.getColor(c, r);
                pic_padding.setColor(c+4, r_p2, down);
            }
        }
        // left->right
        for(int r = 0; r<height;r++){
            int cp1 = width+4;
            for(int c = 0; c<4; c++){
                Color left = pic_reverse.getColor(c, r);
                pic_padding.setColor(cp1, r+4, left);
                cp1++;
                
            }
        }
        // right->left
        for(int r = 0; r<height;r++){
            int cp2 = 0;
            for(int c = width-4; c<width; c++){
                Color right = pic_reverse.getColor(c, r);
                pic_padding.setColor(cp2, r+4, right);
                cp2++;
                
            }
        }
        // normal
        for (int row = 0; row< height; row++){
            for (int col = 0; col< width; col++){
                Color normal = pic_reverse.getColor(col, row);
                pic_padding.setColor(col+4, row+4, normal);
            }
        }

        // calculate
        for (int i = 0; i<pic_padding.height()-9+1;i++){
            for (int j = 0; j<pic_padding.width()-9+1; j++){
                //for loop in every 81 gongge
                int red_buffer[][] = new int[9][9];
                int green_buffer[][] = new int[9][9];
                int blue_buffer[][] = new int[9][9];

                int res_r = 0;
                int res_g = 0;
                int res_b = 0;

                int index_row = -1;
                for (int row = i; row<i+9; row++){
                    index_row++;
                    int index_col = 0;
                    for (int col = j; col<j+9; col++){    
                        red_buffer[index_row][index_col] = pic_padding.getColor(col, row).getRed();
                        green_buffer[index_row][index_col] = pic_padding.getColor(col, row).getGreen();
                        blue_buffer[index_row][index_col] = pic_padding.getColor(col, row).getBlue();
                        index_col++;
                    }
                }

                // only get the item that have the same index
                int r = 0;
                int g = 0;
                int b = 0;
                for(int d = 0; d<9; d++){
                    r = r+ red_buffer[d][d];
                    g = g+ green_buffer[d][d];
                    b = b+ blue_buffer[d][d];
                }
                res_r += (int)Math.round(r/9.0);
                res_g += (int)Math.round(g/9.0);
                res_b += (int)Math.round(b/9.0);

                // exception
                if (res_r<0){
                    res_r = 0;
                }
                if (res_r>255){
                    res_r = 255;
                }
                if (res_g<0){
                    res_g = 0;
                }
                if (res_g>255){
                    res_g = 255;
                }
                if (res_b<0){
                    res_b = 0;
                }
                if (res_b>255){
                    res_b = 255;
                }

                // set color to pic_blur
                Color rgb_res =new Color(res_r,res_g,res_b);
                pic_blur.setColor(j, i, rgb_res);
            }
        }

        // sketch
        Picture pic_sketch = new Picture(width, height);
        for (int row=0; row<height; row++){
            for(int col=0; col<width; col++){
                int color_grey = pic_grey.getColor(col, row).getRed();
                int color_blur = pic_blur.getColor(col, row).getRed();

                int res_rgb = color_grey+(color_grey*color_blur)/(255-color_blur);
                if(res_rgb>255) res_rgb=255;
                if(res_rgb<0) res_rgb=0;

                Color color_res = new Color(res_rgb,res_rgb,res_rgb);
                pic_sketch.setColor(col, row, color_res);
            }
        }

        // ********** left-normal **********
        for (int row=0; row<height; row++){
            for(int col=0; col<width; col++){
                Color color1 = picture.getColor(col, row);
                Color color2 = pic_sketch.getColor(col, row);
                pic_res.setColor(col, row, color1);
                pic_res.setColor(col+width, row, color2);
            }
        }
        return pic_res;
    }
    
	
    public static void main(String[] args) {
        Picture andrew = new Picture("C:\\qp_box\\Liverpool\\Y2\\CPT105\\CPT105\\cw3\\Andrew.png");
        positionalTransform(andrew).show();
        colorTransform(andrew).show();

        Picture peppers = new Picture("C:\\qp_box\\Liverpool\\Y2\\CPT105\\CPT105\\cw3\\Sample\\peppers.tif");
        positionalTransform(peppers).show();
        colorTransform(peppers).show();

        Picture boy = new Picture("C:\\qp_box\\Liverpool\\Y2\\CPT105\\CPT105\\cw3\\Sample\\boy.png");
        positionalTransform(boy).show();
        colorTransform(boy).show();

        Picture lena = new Picture("C:\\qp_box\\Liverpool\\Y2\\CPT105\\CPT105\\cw3\\Sample\\lena.jpg");
        positionalTransform(lena).show();
        colorTransform(lena).show();
        
    }
}