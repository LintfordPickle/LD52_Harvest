package lintfordpickle.harvest.screens.game;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import lintfordpickle.harvest.ConstantsGame;
import lintfordpickle.harvest.controllers.CameraShipChaseController;
import lintfordpickle.harvest.controllers.GameStateController;
import lintfordpickle.harvest.controllers.PlatformsController;
import lintfordpickle.harvest.controllers.SceneController;
import lintfordpickle.harvest.controllers.ShipController;
import lintfordpickle.harvest.controllers.actionevents.GameActionEventController;
import lintfordpickle.harvest.data.CollisionHandler;
import lintfordpickle.harvest.data.game.GameState;
import lintfordpickle.harvest.data.platforms.Platform;
import lintfordpickle.harvest.data.platforms.PlatformManager;
import lintfordpickle.harvest.data.platforms.PlatformType;
import lintfordpickle.harvest.data.players.PlayerManager;
import lintfordpickle.harvest.data.scene.backgrounds.SceneManager;
import lintfordpickle.harvest.data.ships.ShipManager;
import lintfordpickle.harvest.renderers.PlatformsRenderer;
import lintfordpickle.harvest.renderers.ShipRenderer;
import lintfordpickle.harvest.renderers.hud.SurvivalHudRenderer;
import lintfordpickle.harvest.renderers.hud.MinimapRenderer;
import lintfordpickle.harvest.renderers.scene.SceneAdWallRenderer;
import lintfordpickle.harvest.renderers.scene.SceneRenderer;
import lintfordpickle.harvest.screens.FinishedScreen;
import lintfordpickle.harvest.screens.PauseScreen;
import net.lintford.library.ConstantsPhysics;
import net.lintford.library.controllers.core.ControllerManager;
import net.lintford.library.controllers.core.PhysicsController;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.ResourceManager;
import net.lintford.library.core.debug.Debug;
import net.lintford.library.core.graphics.rendertarget.RenderTarget;
import net.lintford.library.core.physics.PhysicsWorld;
import net.lintford.library.core.physics.dynamics.RigidBody;
import net.lintford.library.core.physics.resolvers.CollisionResolverRotationAndFriction;
import net.lintford.library.renderers.debug.DebugPhysicsGridRenderer;
import net.lintford.library.renderers.debug.DebugPhysicsRenderer;
import net.lintford.library.screenmanager.ScreenManager;
import net.lintford.library.screenmanager.screens.BaseGameScreen;
import net.lintford.library.screenmanager.screens.LoadingScreen;

public class SurvivalGameScreen extends BaseGameScreen {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	public static final int NUM_PHYSICS_ITERATIONS = 7;

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private RenderTarget mRenderTarget;

	private PhysicsWorld world;
	private CollisionHandler mCollisionHandler;

	// Data
	private PlayerManager mPlayerManager;
	private GameState mGameState;
	private ShipManager mShipManager;
	private SceneManager mSceneManager;
	private PlatformManager mPlatformManager;

	// Controllers
	private GameActionEventController mGameActionEventController;
	private CameraShipChaseController mCameraShipChaseController;
	private ShipController mShipController;
	private SceneController mSceneController;
	private PlatformsController mPlatformsController;
	private GameStateController mGameStateController;
	private PhysicsController mPhysicsController;

	// Renderers
	private DebugPhysicsRenderer mPhysicsRenderer;
	private DebugPhysicsGridRenderer mPhysicsDebugGridRenderer;
	private ShipRenderer mShipRenderer;
	private SceneRenderer mSceneRenderer;
	private SceneAdWallRenderer mSceneAdWallRenderer;
	private PlatformsRenderer mPlatformsRenderer;
	private SurvivalHudRenderer mHudRenderer;
	private MinimapRenderer mMinimapRenderer;

	// ---------------------------------------------
	// Constructors
	// ---------------------------------------------

	public SurvivalGameScreen(ScreenManager screenManager, PlayerManager playerManager) {
		super(screenManager);

		mPlayerManager = playerManager;
		mPlayerManager.resetSessions();

		ConstantsPhysics.setPhysicsWorldConstants(64.f);
	}

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

	@Override
	public void initialize() {

		mGameState = new GameState();
		mGameState.startNewGame();

		mShipManager = new ShipManager();
		mSceneManager = new SceneManager();
		mPlatformManager = new PlatformManager();

		mCollisionHandler = new CollisionHandler();

		world = new PhysicsWorld();
		world.setGravity(0, 5.87f);
		world.setContactResolver(new CollisionResolverRotationAndFriction());

		world.addCollisionCallback(mCollisionHandler);
		world.initialize();

		createWorldCollidables();
		createWorldPlatforms();

		super.initialize();
		mGameCamera.setPosition(ConstantsPhysics.toPixels(-1.2f), ConstantsPhysics.toPixels(13.1f));
	}

	private void createWorldCollidables() {
		createStaticPolygon(1696, 479, 18, 63, 0);
		createStaticPolygon(1679, 576, 17, 65, 0);
		createStaticPolygon(602, 1312, 128, 128, 0);
		createStaticPolygon(448, 1392, 154, 48, 0);
		createStaticPolygon(1358, 928, 130, 124, 0);
		createStaticPolygon(1698, 1168, 128, 270, 0);
		createStaticPolygon(1954, 1232, 48, 206, 0);
		createStaticPolygon(1696, 1438, 352, 40, 0);
		createStaticPolygon(1183, 543, 130, 289, 0);
		createStaticPolygon(1727, 223, 307, 33, 0);
		createStaticPolygon(1695, 255, 17, 97, 0);
		createStaticPolygon(1503, 256, 192, 20, 0);
		createStaticPolygon(1823, 160, 160, 63, 0);
		createStaticPolygon(1150, 768, 130, 284, 0);
		createStaticPolygon(1150, 1052, 338, 22, 0);
		createStaticPolygon(864, 576, 128, 658, 0);
		createStaticPolygon(320, 224, 128, 899, 0);
		createStaticPolygon(384, 160, 64, 64, 0);
		createStaticPolygon(298, 224, 22, 288, 0);
		createStaticPolygon(448, 512, 20, 128, 0);
		createStaticPolygon(300, 640, 20, 64, 0);
		createStaticPolygon(448, 768, 288, 32, 0);
		createStaticPolygon(194, 800, 125, 96, 0);
		createStaticPolygon(300, 960, 20, 160, 0);
		createStaticPolygon(448, 1088, 20, 33, 0);
		createStaticPolygon(0, 1248, 128, 192, 0);
		createStaticPolygon(128, 1370, 320, 70, 0);
		createStaticPolygon(640, 968, 224, 24, 0);
		createStaticPolygon(864, 358, 128, 58, 0);
		createStaticPolygon(892, 416, 100, 160, 0);
		createStaticPolygon(992, 512, 18, 64, 0);
		createStaticPolygon(992, 512, 18, 64, 0);
		createStaticPolygon(864, 1234, 352, 8, 0);
		createStaticPolygon(1344, 1234, 144, 8, 0);
		createStaticPolygon(1934, 1032, 100, 12, 0);
		createStaticPolygon(1826, 622, 104, 9, 0);
		createStaticPolygon(1480, 0, 186, 256, 0);
		createStaticPolygon(14, 1440, 244, 161, 0);
		createStaticPolygon(931, 1479, 604, 47, 0);
		createStaticPolygon(13, 230, 152, 124, 0);

		// Start platform
		createStaticPolygon(844, 1888, 203, 108, 0);
		createStaticPolygon(844, 1866, 31, 22, 0);
		createStaticPolygon(1344, 1844, 65, 152, 0);
		createStaticPolygon(1248, 1924, 65, 72, 0);
		createStaticPolygon(1120, 1972, 113, 24, 0);

		createStaticPolygon(930, 1438, 607, 40, 0);
		createStaticPolygon(1440, 1328, 80, 110, 0);
		createStaticPolygon(1440, 1234, 32, 94, 0);
		createStaticPolygon(1696, 622, 130, 408, 0);
		createStaticPolygon(1696, 542, 32, 80, 0);

		// walls
		createStaticPolygon(0, 0, 14, 2048, 0);
		createStaticPolygon(2034, 0, 14, 2048, 0);
		createStaticPolygon(0, 0, 2048, 14, 0);
		createStaticPolygon(0, 1996, 2048, 52, 0);

		// Platforms
		createStaticPolygon(529, 1863, 159, 23, 0);
		createStaticPolygon(1472, 1869, 158, 23, 0);
	}

	private void createWorldPlatforms() {
		createPlatform(1748, 574, 160, 48, PlatformType.Warehouse);

		// 10
		createPlatform(852, 318, 160, 48, PlatformType.Farm);

		// 15
		createPlatform(586, 1271, 160, 48, PlatformType.Farm);

		// 14
		createPlatform(142, 1319, 160, 48, PlatformType.Farm);

		// 22
		createPlatform(1343, 877, 160, 48, PlatformType.Farm);

		// 13
		createPlatform(12, 181, 160, 48, PlatformType.Farm);

		createPlatform(529, 1813, 160, 48, PlatformType.Water);
		createPlatform(1470, 1820, 160, 48, PlatformType.Water);
	}

	private void createStaticPolygon(float x, float y, float w, float h, float r) {
		final float worldXOffset = -1024;
		final float worldYOffset = -1024;

		final float lPxToUts = ConstantsPhysics.PixelsToUnits();

		final float staticFriction = 0.8f;
		final float dynamicFriction = 0.5f;

		final var lPolygon = RigidBody.createPolygonBody(w * lPxToUts, h * lPxToUts, 1.f, staticFriction, dynamicFriction, .4f, true);

		lPolygon.categoryBits(ConstantsGame.PHYSICS_WORLD_MASK_WALL);
		lPolygon.maskBits(ConstantsGame.PHYSICS_WORLD_MASK_SHIP | ConstantsGame.PHYSICS_WORLD_MASK_GHOST);

		lPolygon.moveTo((worldXOffset + x + w / 2) * lPxToUts, (worldYOffset + y + h / 2) * lPxToUts);

		world.addBody(lPolygon);
	}

	private void createPlatform(float x, float y, float w, float h, PlatformType type) {
		final float worldXOffset = -1024;
		final float worldYOffset = -1024;

		final var lNewPlatform = new Platform();
		lNewPlatform.set(worldXOffset + x, worldYOffset + y, w, h);
		lNewPlatform.platformType = type;

		mPlatformManager.addPlatform(lNewPlatform);

		switch (type) {
		case Farm:
			lNewPlatform.stockValueI = 0;
			lNewPlatform.stockValueF = 0.f;
			break;
		case Water:
			lNewPlatform.stockValueF = 1.f;
			break;
		default: // warehouse
			break;
		}
	}

	@Override
	public void loadResources(ResourceManager resourceManager) {
		super.loadResources(resourceManager);

		final var lDisplaySettings = resourceManager.config().display();
		final var lCanvasWidth = lDisplaySettings.gameResolutionWidth();
		final var lCanvasHeight = lDisplaySettings.gameResolutionHeight();

		mRenderTarget = mRendererManager.createRenderTarget("RT_MAIN", lCanvasWidth, lCanvasHeight, 1.f, GL11.GL_NEAREST, false);
	}

	@Override
	public void unloadResources() {
		mRendererManager.unloadRenderTarget(mRenderTarget);

		super.unloadResources();
	}

	@Override
	public void handleInput(LintfordCore core) {
		super.handleInput(core);

		if (core.input().keyboard().isKeyDownTimed(GLFW.GLFW_KEY_ESCAPE)) {
			mGameActionEventController.onExitingGame();

			if (ConstantsGame.ESCAPE_RESTART_MAIN_SCENE) {
				final var lLoadingScreen = new LoadingScreen(screenManager(), true, new SurvivalGameScreen(screenManager(), mPlayerManager));
				screenManager().createLoadingScreen(new LoadingScreen(screenManager(), true, lLoadingScreen));
				return;
			}
			screenManager().addScreen(new PauseScreen(screenManager(), mPlayerManager));
			return;
		}
	}

	@Override
	public void update(LintfordCore core, boolean otherScreenHasFocus, boolean coveredByOtherScreen) {
		super.update(core, otherScreenHasFocus, coveredByOtherScreen);

		if (otherScreenHasFocus == false) {
			if (mGameStateController.isPlayerDead() && mGameStateController.gameState().isGameRunning) {
				mGameStateController.gameState().isGameRunning = false;
				screenManager().addScreen(new FinishedScreen(mScreenManager, mPlayerManager, true, mGameState.foodDelivered));

				mGameActionEventController.onExitingGame();
			}

			if (mGameStateController.hasPlayerLostThroughLives() && mGameStateController.gameState().isGameRunning) {
				mGameStateController.gameState().isGameRunning = false;
				screenManager().addScreen(new FinishedScreen(mScreenManager, mPlayerManager, true, mGameState.foodDelivered));

				mGameActionEventController.onExitingGame();
			}

			if (mGameStateController.hasPlayerLostThroughTime() && mGameStateController.gameState().isGameRunning) {
				mGameStateController.gameState().isGameRunning = false;
				screenManager().addScreen(new FinishedScreen(mScreenManager, mPlayerManager, false, mGameState.foodDelivered));

				mGameActionEventController.onExitingGame();
			}
		}

		world.stepWorld((float) core.gameTime().elapsedTimeMilli() * 0.001f, NUM_PHYSICS_ITERATIONS);
	}

	@Override
	public void draw(LintfordCore core) {
		final var lGameCam = mGameCamera; // orig

		mRenderTarget.bind();

		GL11.glClearColor(0.06f, 0.18f, 0.31f, 1.0f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		super.draw(core);

		mRenderTarget.unbind();

		GL11.glClearColor(0.06f, 0.18f, 0.11f, 1.0f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		core.setActiveGameCamera(lGameCam);
		core.config().display().reapplyGlViewport();

		Debug.debugManager().drawers().drawRenderTargetImmediate(core, 0, 0, 960, 540, -0.001f, mRenderTarget);
	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	@Override
	protected void createControllers(ControllerManager controllerManager) {
		mGameActionEventController = new GameActionEventController(controllerManager, inputCounter(), entityGroupUid());
		mPhysicsController = new PhysicsController(controllerManager, world, entityGroupUid());
		mCameraShipChaseController = new CameraShipChaseController(controllerManager, mGameCamera, null, entityGroupUid());
		mGameStateController = new GameStateController(controllerManager, mGameState, entityGroupUid());
		mSceneController = new SceneController(controllerManager, mSceneManager, entityGroupUid());

		mShipController = new ShipController(controllerManager, mShipManager, mPlayerManager, entityGroupUid());
		mPlatformsController = new PlatformsController(controllerManager, mPlatformManager, entityGroupUid());
	}

	@Override
	protected void initializeControllers(LintfordCore core) {
		mGameActionEventController.initialize(core);
		mPhysicsController.initialize(core);
		mCameraShipChaseController.initialize(core);
		mSceneController.initialize(core);
		mShipController.initialize(core);
		mPlatformsController.initialize(core);
		mGameStateController.initialize(core);
	}

	@Override
	protected void createRenderers(LintfordCore core) {
		mSceneRenderer = new SceneRenderer(mRendererManager, entityGroupUid());
		if (ConstantsGame.PHYICS_DEBUG_MODE) {
			mPhysicsRenderer = new DebugPhysicsRenderer(mRendererManager, world, entityGroupUid());
			mPhysicsDebugGridRenderer = new DebugPhysicsGridRenderer(mRendererManager, world, entityGroupUid());
		}
		mShipRenderer = new ShipRenderer(mRendererManager, entityGroupUid());
		mSceneAdWallRenderer = new SceneAdWallRenderer(mRendererManager, entityGroupUid());
		mPlatformsRenderer = new PlatformsRenderer(mRendererManager, entityGroupUid());
		mHudRenderer = new SurvivalHudRenderer(mRendererManager, entityGroupUid());
		mMinimapRenderer = new MinimapRenderer(mRendererManager, entityGroupUid());
	}

	@Override
	protected void initializeRenderers(LintfordCore core) {
		mShipRenderer.initialize(core);
		if (ConstantsGame.PHYICS_DEBUG_MODE) {
			mPhysicsRenderer.initialize(core);
			mPhysicsDebugGridRenderer.initialize(core);
		}
		mSceneRenderer.initialize(core);
		mSceneAdWallRenderer.initialize(core);
		mPlatformsRenderer.initialize(core);
		mHudRenderer.initialize(core);
		mMinimapRenderer.initialize(core);
	}

	@Override
	protected void loadRendererResources(ResourceManager resourceManager) {
		mSceneRenderer.loadResources(resourceManager);
		mShipRenderer.loadResources(resourceManager);
		mSceneAdWallRenderer.loadResources(resourceManager);
		if (ConstantsGame.PHYICS_DEBUG_MODE) {
			mPhysicsRenderer.loadResources(resourceManager);
			mPhysicsDebugGridRenderer.loadResources(resourceManager);
		}
		mPlatformsRenderer.loadResources(resourceManager);
		mHudRenderer.loadResources(resourceManager);
		mMinimapRenderer.loadResources(resourceManager);
	}

}