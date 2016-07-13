package com.huixiangtv.liveshow.Js;

/**
 * Created by Stone on 16/6/13.
 */
public interface IJsInterface {

    interface Doing{
        void doSomeThing(String ... str);
    }

    void setDoing(Doing doing);
    /**
     * js中使用的对象
     */
    String NAME = "java";
}
