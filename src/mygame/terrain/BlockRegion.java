/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mygame.terrain;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.util.HashMap;
import mygame.terrain.blocks.DirtBlock;
import mygame.terrain.blocks.GrassBlock;
import mygame.terrain.blocks.TerrainBlock;

/**
 *
 * @author joshcutler
 */
public class BlockRegion {
    private Node _node = new Node();
    RegionType _region;

    public BlockRegion(int size, RegionType region, HashMap _terrain, AssetManager assetManager)
    {
        _region = region;
        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                TerrainBlock top = null, right = null, left = null, bottom = null;
                String label = "block_" + i + "_" + j;
                
                if (_terrain.containsKey("block_" + i + "_" + (j + 1)))
                {
                    top = (TerrainBlock)_terrain.get("block_" + i + "_" + (j + 1));
                }
                if (_terrain.containsKey("block_" + i + "_" + (j - 1)))
                {
                    bottom = (TerrainBlock)_terrain.get("block_" + i + "_" + (j - 1));
                }
                if (_terrain.containsKey("block_" + (i + 1) + "_" + j))
                {
                    right = (TerrainBlock)_terrain.get("block_" + (i + 1) + "_" + j);
                }
                if (_terrain.containsKey("block_" + (i - 1)  + "_" + j))
                {
                    left = (TerrainBlock)_terrain.get("block_" + (i - 1) + "_" + j);
                }
                TerrainBlock t = _region.getBlock(label, new Vector3f(i, j, 0),assetManager,  top, bottom, left, right);

                _terrain.put(label, t);
                _node.attachChild(t.getBlockGeometry());
            }
        }
    }

    public Node getNode()
    {
        return _node;
    }
}
