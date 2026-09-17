package com.hollow;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PointMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PlayScreen implements Screen, CameraFunctions {
    private Hud hud;
    private boolean isResetting = false;
    Array<Effect> activeEffects = new Array<>();
    private OrthographicCamera camera;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private Array<SolidBlock> solidBlocks;
    private TiledMapHelper mapHelper;
    private float hitStopTimer = 0f;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private Viewport gameViewport;
    private float mapWidth;
    private float mapHeight;
    private float shakeTimer = 0f;
    private float shakeDuration = 0f;
    private float shakeIntensity = 0f;
    private float blackOutTimer = 0f;
    private float blackOutDuration = 0f;
    private Pause pauseMenu;
    Array<Enemy> enemies = new Array<>();
    Array<Projectile> activeProjectiles = new Array<>();
    Animation<TextureRegion> removeSoulBallAnimation;

    private Player player;

    Array<Texture> loadedTextures = new Array<>();

//    private final int[] backGrounds = {0};
//    private final int[] foreGrounds = {1,2};

    private final int[] backGrounds = { 1,2 };
    private int[] foreGrounds = {3, 4,5,6,7};
//    private final IntArray foreGrounds = IntArray.with(3, 4, 5, 6, 7);

    @Override
    public void show() {
        mapHelper = new TiledMapHelper();
        map = mapHelper.loadMap("map.tmx");
//        map =mapHelper.loadMap("C:/Users/ASUS/Downloads/new map/newMap.tmx");

        TiledMapTileLayer mainLayer = (TiledMapTileLayer) map.getLayers().get(1);//todo
//            MapLayer mainLayer = (TiledMapTileLayer) map.getLayers().get(1);

        mapWidth = mainLayer.getWidth() * mainLayer.getTileWidth();
        mapHeight = mainLayer.getHeight() * mainLayer.getTileHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, mapWidth / 10, mapHeight / 20);
        gameViewport = new ExtendViewport(mapWidth / 10, mapHeight / 20, camera);

        renderer = new OrthogonalTiledMapRenderer(map);
        solidBlocks = mapHelper.getSolidRectangles();
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        float playerSize = camera.viewportHeight / 5;

        Texture tmpSheet = new Texture("animation/Effects/BlastSoul.png");
        loadedTextures.add(tmpSheet);
        int frameWidth = tmpSheet.getWidth() / 8;
        int frameHeight = tmpSheet.getHeight();
        TextureRegion[][] tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        Array<TextureRegion> frames = new Array<>();
        for (int i = 0; i < 8; i++) {
            frames.add(tmp[0][i]);
        }
        removeSoulBallAnimation = new Animation<>(0.05f, frames, Animation.PlayMode.NORMAL);



        com.badlogic.gdx.maps.MapLayer spawnLayer = map.getLayers().get("logical");

        com.badlogic.gdx.maps.MapObject spawnPoint = spawnLayer.getObjects().get("SpawnPlayer");
        float spawnX = spawnPoint.getProperties().get("x", Float.class);
        float spawnY = spawnPoint.getProperties().get("y", Float.class);
        player = new Player(spawnX, spawnY, playerSize * 1.876344086f, playerSize, this,this);
        String spawnType;
        float crawlerSize = player.bounds.height * 1.248322147651f / 1.5f;
        float cockroachSize = player.bounds.height * 1.17741935483871f;
        float crystallizedSize = player.bounds.height * 1.0161290f;
        float mosquitoSize = player.bounds.height * 1.21935483871f / 2f;
        float falseKnightSize = player.bounds.height * 3.42f * 1.3f;


        for(MapObject obj : spawnLayer.getObjects()) {
            if(obj instanceof PointMapObject){
                if (obj.getProperties().containsKey("Type")) {
                    spawnX = obj.getProperties().get("x", Float.class);
                    spawnY = obj.getProperties().get("y", Float.class);
                    spawnType = obj.getProperties().get("Type", String.class);
                    switch (spawnType) {
                        case "Crawlid":
                            enemies.add(new Crawler(spawnX, spawnY, crawlerSize * 2.02013422818792f, crawlerSize, CrawlerType.Crawlid,this));
                            break;
                        case "Crystal_Crawler":
                            enemies.add(new Crawler(spawnX, spawnY, crawlerSize * 0.86111111111111111f,crawlerSize, CrawlerType.Crystal_Crawler,this));
                            break;
                        case "MossCreep":
                            enemies.add(new Crawler(spawnX, spawnY, crawlerSize * 1.2764227642276422f, crawlerSize, CrawlerType.MossCreep,this));
                            break;
                        case "Tiktik":
                            enemies.add(new Crawler(spawnX, spawnY, crawlerSize * 1.095f, crawlerSize, CrawlerType.Tiktik,this));
                            break;
                        case "Hornless":
                            enemies.add(new Cockroach(spawnX, spawnY, cockroachSize * 1.0913242f, cockroachSize,CockroachType.Hornless, this));
                            break;
                        case "Hornhead":
                            enemies.add(new Cockroach(spawnX, spawnY, cockroachSize * 1.0913242f, cockroachSize,CockroachType.Hornhead, this));
                            break;
                        case "Crystallized":
                            enemies.add(new Crystallized(spawnX, spawnY, crystallizedSize * 1.5322580f, crystallizedSize, this));
                            break;
                        case "Mosquito":
                            enemies.add(new Mosquito(spawnX, spawnY, mosquitoSize * 1.41935483871f, mosquitoSize, this));
                            break;
                        case "False_knight":
                            enemies.add(new False_knight(spawnX, spawnY, falseKnightSize * 1.72169811320754717f, falseKnightSize, this, activeProjectiles));
                            break;
                        case "Zote":
                            enemies.add(new Zote(spawnX,spawnY,playerSize * 1.876344086f, playerSize,this));

                    }
                }
            }
        }

        hud = new Hud(batch, player);
        player.playerHud = hud;
        pauseMenu = new Pause(new com.badlogic.gdx.utils.viewport.FitViewport(1280, 720), player);
    }

    @Override
    public void render(float delta) {
        if (delta > 0.05f) {
            delta = 0.05f;
        }

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            pauseMenu.setVisible(!pauseMenu.isVisible());
        }
        if (!pauseMenu.isVisible()) {

            if (hitStopTimer > 0) {
                hitStopTimer -= delta;
            } else {
                if (blackOutTimer > 0) {
                    blackOutTimer -= delta;
                }

                player.update(delta, solidBlocks, activeEffects, enemies, activeProjectiles);

                for (Enemy enemy : enemies) {
                    if (!enemy.isDead && player.hitbox.overlaps(enemy.hitbox)) {
                        if (!player.isInvincible) {
                            if (player.hitbox.x + player.hitbox.width / 2 < enemy.hitbox.x + enemy.hitbox.width / 2) {
                                player.setHitFromRight(true);
                            } else {
                                player.setHitFromRight(false);
                            }
                            if (enemy instanceof Zote) {
                                Zote zote = (Zote) enemy;
                                if (zote.currentState != Zote.State.ANGRY_RUN) {
                                    continue;
                                }else{
                                    player.takeDamage(zote.dmg);
                                }
                            }
                            else {
                                player.takeDamage(enemy.dmg);
                            }
                        }
                    }
                }

                updateSoul();
                for (int i = 0; i < activeProjectiles.size; i++) {
                    Projectile proj = activeProjectiles.get(i);
                    proj.update(delta, solidBlocks);
                    if (!proj.isEnemy) {
                        for (Enemy enemy : enemies) {
                            if (!enemy.isInvincible && proj.hitbox.overlaps(enemy.hitbox)) {
                                enemy.takeDamage(proj.damage);
                                proj.isDestroyed = !proj.pierce;
                                shakeCamera(5f, 0.15f);
                                break;
                            }
                        }
                    } else {
                        if (!player.isDead && !player.isInvincible && proj.hitbox.overlaps(player.hitbox)) {
                            player.takeDamage(proj.damage);
                            proj.isDestroyed = !proj.pierce;
                            break;
                        }
                    }

                    if (proj.isDestroyed) {
                        activeEffects.add(new Effect(removeSoulBallAnimation,
                            activeProjectiles.get(i).bounds.x, activeProjectiles.get(i).bounds.y,
                            activeProjectiles.get(i).bounds.width, activeProjectiles.get(i).bounds.height,
                            activeProjectiles.get(i).toRight, false));
                        activeProjectiles.removeIndex(i);
                        i--;
                    }
                }

                for (Enemy enemy : enemies) {
                    enemy.update(delta, solidBlocks, activeEffects, player);
                }
                for (int i = 0; i < activeEffects.size; i++) {
                    Effect fx = activeEffects.get(i);
                    fx.update(delta);
                    if (fx.isFinished()) {
                        activeEffects.removeIndex(i);
                        i--;
                    }
                }
            }
            camera.position.x = player.bounds.x;
            camera.position.y = player.bounds.y;

            float halfWidth = camera.viewportWidth / 2f;
            if (camera.position.x < halfWidth) {
                camera.position.x = halfWidth;
            } else if (camera.position.x > mapWidth - halfWidth) {
                camera.position.x = mapWidth - halfWidth;
            }

            if (shakeTimer > 0) {
                float currentPower = shakeIntensity * (shakeTimer / shakeDuration);
                float xOffset = (float) ((Math.random() - 0.5f) * 2 * currentPower);
                float yOffset = (float) ((Math.random() - 0.5f) * 2 * currentPower);

                camera.position.x += xOffset;
                camera.position.y += yOffset;

                shakeTimer -= delta;
            }

            camera.update();
        }

        gameViewport.apply();

        if (!pauseMenu.isVisible()) {
            com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile.updateAnimationBaseTime();
        }

        renderer.setView(camera);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        MapLayer objectLayer = map.getLayers().get("backgroundIMG");

        if (objectLayer != null) {
            for (MapObject object : objectLayer.getObjects()) {
                if (object instanceof TextureMapObject) {
                    TextureMapObject tileObject = (TextureMapObject) object;

                    float x = tileObject.getX();
                    float y = tileObject.getY();

                    float width = tileObject.getProperties().get("width", Float.class) != null ? tileObject.getProperties().get("width", Float.class) : tileObject.getTextureRegion().getRegionWidth();
                    float height = tileObject.getProperties().get("height", Float.class) != null ? tileObject.getProperties().get("height", Float.class) : tileObject.getTextureRegion().getRegionHeight();

                    TextureRegion textureRegion = tileObject.getTextureRegion();

                    batch.draw(textureRegion, x, y, width, height);
                }
            }
        }

        batch.end();
        renderer.render(backGrounds);
        batch.begin();
        for (Effect fx : activeEffects) {
            if (fx.behindPlayer) {
                fx.draw(batch);
            }
        }

        player.draw(batch);

        for (Enemy enemy : enemies) {
            enemy.draw(batch);
        }

        for (Effect fx : activeEffects) {
            if (!fx.behindPlayer) {
                fx.draw(batch);
            }
        }

        for (Projectile proj : activeProjectiles) {
            proj.draw(batch);
        }
        batch.end();

        shapeRenderer.setProjectionMatrix(camera.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

//        player.drawDebug(shapeRenderer);
//
//        for (Enemy enemy : enemies) {
//            enemy.drawDebug(shapeRenderer);
//        }

        shapeRenderer.end();

        renderer.render(foreGrounds);

        if (blackOutTimer > 0) {
            float alpha = 1.0f;
            float elapsedTime = blackOutDuration - blackOutTimer;

            if (elapsedTime < 0.1f) {
                alpha = elapsedTime / 0.1f;
            } else if (blackOutTimer < 0.1f) {
                alpha = blackOutTimer / 0.1f;
            }

            alpha = Math.max(0f, Math.min(1f, alpha));

            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

            shapeRenderer.setColor(0f, 0f, 0f, alpha);

            float camX = camera.position.x - (camera.viewportWidth / 2f);
            float camY = camera.position.y - (camera.viewportHeight / 2f);
            shapeRenderer.rect(camX, camY, camera.viewportWidth, camera.viewportHeight);

            shapeRenderer.end();

            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
        hud.stage.getViewport().apply();

        batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
        hud.drawElements(batch,delta);

        if (pauseMenu.isVisible()) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0f, 0f, 0f, 0.7f);
            float camX = camera.position.x - (camera.viewportWidth / 2f);
            float camY = camera.position.y - (camera.viewportHeight / 2f);
            shapeRenderer.rect(camX, camY, camera.viewportWidth, camera.viewportHeight);
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);

            pauseMenu.stage.getViewport().apply();
            pauseMenu.draw();
        }
        gameViewport.apply();

        if (isResetting) {
            this.dispose();
            ((com.badlogic.gdx.Game) Gdx.app.getApplicationListener()).setScreen(new PlayScreen());
        }
    }

    @Override
    public void resize(int width, int height) {
        gameViewport.update(width, height, false);
        hud.stage.getViewport().update(width, height, true);

        if (pauseMenu != null) {
            pauseMenu.stage.getViewport().update(width, height, true);
        }
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        batch.dispose();
        player.dispose();
        pauseMenu.dispose();
    }

    public void updateSoul() {
        if (player.theRealSoul != player.currentSoul) {
            if (player.theRealSoul > player.currentSoul) {
                player.currentSoul += 1f;
            } else if (player.theRealSoul < player.currentSoul) {
                player.currentSoul -= 1f;
            }
        }
    }

    @Override
    public void shakeCamera(float intensity, float duration) {
        this.shakeIntensity = intensity;
        this.shakeDuration = duration;
        this.shakeTimer = duration;
    }

    @Override
    public void triggerHitStop(float duration) {
        this.hitStopTimer = duration;
    }

    @Override
    public void triggerBlackOut(float duration) {
        this.blackOutTimer = duration;
        this.blackOutDuration = duration;
    }
    @Override
    public void resetLevel() {
        isResetting = true;
    }
//    public void removeLayer(int layer) {
//        for (int i = 0; i < foreGrounds.length; i++) {
//            if (foreGrounds[i] == layer) {
//                foreGrounds[i] = -1;
//                break;
//            }
//        }
//    }
public void removeLayer(int layer) {
    int count = 0;
    for (int currentLayer : foreGrounds) {
        if (currentLayer == layer) {
            count++;
        }
    }

    if (count == 0) return;

    int[] newArray = new int[foreGrounds.length - count];

    int index = 0;
    for (int currentLayer : foreGrounds) {
        if (currentLayer != layer) {
            newArray[index] = currentLayer;
            index++;
        }
    }

    foreGrounds = newArray;
}
}
