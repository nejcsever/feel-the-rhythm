package com.sever.ftr.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Scaling;

public class MyWidget extends Image {
	
	public static final int TOP_LEFT = 0;
	public static final int TOP_CENTER = 1;
	public static final int TOP_RIGHT = 2;
	public static final int CENTER_LEFT = 3;
	public static final int CENTER_RIGHT = 5;
	public static final int BOTOTM_LEFT = 6;
	public static final int BOTTOM_CENTER = 7;
	public static final int BOTTOM_RIGHT = 8;
	
	private int origin= 4;
	private float heightPercentage;
	private float imageAspectRatio = 1;
	private float xPercentage;
	private float yPercentage;
	
	public MyWidget(Drawable drawable, float xPercentage, float yPercentage, float heightPercentage) {
		super(drawable);
		setScaling(Scaling.fillY);
		imageAspectRatio = drawable.getMinHeight() / drawable.getMinWidth();
		this.heightPercentage = heightPercentage;
		this.xPercentage = xPercentage;
		this.yPercentage = yPercentage;
		resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}
	
	public MyWidget(Drawable drawable, float x, float y, float height, float width, int origin) {
		super(drawable);
		super.setPosition(x, y);
		super.setSize(width, height);
	}
	
	public void resize(int width, int height) {
		float widgetHeight = height * heightPercentage;
		float widgetWidth = widgetHeight / imageAspectRatio;
		super.setSize(widgetWidth, widgetHeight);
		setPosition(width * xPercentage - widgetWidth/2, height * yPercentage - widgetHeight/2);
	}
}
