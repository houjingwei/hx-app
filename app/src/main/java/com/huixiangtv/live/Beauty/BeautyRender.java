package com.huixiangtv.live.Beauty;

import android.content.res.AssetManager;

import com.duanqu.qupai.android.camera.CameraClient;
import com.duanqu.qupai.render.BeautifierNode;
import com.duanqu.qupai.render.BlurNode;
import com.duanqu.qupai.render.CameraNode;
import com.duanqu.qupai.render.RendererClient;
import com.duanqu.qupai.render.RendererProcessor;

/**
 * 美颜示例
 */
public class BeautyRender {

    private static BeautyRender instance;

    public static BeautyRender getInstance() {
        if (instance == null) {
            instance = new BeautyRender();
        }
        return instance;
    }

    private RendererProcessor mProcessor;
    private RendererClient mRendererClient;
    private CameraNode mCameraNode;
    private BlurNode mBlurInNode;
    private BlurNode mBlurOutNode;
    private BeautifierNode mBeautifierNode;

    public void initRenderer(AssetManager assetManager, CameraClient _Client) {
        mRendererClient = new RendererClient();
        mProcessor = new RendererProcessor(mRendererClient, assetManager);
        mCameraNode = new CameraNode();
        mCameraNode.setShaderCache(mProcessor.getCache());
        mRendererClient.addNode(mCameraNode);

        mBlurInNode = new BlurNode(1, 1);
        mBlurInNode.setShaderCache(mProcessor.getCache());
        mBlurInNode.addRelyNode(mCameraNode);

        mBlurOutNode = new BlurNode(-1, 1);
        mBlurOutNode.setShaderCache(mProcessor.getCache());
        mBlurOutNode.addRelyNode(mBlurInNode);

        mBeautifierNode = new BeautifierNode(assetManager);
        mBeautifierNode.setShaderCache(mProcessor.getCache());
        mBeautifierNode.addRelyNode(mCameraNode);
        mBeautifierNode.addRelyNode(mBlurOutNode);

        _Client.setRendererCallback(mProcessor);
    }

    public void switchBeauty(boolean on) {
        if (on) {
            mBlurInNode.setBlurRadius(4);
            mBlurOutNode.setBlurRadius(4);
            mRendererClient.addNode(mBlurInNode);
            mRendererClient.addNode(mBlurOutNode);
            mRendererClient.addNode(mBeautifierNode);
            mRendererClient.commit();
        } else {
            mRendererClient.removeNode(mBeautifierNode);
            mRendererClient.removeNode(mBlurOutNode);
            mRendererClient.removeNode(mBlurInNode);
            mRendererClient.commit();
        }
    }

}
