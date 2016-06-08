package com.huixiangtv.live.model;

/**
 * Created by hjw on 16/6/8.
 */
public class ChatMessage {

    private String content;
    private MsgExt ext;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MsgExt getExt() {
        return ext;
    }

    public void setExt(MsgExt ext) {
        this.ext = ext;
    }
}
