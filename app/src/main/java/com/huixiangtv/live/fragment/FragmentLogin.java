package com.huixiangtv.live.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.huixiangtv.live.App;
import com.huixiangtv.live.R;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.xutils.x;

import java.util.Map;


public class FragmentLogin extends Fragment {




	private View mRootView;



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		x.view().inject(getActivity());
		mRootView = inflater.inflate(R.layout.fragment_login, container, false);
		mRootView.findViewById(R.id.llQQ).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				login(SHARE_MEDIA.QQ);
			}
		});
		mRootView.findViewById(R.id.llWchat).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				login(SHARE_MEDIA.WEIXIN);
			}
		});
		mRootView.findViewById(R.id.llSina).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				login(SHARE_MEDIA.SINA);
			}
		});
		return mRootView;
	}
	private void login(SHARE_MEDIA platform) {


		App.mShareAPI.doOauthVerify(getActivity(), platform, umAuthListener);

	}


	private UMAuthListener umAuthListener = new UMAuthListener() {
		@Override
		public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
			Toast.makeText(getActivity(), "Authorize succeed"+data, Toast.LENGTH_SHORT).show();
			//App.mShareAPI.deleteOauth(getActivity(), platform, umAuthListener);
			if( null != data && data.size()>0) {
				String refresh_token = data.get("refresh_token");
				String access_token = data.get("access_token");
				String uid = data.get("uid");
				Toast.makeText(getActivity(),refresh_token+"   "+access_token+"  "+uid,Toast.LENGTH_LONG).show();
				//doing to db waiting
			}
		}

		@Override
		public void onError(SHARE_MEDIA platform, int action, Throwable t) {
			Toast.makeText( getActivity(), "Authorize fail", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onCancel(SHARE_MEDIA platform, int action) {
			Toast.makeText( getActivity(), "Authorize cancel", Toast.LENGTH_SHORT).show();
		}
	};

}
