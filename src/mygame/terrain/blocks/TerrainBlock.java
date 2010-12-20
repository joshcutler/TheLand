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
import com.jme3.scene.shape.Box;

/**
 *
 * @author joshcutler
 */
public abstract class TerrainBlock {
    protected static Box box;
    protected Geometry geometry;
    protected Material material;
    private AssetManager _asset_manager;
    private boolean _selected = false;

    public TerrainBlock(AssetManager assetManager)
    {
        _asset_manager = assetManager;
        if (TerrainBlock.box == null)
        {
            box = new Box(Vector3f.ZERO, 0.5f, 0.5f, 0.5f);
        }
    }

    protected void createBlockGeometry(String name, Vector3f center, Material material)
    {
        this.material = material;
        geometry = new Geometry(name, box);
        geometry.setMaterial(this.material);
        geometry.setLocalTranslation(center);
    }

    public Geometry getBlockGeometry()
    {
        return geometry;
    }

    public void highlight()
    {
        if (_selected)
        {
            return;
        }
        Material highlight_material = new Material(_asset_manager, "Common/MatDefs/Misc/SolidColor.j3md");
        highlight_material.setColor("m_Color", ColorRGBA.Blue);
        geometry.setMaterial(highlight_material);
    }

    public void unhighlight()
    {
        if (_selected)
        {
            return;
        }
        geometry.setMaterial(material);
    }

    public void select()
    {
        _selected = true;
        Material highlight_material = new Material(_asset_manager, "Common/MatDefs/Misc/SolidColor.j3md");
        highlight_material.setColor("m_Color", ColorRGBA.Red);
        geometry.setMaterial(highlight_material);
    }

    public void deselect()
    {
        _selected = false;
        geometry.setMaterial(material);
    }

    public abstract String getName();
}
