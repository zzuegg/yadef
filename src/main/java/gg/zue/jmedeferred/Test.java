package gg.zue.jmedeferred;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.post.SceneProcessor;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.SimpleBatchNode;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;


/**
 * Created by MiZu on 19.05.2015.
 */
public class Test extends SimpleApplication {
    Geometry cube;

    @Override
    public void simpleInitApp() {
        flyCam.setMoveSpeed(100f);
        cube = new Geometry("Cube", new Sphere(20, 20, 5));
        cam.setFrustumFar(10000);

        initDeferred();
        //initRegular();

        addAmbientLight();
        //addDirectionalLights();
        addPointLights();
        //addSingleSphere();
        addSphereGrid();
    }

    private void addPointLights() {
        for (int i = 0; i < 100; i++) {
            PointLight pointLight = new PointLight();
            pointLight.setColor(ColorRGBA.randomColor());
            pointLight.setRadius(40);
            pointLight.setPosition(new Vector3f(FastMath.nextRandomFloat() * 12 * 20, 10, FastMath.nextRandomFloat() * 12 * 20));
            rootNode.addLight(pointLight);
        }
    }

    void addSingleSphere() {
        rootNode.attachChild(cube);
    }

    void addSphereGrid() {
        SimpleBatchNode simpleBatchNode = new SimpleBatchNode();
        for (int x = 0; x < 20; x++) {
            for (int y = 0; y < 20; y++) {
                Geometry clone = cube.clone();
                /*clone.addControl(new AbstractControl() {
                    float s = FastMath.nextRandomFloat() * 10;
                    float up = 1;

                    @Override
                    protected void controlUpdate(float v) {
                        s += up * (v * 4);
                        if (s > 10) {
                            s = 10 - (s - 10);
                            up = -1;
                        }
                        if (s < 0) {
                            s = s * -1;
                            up = 1;
                        }
                        Vector3f pos = getSpatial().getLocalTranslation();
                        pos.y = s;
                        getSpatial().setLocalTranslation(pos);
                    }

                    @Override
                    protected void controlRender(RenderManager renderManager, ViewPort viewPort) {

                    }
                });*/
                clone.setLocalTranslation(x * 12, 0, y * 12);
                simpleBatchNode.attachChild(clone);
            }
        }
        simpleBatchNode.batch();
        rootNode.attachChild(simpleBatchNode);
        simpleBatchNode.updateGeometricState();
    }

    void addAmbientLight() {
        AmbientLight ambientLight = new AmbientLight();
        ambientLight.setColor(ColorRGBA.White.mult(0.1f));
        rootNode.addLight(ambientLight);
    }

    void addDirectionalLights() {
        DirectionalLight directionalLight = new DirectionalLight();
        directionalLight.setDirection(new Vector3f(-1, 0.5f, 0).normalize());
        directionalLight.setColor(ColorRGBA.Red.mult(0.5f));
        rootNode.addLight(directionalLight);

        DirectionalLight directionalLight2 = new DirectionalLight();
        directionalLight2.setDirection(new Vector3f(1, 0.5f, 0).normalize());
        directionalLight2.setColor(ColorRGBA.Green.mult(0.5f));
        rootNode.addLight(directionalLight2);
    }

    void initDeferred() {
        for (SceneProcessor sceneProcessor : viewPort.getProcessors()) {
            viewPort.removeProcessor(sceneProcessor);
        }

        viewPort.addProcessor(new DeferredSceneprocessor(assetManager));

        cube.setMaterial(new Material(assetManager, "Materials/Deferred/Default/Deferred.j3md"));
    }

    void initRegular() {
        cube.setMaterial(new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md"));
        cube.getMaterial().setColor("Diffuse", new ColorRGBA(0.5f, 0.5f, 0.5f, 0f));
    }

    public static void main(String[] args) {
        new Test().start();
    }
}
