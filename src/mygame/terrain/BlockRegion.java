/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mygame.terrain;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.util.HashMap;
import mygame.terrain.blocks.TerrainBlock;

/**
 *
 * @author joshcutler
 */
public class BlockRegion {
    private Node _node = new Node();
    private boolean _is_drawn = false;
    RegionType _region;

    public BlockRegion(int size, int offset_x, int offset_y, RegionType region, HashMap _terrain, AssetManager assetManager)
    {
        _region = region;
        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                TerrainBlock top = null, right = null, left = null, bottom = null;
                String label = "block_" + (i + offset_x) + "_" + (j + offset_y);
                
                if (_terrain.containsKey("block_" + (i + offset_x) + "_" + (j + offset_y + 1)))
                {
                    top = (TerrainBlock)_terrain.get("block_" + (i + offset_x) + "_" + (j + offset_y + 1));
                }
                if (_terrain.containsKey("block_" + (i + offset_x) + "_" + (j + offset_y - 1)))
                {
                    bottom = (TerrainBlock)_terrain.get("block_" + (i + offset_x) + "_" + (j + offset_y - 1));
                }
                if (_terrain.containsKey("block_" + (i + offset_x + 1) + "_" + (j + offset_y)))
                {
                    right = (TerrainBlock)_terrain.get("block_" + (i + offset_x + 1) + "_" + (j + offset_y));
                }
                if (_terrain.containsKey("block_" + (i + offset_x - 1)  + "_" + (j + offset_y)))
                {
                    left = (TerrainBlock)_terrain.get("block_" + (i +offset_x - 1) + "_" + (j + offset_y));
                }
                TerrainBlock t = _region.getBlock(label, new Vector3f(i + offset_x, j + offset_y, 0),assetManager,  top, bottom, left, right);

                _terrain.put(label, t);
                _node.attachChild(t.getBlockGeometry());
            }
        }
    }

    public Node getNode()
    {
        return _node;
    }

    /**
     * @return the _is_drawn
     */
    public boolean isDrawn() {
        return _is_drawn;
    }

    /**
     * @param is_drawn the _is_drawn to set
     */
    public void setDrawn(boolean is_drawn) {
        this._is_drawn = is_drawn;
    }
}
