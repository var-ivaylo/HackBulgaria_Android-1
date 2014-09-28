package stainedglassfilter;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import edu.wlu.cs.levy.CG.KDTree;
import edu.wlu.cs.levy.CG.KeyDuplicateException;
import edu.wlu.cs.levy.CG.KeySizeException;

public class StainedGlassFilter {
	private static Random rng;

	private KDTree<Integer> kd;

	private int[][] colors; // [0]red; [1]green; [2]blue; [3]saturation;
	private int[][] nearestPoints;

	public StainedGlassFilter() {
		StainedGlassFilter.rng = new Random();
	}

	public static void main(String[] args) {
		StainedGlassFilter filter = new StainedGlassFilter();
		filter.filter(args[0], args[1], Integer.parseInt(args[2]));
	}

	public void filter(String inputFilePath, String outputFileName, int regions) {
		this.kd = new KDTree<Integer>(2);
		this.colors = new int[4][regions];

		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(inputFilePath));
		} catch (IOException e) {
			System.out.println("Error reading image");
			System.exit(-1);
		}

		int imageWidth = img.getWidth();
		int imageHeight = img.getHeight();
		this.nearestPoints = new int[imageWidth][imageHeight];

		this.generateCenters(imageWidth, imageHeight, regions);
		this.checkPixels(imageWidth, imageHeight, img);
		this.recolorPixels(imageWidth, imageHeight, img);

		try {
			ImageIO.write(img, inputFilePath.substring(inputFilePath.lastIndexOf('.') + 1), new File(outputFileName));
		} catch (IOException e) {
			System.out.println("Error saving image");
			System.exit(-1);
		}
	}

	private void generateCenters(int width, int height, int regions) {
		boolean[][] usedSpots = new boolean[width][height];
		double[][] points = new double[regions][2];

		for (int point = 0; point < regions; point++) {
			int randomWidth = rng.nextInt(width);
			int randomHeight = rng.nextInt(height);

			if (!usedSpots[randomWidth][randomHeight]) {
				usedSpots[randomWidth][randomHeight] = true;
				points[point][0] = randomWidth;
				points[point][1] = randomHeight;
				try {
					this.kd.insert(points[point], point);
				} catch (KeySizeException e) {
					// should never happen;
				} catch (KeyDuplicateException e) {
					// should never happen;
				}
			} else {
				point--;
				continue;
			}
		}
	}

	private void checkPixels(int width, int height, BufferedImage img) {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				Color pixelColor = new Color(img.getRGB(i, j));
				try {
					int nearestPoint = this.kd.nearest(new double[] { i, j });

					this.nearestPoints[i][j] = nearestPoint;

					this.colors[0][nearestPoint] += pixelColor.getRed();
					this.colors[1][nearestPoint] += pixelColor.getGreen();
					this.colors[2][nearestPoint] += pixelColor.getBlue();
					this.colors[3][nearestPoint]++; // saturation;
				} catch (KeySizeException e) {
					// should never happen;
				}
			}
		}
	}

	private void recolorPixels(int height, int width, BufferedImage img) {
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int nearestPoint = this.nearestPoints[i][j];
				int pixelSaturation = this.colors[3][nearestPoint];

				Color newPixelColor = new Color(
						this.colors[0][nearestPoint] / pixelSaturation,
						this.colors[1][nearestPoint] / pixelSaturation,
						this.colors[2][nearestPoint] / pixelSaturation);

				img.setRGB(i, j, newPixelColor.getRGB());
			}
		}
	}
}