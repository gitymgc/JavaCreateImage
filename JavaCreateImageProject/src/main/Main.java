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

		//		System.out.println(dstImg.getType());
		//		System.out.println(this._imageTypes[dstImg.getType()]);

		DataBuffer dstBuf = dstImg.getRaster().getDataBuffer();
		//		System.out.println(dstBuf.getSize());

		//		System.out.println();
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
			break;
		case 2:
			getArgb(gra2d, dstImg, dst2d);
			break;
		case 3:
			//(getArgb)
			getArgbPre(gra2d, dstImg,dst2d);
			break;
		case 4:
			//(getRgb)
			getBgr(gra2d, dstImg,dst2d);
			break;
		case 5:
			get3Bgr(gra2d, dstImg,dst2d);
			break;
		case 6:
			get4Abgr(gra2d, dstImg,dst2d);
			break;
		case 7:
			get4AbgrPre(gra2d, dstImg,dst2d);
			break;
		case 8:
			get565Rgb(gra2d, dstImg,dst2d);
			break;
		case 9:
			get555Rgb(gra2d, dstImg,dst2d);
			break;
		case 10:
			getGray(gra2d, dstImg,dst2d);
			break;
		case 11:
			getUshortGray(gra2d, dstImg,dst2d);
			break;
		case 12:
			getTypeBiteBinary(gra2d, dstImg,dst2d);
			break;
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
				//				System.out.println(Integer.toBinaryString(dst2d[y][x]));
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
				//アルファ値格納
				dst2d[y][x] = 255 <<24 ;
				for(int c = 0; c < 3; c++){
					dst2d[y][x] += gra2d[y][x] <<(16-(8*c)) ;
				}
				//				System.out.println(Integer.toBinaryString(dst2d[y][x]));
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
				//アルファ値格納
				dst2d[y][x] = 255 <<24 ;
				for(int c = 0; c < 3; c++){
					dst2d[y][x] += gra2d[y][x] <<(16-(8*c)) ;
				}
				//				System.out.println(Integer.toBinaryString(dst2d[y][x]));
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

	private void getBgr(int[][] gra2d, BufferedImage dstImg,int[][] dst2d) {

		//各チャンネルに格納
		for(int y = 0; y < h; y++){
			for(int x = 0; x < w; x++){
				for(int c = 0; c < 3; c++){
					dst2d[y][x] += gra2d[y][x] <<(16-(8*c)) ;
				}
				//				System.out.println(Integer.toBinaryString(dst2d[y][x]));
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
	private void get3Bgr(int[][] gra2d, BufferedImage dstImg,int[][] dst2d) {

		DataBuffer dstBuf = dstImg.getRaster().getDataBuffer();
		//各チャンネルに格納
		//		int rgb1d[] = new int[dstBuf.getSize()];
		//		for(int y = 0; y < h; y++){
		//			for(int x = 0; x < w; x++){
		//				for(int c = 0; c < 3; c++){
		//					rgb1d[3*(y*w+x)+c] = gra2d[y][x];
		//				}
		//			}
		//		}
		//		for(int y = 0; y < h; y++){
		//			for(int x = 0; x < w; x++){
		//				for(int c = 0; c < 3; c++){
		//					dstBuf.setElem(3*(y*w+x)+c,rgb1d[3*(y*w+x)+c]);
		//				}
		//			}
		//		}

		for(int y = 0; y < h; y++){
			for(int x = 0; x < w; x++){
				for(int c = 0; c < 3; c++){
					dstBuf.setElem(3*(y*w+x)+c,gra2d[y][x]);
				}
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

	private void get4Abgr(int[][] gra2d, BufferedImage dstImg,int[][] dst2d) {

		DataBuffer dstBuf = dstImg.getRaster().getDataBuffer();
		//各チャンネルに格納

		for(int y = 0; y < h; y++){
			for(int x = 0; x < w; x++){
				for(int c = 1; c < 4; c++){
					dstBuf.setElem(4*(y*w+x)+c,gra2d[y][x]);
					dstBuf.setElem(4*(y*w+x),255);
				}
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

	private void get4AbgrPre(int[][] gra2d, BufferedImage dstImg,int[][] dst2d) {

		DataBuffer dstBuf = dstImg.getRaster().getDataBuffer();
		//各チャンネルに格納

		for(int y = 0; y < h; y++){
			for(int x = 0; x < w; x++){
				for(int c = 1; c < 4; c++){
					dstBuf.setElem(4*(y*w+x)+c,gra2d[y][x]);
					dstBuf.setElem(4*(y*w+x),255);
				}
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

	private void get565Rgb(int[][] gra2d, BufferedImage dstImg,int[][] dst2d) {

		DataBuffer dstBuf = dstImg.getRaster().getDataBuffer();
		//gra2d正規化
		double nrm2d[][] =  new double[h][w];
		for(int y = 0; y < h; y++){
			for(int x = 0; x < w; x++){
				nrm2d[y][x] = (double)gra2d[y][x]/255;
			}
		}
		int pac2d[][] = new int[h][w];
		for(int y = 0 ;y < h; y++){
			for(int x = 0; x < w; x++){
				pac2d[y][x] |= (int)(nrm2d[y][x]*31)<<11;
				pac2d[y][x] |= (int)(nrm2d[y][x]*63)<<5;
				pac2d[y][x] |= (int)(nrm2d[y][x]*31);
				dstBuf.setElem(y*w+x,pac2d[y][x]);
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
	private void get555Rgb(int[][] gra2d, BufferedImage dstImg,int[][] dst2d) {

		DataBuffer dstBuf = dstImg.getRaster().getDataBuffer();
		//gra2d正規化
		double nrm2d[][] =  new double[h][w];
		for(int y = 0; y < h; y++){
			for(int x = 0; x < w; x++){
				nrm2d[y][x] = (double)gra2d[y][x]/255;
			}
		}
		int pac2d[][] = new int[h][w];
		for(int y = 0 ;y < h; y++){
			for(int x = 0; x < w; x++){
				pac2d[y][x] |= (int)(nrm2d[y][x]*31)<<10;
				pac2d[y][x] |= (int)(nrm2d[y][x]*31)<<5;
				pac2d[y][x] |= (int)(nrm2d[y][x]*31);
				dstBuf.setElem(y*w+x,pac2d[y][x]);
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
	private void getGray(int[][] gra2d, BufferedImage dstImg, int[][] dst2d) {

		DataBuffer dstBuf = dstImg.getRaster().getDataBuffer();
		for(int y = 0; y < h; y++){
			for(int x = 0; x < w; x++){
				dstBuf.setElem(y*w+x,gra2d[y][x]);
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

	private void getUshortGray(int[][] gra2d, BufferedImage dstImg, int[][] dst2d) {

		DataBuffer dstBuf = dstImg.getRaster().getDataBuffer();
		//gra2d正規化
		double nrm2d[][] =  new double[h][w];
		for(int y = 0; y < h; y++){
			for(int x = 0; x < w; x++){
				nrm2d[y][x] = (double)gra2d[y][x]/255;
			}
		}
		
		int pac2d[][] = new int[h][w];
		for(int y = 0 ;y < h; y++){
			for(int x = 0; x < w; x++){
				pac2d[y][x] = (int)(nrm2d[y][x]*65535);
				System.out.println(Integer.toBinaryString(pac2d[y][x]));
				dstBuf.setElem(y*w+x,pac2d[y][x]);
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
	
	private void getTypeBiteBinary(int[][] gra2d, BufferedImage dstImg, int[][] dst2d) {
		
		DataBuffer dstBuf = dstImg.getRaster().getDataBuffer();
		System.out.println(dstBuf.getSize());
		for(int y = 0; y < h; y++){
			for(int x = 0; x < w/8; x++){
				dstBuf.setElem(y*w/8+x, gra2d[y][x]);
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
