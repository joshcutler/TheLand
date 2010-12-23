/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mygame.terrain.blocks;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.texture.Texture;

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
            _grassy_material = new Material(assetManager, "Common/MatDefs/Misc/SimpleTextured.j3md");
            Texture tex_ml = assetManager.loadTexture("Materials/Blocks/Grass.jpg");
            _grassy_material.setTexture("m_ColorMap", tex_ml);
        }
        this.createBlockGeometry(name, center, GrassBlock._grassy_material);
    }

    public String getName()
    {
        return "GrassBlock";
    }

    public Material getAlphaMaterial()
    {
        Material highlight_material = new Material(_asset_manager, "Common/MatDefs/Misc/ColoredTextured.j3md");
        highlight_material.setTexture("m_ColorMap", _asset_manager.loadTexture("Materials/Blocks/Grass_Alpha.png"));
        highlight_material.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        return highlight_material;
    }
}