package cw3;

import java.awt.*;

/**
 * CPT105 2020 Coursework 3 Part A
 */
public class CW3_Part_A_copy{
    
    /****************
     * CW3 Part A.1 *
     ****************/    
    
    // Returns a new picture that tilts it 30 degrees clockwise.
    public static Picture tilt(Picture picture) {
		int height = picture.height();
        int width = picture.width();
        Picture res_pic = new Picture(width, height);

        int left = 0;
        int right = left+50;
        int up = 0;
        int down = up+50;
        double a = Math.toRadians(330);

        for(int count=1; count<13; count++){
            for(int row=up; row<down; row++){
                for(int col=left; col<right; col++){
                    double row_c = 0.5*(right-left-1)+left;
                    double col_c = 0.5*(down-up-1)+up;
                    
                    for(int r=row; r<down; r++){
                        for(int c=col; c<right; c++){
                            int col_t = (int)((c-col_c)*Math.cos(a)-(r-row_c)*Math.sin(a)+col_c); 
                            int row_t = (int)((c-col_c)*Math.sin(a)+(r-row_c)*Math.cos(a)+row_c);
    
                            if(col_t<width && col_t>=0 && row_t<height && row_t>=0){
                                res_pic.setColor(col_t, row_t, picture.getColor(col_t, row_t));
                            }
                        }
                    }
                }
            }
            left+=50;
            if(count%4==0){
                up+=50;
                left=0;
            }
        }
        return res_pic;
    }
    
    /****************
     * CW3 Part A.2 *
     ****************/
    
    // Returns a new picture that applies an emboss filter to the given picture.
    public static Picture emboss(Picture picture) {
        int width = picture.width();
        int height = picture.height();

        Picture pic_padding = new Picture(width+2,height+2);
        Picture pic_res = new Picture(width, height);


        // *********set padding*********
        // *edge*
        Color edge1 = picture.getColor(0, 0);
        pic_padding.setColor(width+1, height+1, edge1);
        Color edge2 = picture.getColor(width-1, 0);
        pic_padding.setColor(0, height+1, edge2);
        Color edge3 = picture.getColor(0, height-1);
        pic_padding.setColor(width+1, 0, edge3);
        Color edge4 = picture.getColor(width-1, height-1);
        pic_padding.setColor(0, 0, edge4);
        // *side*
        // up<->down
        for (int i = 0; i<width; i++){
            Color upside = picture.getColor(i, 0);
            pic_padding.setColor(i+1, height+1, upside);
            Color downside = picture.getColor(i, height-1);
            pic_padding.setColor(i+1, 0, downside);
        }
        // left<->right
        for (int i = 0; i<height; i++){
            Color leftside = picture.getColor(0, i);
            pic_padding.setColor(width+1, i+1, leftside);
            Color rightside = picture.getColor(width-1, i);
            pic_padding.setColor(0, i+1, rightside);
            
        }
        // *normal*
        for (int row = 0; row< height; row++){
            for (int col = 0; col< width; col++){
                Color normal = picture.getColor(col, row);
                pic_padding.setColor(col+1, row+1, normal);
            }
        }

        //*********set kernel*********
        int kernel[][] = new int[3][3];
        kernel[0][0] = -2;
        kernel[0][1] = -1;
        kernel[0][2] = 0;
        kernel[1][0] = -1;
        kernel[1][1] = 1;
        kernel[1][2] = 1;
        kernel[2][0] = 0;
        kernel[2][1] = 1;
        kernel[2][2] = 2;

        //*********calculate*********
        // for loop in pic_padding (i is row；j is col)
        for (int i = 0; i<pic_padding.height()-3+1;i++){
            for (int j = 0; j<pic_padding.width()-3+1; j++){
                //for loop in every 9 gongge
                int red_buffer[][] = new int[3][3];
                int green_buffer[][] = new int[3][3];
                int blue_buffer[][] = new int[3][3];

                int res_r = 0;
                int res_g = 0;
                int res_b = 0;

                int index_row = -1;
                for (int row = i; row<i+3; row++){
                    index_row++;
                    int index_col = 0;
                    for (int col = j; col<j+3; col++){    
                        red_buffer[index_row][index_col] = pic_padding.getColor(col, row).getRed();
                        green_buffer[index_row][index_col] = pic_padding.getColor(col, row).getGreen();
                        blue_buffer[index_row][index_col] = pic_padding.getColor(col, row).getBlue();
                        index_col++;
                    }
                }

                // Now kernel and 9 gongge in pic_padding have the same index
                for(int t1 = 0; t1<3; t1++){
                    for(int t2 = 0; t2<3; t2++){
                        int r = kernel[t1][t2]*red_buffer[t1][t2];
                        int g = kernel[t1][t2]*green_buffer[t1][t2];
                        int b = kernel[t1][t2]*blue_buffer[t1][t2];

                        res_r += r;
                        res_g += g;
                        res_b += b;
                    }
                }

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

                // set color to pic_res
                Color rgb_res =new Color(res_r,res_g,res_b);
                pic_res.setColor(j, i, rgb_res);
            }
        }
        picture  = pic_res;
        return picture;
    }
    
    
    /****************
     * CW3 Part A.3 *
     ****************/
    
    // Returns a new picture that applies a blur filter to the given picture.
    public static Picture blur(Picture picture) {
        int width = picture.width();
        int height = picture.height();

        Picture pic_padding = new Picture(width+8,height+8);
        Picture pic_res = new Picture(width, height);


        // *********set padding*********
        // *edge*
        // right-down->left-up
        int r_1 = -1;
        for(int r = height-4; r<height; r++){
            r_1++;
            int c_1 = -1;
            for(int c = width-4; c<width; c++){
                c_1++;
                Color edge1 = picture.getColor(c, r);
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
                Color edge2 = picture.getColor(c, r);
                pic_padding.setColor(c_2, r_2, edge2);
                
            }
        }
        // youshang ->zuoxia
        int r_3 = pic_padding.height()-4-1;
        for(int r = 0; r<4; r++){
            r_3++;
            int c_3 = -1;
            for(int c = width-4; c<width; c++){
                c_3++;
                Color edge3 = picture.getColor(c, r);
                pic_padding.setColor(c_3, r_3, edge3);
                
            }
        }
        // zuoxia->youshang
        int r_4 = -1;
        for(int r = height-4; r<height; r++){
            r_4++;
            int c_4 = pic_padding.width()-4-1;
            for(int c = 0; c<4; c++){
                c_4++;
                Color edge4 = picture.getColor(c, r);
                pic_padding.setColor(c_4, r_4, edge4);
                
            }
        }

        // *side*
        // up->down
        int r_p1 = pic_padding.height()-4-1;
        for(int r = 0; r<4;r++){
            r_p1++;
            for(int c = 0; c<width; c++){
                Color up = picture.getColor(c, r);
                pic_padding.setColor(c+4, r_p1, up);
            }
        }
        // down->up
        int r_p2 = -1;
        for(int r = height-4; r<height;r++){
            r_p2++;
            for(int c = 0; c<width; c++){
                Color down = picture.getColor(c, r);
                pic_padding.setColor(c+4, r_p2, down);
            }
        }

        // left->right
        for(int r = 0; r<height;r++){
            int cp1 = width+4;
            for(int c = 0; c<4; c++){
                Color left = picture.getColor(c, r);
                pic_padding.setColor(cp1, r+4, left);
                cp1++;
                
            }
        }
        // right->left
        for(int r = 0; r<height;r++){
            int cp2 = 0;
            for(int c = width-4; c<width; c++){
                Color right = picture.getColor(c, r);
                pic_padding.setColor(cp2, r+4, right);
                cp2++;
                
            }
        }

        // *normal*
        for (int row = 0; row< height; row++){
            for (int col = 0; col< width; col++){
                Color normal = picture.getColor(col, row);
                pic_padding.setColor(col+4, row+4, normal);
            }
        }

        // *********No need to set kernel*********

        //*********calculate*********
        // for loop in pic_padding (i is row；j is col)
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

                // set color to pic_res
                Color rgb_res =new Color(res_r,res_g,res_b);
                pic_res.setColor(j, i, rgb_res);
            }
        }
        //picture = pic_padding;
        picture  = pic_res;
        return picture;



        //********************method 2 *************************//
        // int w = picture.width();
        // int h = picture.height();
        // Picture pic = new Picture(w,h);
        // int[][][] padding_matrix = new int[h][w][3];
        // int[][][] res_matrix = new int[h][w][3];
        
        // //将每个像素对应的RGB值存储到三维矩阵中
        // for(int row = 0;row<h;row++){
        //     for(int col = 0;col<w;col++){
        //         Color color = picture.getColor(col, row);
        //         padding_matrix[row][col][0] = color.getRed();
        //         padding_matrix[row][col][1] = color.getGreen();
        //         padding_matrix[row][col][2] = color.getBlue(); 
        //     }}              
        
        // for(int row = 0;row<h;row++){
        //     for(int col = 0;col<w;col++){
        //         for(int i = 0;i<3;i++){
        //             for(int index = -4;index<=4;index++){                        
        //                        int index1 = row+index;    //目标行
        //                        int index2 = col+index;    //目标列
        //                        //在计算时直接取对应点的RGB值
        //                        if(index1<0){
        //                        index1 = h+index1;
        //                        }
        //                        if(index2<0){
        //                        index2 = w+index2;
        //                        }
        //                        if(index1>h-1){
        //                        index1 = index1-h;
        //                        }
        //                        if(index2>w-1){
        //                        index2 = index2-w;
        //                        }
        //                        res_matrix[row][col][i] = padding_matrix[index1][index2][i] + res_matrix[row][col][i];                        
        //             }
        //             res_matrix[row][col][i] = (int) Math.round(res_matrix[row][col][i]/9.0);
        //         }
        //         Color new_color = new Color(res_matrix[row][col][0],res_matrix[row][col][1],res_matrix[row][col][2]);
        //         pic.setColor(col, row, new_color);
        //     }}
        
        //        return pic;
    }
    
    
    /****************
     * CW3 Part A.4 *
     ****************/
    
    // Returns a new picture that applies an emboss filter to the given picture.
    public static Picture edge(Picture picture) {
        int w = picture.width();
        int h = picture.height();
        picture = CW3_Example.grayScale(picture);
        Picture pic_res = new Picture(w,h);

        //pic_padding
        int[][][] padding_tensor = new int[h+2][w+2][3];
        //pic_res
        int[][][] res_tensor = new int[h][w][3];

        //***********set kernel **********
        int[][] kernel = {{-1,0,1},{-2,0,2},{-1,0,1}};
        int[][] kernel2 = {{1,2,1},{0,0,0},{-1,-2,-1}};
        
        //***********padding **********
        // normal padding
        for(int row = 0;row<h;row++){
            for(int col = 0;col<w;col++){
                Color color = picture.getColor(col, row);
                padding_tensor[row+1][col+1][0] = color.getRed();
                padding_tensor[row+1][col+1][1] = color.getGreen();
                padding_tensor[row+1][col+1][2] = color.getBlue(); 
            }
        }
        
        // side padding (normal padding is done, so we can get color directly from padding_tensor)
        // i:row
        for(int i = 1;i<=h;i++){
            for(int rgb =0;rgb<3;rgb++){
                // left->right
                padding_tensor[i][w+1][rgb] = padding_tensor[i][1][rgb];
                // right->left
                padding_tensor[i][0][rgb] = padding_tensor[i][w][rgb];
            }
        }
        // i:col
        for(int i = 1;i<=w;i++){
            for(int rgb =0;rgb<3;rgb++){
                //up->down
                padding_tensor[h+1][i][rgb] = padding_tensor[1][i][rgb];
                // down->up
                padding_tensor[0][i][rgb] = padding_tensor[h][i][rgb];
            }
        }

        // edge padding
        for(int rgb = 0;rgb<3;rgb++){
            padding_tensor[0][0][rgb] = padding_tensor[h][w][rgb];
            padding_tensor[0][w+1][rgb] = padding_tensor[h][1][rgb];
            padding_tensor[h+1][w+1][rgb] = padding_tensor[1][1][rgb];
            padding_tensor[h+1][0][rgb] = padding_tensor[1][w][rgb];
        }


        //***********calculate**********
        double res_x = 0;
        double res_y = 0;
        // below two is loop in padding_tensor
        for(int row = 1;row<=h;row++){
            for(int col = 1;col<=w;col++){  
                // below two is loop in kernel
                for(int index_1 = -1;index_1<2;index_1++){
                    for(int index_2 = -1;index_2<2;index_2++){
                        res_x += kernel[index_1+1][index_2+1]*padding_tensor[row+index_1][col+index_2][0];
                        res_y += kernel2[index_1+1][index_2+1]*padding_tensor[row+index_1][col+index_2][0];
                    }
                }

                int result = (int)(Math.sqrt(res_x*res_x+res_y*res_y));
                for(int rgb =0;rgb<3;rgb++){
                    res_tensor[row-1][col-1][rgb] = 255-result;
                    if(res_tensor[row-1][col-1][rgb]>255){
                        res_tensor[row-1][col-1][rgb] = 255;
                    }
                    if(res_tensor[row-1][col-1][rgb]<0){
                        res_tensor[row-1][col-1][rgb] = 0;
                    }
                }
                res_x = 0;
                res_y = 0;

                Color res_color = new Color(res_tensor[row-1][col-1][0],res_tensor[row-1][col-1][1],res_tensor[row-1][col-1][2]);
                pic_res.setColor(col-1, row-1, res_color);
            }
        }
        picture = pic_res;
        return picture;
    }
    
    
    public static void main(String[] args) {
        Picture andrew = new Picture("C:\\qp_box\\Liverpool\\Y2\\CPT105\\CPT105\\cw3\\Andrew.png");
        // Picture qp = new Picture("C:\\qp_box\\Liverpool\\Y2\\CPT105\\CPT105\\cw3\\2.png");
        // emboss(qp).show();
        // edge(qp).show();

        // System.out.println(andrew.width());
        // System.out.println(andrew.height());
        // andrew.show();
        
        tilt(andrew).show();
        // Picture tiltAndrew = new Picture("C:\\qp_box\\Liverpool\\Y2\\CPT105\\CPT105\\cw3\\Expected\\TiltAndrew.png");
        // System.out.println(tilt(andrew).equals(tiltAndrew));
        
        // emboss(andrew).show();
        // Picture embossAndrew = new Picture("C:\\qp_box\\Liverpool\\Y2\\CPT105\\CPT105\\cw3\\Expected\\EmbossAndrew.png");
        // System.out.println(emboss(andrew).equals(embossAndrew));
        
        // blur(andrew).show();
        // Picture blurAndrew = new Picture("C:\\qp_box\\Liverpool\\Y2\\CPT105\\CPT105\\cw3\\Expected\\BlurAndrew.png");
        // System.out.println(blur(andrew).equals(blurAndrew));
        
        // edge(andrew).show();
        // Picture edgeAndrew = new Picture("C:\\qp_box\\Liverpool\\Y2\\CPT105\\CPT105\\cw3\\Expected\\EdgeAndrew.png");
        // System.out.println(edge(andrew).equals(edgeAndrew));
    }
    
}
