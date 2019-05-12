/*package com.vbee.springbootmongodbnewspapersrestapi.ulti;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class ImageResize {

	private static Integer IMAGE_SMALL_WIDTH = 140;
	private static Integer IMAGE_SMALL_HEIGHT = 90;
	private static Integer IMAGE_LARGE_WIDTH = 280;
	private static Integer IMAGE_LARGE_HEIGHT = 180;
	public static String resizePictureSmall(String picture) {
		 byte[] imageInByte = null;

	        try {
	            URL url = new URL("https://image.thanhnien.vn/665/uploaded/minhnguyet/2018_01_14/tuvan3_ytof.jpg");
	            BufferedImage imageRead = ImageIO.read(url);
	            System.out.println("height: " + imageRead.getHeight());
	            System.out.println("width: " + imageRead.getWidth());
	            int widthOut = imageRead.getWidth();
	            int heightOut = (imageRead.getHeight() * widthOut) / imageRead.getWidth();
	            BufferedImage imageResized = resize(url, new Dimension(widthOut, heightOut));
	            ByteArrayOutputStream baos = new ByteArrayOutputStream();
	            ImageIO.write(imageResized, "jpg", baos);
	            baos.flush();
	            imageInByte = baos.toByteArray();
	            baos.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	}

	public static String resizePictureLarge(String picture){
		 byte[] imageInByte = null;

	        try {
	            URL url = new URL(picture);
	            BufferedImage imageRead = ImageIO.read(url);
	            System.out.println("height: " + imageRead.getHeight());
	            System.out.println("width: " + imageRead.getWidth());
	            int widthOut = imageRead.getWidth();
	            int heightOut = (imageRead.getHeight() * widthOut) / imageRead.getWidth();
	            BufferedImage imageResized = resize(url, new Dimension(widthOut, heightOut));
	            ByteArrayOutputStream baos = new ByteArrayOutputStream();
	            ImageIO.write(imageResized, "jpg", baos);
	            baos.flush();
	            imageInByte = baos.toByteArray();
	            baos.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	}

	public static BufferedImage resize(final URL url, final Dimension size) throws IOException {
		final BufferedImage image = ImageIO.read(url);
		final BufferedImage resized = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
		final Graphics2D g = resized.createGraphics();
		g.drawImage(image, 0, 0, size.width, size.height, null);
		g.dispose();
		return resized;
	}

}
*/