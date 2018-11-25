package gtclassic.util;

import ic2.core.platform.textures.Sprites;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static ic2.core.platform.textures.Ic2Icons.addSprite;
import static ic2.core.platform.textures.Ic2Icons.addTextureEntry;

public class GTIcons {
    @SideOnly(Side.CLIENT)
    public static void loadSprites(){
        addSprite(new Sprites.SpriteData("gtclassic_blocks", "gtclassic:textures/sprites/blocks.png", new Sprites.SpriteInfo(16, 16)));
        addSprite(new Sprites.SpriteData("gtclassic_items", "gtclassic:textures/sprites/items.png", new Sprites.SpriteInfo(16, 16)));
     
        addSprite(new Sprites.SpriteData("gtclassic_builder", "gtclassic:textures/sprites/builder.png", new Sprites.SpriteInfo(1, 12)));
        addSprite(new Sprites.SpriteData("gtclassic_smallbuffer", "gtclassic:textures/sprites/smallbuffer.png", new Sprites.SpriteInfo(1, 12)));
        addSprite(new Sprites.SpriteData("gtclassic_largebuffer", "gtclassic:textures/sprites/largebuffer.png", new Sprites.SpriteInfo(1, 12)));
        
        
        
        addTextureEntry(new Sprites.TextureEntry("gtclassic_blocks", 0, 0, 16, 8));
        addTextureEntry(new Sprites.TextureEntry("gtclassic_items", 0, 0, 16, 5));
        
        addTextureEntry(new Sprites.TextureEntry("gtclassic_builder", 0, 0, 1, 12));
        addTextureEntry(new Sprites.TextureEntry("gtclassic_smallbuffer", 0, 0, 1, 12));
        addTextureEntry(new Sprites.TextureEntry("gtclassic_largebuffer", 0, 0, 1, 12));
        
    }
}
