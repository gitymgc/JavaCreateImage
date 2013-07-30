package main;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;

import javax.imageio.ImageIO;

public class Main {

	public static void main(String args[]) throws Exception{
		new Main().exec();
	}
	
	String _srcDirPath = "./resources/Main/src/";
	String _dstDirPath = "./resources/Main/dst/";
	
    String _imageTypes[] =
        {
             "TYPE_CUSTOM",
             "TYPE_INT_RGB",
             "TYPE_INT_ARGB",
             "TYPE_INT_ARGB_PRE",
             "TYPE_INT_BGR",
             "TYPE_3BYTE_BGR",
             "TYPE_4BYTE_ABGR",
             "TYPE_4BYTE_ABGR_PRE",
             "TYPE_USHORT_565_RGB",
             "TYPE_USHORT_555_RGB",
             "TYPE_BYTE_GRAY",
             "TYPE_USHORT_GRAY",
             "TYPE_BYTE_BINARY",
             "TYPE_BYTE_INDEXED",
        };
	
	private void exec() throws Exception{
		
		/*
		 * 入力ファイルは 3byte BGR。
		 * 配列の長さなど簡単に考えるために 64*64 にリサイズしてある。
		 */
		File srcFile = new File(this._srcDirPath+"001.png");
		BufferedImage srcImg = ImageIO.read(srcFile);
		int w = srcImg.getWidth(), h = srcImg.getHeight();
		int src2d[][] = new int[h][w];
		DataBuffer srcBuf = srcImg.getRaster().getDataBuffer();
		for(int y = 0; y < h; y++)
			for(int x = 0; x < w; x++)
				src2d[y][x] = srcBuf.getElem((y*w+x)*3);
		
		int gra2d[][] = src2d;
		
		/*
		 * この時点で、
		 * 1byte グレースケール の二次元配列は得ることができた。
		 * この値を BufferedImage に書き込んでほちい。
		 */
		
		// 14 == BufferedImage.TYPE_XX の数
		BufferedImage dstImgs[] = new BufferedImage[14];
		
		// CUSTOM は考えない
		for(int i = 1; i < 14; i++){
			
			// 各イメージタイプでインスタンス生成
			dstImgs[i] = new BufferedImage(w, h, i);
			// このインスタンスを直接書き換える形で、グレイスケール画像取得
			getGrayScaleImage(gra2d, dstImgs[i]);
			
		}
	}

	private void getGrayScaleImage(int[][] gra2d, BufferedImage dstImg) {
		
		System.out.println(this._imageTypes[dstImg.getType()]);
		
		DataBuffer dstBuf = dstImg.getRaster().getDataBuffer();
		System.out.println(dstBuf.getSize());
		
		System.out.println();
	}
}
