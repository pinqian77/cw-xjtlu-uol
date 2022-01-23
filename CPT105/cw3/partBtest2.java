package cw3;

import java.awt.Color;
/**
 * CPT105 2020 Coursework 3 Part B
 */
public class partBtest2 {
    
    /****************
     * CW3 Part B.1 *
     ****************/
    
    // Original picture and positionally tranformed picture.
    public static Picture positionalTransform(Picture picture) {
        int src_width = picture.width();
        int src_height = picture. height();
        Picture pic_res = new Picture(src_width*2,src_height);

        //********** right-tranformed **********
        //////fish-eye effect//////
		int cenX = (int)(picture.width()/2);
		int cenY = (int)(picture.height()/2);
        int r = (int)picture.width()/2;
        
		for (int row = 0; row < picture.height(); row++) {
            for (int col = 0; col < picture.width(); col++) {
                //先处理像素坐标
                int m= 1;//几个像素为一个单元
                int rate = r/m; //与边缘的清晰程度
                if(Math.sqrt((cenX-col)*(cenX-col) +(cenY-row)*(cenY-row)) < r){
                    int tx = (col - cenX)/m ; //不同的函数会导致不同的效果
                    int ty = (row - cenY)/m ;

                    double xx = (cenX - col)*(cenX - col);  
                    double yy = (cenY - row)*(cenY - row);  
                    int distance = (int)Math.sqrt(xx + yy);  

                    tx  =(int)(tx*distance/rate) + cenX;
                    ty  =(int)(ty*distance/rate) + cenY;
                    
                    // 以上是处理坐标，接下俩是取色设色
                    Color color = picture.getColor(tx, ty);
                    pic_res.setColor(col+src_width, row, color);
                }
                else{
                    Color color = picture.getColor(col, row);
                    pic_res.setColor(col+src_width, row, color);
                }
            }
        }

        Picture pic_res2 = new Picture(src_width*2,src_height);
        //////sin effect//////
        for (int col = 0; col < picture.width(); col++) {
            for (int row = 0; row < picture.height(); row++) {
                int theta = (int)(Math.sin(Math.PI/15*row)*6);
                Color color = pic_res.getColor(col+src_width, row);
                if(col+theta<src_width && col+theta>=0 && row<src_height && row>=0){
                    pic_res2.setColor(col+src_width+theta, row, color);
                }                
            }
        }

        // ********** left-normal **********
        for (int row=0; row<src_height; row++){
            for(int col=0; col<src_width; col++){
                Color color = picture.getColor(col, row);
                pic_res2.setColor(col, row, color);
            }
        }

        System.out.println("height:"+pic_res.height());
        System.out.println("width:"+pic_res.width());
        picture = pic_res2;
        return picture;
    }
    
	
    /****************
     * CW3 Part B.2 *
     ****************/
    
    // Original picture and color tranformed picture.
    public static Picture colorTransform(Picture picture) {
        int t_width = 150;
        int t_height = 200;
        Picture target = new Picture(t_width,t_height);
        for (int t_col=0; t_col<t_width; t_col++){
            for(int t_row=0; t_row<t_height; t_row++){
                int src_col = t_col*picture.width()/(t_width+20);
                int src_row = t_row*picture.height()/t_height;
                target.setColor(t_col, t_row, picture.getColor(src_col, src_row));
            }
        }
        return target;
    }
    
	
    public static void main(String[] args) {
        Picture andrew = new Picture("C:\\qp_box\\Liverpool\\Y2\\CPT105\\CPT105\\cw3\\Andrew.png");
        //andrew.show();
        
        positionalTransform(andrew).show();
        
        colorTransform(andrew).show();
        
    }
}