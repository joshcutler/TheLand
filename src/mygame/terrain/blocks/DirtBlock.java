/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mygame.terrain.blocks;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;

/**
 *
 * @author joshcutler
 */
public class DirtBlock extends TerrainBlock{

    private static Material _dirty_material;

    public DirtBlock(String name, Vector3f center, AssetManager assetManager)
    {
        super(assetManager);
        if (DirtBlock._dirty_material == null)
        {
            DirtBlock._dirty_material = new Material(assetManager, "Common/MatDefs/Misc/SolidColor.j3md");
            DirtBlock._dirty_material.setColor("m_Color", ColorRGBA.Brown);
        }
        this.createBlockGeometry(name, center, DirtBlock._dirty_material);
    }

    public String getName()
    {
        return "DirtBlock";
    }
}
