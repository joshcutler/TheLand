/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mygame;
import com.jme3.app.StatsView;
import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;

/**
 *
 * @author joshcutler
 */
public class HUD
{
    private static BitmapText _hud_day;
    private static Node _gui_node;
    private static BitmapFont _gui_font;
    private static AssetManager _asset_manager;

    public static void updateHUD(int day)
    {
        if (Config.HUD_ENABLED)
        {
            _hud_day.setText("Day: " + day);
        }
    }

    public static void initHUD(Node guiNode, StatsView statsView, BitmapFont guiFont, AssetManager assetManager, AppSettings settings)
    {
        if (Config.HUD_ENABLED)
        {
            _gui_node = guiNode;
            _gui_font = guiFont;
            _asset_manager = assetManager;
            
            //Remove stats
            statsView.removeFromParent();

            //Background
            _hud_day = new BitmapText(_gui_font, false);
            Box background = new Box(Vector3f.ZERO, settings.getWidth(), 80, 0);
            Geometry geometry = new Geometry("Box", background);
            Material material = new Material(_asset_manager, "Common/MatDefs/Misc/SolidColor.j3md");
            material.setColor("m_Color", ColorRGBA.DarkGray);
            geometry.setMaterial(material);
            guiNode.attachChild(geometry);

            //Calendar
            _hud_day.setSize(_gui_font.getCharSet().getRenderedSize());      // font size
            _hud_day.setColor(ColorRGBA.White);                             // font color
            _hud_day.setLocalTranslation(settings.getWidth() - 100, _hud_day.getLineHeight(), 0); // position
            _gui_node.attachChild(_hud_day);
        }
    }
}
