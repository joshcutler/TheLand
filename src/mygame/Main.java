package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.debug.Arrow;
import com.jme3.scene.shape.Box;
import mygame.objects.WorldObjectRenderer;
import mygame.terrain.TerrainRenderer;
import mygame.terrain.blocks.TerrainBlock;

/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    TerrainRenderer _terrain_renderer;
    WorldObjectRenderer _world_object_renderer;
    int _day, _fps = 0;
    float _game_timer = 0;

    BitmapText _hud_day;
    
    public static void main(String[] args) {
        Main app = new Main();
        app.setShowSettings(false);
        app.start();
    }

    @Override
    public void simpleInitApp()
    {
        initCamera();
        initTerrain();
        initWorldObjects();
        initControls();
        initHUD();
    }

    private void initHUD()
    {
        if (Config.HUD_ENABLED)
        {
            //Remove stats
            statsView.removeFromParent();

            //Background
            _hud_day = new BitmapText(guiFont, false);
            Box background = new Box(Vector3f.ZERO, settings.getWidth(), 80, 0);
            Geometry geometry = new Geometry("Box", background);
            Material material = new Material(assetManager, "Common/MatDefs/Misc/SolidColor.j3md");
            material.setColor("m_Color", ColorRGBA.DarkGray);
            geometry.setMaterial(material);
            guiNode.attachChild(geometry);

            //Calendar
            _hud_day.setSize(guiFont.getCharSet().getRenderedSize());      // font size
            _hud_day.setColor(ColorRGBA.White);                             // font color
            _hud_day.setText("Day: " + _day);             // the text
            _hud_day.setLocalTranslation(settings.getWidth() - 100, _hud_day.getLineHeight(), 0); // position
            guiNode.attachChild(_hud_day);
        }
    }

    private void updateHUD()
    {
        if (Config.HUD_ENABLED)
        {
            _hud_day.setText("Day: " + _day);
        }
    }

    private void initCamera()
    {
        //Look straight at the world
        cam.setDirection(new Vector3f(0, 0,-1));
    }

    private void initControls()
    {
        //Reset JME mappings
        if (Config.DISABLE_DEFAULT_CAMERA)
        {
            inputManager.clearMappings();
        }
        mouseInput.setCursorVisible(true);

        //Standard Controls
        inputManager.addMapping("Quit", new KeyTrigger(KeyInput.KEY_ESCAPE));
        inputManager.addListener(menuControlListener, new String[]{"Quit"});

        //Init the camera controls
        inputManager.addMapping("CameraNorth", new KeyTrigger(KeyInput.KEY_UP));
        inputManager.addMapping("CameraSouth", new KeyTrigger(KeyInput.KEY_DOWN));
        inputManager.addMapping("CameraEast", new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping("CameraWest", new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addMapping("CameraNorth", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("CameraSouth", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("CameraEast", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("CameraWest", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addListener(cameraControlListener, new String[]{"CameraNorth", "CameraSouth", "CameraEast", "CameraWest"});
        //Handle Zoom
        inputManager.addMapping("ZoomIn", new MouseAxisTrigger(2, false));
        inputManager.addMapping("ZoomOut", new MouseAxisTrigger(2, true));


        //Init Mouse Click listeners
        inputManager.addMapping("Click", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(_terrain_renderer.mouseControlListener, new String[]{"Click"});
        inputManager.addListener(mouseZoomControlListener, new String[]{"ZoomIn", "ZoomOut"});
    }

    private AnalogListener mouseZoomControlListener = new AnalogListener() {
        public void onAnalog(String name, float value, float tpf)
        {
            if (name.equals("ZoomIn"))
            {
                zoomCamera(value);
            }
            else if (name.equals("ZoomOut"))
            {
                zoomCamera(-value);
            }
        }
    };

    protected void zoomCamera(float value)
    {
        // derive fovY value
        float h = cam.getFrustumTop();
        float w = cam.getFrustumRight();
        float aspect = w / h;

        float near = cam.getFrustumNear();

        float fovY = FastMath.atan(h / near)
                  / (FastMath.DEG_TO_RAD * .5f);
        fovY += value * 0.1f;

        if (fovY < Config.ZOOM_FOV_MAX && fovY > Config.ZOOM_FOV_MIN)
        {
            h = FastMath.tan( fovY * FastMath.DEG_TO_RAD * .5f) * near;
            w = h * aspect;

            cam.setFrustumTop(h);
            cam.setFrustumBottom(-h);
            cam.setFrustumLeft(-w);
            cam.setFrustumRight(w);
        }
    }

    private ActionListener menuControlListener = new ActionListener() {
        public void onAction(String name, boolean keyPressed, float tpf) {
          if (name.equals("Quit"))
          {
            System.exit(0);
          }
        }
    };

    private ActionListener cameraControlListener = new ActionListener() {
        public void onAction(String name, boolean keyPressed, float tpf) {
          Vector3f old_location = cam.getLocation();
          if (name.equals("CameraNorth"))
          {
            cam.setLocation(new Vector3f(old_location.x, old_location.y + 1, old_location.z));
          }
          else if(name.equals("CameraSouth"))
          {
            cam.setLocation(new Vector3f(old_location.x, old_location.y - 1, old_location.z));
          }
          else if (name.equals("CameraEast"))
          {
            cam.setLocation(new Vector3f(old_location.x - 1, old_location.y, old_location.z));
          }
          else if (name.equals("CameraWest"))
          {
            cam.setLocation(new Vector3f(old_location.x + 1, old_location.y, old_location.z));
          }
        }
    };

    private void initTerrain()
    {
        _terrain_renderer = new TerrainRenderer(rootNode, cam, inputManager, assetManager);
    }

    private void initWorldObjects()
    {
        _world_object_renderer = new WorldObjectRenderer(rootNode, cam, inputManager, assetManager);
    }

    @Override
    public void simpleUpdate(float tpf) 
    {
        //Get timer info
        _game_timer += tpf;
        _fps = (int) timer.getFrameRate();
        
        //Compute the day
        int _old_day_count = _day;
        boolean _new_day = false;
        _day = ((int) Math.floor(_game_timer)) / Config.SECONDS_PER_DAY;
        if (_day > _old_day_count)
        {
            _new_day = true;
        }

        _terrain_renderer.handleMousePicking();

        _terrain_renderer.updateTerrain();
        _world_object_renderer.updateObjects(_new_day);
        updateHUD();

    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
