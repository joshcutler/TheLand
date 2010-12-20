/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mygame.terrain.blocks;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

/**
 *
 * @author joshcutler
 */
public class StoneBlock extends TerrainBlock {
    private static Material _stone_material;

    public StoneBlock(String name, Vector3f center, AssetManager assetManager)
    {
        super(assetManager);
        if (StoneBlock._stone_material == null)
        {
            StoneBlock._stone_material = new Material(assetManager, "Common/MatDefs/Misc/SolidColor.j3md");
            StoneBlock._stone_material.setColor("m_Color", ColorRGBA.Gray);
        }
        this.createBlockGeometry(name, center, StoneBlock._stone_material);
    }

    public String getName()
    {
        return "StoneBlock";
    }
}
