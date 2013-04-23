package com.fizz.dataa;




import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.color.Color;

import com.fizz.dataa.SceneManager.SceneType;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.WindowManager;

public class Dataa extends BaseGameActivity{
	
	
	private Sprite splash;
	private Scene splashScene,mainScene;
	private Camera camera;
	private SceneManager sceneManager;
	private static int CAMERA_WIDTH;
	private  static int CAMERA_HEIGHT;
	
	
	
	
	@Override
	public EngineOptions onCreateEngineOptions() {     WindowManager w = getWindowManager();
    Display d = w.getDefaultDisplay();
    int width = d.getWidth();
    int height = d.getHeight();
    CAMERA_WIDTH=width;
    CAMERA_HEIGHT=height;
    camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		
		
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, new FillResolutionPolicy(), camera);
		return engineOptions;
	}
	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws Exception {

		sceneManager = new SceneManager(this, mEngine, camera);
		sceneManager.loadSplashSceneResources();
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	
	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws Exception {
		pOnCreateSceneCallback.onCreateSceneFinished(sceneManager.createSplashScene());
	}

	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
		mEngine.registerUpdateHandler(new TimerHandler(1f, new ITimerCallback()
		{
			public void onTimePassed(final TimerHandler pTimerHandler)
			{
				mEngine.unregisterUpdateHandler(pTimerHandler);
				sceneManager.loadGameSceneResources();
				sceneManager.createGameScenes();
				
				sceneManager.setCurrentScene(SceneType.TITLE);
			}
		}));
			pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{  
	    if(sceneManager.getCurrentScene()!=SceneType.TITLE){
		if (keyCode == KeyEvent.KEYCODE_BACK)
	    {
	    	sceneManager.setCurrentScene(SceneType.TITLE);
	    }}
	    else
	    	this.finish();
	    return false; 
	}


	
	
	

}
