package com.huixiangtv.live.Js;

/**
 * Created by Stone on 16/6/13.
 */
public interface IJsInterface {

    public interface Doing{
        void doSomeThing(String ... str);
    }

    void setDoing(Doing doing);
    /**
     * js中使用的对象
     */
    static String NAME = "java";
}
