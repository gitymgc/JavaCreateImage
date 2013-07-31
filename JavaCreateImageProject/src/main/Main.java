package main;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Main {

	public static void main(String args[]) throws Exception{
		new Main().exec();
	}
	int w,h;
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
		w = srcImg.getWidth();
		h = srcImg.getHeight();
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

		System.out.println(dstImg.getType());
		System.out.println(this._imageTypes[dstImg.getType()]);

		DataBuffer dstBuf = dstImg.getRaster().getDataBuffer();
		System.out.println(dstBuf.getSize());

		System.out.println();
		int dst2d[][]  = new int[h][w];
		getDst2d(gra2d,dstImg,dst2d);

		//		for(int y = 0; y< h; y++){
		//			for(int x = 0; x < w; x++){
		//				dstBuf.setElem(y*w+x, dst2d[y][x]);
		//			}
		//		}
		//
		//		String dstFilePath = _dstDirPath + this._imageTypes[dstImg.getType()]+".png";
		//		File dstFile = new File(dstFilePath);
		//		try {
		//			ImageIO.write(dstImg, "png", dstFile);
		//		} catch (IOException e) {
		//			// TODO 自動生成された catch ブロック
		//			e.printStackTrace();
		//		}



	}
	private void getDst2d(int[][] gra2d, BufferedImage dstImg,int[][] dst2d) {

		switch(dstImg.getType()){
		case 1: 
			getRgb(gra2d, dstImg,dst2d);
			System.out.println("type 1");
			break;
		case 2:
			getArgb(gra2d, dstImg, dst2d);
			System.out.println("type 2");
			break;
		case 3:
			getArgbPre(gra2d, dstImg,dst2d);
			System.out.println("type 3");
			break;
		case 4:
			getRgb(gra2d, dstImg,dst2d);
			System.out.println("type 1");
			break;
		case 5:
			
		case 6:
		case 7:
		case 8:
		case 9:
		case 10:
		case 11:
		case 12:
		case 13:

		}

	}

	/**
	 * case1出力
	 * @param gra2d
	 * @param dstImg
	 */
	private void getRgb(int[][] gra2d, BufferedImage dstImg,int[][] dst2d) {

		//各チャンネルに格納

		for(int y = 0; y < h; y++){
			for(int x = 0; x < w; x++){
				for(int c = 0; c < 3; c++){
					dst2d[y][x] += gra2d[y][x] <<(16-(8*c)) ;
				}
				System.out.println(Integer.toBinaryString(dst2d[y][x]));
			}
		}
		DataBuffer dstBuf = dstImg.getRaster().getDataBuffer();
		for(int y = 0; y< h; y++){
			for(int x = 0; x < w; x++){
				dstBuf.setElem(y*w+x, dst2d[y][x]);
			}
		}

		String dstFilePath = _dstDirPath + this._imageTypes[dstImg.getType()]+".png";
		File dstFile = new File(dstFilePath);
		try {
			ImageIO.write(dstImg, "png", dstFile);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	private void getArgb(int[][] gra2d, BufferedImage dstImg,int[][] dst2d) {

		//各チャンネルに格納

		for(int y = 0; y < h; y++){
			for(int x = 0; x < w; x++){
				dst2d[y][x] = 255 <<24 ;
				for(int c = 0; c < 3; c++){
					dst2d[y][x] += gra2d[y][x] <<(16-(8*c)) ;
				}
				System.out.println(Integer.toBinaryString(dst2d[y][x]));
			}
		}
		DataBuffer dstBuf = dstImg.getRaster().getDataBuffer();
		for(int y = 0; y< h; y++){
			for(int x = 0; x < w; x++){
				dstBuf.setElem(y*w+x, dst2d[y][x]);
			}
		}

		String dstFilePath = _dstDirPath + this._imageTypes[dstImg.getType()]+".png";
		File dstFile = new File(dstFilePath);
		try {
			ImageIO.write(dstImg, "png", dstFile);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	private void getArgbPre(int[][] gra2d, BufferedImage dstImg,int[][] dst2d) {

		for(int y = 0; y < h; y++){
			for(int x = 0; x < w; x++){
				dst2d[y][x] = 255 <<24 ;
				for(int c = 0; c < 3; c++){
					dst2d[y][x] += gra2d[y][x] <<(16-(8*c)) ;
				}
				System.out.println(Integer.toBinaryString(dst2d[y][x]));
			}
		}
		DataBuffer dstBuf = dstImg.getRaster().getDataBuffer();
		for(int y = 0; y< h; y++){
			for(int x = 0; x < w; x++){
				dstBuf.setElem(y*w+x, dst2d[y][x]);
			}
		}

		String dstFilePath = _dstDirPath + this._imageTypes[dstImg.getType()]+".png";
		File dstFile = new File(dstFilePath);
		try {
			ImageIO.write(dstImg, "png", dstFile);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
}
