package main;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class ImageDecoderSample {
	public static void main(String[] args)throws Exception{

		new ImageDecoderSample().exec();

	}
	String _srcDirPath = "./resources/ImageDecoderSample/src/";
	String _dstDirPath = "./resources/ImageDecoderSample/dst/";

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

	private void exec()throws Exception{

		File srcDir = new File(this._srcDirPath);
		File srcFiles[] = srcDir.listFiles();

		ArrayList<Integer> typeList = new ArrayList<Integer>();

		for(File srcFile : srcFiles){

			BufferedImage srcImg = ImageIO.read(srcFile);

			int h = srcImg.getHeight();
			int w = srcImg.getWidth();
			int src2d[][] = new int[h][w];

			getImage(srcImg,src2d,srcFile);

			/*
			 * 統一出力TYPE_BYTE_GRAY.png
			 */
			//			BufferedImage dstImg = new BufferedImage(w,h,BufferedImage.TYPE_BYTE_GRAY);
			//			WritableRaster dstRas = dstImg.getRaster();
			//			DataBuffer dstBuf = dstRas.getDataBuffer();
			//			
			//			int dst2d[][] = new int[h][w];
			//			for(int y = 0; y < h; y ++){
			//				for(int x = 0; x < w; x++){
			//					dst2d[y][x] = src2d[y][x];
			//					dstBuf.setElem(y*w+x, dst2d[y][x]);
			//				}
			//			}
			//
			//			String path = srcFile.getName();
			//			String[] name = path.split("\\.");
			//			String dstFilePath = this._dstDirPath+name[0]+".png";
			//			File dstFile = new File(dstFilePath);
			//		
			//			ImageIO.write(dstImg, "png", dstFile);

			//						System.out.println(srcFile.getName()+" , "+this._imageTypes[srcImg.getType()]);
			//			if( ! typeList.contains( srcImg.getType() ))
			//				typeList.add( srcImg.getType() );
		}
		//		System.out.println(Arrays.toString( typeList.toArray() ));
		//
		//		for(Integer idx : typeList){
		//			System.out.println(this._imageTypes[ idx ]);
		//		}
	}



	private void getImage(BufferedImage srcImg, int[][] src2d, File srcFile) {

		WritableRaster srcRas = srcImg.getRaster();
		DataBuffer srcBuf = srcRas.getDataBuffer();

		int h = srcImg.getHeight();
		int w = srcImg.getWidth();

		switch(srcImg.getType()){

		case 5:
			//TYPE_3BYTE_BGR
			for(int y = 0; y < h; y++){
				for(int x = 0; x < w; x++){
					src2d[y][x] = srcBuf.getElem((y*w+x)*3);
				}
			}
			break;
		case 6:
			//TYPE_4BYTE_ABGR
			for(int y = 0; y < h; y++){
				for(int x = 0; x < w; x++){
					src2d[y][x] = srcBuf.getElem((y*w+x)*4+1);
				}
			}
			break;
		case 8:
			//TYPE_USHORT_565_RGB
			for(int y = 0; y < h; y++){
				for(int x = 0; x < w; x++){
					src2d[y][x] = srcBuf.getElem(y*w+x);
				}
			}
			//いずれかのチャンネルの倍率取得
			double red[][] = new double[h][w];
			for(int y = 0; y < h; y++){
				for(int x = 0; x < w; x++){
					red[y][x] = src2d[y][x] >>11 & 0xff;
				red[y][x] = red[y][x]/31;
				src2d[y][x] = (int) (red[y][x]*255);
				}
			}
			break;
		case 9:
			//TYPE_USHORT_555_RGB
			for(int y = 0; y < h; y++){
				for(int x = 0; x < w; x++){
					src2d[y][x] = srcBuf.getElem(y*w+x);
				}
			}

			//いずれかのチャンネルの倍率取得
			double red2[][] = new double[h][w];
			for(int y = 0; y < h; y++){
				for(int x = 0; x < w; x++){
					red2[y][x] = src2d[y][x] >>10 & 0xff;
				red2[y][x] = red2[y][x]/31;
				src2d[y][x] = (int) (red2[y][x]*255);
				}
			}
			break;
		case 10:
			//TYPE_BYTE_GRAY
			for(int y = 0; y < h; y++){
				for(int x = 0; x < w; x++){
					src2d[y][x] = srcBuf.getElem(y*w+x);
				}
			}
			break;
		case 11:
			//TYPE_USHORT_GRAY
			for(int y = 0; y < h; y++){
				for(int x = 0; x < w; x++){
					src2d[y][x] = srcBuf.getElem(y*w+x);
					src2d[y][x] = src2d[y][x]/256;
				}
			}
			break;
		case 12:
			//TYPE_BYTE_BINARY
			if(srcBuf.getSize() != 512){
				//まずバイト配列を取得する
				//2048
				int bin2d[][] = new int[h][w/2];
				for(int y = 0; y < h; y++){
					for(int x = 0; x < w/2; x++){
						bin2d[y][x] = srcBuf.getElem(y*(w/2)+x);
					}
				}
				//パックされた輝度値を解体する
				int part2d[][] = new int[h][w*4];
				for(int y = 0; y < h; y++){
					for(int x = 0; x < w/2;x++){
						for(int b = 0; b < 8; b++){
							part2d[y][x*8+b] = bin2d[y][x]>>7-b & 0x01;
						if(part2d[y][x*8+b] != 0)
							part2d[y][x*8+b] = 255;
						}
					}
				}

				//一つのチャンネルを取得する
				int red3[][] = new int[h][w];
				for(int y = 0; y < h; y++){
					for(int x = 0; x < w; x++){
						red3[y][x] = part2d[y][4*x+0];
						src2d[y][x] = red3[y][x];
					}
				}

			}else{
				int bin2d[][] = new int[h][w/8];
				for(int y = 0; y < h; y++){
					for(int x = 0; x < w/8; x++){
						bin2d[y][x] = srcBuf.getElem(y*(w/8)+x);
					}
				}
				for(int y = 0; y < h; y++){
					for(int x = 0; x < w/8;x++){
						for(int b = 0; b < 8; b++){
							src2d[y][x*8+b] = bin2d[y][x]>>7-b & 0x01;
						if(src2d[y][x*8+b] != 0)
							src2d[y][x*8+b] = 255;
						}
					}
				}
			}
			BufferedImage dstImg = new BufferedImage(w,h,BufferedImage.TYPE_BYTE_GRAY);
			WritableRaster dstRas = dstImg.getRaster();
			DataBuffer dstBuf = dstRas.getDataBuffer();

			int dst2d[][] = new int[h][w];
			for(int y = 0; y < h; y ++){
				for(int x = 0; x < w; x++){
					dst2d[y][x] = src2d[y][x];
					dstBuf.setElem(y*w+x, dst2d[y][x]);
				}
			}

			String path = srcFile.getName();
			String[] name = path.split("\\.");
			String dstFilePath = this._dstDirPath+name[0]+".png";
			File dstFile = new File(dstFilePath);

			try {
				ImageIO.write(dstImg, "png", dstFile);
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}

			break;


			//		case 13:
			//			//TYPE_BYTE_INDEXED
			//			for(int y = 0; y < h; y++){
			//				for(int x = 0; x < w; x++){
			//					src2d[y][x] = srcBuf.getElem(y*w+x);
			//				}
			//			}
			//			break;
		}

	}
}
