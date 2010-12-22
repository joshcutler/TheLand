/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mygame.terrain;

import com.jme3.asset.AssetManager;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.debug.Arrow;
import java.util.HashMap;
import mygame.Config;
import mygame.terrain.blocks.TerrainBlock;

/**
 *
 * @author joshcutler
 */
public class TerrainRenderer {
    Node _terrain_node;
    Node _root_node;
    HashMap _terrain = new HashMap();
    HashMap _terrain_blocks = new HashMap();
    AssetManager _asset_manager;
    InputManager _input_manager;
    Camera _cam;
    Geometry _mark;

    TerrainBlock _highlighted;
    TerrainBlock _selected;

    public TerrainRenderer(Node root, Camera cam, InputManager im, AssetManager am)
    {
        _asset_manager = am;
        _root_node = root;
        _cam = cam;
        _input_manager = im;
        
        _terrain_node = new Node();
        _root_node.attachChild(_terrain_node);

        initMark();
    }

    public Node getTerrainNode()
    {
        return _terrain_node;
    }

    private void redrawTerrain(BlockRegion b)
    {
        _terrain_node.attachChild(b.getNode());
        b.setDrawn(true);
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
        if (!_terrain_blocks.containsKey("block_" + current_block_x + "_" + (current_block_y + Config.BLOCK_REGION_SIZE)))
        {
            buildTerrain(current_block_x, current_block_y + Config.BLOCK_REGION_SIZE);
        }
        else
        {
            BlockRegion b = ((BlockRegion)_terrain_blocks.get("block_" + current_block_x + "_" + (current_block_y + Config.BLOCK_REGION_SIZE)));
            if (!b.isDrawn())
            {
                redrawTerrain(b);
            }
        }
        //Is the bottom one drawn?
        if (!_terrain_blocks.containsKey("block_" + current_block_x + "_" + (current_block_y - Config.BLOCK_REGION_SIZE)))
        {
            buildTerrain(current_block_x, current_block_y - Config.BLOCK_REGION_SIZE);
        }
        else
        {
            BlockRegion b = ((BlockRegion)_terrain_blocks.get("block_" + current_block_x + "_" + (current_block_y - Config.BLOCK_REGION_SIZE)));
            if (!b.isDrawn())
            {
                redrawTerrain(b);
            }
        }
        //Is the left one drawn?
        if (!_terrain_blocks.containsKey("block_" + (current_block_x - Config.BLOCK_REGION_SIZE) + "_" + current_block_y))
        {
            buildTerrain(current_block_x - Config.BLOCK_REGION_SIZE, current_block_y);
        }
        else
        {
            BlockRegion b = ((BlockRegion)_terrain_blocks.get("block_" + (current_block_x - Config.BLOCK_REGION_SIZE) + "_" + current_block_y));
            if (!b.isDrawn())
            {
                redrawTerrain(b);
            }
        }
        //Is the right one drawn?
        if (!_terrain_blocks.containsKey("block_" + (current_block_x + Config.BLOCK_REGION_SIZE) + "_" + current_block_y))
        {
            buildTerrain(current_block_x + Config.BLOCK_REGION_SIZE, current_block_y);
        }
        else
        {
            BlockRegion b = ((BlockRegion)_terrain_blocks.get("block_" + (current_block_x + Config.BLOCK_REGION_SIZE) + "_" + current_block_y));
            if (!b.isDrawn())
            {
                redrawTerrain(b);
            }
        }
        //Is the top-right one drawn?
        if (!_terrain_blocks.containsKey("block_" + (current_block_x + Config.BLOCK_REGION_SIZE) + "_" + (current_block_y + Config.BLOCK_REGION_SIZE)))
        {
            buildTerrain(current_block_x + Config.BLOCK_REGION_SIZE, current_block_y + Config.BLOCK_REGION_SIZE);
        }
        else
        {
            BlockRegion b = ((BlockRegion)_terrain_blocks.get("block_" + (current_block_x + Config.BLOCK_REGION_SIZE) + "_" + (current_block_y + Config.BLOCK_REGION_SIZE)));
            if (!b.isDrawn())
            {
                redrawTerrain(b);
            }
        }
        //Is the bottom-right one drawn?
        if (!_terrain_blocks.containsKey("block_" + (current_block_x + Config.BLOCK_REGION_SIZE) + "_" + (current_block_y - Config.BLOCK_REGION_SIZE)))
        {
            buildTerrain(current_block_x + Config.BLOCK_REGION_SIZE, current_block_y - Config.BLOCK_REGION_SIZE);
        }
        else
        {
            BlockRegion b = ((BlockRegion)_terrain_blocks.get("block_" + (current_block_x + Config.BLOCK_REGION_SIZE) + "_" + (current_block_y - Config.BLOCK_REGION_SIZE)));
            if (!b.isDrawn())
            {
                redrawTerrain(b);
            }
        }
        //Is the top-left one drawn?
        if (!_terrain_blocks.containsKey("block_" + (current_block_x - Config.BLOCK_REGION_SIZE) + "_" + (current_block_y + Config.BLOCK_REGION_SIZE)))
        {
            buildTerrain(current_block_x - Config.BLOCK_REGION_SIZE, current_block_y + Config.BLOCK_REGION_SIZE);
        }
        else
        {
            BlockRegion b = ((BlockRegion)_terrain_blocks.get("block_" + (current_block_x - Config.BLOCK_REGION_SIZE) + "_" + (current_block_y + Config.BLOCK_REGION_SIZE)));
            if (!b.isDrawn())
            {
                redrawTerrain(b);
            }
        }
         //Is the bottom-left one drawn?
        if (!_terrain_blocks.containsKey("block_" + (current_block_x - Config.BLOCK_REGION_SIZE) + "_" + (current_block_y - Config.BLOCK_REGION_SIZE)))
        {
            buildTerrain(current_block_x - Config.BLOCK_REGION_SIZE, current_block_y - Config.BLOCK_REGION_SIZE);
        }
        else
        {
            BlockRegion b = ((BlockRegion)_terrain_blocks.get("block_" + (current_block_x - Config.BLOCK_REGION_SIZE) + "_" + (current_block_y - Config.BLOCK_REGION_SIZE)));
            if (!b.isDrawn())
            {
                redrawTerrain(b);
            }
        }
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

    private void buildTerrain(int offset_x, int offset_y) {
        if (Math.abs(offset_y / Config.BLOCK_REGION_SIZE) <= Config.BLOCK_REGION_MAX_Y &&
            Math.abs(offset_x / Config.BLOCK_REGION_SIZE) <= Config.BLOCK_REGION_MAX_X)
        {
            RegionType top = null, right = null, bottom = null, left = null;
            //Is there a top one
            if (_terrain_blocks.containsKey("block_" + offset_x + "_" + (offset_y + Config.BLOCK_REGION_SIZE)))
            {
                top = ((BlockRegion) _terrain_blocks.get("block_" + offset_x + "_" + (offset_y + Config.BLOCK_REGION_SIZE))).getRegion();
            }
            //Is there a bottom one
            if (_terrain_blocks.containsKey("block_" + offset_x + "_" + (offset_y - Config.BLOCK_REGION_SIZE)))
            {
                bottom = ((BlockRegion) _terrain_blocks.get("block_" + offset_x + "_" + (offset_y - Config.BLOCK_REGION_SIZE))).getRegion();
            }
            //Is there a left one
            if (_terrain_blocks.containsKey("block_" + (offset_x - Config.BLOCK_REGION_SIZE) + "_" + offset_y))
            {
                left = ((BlockRegion) _terrain_blocks.get("block_" + (offset_x - Config.BLOCK_REGION_SIZE) + "_" + offset_y)).getRegion();
            }
            //Is there a right one
            if (_terrain_blocks.containsKey("block_" + (offset_x + Config.BLOCK_REGION_SIZE) + "_" + offset_y))
            {
                right = ((BlockRegion) _terrain_blocks.get("block_" + (offset_x + Config.BLOCK_REGION_SIZE) + "_" + offset_y)).getRegion();
            }

            RegionType region_type = RegionType.getRegionType(top, right, bottom, left);

            BlockRegion b1 = new BlockRegion(Config.BLOCK_REGION_SIZE, offset_x, offset_y, region_type, _terrain, _asset_manager);
            String label = "block_" + offset_x + "_" + offset_y;
            _terrain_blocks.put(label, b1);
            _terrain_node.attachChild(b1.getNode());
            b1.setDrawn(true);
        }
    }

    private void cullOldTerrain(int current_block_x, int current_block_y)
    {
        //Is the top one drawn?
        if (_terrain_blocks.containsKey("block_" + current_block_x + "_" + (current_block_y + 2 * Config.BLOCK_REGION_SIZE)))
        {
            cullTerrain(current_block_x, current_block_y + 2*Config.BLOCK_REGION_SIZE);
        }
        //Is the bottom one drawn?
        if (_terrain_blocks.containsKey("block_" + current_block_x + "_" + (current_block_y - 2 * Config.BLOCK_REGION_SIZE)))
        {
            cullTerrain(current_block_x, current_block_y - 2*Config.BLOCK_REGION_SIZE);
        }
        //Is the left one drawn?
        if (_terrain_blocks.containsKey("block_" + (current_block_x - 2*Config.BLOCK_REGION_SIZE) + "_" + current_block_y))
        {
            cullTerrain(current_block_x - 2*Config.BLOCK_REGION_SIZE, current_block_y);
        }
        //Is the right one drawn?
        if (_terrain_blocks.containsKey("block_" + (current_block_x + 2*Config.BLOCK_REGION_SIZE) + "_" + current_block_y))
        {
            cullTerrain(current_block_x + 2*Config.BLOCK_REGION_SIZE, current_block_y);
        }

        //Is the top-right one drawn?
        if (_terrain_blocks.containsKey("block_" + (current_block_x + 2*Config.BLOCK_REGION_SIZE) + "_" + (current_block_y + 1*Config.BLOCK_REGION_SIZE)))
        {
            cullTerrain(current_block_x + 2*Config.BLOCK_REGION_SIZE, current_block_y + 1*Config.BLOCK_REGION_SIZE);
        }
        //Is the bottom-right one drawn?
        if (_terrain_blocks.containsKey("block_" + (current_block_x + 2*Config.BLOCK_REGION_SIZE) + "_" + (current_block_y - 1*Config.BLOCK_REGION_SIZE)))
        {
            cullTerrain(current_block_x + 2*Config.BLOCK_REGION_SIZE, current_block_y - 1*Config.BLOCK_REGION_SIZE);
        }
        //Is the top-left one drawn?
        if (_terrain_blocks.containsKey("block_" + (current_block_x - 2*Config.BLOCK_REGION_SIZE) + "_" + (current_block_y + 1*Config.BLOCK_REGION_SIZE)))
        {
            cullTerrain(current_block_x - 2*Config.BLOCK_REGION_SIZE, current_block_y + 1*Config.BLOCK_REGION_SIZE);
        }
         //Is the bottom-left one drawn?
        if (_terrain_blocks.containsKey("block_" + (current_block_x - 2*Config.BLOCK_REGION_SIZE) + "_" + (current_block_y - 1*Config.BLOCK_REGION_SIZE)))
        {
            cullTerrain(current_block_x - 2*Config.BLOCK_REGION_SIZE, current_block_y - 1*Config.BLOCK_REGION_SIZE);
        }
        //Is the top-right one drawn?
        if (_terrain_blocks.containsKey("block_" + (current_block_x + 1*Config.BLOCK_REGION_SIZE) + "_" + (current_block_y + 2*Config.BLOCK_REGION_SIZE)))
        {
            cullTerrain(current_block_x + 1*Config.BLOCK_REGION_SIZE, current_block_y + 2*Config.BLOCK_REGION_SIZE);
        }
        //Is the bottom-right one drawn?
        if (_terrain_blocks.containsKey("block_" + (current_block_x + 1*Config.BLOCK_REGION_SIZE) + "_" + (current_block_y - 2*Config.BLOCK_REGION_SIZE)))
        {
            cullTerrain(current_block_x + 1*Config.BLOCK_REGION_SIZE, current_block_y - 2*Config.BLOCK_REGION_SIZE);
        }
        //Is the top-left one drawn?
        if (_terrain_blocks.containsKey("block_" + (current_block_x - 1*Config.BLOCK_REGION_SIZE) + "_" + (current_block_y + 2*Config.BLOCK_REGION_SIZE)))
        {
            cullTerrain(current_block_x - 1*Config.BLOCK_REGION_SIZE, current_block_y + 2*Config.BLOCK_REGION_SIZE);
        }
         //Is the bottom-left one drawn?
        if (_terrain_blocks.containsKey("block_" + (current_block_x - 1*Config.BLOCK_REGION_SIZE) + "_" + (current_block_y - 2*Config.BLOCK_REGION_SIZE)))
        {
            cullTerrain(current_block_x - 1*Config.BLOCK_REGION_SIZE, current_block_y - 2*Config.BLOCK_REGION_SIZE);
        }
    }

    public void updateTerrain()
    {
        //See if we need to draw any new terrains
        Vector3f current_cam_location = _cam.getLocation();
        int current_block_x = (current_cam_location.x < 0) ? (((int)current_cam_location.x - Config.BLOCK_REGION_SIZE)/Config.BLOCK_REGION_SIZE)*Config.BLOCK_REGION_SIZE : ((int)current_cam_location.x/Config.BLOCK_REGION_SIZE)*Config.BLOCK_REGION_SIZE;
        int current_block_y = (current_cam_location.y < 0) ? (((int)current_cam_location.y - Config.BLOCK_REGION_SIZE)/Config.BLOCK_REGION_SIZE)*25 : ((int)current_cam_location.y/Config.BLOCK_REGION_SIZE)*Config.BLOCK_REGION_SIZE;

        drawNewTerrain(current_block_x, current_block_y);
        cullOldTerrain(current_block_x, current_block_y);
    }

    public TerrainBlock lookupBlock(String name)
    {
        return (TerrainBlock)_terrain.get(name);
    }

    private void initMark()
    {
        Arrow arrow = new Arrow(Vector3f.UNIT_Z.mult(2f));
        arrow.setLineWidth(3);

        _mark = new Geometry("red_mark", arrow);
        Material mark_mat = new Material(_asset_manager, "Common/MatDefs/Misc/SolidColor.j3md");
        mark_mat.setColor("m_Color", ColorRGBA.Red);
        _mark.setMaterial(mark_mat);
    }

    public void handleMousePicking()
    {
        CollisionResults results = getBlockCollisionFromMousePointer(_cam, _input_manager);
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
                Config.logger.info("Highlighted Block: " + t.getBlockGeometry().getName());
            }

            //Draw the red thingy
            _mark.setLocalTranslation(farthest.getContactPoint());

            Quaternion q = new Quaternion();
            q.lookAt(farthest.getContactNormal(), Vector3f.UNIT_Y);
            _mark.setLocalRotation(q);

            _root_node.attachChild(_mark);
        } else {
            _root_node.detachChild(_mark);
            if (_highlighted != null)
            {
                _highlighted.unhighlight();
                _highlighted= null;
            }
        }
    }

    public ActionListener mouseControlListener = new ActionListener() {
        public void onAction(String name, boolean keyPressed, float tpf) {
          if (name.equals("Click"))
          {
            CollisionResults results = getBlockCollisionFromMousePointer(_cam, _input_manager);
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
                Config.logger.info("Selected Block: " + t.getBlockGeometry().getName());
            }
          }
        }
    };

    private CollisionResults getBlockCollisionFromMousePointer(Camera cam, InputManager inputManager)
    {
        Vector3f origin    = cam.getWorldCoordinates(inputManager.getCursorPosition(), 0.0f);
        Vector3f direction = cam.getWorldCoordinates(inputManager.getCursorPosition(), 0.3f);
        direction.subtractLocal(origin).normalizeLocal();

        Ray ray = new Ray(origin, direction);
        CollisionResults results = new CollisionResults();
        _terrain_node.collideWith(ray, results);
        return results;
    }
}
