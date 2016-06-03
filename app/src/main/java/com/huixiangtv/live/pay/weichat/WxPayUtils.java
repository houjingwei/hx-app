package com.huixiangtv.live.pay.weichat;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Toast;

import com.huixiangtv.live.Api;
import com.huixiangtv.live.model.Live;
import com.huixiangtv.live.model.WxRequest;
import com.huixiangtv.live.service.RequestUtils;
import com.huixiangtv.live.service.ResponseCallBack;
import com.huixiangtv.live.service.ServiceException;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Stone on 16/5/26.
 */
public class WxPayUtils {
    private static final String TAG = "WxPayUtils";
//
//
//    public static void requestOrder(WxRequest wxRequest)
//    {
//        Map<String, String> paramsMap = new HashMap<String, String>();
//        paramsMap.put("appid", );
//        paramsMap.put("mch_id", wxRequest.mch_id + "");
//        paramsMap.put("device_info", wxRequest.device_info);
//        paramsMap.put("nonce_str", wxRequest.nonce_str + "");
//        paramsMap.put("sign", wxRequest.sign + "");
//        paramsMap.put("body", "");
//        paramsMap.put("detail", wxRequest.detail + "");
//        paramsMap.put("attach", wxRequest.attach + "");
//        paramsMap.put("out_trade_no", wxRequest.out_trade_no);
//        paramsMap.put("fee_type", wxRequest.fee_type + "");
//        paramsMap.put("total_fee", wxRequest.total_fee + "");
//        paramsMap.put("spbill_create_ip", wxRequest.spbill_create_ip);
//        paramsMap.put("time_start", wxRequest.time_start);
//        paramsMap.put("time_expire", wxRequest.time_expire);
//        paramsMap.put("goods_tag", wxRequest.goods_tag);
//        paramsMap.put("notify_url", wxRequest.notify_url);
//        paramsMap.put("trade_type", wxRequest.trade_type);
//        paramsMap.put("limit_pay", wxRequest.limit_pay);
//
//
//
//        RequestUtils.sendPostRequest(Api.LIVE_LIST, paramsMap, new ResponseCallBack<Live>() {
//            @Override
//            public void onSuccessList(List<Live> data) {
//
//                if (data != null && data.size() > 0) {
//
//                    Long totalCount = Long.parseLong(data.size() + "");
//                    if (0 == totalCount) {
//                        Toast.makeText(getActivity(), "已经没有更多内容了", Toast.LENGTH_LONG).show();
//                    } else {
//                        if (null != listPager) {
//                            //dl_pager.removeAllViews();
//                            addViewSelf(data);
//                            listPager.notifyDataSetChanged();
//                            tvInfo.setText(listPager.list.get(currentViewPage).getNickName());
//                            loadSuc = true;
//                        }
//                    }
//                } else {
//
//                }
//                mRefreshLayout.onRefreshComplete();
//                ll_search.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onFailure(ServiceException e) {
//                super.onFailure(e);
//                mRefreshLayout.onRefreshComplete();
//                showToast("当有网络不可用，请检查您的网络设置");
//            }
//        }, Live.class);
//
//    }



    public static void pay(final Activity activity, String appId, String prepayId, String nonceStr, String partnerId, String timeStamp, String packageValue, final String paySignature) {
        PayReq payReq = new PayReq();

        payReq.appId = appId;
        payReq.prepayId = prepayId;
        payReq.packageValue = packageValue;
        payReq.nonceStr = nonceStr;
        payReq.partnerId = partnerId;
        payReq.timeStamp = timeStamp;
        payReq.sign = paySignature;

        final IWXAPI msgApi = WXAPIFactory.createWXAPI(activity, null);
        msgApi.registerApp(payReq.appId);
        msgApi.sendReq(payReq);
    }

    public static void startPayTask(Context context,
                                    final String appid,
                                    final String apiKey,
                                    final String body,
                                    final String macId,
                                    final String notifyUrl,
                                    final String outTradeNo,
                                    final String price,
                                    final String extData) {

        PayTask payTask = new PayTask(context, appid, apiKey, body, macId, notifyUrl, outTradeNo, price, extData);
        payTask.execute();
    }

    private static class PayTask extends AsyncTask<Void, Void, PayReq> {
        private Context context;
        private String appid;
        private String apiKey;
        private String body;
        private String macId;
        private String notifyUrl;
        private String outTradeNo;
        private String price;
        private String extData;

        public PayTask(Context context,
                       final String appid,
                       final String apiKey,
                       final String body,
                       final String macId,
                       final String notifyUrl,
                       final String outTradeNo,
                       final String price,
                       final String extData) {
            this.appid = appid;
            this.context = context;
            this.apiKey = apiKey;
            this.body = body;
            this.macId = macId;
            this.notifyUrl = notifyUrl;
            this.outTradeNo = outTradeNo;
            this.price = price;
            this.extData = extData;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(PayReq result) {
            final IWXAPI msgApi = WXAPIFactory.createWXAPI(context, null);
            msgApi.registerApp(appid);
            boolean b = msgApi.sendReq(result);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected PayReq doInBackground(Void... params) {
            PayReq req = getPayReq(appid, apiKey, body, macId, notifyUrl, outTradeNo, price, extData);
            return req;
        }
    }

    private static PayReq getPayReq(String appid,
                                    String apiKey,
                                    String body,
                                    String mchId,
                                    String notifyUrl,
                                    String outTradeNo,
                                    String price,
                                    String extData) {
        PayReq payReq = new PayReq();

        String url = String.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
        String entity = genProductArgs(appid, apiKey, body, mchId, notifyUrl, outTradeNo, price);
        byte[] buf = Util.httpPost(url, entity);

        String content = new String(buf);
        Map<String, String> xml = decodeXml(content);

        payReq.appId = appid;
        payReq.partnerId = mchId;
        payReq.prepayId = xml.get("prepay_id");
        payReq.packageValue = "Sign=WXPay";
        payReq.nonceStr = genNonceStr();
        payReq.extData = extData;
        payReq.timeStamp = String.valueOf(genTimeStamp());

        List<NameValuePair> signParams = new LinkedList<NameValuePair>();
        signParams.add(new BasicNameValuePair("appid", payReq.appId));
        signParams.add(new BasicNameValuePair("noncestr", payReq.nonceStr));
        signParams.add(new BasicNameValuePair("package", payReq.packageValue));
        signParams.add(new BasicNameValuePair("partnerid", payReq.partnerId));
        signParams.add(new BasicNameValuePair("prepayid", payReq.prepayId));
        signParams.add(new BasicNameValuePair("timestamp", payReq.timeStamp));

        payReq.sign = genAppSign(signParams, apiKey);

        return payReq;
    }

    private static String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    private static long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    private static Map<String, String> decodeXml(String content) {
        try {
            Map<String, String> xml = new HashMap<String, String>();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String nodeName = parser.getName();
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if ("xml".equals(nodeName) == false) {
                            //实例化student对象
                            xml.put(nodeName, parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = parser.next();
            }
            return xml;
        } catch (Exception e) {
            Log.e("orion", e.toString());
        }
        return null;
    }

    private static String genProductArgs(String appid,
                                         String apiKey,
                                         String body,
                                         String macId,
                                         String notifyUrl,
                                         String outTradeNo,
                                         String price) {
        try {
            String nonceStr = genNonceStr();
            List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
            packageParams.add(new BasicNameValuePair("appid", appid));
            packageParams.add(new BasicNameValuePair("body", body));
            packageParams.add(new BasicNameValuePair("mch_id", macId));
            packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
            packageParams.add(new BasicNameValuePair("notify_url", notifyUrl));
            packageParams.add(new BasicNameValuePair("out_trade_no", outTradeNo));
            packageParams.add(new BasicNameValuePair("spbill_create_ip", "127.0.0.1"));
            packageParams.add(new BasicNameValuePair("total_fee", price));
            packageParams.add(new BasicNameValuePair("trade_type", "APP"));

            String sign = genPackageSign(packageParams, apiKey);

            packageParams.add(new BasicNameValuePair("sign", sign));

            String xmlString = toXml(packageParams);
            return new String(xmlString.toString().getBytes(), "ISO8859-1");
        } catch (Exception e) {
            Log.e(TAG, "genProductArgs fail, ex = " + e.getMessage());
            return null;
        }
    }

    private static String genAppSign(List<NameValuePair> params, String apiKey) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(apiKey);
        //   this.sb.append("sign str\n"+sb.toString()+"\n\n");
        String appSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        Log.e("orion", appSign);
        return appSign;
    }

    private static String toXml(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        for (int i = 0; i < params.size(); i++) {
            sb.append("<" + params.get(i).getName() + ">");
            sb.append(params.get(i).getValue());
            sb.append("</" + params.get(i).getName() + ">");
        }
        sb.append("</xml>");
        Log.e("orion", sb.toString());
        return sb.toString();
    }

    /**
     * 生成签名
     */

    private static String genPackageSign(List<NameValuePair> params, String apiKey) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(apiKey);
        String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        Log.e("orion", packageSign);
        return packageSign;
    }
}
