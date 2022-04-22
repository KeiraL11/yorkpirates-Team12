package com.engteam14.yorkpirates.tests;

import com.badlogic.gdx.Gdx;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertTrue;

@RunWith(GdxTestRunner.class)
public class AssetTests {

    @Test
    public void testShip1AssetExists() {
        assertTrue("This test will only pass if ship1.png exists",
                Gdx.files.internal("../assets/ship1.png").exists());
    }
    @Test
    public void testShip2AssetExists() {
        assertTrue("This test will only pass if ship2.png exists",
                Gdx.files.internal("../assets/ship2.png").exists());
    }
    @Test
    public void testShip3AssetExists() {
        assertTrue("This test will only pass if ship3.png exists",
                Gdx.files.internal("../assets/ship3.png").exists());
    }
    @Test
    public void testAlcuinAssetExists() {
        assertTrue("This test will only pass if alcuin.png exists",
                Gdx.files.internal("../assets/alcuin.png").exists());
    }
    @Test
    public void testAlcuin2AssetExists() {
        assertTrue("This test will only pass if alcuin_2.png exists",
                Gdx.files.internal("../assets/alcuin_2.png").exists());
    }
    @Test
    public void testAlcuinBoatAssetExists() {
        assertTrue("This test will only pass if alcuin_boat.png exists",
                Gdx.files.internal("../assets/alcuin_boat.png").exists());
    }
    @Test
    public void testAllyArrowAssetExists() {
        assertTrue("This test will only pass if allyArrow.png exists",
                Gdx.files.internal("../assets/allyArrow.png").exists());
    }
    @Test
    public void testAllyHealthBarAssetExists() {
        assertTrue("This test will only pass if allyHealthBar.png exists",
                Gdx.files.internal("../assets/allyHealthBar.png").exists());
    }
    @Test
    public void testDefaultFontAssetExists() {
        assertTrue("This test will only pass if default.fnt exists",
                Gdx.files.internal("../assets/default.fnt").exists());
    }
    @Test
    public void testDerwentExists() {
        assertTrue("This test will only pass if derwent.png exists",
                Gdx.files.internal("../assets/derwent.png").exists());
    }
    @Test
    public void testDerwent2Exists() {
        assertTrue("This test will only pass if derwent_2.png exists",
                Gdx.files.internal("../assets/derwent_2.png").exists());
    }
    @Test
    public void testDerwentBoatExists() {
        assertTrue("This test will only pass if derwent_boat.png exists",
                Gdx.files.internal("../assets/derwent_boat.png").exists());
    }
    @Test
    public void testEdgesExists() {
        assertTrue("This test will only pass if edges.csv exists",
                Gdx.files.internal("../assets/edges.csv").exists());
    }
    @Test
    public void testEnemyHealthBarExists() {
        assertTrue("This test will only pass if enemyHealthBar.png exists",
                Gdx.files.internal("../assets/enemyHealthBar.png").exists());
    }
    @Test
    public void testEnterExists() {
        assertTrue("This test will only pass if enter.png exists",
                Gdx.files.internal("../assets/enter.png").exists());
    }
    @Test
    public void testFINALMAPExists() {
        assertTrue("This test will only pass if FINAL_MAP.tmx exists",
                Gdx.files.internal("../assets/FINAL_MAP.tmx").exists());
    }
    @Test
    public void testFINALMAPTERRAINExists() {
        assertTrue("This test will only pass if FINAL_MAP_Terrain.csv exists",
                Gdx.files.internal("../assets/FINAL_MAP_Terrain.csv").exists());
    }
    @Test
    public void testGameOverExists() {
        assertTrue("This test will only pass if game_over.png exists",
                Gdx.files.internal("../assets/game_over.png").exists());
    }
    @Test
    public void testGameWinExists() {
        assertTrue("This test will only pass if game_win.png exists",
                Gdx.files.internal("../assets/game_win.png").exists());
    }
    @Test
    public void testGiveMoreDamageExists() {
        assertTrue("This test will only pass if give_more_damage.png exists",
                Gdx.files.internal("../assets/give_more_damage.png").exists());
    }
    @Test
    public void testGiveMoreDamageGreyExists() {
        assertTrue("This test will only pass if give_more_damage_grey.png exists",
                Gdx.files.internal("../assets/give_more_damage_grey.png").exists());
    }
    @Test
    public void testGoodrickeExists() {
        assertTrue("This test will only pass if goodricke.png exists",
                Gdx.files.internal("../assets/goodricke.png").exists());
    }
    @Test
    public void testHealthRestoreExists() {
        assertTrue("This test will only pass if health_restore.png exists",
                Gdx.files.internal("../assets/health_restore.png").exists());
    }
    @Test
    public void testHealthRestoreGreyExists() {
        assertTrue("This test will only pass if health_restore_grey.png exists",
                Gdx.files.internal("../assets/health_restore_grey.png").exists());
    }
    @Test
    public void testHomeArrowExists() {
        assertTrue("This test will only pass if homeArrow.png exists",
                Gdx.files.internal("../assets/homeArrow.png").exists());
    }
    @Test
    public void testImmunityExists() {
        assertTrue("This test will only pass if immunity.png exists",
                Gdx.files.internal("../assets/immunity.png").exists());
    }
    @Test
    public void testImmunityGreyExists() {
        assertTrue("This test will only pass if immunity_grey.png exists",
                Gdx.files.internal("../assets/immunity_grey.png").exists());
    }
    @Test
    public void testKeyboardExists() {
        assertTrue("This test will only pass if keyboard.png exists",
                Gdx.files.internal("../assets/keyboard.png").exists());
    }
    @Test
    public void testLangwithExists() {
        assertTrue("This test will only pass if langwith.png exists",
                Gdx.files.internal("../assets/langwith.png").exists());
    }
    @Test
    public void testLangwith2Exists() {
        assertTrue("This test will only pass if langwith_2.png exists",
                Gdx.files.internal("../assets/langwith_2.png").exists());
    }
    @Test
    public void testLangwithBoatExists() {
        assertTrue("This test will only pass if langwith_boat.png exists",
                Gdx.files.internal("../assets/langwith_boat.png").exists());
    }
    @Test
    public void testLogoExists() {
        assertTrue("This test will only pass if logo.png exists",
                Gdx.files.internal("../assets/logo.png").exists());
    }
    @Test
    public void testLootExists() {
        assertTrue("This test will only pass if loot.png exists",
                Gdx.files.internal("../assets/loot.png").exists());
    }
    @Test
    public void testMouseExists() {
        assertTrue("This test will only pass if mouse.png exists",
                Gdx.files.internal("../assets/mouse.png").exists());
    }
    @Test
    public void testPausedExists() {
        assertTrue("This test will only pass if paused.png exists",
                Gdx.files.internal("../assets/paused.png").exists());
    }
    @Test
    public void testPirateExists() {
        assertTrue("This test will only pass if Pirate1.tsx exists",
                Gdx.files.internal("../assets/Pirate1.tsx").exists());
    }
    @Test
    public void testPirateThemeExists() {
        assertTrue("This test will only pass if Pirate1_Theme1.ogg exists",
                Gdx.files.internal("../assets/Pirate1_Theme1.ogg").exists());
    }
    @Test
    public void testPirateThemeURLExists() {
        assertTrue("This test will only pass if Pirate1_Theme1.ogg.url exists",
                Gdx.files.internal("../assets/Pirate1_Theme1.ogg.url").exists());
    }
    @Test
    public void testPirateThemeWAVExists() {
        assertTrue("This test will only pass if Pirate1_Theme1.wav exists",
                Gdx.files.internal("../assets/Pirate1_Theme1.wav").exists());
    }
    @Test
    public void testPirate12Exists() {
        assertTrue("This test will only pass if pirate12.tmx exists",
                Gdx.files.internal("../assets/pirate12.tmx").exists());
    }
    @Test
    public void testPirate16x16Exists() {
        assertTrue("This test will only pass if pirate16x16.tsx exists",
                Gdx.files.internal("../assets/pirate16x16.tsx").exists());
    }
    @Test
    public void testPointsExists() {
        assertTrue("This test will only pass if points.png exists",
                Gdx.files.internal("../assets/points.png").exists());
    }
    @Test
    public void testQuestArrowExists() {
        assertTrue("This test will only pass if questArrow.png exists",
                Gdx.files.internal("../assets/questArrow.png").exists());
    }
    @Test
    public void testRedfshExists() {
        assertTrue("This test will only pass if red.fsh exists",
                Gdx.files.internal("../assets/red.fsh").exists());
    }
    @Test
    public void testRedvshExists() {
        assertTrue("This test will only pass if red.fsh exists",
                Gdx.files.internal("../assets/red.vsh").exists());
    }
    @Test
    public void testSpeedExists() {
        assertTrue("This test will only pass if speed.png exists",
                Gdx.files.internal("../assets/speed.png").exists());
    }
    @Test
    public void testSpeedGreyExists() {
        assertTrue("This test will only pass if speed_grey.png exists",
                Gdx.files.internal("../assets/speed_grey.png").exists());
    }
    @Test
    public void testTakeMoreDamageExists() {
        assertTrue("This test will only pass if take_more_damage.png exists",
                Gdx.files.internal("../assets/take_more_damage.png").exists());
    }
    @Test
    public void testTakeMoreDamageGreyExists() {
        assertTrue("This test will only pass if take_more_damage_grey.png exists",
                Gdx.files.internal("../assets/take_more_damage_grey.png").exists());
    }
    @Test
    public void testTempProjectileExists() {
        assertTrue("This test will only pass if tempProjectile.png exists",
                Gdx.files.internal("../assets/tempProjectile.png").exists());
    }
    @Test
    public void testTilesSheetExists() {
        assertTrue("This test will only pass if tiles_sheet2.png exists",
                Gdx.files.internal("../assets/tiles_sheet2.png").exists());
    }
    @Test
    public void testTilesSheet1Exists() {
        assertTrue("This test will only pass if tiles_sheet2(1).png exists",
                Gdx.files.internal("../assets/tiles_sheet2(1).png").exists());
    }
    @Test
    public void testTilesSheetNewExists() {
        assertTrue("This test will only pass if tiles_sheet2new.tsx exists",
                Gdx.files.internal("../assets/tiles_sheet2 new.tsx").exists());
    }
    @Test
    public void testTransparentExists() {
        assertTrue("This test will only pass if transparent.png exists",
                Gdx.files.internal("../assets/transparent.png").exists());
    }
}
