package com.sever.ftr.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Scaling;

/**
 * Image which is properly scaled in percentages depending on origin and screen size.
 * Scaling is fitted to Y axis.
 * All values are in percentages (xPercentage, yPercentage and heightPercentage)
 * Origin tells which point of the image is used for positioning the image.\n
 * Example: origin is TOP_CENTER, xPercentage = 0.5f, yPercentage = 1f, heightPercentage = 0.5f:\n
 * In this case image would be positioned at the top center of the screen and it's height would be half of the screem height.
 * Width of widget is calculated using acpect ratio of image and widget height.
 * \nDefault value for origin is CENTER CENTER
 * 
 * @author Nejc Sever
 */
public class ResizableImage extends Image {
	
	public static final int TOP_LEFT = 0;
	public static final int TOP_CENTER = 1;
	public static final int TOP_RIGHT = 2;
	public static final int CENTER_LEFT = 3;
	public static final int CENTER_CENTER = 4;
	public static final int CENTER_RIGHT = 5;
	public static final int BOTTOM_LEFT = 6;
	public static final int BOTTOM_CENTER = 7;
	public static final int BOTTOM_RIGHT = 8;
	
	private int origin;
	private float heightPercentage;
	private float imageAspectRatio = 1;
	private float xPercentage;
	private float yPercentage;
	private Drawable image;
	private Drawable imageDown;
	
	public ResizableImage(float heightPercentage, int origin) {
		super();
		setScaling(Scaling.fillY);
		this.origin = CENTER_CENTER;
		this.heightPercentage = heightPercentage;
		
		resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}
	
	/**
	 * Default origin is CENTER_CENTER. Constructor places widget depending on given xPercentage, yPercentage and heightPErcentage.
	 * @param image - Drawable of the widget
	 * @param xPercentage
	 * @param yPercentage
	 * @param heightPercentage - Screen height percentage
	 */
	public ResizableImage(Drawable image, float xPercentage, float yPercentage, float heightPercentage) {
		super(image);
		
		setScaling(Scaling.fillY);
		
		this.origin = CENTER_CENTER;
		this.imageAspectRatio = image.getMinHeight() / image.getMinWidth();
		this.image = image;
		this.xPercentage = xPercentage;
		this.yPercentage = yPercentage;
		this.heightPercentage = heightPercentage;
		
		resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}
	
	public ResizableImage(Drawable image, float xPercentage, float yPercentage, float heightPercentage, int origin) {
		this(image, xPercentage, yPercentage, heightPercentage);
		this.origin = origin;
	}
	
	public ResizableImage(Drawable image, Drawable imageDown, float xPercentage, float yPercentage, float heightPercentage, int origin) {
		this(image, xPercentage, yPercentage, heightPercentage, origin);
		this.imageDown = imageDown;
	}
	
	/**
	 * Scales widget to Y axis while keeping aspect ratio the same.
	 * @param width - Screen width
	 * @param height - Screen height
	 */
	public void resize(int width, int height) {
		float widgetHeight = height * heightPercentage;
		float widgetWidth = widgetHeight / imageAspectRatio;
		super.setSize(widgetWidth, widgetHeight);
		float positionX = width * xPercentage;
		float positionY = height * yPercentage;
		switch (origin) {
			case TOP_LEFT:
				setPosition(positionX, positionY - widgetHeight);
				break; 
			case TOP_CENTER:
				setPosition(positionX - widgetWidth/2, positionY - widgetHeight);
				break;
			case TOP_RIGHT:
				setPosition(positionX - widgetWidth, positionY - widgetHeight);
				break;
			case CENTER_LEFT:
				setPosition(positionX, positionY - widgetHeight/2);
				break;
			case CENTER_CENTER:
				setPosition(positionX - widgetWidth/2, positionY - widgetHeight/2);
				break;
			case CENTER_RIGHT:
				setPosition(positionX - widgetWidth, positionY - widgetHeight/2);
				break;
			case BOTTOM_LEFT:
				setPosition(positionX, positionY);
				break;
			case BOTTOM_CENTER:
				setPosition(positionX - widgetWidth/2, positionY);
				break;
			case BOTTOM_RIGHT:
				setPosition(positionX - widgetWidth, positionY);
				break;
		}
	}
	
	/**
	 * Draws imageDown. Used for click events.
	 */
	public void drawImageDown() {
		setDrawable(imageDown);
	}
	
	/**
	 * Draws image. Used to reset drawable from imageDown to image.
	 */
	public void drawOriginalImage() {
		setDrawable(image);
	}
	
	/**
	 * Toggles between original image and down image.
	 */
	public void toggleImage() {
		if (getDrawable() == image) {
			drawImageDown();
		} else {
			drawOriginalImage();
		}
	}
	
	public void setxPercentage(float yPercentage) {
		this.xPercentage = yPercentage;
		resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	public void setyPercentage(float yPercentage) {
		this.yPercentage = yPercentage;
		resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	public void setImage(Drawable image) {
		this.image = image;
		this.imageAspectRatio = image.getMinHeight() / image.getMinWidth();
	}
	
	public void resetImage() {
		super.setDrawable(null);
		this.image = null;
	}
	
}
