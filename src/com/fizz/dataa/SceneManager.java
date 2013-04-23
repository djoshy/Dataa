package com.fizz.dataa;

import java.util.ArrayList;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.PathModifier;
import org.andengine.entity.modifier.PathModifier.IPathModifierListener;
import org.andengine.entity.modifier.PathModifier.Path;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.color.Color;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class SceneManager {
	//Field used, too many :/
	private SceneType currentScene;
	private BaseGameActivity activity;
	private Engine engine;
	private Camera camera;
	//For Texture
	private BitmapTextureAtlas splashTextureAtlas;
	private ITextureRegion splashTextureRegion;
	private ITextureRegion stackTextureRegion;
	private ITextureRegion queueTextureRegion;
	private BitmapTextureAtlas queueTextureAtlas;
	private BitmapTextureAtlas BgTextureAtlas;
	private TextureRegion BgTextureRegion;
	private BitmapTextureAtlas stackTextureAtlas;
	private SpriteBackground bg;
	private TextureRegion carTextureRegion;
	private TextureRegion crossTextureRegion;
	private TextureRegion arrowTextureRegion;
	private TextureRegion car2TextureRegion;
	private TextureRegion car3TextureRegion;
	private TextureRegion pushTextureRegion;
	private TextureRegion popTextureRegion;
	private TextureRegion legoTextureRegion;

	//Different scenes used
	private Scene splashScene;
	private Scene titleScene;
	private Scene stackGameScene;
	private Scene queueGameScene;
	//Temps used.
	private Sprite temp;
	private Sprite cartest2;
	protected int queueflag;//used for preventing dequeing while a car is being enqueued
	private Stacker stack=new Stacker();//simulating stacks
	private int queuerear=-1;//queuerear
	//fields used for Physics engine
	private PhysicsWorld stackPhysicsWorld=new PhysicsWorld(new Vector2(0,100 ), false);
	private static final FixtureDef fdlego=PhysicsFactory.createFixtureDef(1,0,1);
	private ArrayList<Body> stackbodylist=new ArrayList<Body>();
	//Sprites used
	private ArrayList<Sprite> queuelist=new ArrayList<Sprite>();
	private Sprite lego;
	private Sprite cross;
	private Sprite cartest;
	private Sprite Dequeue;
	private Sprite Enqueue;
	private ArrayList<Sprite> templist=new ArrayList<Sprite>();
	//used for parking locations
	private float[] xes=new float[2];
	private float[] yes=new float[2];
	public  enum SceneType
	{
		SPLASH,
		TITLE,
		STACK,
		QUEUE, TREE
	}

	public SceneManager(BaseGameActivity activity, Engine engine, Camera camera) {
		this.activity = activity;
		this.engine = engine;
		this.camera = camera;
	}
	public SceneType getCurrentScene(){
		return currentScene;


	}

	//Method loads all of the splash scene resources
	public void loadSplashSceneResources() {

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		splashTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 800, 600, TextureOptions.DEFAULT);
		splashTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas, activity, "splash.png", 0, 0);
		splashTextureAtlas.load();
		BgTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(),1999,1999, TextureOptions.DEFAULT);
		BgTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(BgTextureAtlas, activity, "bg.png", 0, 0);
		BgTextureAtlas.load();

	}


	//Method loads all of the resources for the game scenes
	public void loadGameSceneResources() {

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		stackTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.DEFAULT);
		stackTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(stackTextureAtlas, activity, "stack.png", 0, 0);
		pushTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(stackTextureAtlas, activity, "push.png", 201, 0);
		popTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(stackTextureAtlas, activity, "pop.png",402 , 0);
		legoTextureRegion=BitmapTextureAtlasTextureRegionFactory.createFromAsset(stackTextureAtlas, activity, "leg2dv2.png",603,0);
		crossTextureRegion=BitmapTextureAtlasTextureRegionFactory.createFromAsset(stackTextureAtlas, activity, "cross.png",900,0);
		queueTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.DEFAULT);
		queueTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(queueTextureAtlas, activity, "queue.png", 0, 0);
		carTextureRegion=BitmapTextureAtlasTextureRegionFactory.createFromAsset(queueTextureAtlas, activity, "car.png",201,0);
		car2TextureRegion=BitmapTextureAtlasTextureRegionFactory.createFromAsset(queueTextureAtlas, activity, "car2.png",403,0);
		car3TextureRegion=BitmapTextureAtlasTextureRegionFactory.createFromAsset(queueTextureAtlas, activity, "car3.png",620,0);
		arrowTextureRegion=BitmapTextureAtlasTextureRegionFactory.createFromAsset(queueTextureAtlas, activity, "arrowup.png",850,0);
		stackTextureAtlas.load();
		queueTextureAtlas.load();



	}

	//Method creates the Splash Scene
	public Scene createSplashScene() {
		//Create the Splash Scene and set background colour to red and add the splash logo.
		final float centerX = (camera.getWidth() - BgTextureRegion.getWidth()) / 2;
		final float centerY = (camera.getHeight() - BgTextureRegion.getHeight()) / 2;

		bg = new SpriteBackground(new Sprite(centerX, centerY, BgTextureRegion,activity.getVertexBufferObjectManager()));

		splashScene = new Scene();
		splashScene.setBackground(bg);
		Sprite splash = new Sprite(0, 0, splashTextureRegion, activity.getVertexBufferObjectManager())
		{
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera)
			{
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
		};
		splash.setScale(1.0f);
		splash.setPosition((camera.getWidth() - splash.getWidth()) * 0.5f, (camera.getHeight() - splash.getHeight()) * 0.5f);

		splashScene.attachChild(splash);

		return splashScene;
	}


	//Method creates all of the Game Scenes
	public void createGameScenes() {
		//******************TITLE SCENE SETUP 
		titleScene = new Scene();
		titleScene.setBackground(bg);

		//stack button
		Sprite stacksp = new Sprite(0, 0, stackTextureRegion, activity.getVertexBufferObjectManager())
		{
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera)
			{
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				switch(pSceneTouchEvent.getAction()) {
				case TouchEvent.ACTION_DOWN:
					setScale(2.0f);
					break;
				case TouchEvent.ACTION_UP:
					//setCurrentScene(SceneType.STACK);
					setScale(1.0f);
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					setCurrentScene(SceneType.STACK);
					break;



				}

				return true;
			}
		};
		stacksp.setScale(1.0f);
		stacksp.setPosition((camera.getWidth() - stacksp.getWidth()) * 0.5f, ((camera.getHeight() - stacksp.getHeight()) * 0.2f));
		titleScene.registerTouchArea(stacksp);
		titleScene.setTouchAreaBindingOnActionDownEnabled(true);
		titleScene.setTouchAreaBindingOnActionMoveEnabled(true);


		titleScene.attachChild(stacksp);
		//queue button
		Sprite queuesp = new Sprite(0, 0, queueTextureRegion, activity.getVertexBufferObjectManager())
		{
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera)
			{
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				switch(pSceneTouchEvent.getAction()) {
				case TouchEvent.ACTION_DOWN:
					setScale(2.0f);
					break;
				case TouchEvent.ACTION_UP:
					setScale(1.0f);
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					setCurrentScene(SceneType.QUEUE);
					break;

				}


				return true;
			}


		};
		queuesp.setScale(1.0f);
		queuesp.setPosition((camera.getWidth() - queuesp.getWidth()) * 0.5f, ((camera.getHeight() - queuesp.getHeight()) * 0.5f));
		titleScene.registerTouchArea(queuesp);

		titleScene.attachChild(queuesp);
		//STACKGAMESCENE*******************************************************

		stackGameScene = new Scene();
		stackGameScene.setBackground(bg);
		//setting up the floor, connecting with physics engine
		Rectangle line_floor = new Rectangle(0,camera.getHeight()-120,camera.getWidth(),1,activity.getVertexBufferObjectManager());
		line_floor.setColor(Color.BLACK);
		Body floor=PhysicsFactory.createBoxBody(stackPhysicsWorld, line_floor, BodyType.StaticBody, fdlego);
		stackGameScene.attachChild(line_floor);
		stackPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(line_floor, floor,true,true));

		//push button
		Sprite push= new Sprite(0, 0, pushTextureRegion, activity.getVertexBufferObjectManager())
		{
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera)
			{
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				switch(pSceneTouchEvent.getAction()) {
				case TouchEvent.ACTION_DOWN:
					setScale(2.0f);
					break;
				case TouchEvent.ACTION_UP:
					setScale(1.0f);
					stackpush();
					break;



				}

				return true;
			}
		};
		//pop button
		Sprite pop= new Sprite(0, 0, popTextureRegion, activity.getVertexBufferObjectManager())
		{
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera)
			{
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				switch(pSceneTouchEvent.getAction()) {
				case TouchEvent.ACTION_DOWN:
					setScale(2.0f);
					break;
				case TouchEvent.ACTION_UP:
					setScale(1.0f);
					stackpop();
					break;



				}

				return true;
			}
		};
		//cross-incase incorrect input is done
		cross=new Sprite(0,0,crossTextureRegion,activity.getVertexBufferObjectManager()){
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera)
			{
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}	
		};
		cross.setPosition((camera.getWidth()-cross.getWidth())*0.9f, (camera.getHeight()-camera.getHeight())*0.1f);
		push.setPosition((camera.getWidth() - stacksp.getWidth()) * 0.25f, camera.getHeight() -100);
		pop.setPosition((camera.getWidth() - stacksp.getWidth()) * 0.75f, ((camera.getHeight() -100)));
		cross.setVisible(false);
		stackGameScene.registerTouchArea(push);
		stackGameScene.setTouchAreaBindingOnActionDownEnabled(true);
		stackGameScene.setTouchAreaBindingOnActionMoveEnabled(true);
		stackGameScene.attachChild(push);
		stackGameScene.registerTouchArea(pop);
		stackGameScene.attachChild(pop);

		stackGameScene.attachChild(cross);
		stackGameScene.registerUpdateHandler(stackPhysicsWorld);

		//QUEUEGAMESCENE***********************************
		queueGameScene = new Scene();
		queueGameScene.setBackground(bg);

		//enqueue button or arrow
		Enqueue= new Sprite(0, 0, arrowTextureRegion, activity.getVertexBufferObjectManager())
		{
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera)
			{
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				switch(pSceneTouchEvent.getAction()) {
				case TouchEvent.ACTION_DOWN:
					setScale(2.0f);
					break;
				case TouchEvent.ACTION_UP:
					setScale(1.0f);
					Enqueue();
					Dequeue.setVisible(false);

					cross.setVisible(false);
					break;



				}

				return true;
			}
		};
		//dequeue arrow
		Dequeue= new Sprite(0, 0, arrowTextureRegion, activity.getVertexBufferObjectManager())
		{
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera)
			{
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				switch(pSceneTouchEvent.getAction()) {
				case TouchEvent.ACTION_DOWN:
					setScale(2.0f);
					break;
				case TouchEvent.ACTION_UP:
					setScale(1.0f);
					if(queueflag==0)
					{Dequeue();
					}
					break;



				}

				return true;
			}
		};
		Enqueue.setPosition((camera.getWidth() - Enqueue.getWidth()) * 0.5f, camera.getHeight()*0.90f);
		Dequeue.setPosition((camera.getWidth() - Dequeue.getWidth()) * 0.5f, ((camera.getHeight()*0.01f)));

		queueGameScene.attachChild(Dequeue);
		queueGameScene.registerTouchArea(Dequeue);
		queueGameScene.attachChild(Enqueue);
		queueGameScene.registerTouchArea(Enqueue);
	}



	//function for enqueing cars, adds new car and they wait at end of line
	protected void Enqueue() {
		if (queuelist.size()<=7) {
			switch ((int) (Math.random() * 3)) {
			case 0:
				cartest = new Sprite(0, 0, carTextureRegion,
						activity.getVertexBufferObjectManager());
				break;
			case 1:
				cartest = new Sprite(0, 0, car2TextureRegion,
						activity.getVertexBufferObjectManager());
				break;
			case 2:
				cartest = new Sprite(0, 0, car3TextureRegion,
						activity.getVertexBufferObjectManager());
				break;

			}

			yes[0] = camera.getHeight();
			if (!queuelist.isEmpty()) {
				yes[1] = Dequeue.getY() + 100 + (queuerear + 1) * 200;
			} else
				yes[1] = Dequeue.getY() + Dequeue.getHeight();
			xes[0] = xes[1] = camera.getWidth() * 0.5f - cartest.getWidth()
					* 0.5f;
			queueGameScene.attachChild(cartest);
			Path carpath = new Path(xes, yes);
			PathModifier modifier = new PathModifier(1f, carpath,	new IPathModifierListener() {

				@Override
				public void onPathWaypointStarted(
						PathModifier pPathModifier, IEntity pEntity,
						int pWaypointIndex) {
				}

				@Override
				public void onPathWaypointFinished(
						PathModifier pPathModifier, IEntity pEntity,
						int pWaypointIndex) {

				}

				@Override
				public void onPathStarted(PathModifier pPathModifier,
						IEntity pEntity) {

				}

				@Override
				public void onPathFinished(PathModifier pPathModifier,
						IEntity pEntity) {
					Dequeue.setVisible(true);
				}
			});
			queuelist.add(cartest);
			queuerear++;
			cartest.registerEntityModifier(modifier);
		}
		else
			displaycross();


	}
	//function for removing first element of queue
	protected void Dequeue() {
		if(!queuelist.isEmpty())
		{
			queueflag=1;
			cartest2=queuelist.get(0);
			xes[0]=xes[1]=camera.getWidth()*0.5f-cartest2.getWidth()*0.5f;
			yes[0]=Dequeue.getY()+Dequeue.getHeight();
			queueflag=1;
			yes[1]=-200;
			Path carpath=new Path(xes,yes);
			PathModifier modifier = new PathModifier(1f, carpath,	new IPathModifierListener() {

				@Override
				public void onPathWaypointStarted(
						PathModifier pPathModifier, IEntity pEntity,
						int pWaypointIndex) {

				}

				@Override
				public void onPathWaypointFinished(
						PathModifier pPathModifier, IEntity pEntity,
						int pWaypointIndex) {

				}

				@Override
				public void onPathStarted(PathModifier pPathModifier,
						IEntity pEntity) {
				}

				@Override
				public void onPathFinished(PathModifier pPathModifier,
						IEntity pEntity) {
					queueflag=0;
				}
			});
			cartest2.registerEntityModifier(modifier);

			rearrange();
			queuerear--;}
	}//rearranges all the cars if the first car has left the queue
	protected void rearrange() {
		templist.clear();
		final int queuesize=queuelist.size();
		PathModifier modifier;
		for ( int i = 1; i < queuelist.size(); i++) {
			temp=queuelist.get(i);
			xes[0]=xes[1]=camera.getWidth()*0.5f-temp.getWidth()*0.5f;
			yes[0]=temp.getY();
			yes[1]=yes[0]-200;
			Path carpath=new Path(xes,yes);
			if(i==queuesize-1){
			modifier = new PathModifier(1f, carpath,	new IPathModifierListener() {

				@Override
				public void onPathWaypointStarted(
						PathModifier pPathModifier, IEntity pEntity,
						int pWaypointIndex) {
				}

				@Override
				public void onPathWaypointFinished(
						PathModifier pPathModifier, IEntity pEntity,
						int pWaypointIndex) {
				}

				@Override
				public void onPathStarted(PathModifier pPathModifier,
						IEntity pEntity) {

				}

				@Override
				public void onPathFinished(PathModifier pPathModifier,
						IEntity pEntity) {
					queueflag=0;
				}
			});}
			else
				  modifier = new PathModifier(1f, carpath);
			temp.registerEntityModifier(modifier);
			templist.add(temp);	
		}
		
		queuelist.clear();
		queuelist.addAll(templist);


	}
	//pushes element or drops lego brick into stack
	protected void stackpush() {
		if(stack.gettop()<=7){

			cross.setVisible(false);

			lego=new Sprite(0,0,legoTextureRegion,activity.getVertexBufferObjectManager()){
				@Override
				protected void preDraw(GLState pGLState, Camera pCamera)
				{
					super.preDraw(pGLState, pCamera);
					pGLState.enableDither();
				}	
			};
			stack.push(lego);
			lego.setPosition((camera.getWidth() - lego.getWidth()) * 0.5f,(camera.getHeight() - lego.getHeight())*0.1f);	
			lego.setScale(1.1f);
			stackbodylist.add(PhysicsFactory.createBoxBody(stackPhysicsWorld,lego, BodyType.DynamicBody, fdlego));
			stackGameScene.attachChild(lego);
			stackPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(stack.retlast(),stackbodylist.get(stack.gettop()),true,true));
		}
		else
			displaycross();

	}
	private void displaycross() {

		cross.setVisible(true);
	}//removes top elemnt or lego brick from stack
	protected void stackpop() {
		if(stack.gettop()>=0){
			cross.setVisible(false);

			final PhysicsConnector physicsConnector =stackPhysicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(stack.retlast());
			stackPhysicsWorld.unregisterPhysicsConnector(physicsConnector);
			stackbodylist.get(stack.gettop()).setActive(false);
			stackPhysicsWorld.destroyBody(stackbodylist.get(stack.gettop()));
			stackGameScene.detachChild(stack.retlast());
			stack.pop();}
		else
			displaycross();

	}
	//Method allows you to set the currently active scene
	public void setCurrentScene(SceneType scene) {
		currentScene = scene;
		switch (scene)
		{
		case SPLASH:
			break;
		case TITLE:
			engine.setScene(titleScene);
			break;
		case STACK:
			engine.setScene(stackGameScene);
			break;
		case QUEUE:
			engine.setScene(queueGameScene);
		}
	}







}




