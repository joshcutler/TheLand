package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.debug.Arrow;
import com.jme3.scene.shape.Box;
import java.util.HashMap;
import java.util.logging.Logger;
import mygame.terrain.BlockRegion;
import mygame.terrain.GrassyRegion;
import mygame.terrain.blocks.TerrainBlock;

/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    Node _terrain_node;
    HashMap _terrain = new HashMap();
    HashMap _terrain_blocks = new HashMap();
    int _day, _fps = 0;
    float _game_timer = 0;

    static final int SECONDS_PER_DAY = 10;
    static final int BLOCK_REGION_SIZE = 25;

    boolean _hud_enabled = false;

    BitmapText _hud_day;
    Geometry mark;
    TerrainBlock _highlighted;
    TerrainBlock _selected;
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp()
    {
        initCamera();
        initControls();
        initHUD();
        initTerrain();
    }

    private void initHUD()
    {
        if (_hud_enabled)
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
        if (_hud_enabled)
        {
            _hud_day.setText("Day: " + _day);
        }
    }

    private void initCamera()
    {
        //Look straight at the world
        cam.setDirection(new Vector3f(0, 0,-1));
    }

    private void initMark()
    {
        Arrow arrow = new Arrow(Vector3f.UNIT_Z.mult(2f));
        arrow.setLineWidth(3);

        mark = new Geometry("red_mark", arrow);
        Material mark_mat = new Material(assetManager, "Common/MatDefs/Misc/SolidColor.j3md");
        mark_mat.setColor("m_Color", ColorRGBA.Red);
        mark.setMaterial(mark_mat);
    }

    private void initControls()
    {
        //Reset JME mappings
        inputManager.clearMappings();
        mouseInput.setCursorVisible(true);
        initMark();

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

        //Init Mouse Click listeners
        inputManager.addMapping("Click", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(mouseControlListener, new String[]{"Click"});
    }

    private ActionListener mouseControlListener = new ActionListener() {
        public void onAction(String name, boolean keyPressed, float tpf) {
          if (name.equals("Click"))
          {
            CollisionResults results = getBlockCollisionFromMousePointer();
            if (results.size() > 0)
            {
                CollisionResult closest = results.getClosestCollision();
                TerrainBlock t = lookupBlock(closest.getGeometry().getName());
                if (_selected != null && _selected != t)
                {
                    _selected.deselect();
                }
                t.select();
                _selected = t;
                logger.info("Selected Block: " + t.getBlockGeometry().getName());
            }
          }
        }
    };

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

    private void updateTerrain()
    {
        //See if we need to draw any new terrains
        Vector3f current_cam_location = cam.getLocation();
        int current_block_x = (current_cam_location.x < 0) ? (((int)current_cam_location.x - BLOCK_REGION_SIZE)/BLOCK_REGION_SIZE)*BLOCK_REGION_SIZE : ((int)current_cam_location.x/BLOCK_REGION_SIZE)*BLOCK_REGION_SIZE;
        int current_block_y = (current_cam_location.y < 0) ? (((int)current_cam_location.y - BLOCK_REGION_SIZE)/BLOCK_REGION_SIZE)*25 : ((int)current_cam_location.y/BLOCK_REGION_SIZE)*BLOCK_REGION_SIZE;

        drawNewTerrain(current_block_x, current_block_y);
        cullOldTerrain(current_block_x, current_block_y);
    }

    private void redrawTerrain(BlockRegion b)
    {
        _terrain_node.attachChild(b.getNode());
        b.setDrawn(true);
    }

    private void cullTerrain(int offset_x, int offset_y)
    {
        String label = "block_" + offset_x + "_" + offset_y;
        BlockRegion b1 = (BlockRegion) _terrain_blocks.get(label);
        if (b1.isDrawn())
        {
            _terrain_node.detachChild(b1.getNode());
            b1.setDrawn(false);
        }
    }

    private void cullOldTerrain(int current_block_x, int current_block_y)
    {
        //Is the top one drawn?
        if (_terrain_blocks.containsKey("block_" + current_block_x + "_" + (current_block_y + 2 * BLOCK_REGION_SIZE)))
        {
            cullTerrain(current_block_x, current_block_y + 2*BLOCK_REGION_SIZE);
        }
        //Is the bottom one drawn?
        if (_terrain_blocks.containsKey("block_" + current_block_x + "_" + (current_block_y - 2 * BLOCK_REGION_SIZE)))
        {
            cullTerrain(current_block_x, current_block_y - 2*BLOCK_REGION_SIZE);
        }
        //Is the left one drawn?
        if (_terrain_blocks.containsKey("block_" + (current_block_x - 2*BLOCK_REGION_SIZE) + "_" + current_block_y))
        {
            cullTerrain(current_block_x - 2*BLOCK_REGION_SIZE, current_block_y);
        }
        //Is the right one drawn?
        if (_terrain_blocks.containsKey("block_" + (current_block_x + 2*BLOCK_REGION_SIZE) + "_" + current_block_y))
        {
            cullTerrain(current_block_x + 2*BLOCK_REGION_SIZE, current_block_y);
        }

        //Is the top-right one drawn?
        if (_terrain_blocks.containsKey("block_" + (current_block_x + 2*BLOCK_REGION_SIZE) + "_" + (current_block_y + 1*BLOCK_REGION_SIZE)))
        {
            cullTerrain(current_block_x + 2*BLOCK_REGION_SIZE, current_block_y + 1*BLOCK_REGION_SIZE);
        }
        //Is the bottom-right one drawn?
        if (_terrain_blocks.containsKey("block_" + (current_block_x + 2*BLOCK_REGION_SIZE) + "_" + (current_block_y - 1*BLOCK_REGION_SIZE)))
        {
            cullTerrain(current_block_x + 2*BLOCK_REGION_SIZE, current_block_y - 1*BLOCK_REGION_SIZE);
        }
        //Is the top-left one drawn?
        if (_terrain_blocks.containsKey("block_" + (current_block_x - 2*BLOCK_REGION_SIZE) + "_" + (current_block_y + 1*BLOCK_REGION_SIZE)))
        {
            cullTerrain(current_block_x - 2*BLOCK_REGION_SIZE, current_block_y + 1*BLOCK_REGION_SIZE);
        }
         //Is the bottom-left one drawn?
        if (_terrain_blocks.containsKey("block_" + (current_block_x - 2*BLOCK_REGION_SIZE) + "_" + (current_block_y - 1*BLOCK_REGION_SIZE)))
        {
            cullTerrain(current_block_x - 2*BLOCK_REGION_SIZE, current_block_y - 1*BLOCK_REGION_SIZE);
        }
        //Is the top-right one drawn?
        if (_terrain_blocks.containsKey("block_" + (current_block_x + 1*BLOCK_REGION_SIZE) + "_" + (current_block_y + 2*BLOCK_REGION_SIZE)))
        {
            cullTerrain(current_block_x + 1*BLOCK_REGION_SIZE, current_block_y + 2*BLOCK_REGION_SIZE);
        }
        //Is the bottom-right one drawn?
        if (_terrain_blocks.containsKey("block_" + (current_block_x + 1*BLOCK_REGION_SIZE) + "_" + (current_block_y - 2*BLOCK_REGION_SIZE)))
        {
            cullTerrain(current_block_x + 1*BLOCK_REGION_SIZE, current_block_y - 2*BLOCK_REGION_SIZE);
        }
        //Is the top-left one drawn?
        if (_terrain_blocks.containsKey("block_" + (current_block_x - 1*BLOCK_REGION_SIZE) + "_" + (current_block_y + 2*BLOCK_REGION_SIZE)))
        {
            cullTerrain(current_block_x - 1*BLOCK_REGION_SIZE, current_block_y + 2*BLOCK_REGION_SIZE);
        }
         //Is the bottom-left one drawn?
        if (_terrain_blocks.containsKey("block_" + (current_block_x - 1*BLOCK_REGION_SIZE) + "_" + (current_block_y - 2*BLOCK_REGION_SIZE)))
        {
            cullTerrain(current_block_x - 1*BLOCK_REGION_SIZE, current_block_y - 2*BLOCK_REGION_SIZE);
        }
    }

    private void drawNewTerrain(int current_block_x, int current_block_y)
    {
        //Is the current one drawn?
        if (!_terrain_blocks.containsKey("block_" + current_block_x + "_" + current_block_y))
        {
            buildTerrain(current_block_x, current_block_y);
        }
        else
        {
            BlockRegion b = ((BlockRegion)_terrain_blocks.get("block_" + current_block_x + "_" + current_block_y));
            if (!b.isDrawn())
            {
                redrawTerrain(b);
            }
        }
        //Is the top one drawn?
        if (!_terrain_blocks.containsKey("block_" + current_block_x + "_" + (current_block_y + BLOCK_REGION_SIZE)))
        {
            buildTerrain(current_block_x, current_block_y + BLOCK_REGION_SIZE);
        }
        else
        {
            BlockRegion b = ((BlockRegion)_terrain_blocks.get("block_" + current_block_x + "_" + (current_block_y + BLOCK_REGION_SIZE)));
            if (!b.isDrawn())
            {
                redrawTerrain(b);
            }
        }
        //Is the bottom one drawn?
        if (!_terrain_blocks.containsKey("block_" + current_block_x + "_" + (current_block_y - BLOCK_REGION_SIZE)))
        {
            buildTerrain(current_block_x, current_block_y - BLOCK_REGION_SIZE);
        }
        else
        {
            BlockRegion b = ((BlockRegion)_terrain_blocks.get("block_" + current_block_x + "_" + (current_block_y - BLOCK_REGION_SIZE)));
            if (!b.isDrawn())
            {
                redrawTerrain(b);
            }
        }
        //Is the left one drawn?
        if (!_terrain_blocks.containsKey("block_" + (current_block_x - BLOCK_REGION_SIZE) + "_" + current_block_y))
        {
            buildTerrain(current_block_x - BLOCK_REGION_SIZE, current_block_y);
        }
        else
        {
            BlockRegion b = ((BlockRegion)_terrain_blocks.get("block_" + (current_block_x - BLOCK_REGION_SIZE) + "_" + current_block_y));
            if (!b.isDrawn())
            {
                redrawTerrain(b);
            }
        }
        //Is the right one drawn?
        if (!_terrain_blocks.containsKey("block_" + (current_block_x + BLOCK_REGION_SIZE) + "_" + current_block_y))
        {
            buildTerrain(current_block_x + BLOCK_REGION_SIZE, current_block_y);
        }
        else
        {
            BlockRegion b = ((BlockRegion)_terrain_blocks.get("block_" + (current_block_x + BLOCK_REGION_SIZE) + "_" + current_block_y));
            if (!b.isDrawn())
            {
                redrawTerrain(b);
            }
        }
        //Is the top-right one drawn?
        if (!_terrain_blocks.containsKey("block_" + (current_block_x + BLOCK_REGION_SIZE) + "_" + (current_block_y + BLOCK_REGION_SIZE)))
        {
            buildTerrain(current_block_x + BLOCK_REGION_SIZE, current_block_y + BLOCK_REGION_SIZE);
        }
        else
        {
            BlockRegion b = ((BlockRegion)_terrain_blocks.get("block_" + (current_block_x + BLOCK_REGION_SIZE) + "_" + (current_block_y + BLOCK_REGION_SIZE)));
            if (!b.isDrawn())
            {
                redrawTerrain(b);
            }
        }
        //Is the bottom-right one drawn?
        if (!_terrain_blocks.containsKey("block_" + (current_block_x + BLOCK_REGION_SIZE) + "_" + (current_block_y - BLOCK_REGION_SIZE)))
        {
            buildTerrain(current_block_x + BLOCK_REGION_SIZE, current_block_y - BLOCK_REGION_SIZE);
        }
        else
        {
            BlockRegion b = ((BlockRegion)_terrain_blocks.get("block_" + (current_block_x + BLOCK_REGION_SIZE) + "_" + (current_block_y - BLOCK_REGION_SIZE)));
            if (!b.isDrawn())
            {
                redrawTerrain(b);
            }
        }
        //Is the top-left one drawn?
        if (!_terrain_blocks.containsKey("block_" + (current_block_x - BLOCK_REGION_SIZE) + "_" + (current_block_y + BLOCK_REGION_SIZE)))
        {
            buildTerrain(current_block_x - BLOCK_REGION_SIZE, current_block_y + BLOCK_REGION_SIZE);
        }
        else
        {
            BlockRegion b = ((BlockRegion)_terrain_blocks.get("block_" + (current_block_x - BLOCK_REGION_SIZE) + "_" + (current_block_y + BLOCK_REGION_SIZE)));
            if (!b.isDrawn())
            {
                redrawTerrain(b);
            }
        }
         //Is the bottom-left one drawn?
        if (!_terrain_blocks.containsKey("block_" + (current_block_x - BLOCK_REGION_SIZE) + "_" + (current_block_y - BLOCK_REGION_SIZE)))
        {
            buildTerrain(current_block_x - BLOCK_REGION_SIZE, current_block_y - BLOCK_REGION_SIZE);
        }
        else
        {
            BlockRegion b = ((BlockRegion)_terrain_blocks.get("block_" + (current_block_x - BLOCK_REGION_SIZE) + "_" + (current_block_y - BLOCK_REGION_SIZE)));
            if (!b.isDrawn())
            {
                redrawTerrain(b);
            }
        }
    }
    private void buildTerrain(int offset_x, int offset_y) {
        BlockRegion b1 = new BlockRegion(BLOCK_REGION_SIZE, offset_x, offset_y, new GrassyRegion(), _terrain, assetManager);
        String label = "block_" + offset_x + "_" + offset_y;
        _terrain_blocks.put(label, b1);
        _terrain_node.attachChild(b1.getNode());
        b1.setDrawn(true);
    }

    private void initTerrain()
    {
        _terrain_node = new Node();
        rootNode.attachChild(_terrain_node);
    }

    private CollisionResults getBlockCollisionFromMousePointer()
    {
        Vector3f origin    = cam.getWorldCoordinates(inputManager.getCursorPosition(), 0.0f);
        Vector3f direction = cam.getWorldCoordinates(inputManager.getCursorPosition(), 0.3f);
        direction.subtractLocal(origin).normalizeLocal();

        Ray ray = new Ray(origin, direction);
        CollisionResults results = new CollisionResults();
        _terrain_node.collideWith(ray, results);
        return results;
    }

    private void handleMousePicking()
    {   
        CollisionResults results = getBlockCollisionFromMousePointer();
        if (results.size() > 0) {
            CollisionResult farthest = results.getClosestCollision();
            TerrainBlock t = lookupBlock(farthest.getGeometry().getName());
            if (t != _highlighted)
            {
                if (_highlighted != null)
                {
                    _highlighted.unhighlight();
                }
                _highlighted = t;
                t.highlight();
                logger.info("Highlighted Block: " + t.getBlockGeometry().getName());
            }

            //Draw the red thingy
            mark.setLocalTranslation(farthest.getContactPoint());

            Quaternion q = new Quaternion();
            q.lookAt(farthest.getContactNormal(), Vector3f.UNIT_Y);
            mark.setLocalRotation(q);

            rootNode.attachChild(mark);
        } else {
            rootNode.detachChild(mark);
            if (_highlighted != null)
            {
                _highlighted.unhighlight();
                _highlighted= null;
            }
        }
    }

    public TerrainBlock lookupBlock(String name)
    {
        return (TerrainBlock)_terrain.get(name);
    }

    @Override
    public void simpleUpdate(float tpf) 
    {
        //Get timer info
        _game_timer += tpf;
        _fps = (int) timer.getFrameRate();
        
        //Compute the day
        _day = ((int) Math.floor(_game_timer)) / SECONDS_PER_DAY;
        //_day = _fps;

        rootNode.updateGeometricState();
        handleMousePicking();
        updateHUD();
        updateTerrain();
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
