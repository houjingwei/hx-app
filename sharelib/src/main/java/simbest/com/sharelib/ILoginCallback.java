package simbest.com.sharelib;

import java.util.Map;

/**
 * hjw
 */
public interface ILoginCallback {
    void onSuccess(Map<String, String> data);
    void onFaild(String msg);
    void onCancel();
}
