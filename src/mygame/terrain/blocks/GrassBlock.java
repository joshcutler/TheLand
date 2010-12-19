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
public class GrassBlock extends TerrainBlock {

    private static Material _grassy_material;
    
    public GrassBlock(String name, Vector3f center, AssetManager assetManager)
    {
        super(assetManager);
        if (GrassBlock._grassy_material == null)
        {
            GrassBlock._grassy_material = new Material(assetManager, "Common/MatDefs/Misc/SolidColor.j3md");
            GrassBlock._grassy_material.setColor("m_Color", ColorRGBA.Green);
        }
        geometry = new Geometry(name, box);
        geometry.setMaterial(GrassBlock._grassy_material);
        geometry.setLocalTranslation(center);
    }
}