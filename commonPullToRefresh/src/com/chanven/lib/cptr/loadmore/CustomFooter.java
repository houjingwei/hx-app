/*
Copyright 2015 Chanven

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.chanven.lib.cptr.loadmore;

import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chanven.lib.cptr.R;

/**
 * default load more view
 */
public class CustomFooter implements ILoadViewMoreFactory {

    @Override
    public ILoadMoreView madeLoadMoreView() {
        return new LoadMoreHelper();
    }

    private class LoadMoreHelper implements ILoadMoreView {


        private ImageView imageView;
        private AnimationDrawable animationDrawable;

        protected OnClickListener onClickRefreshListener;

        @Override
        public void init(FootViewAdder footViewHolder, OnClickListener onClickRefreshListener) {
            View view = footViewHolder.addFootView(R.layout.custom_footer);
            imageView = (ImageView) view.findViewById(R.id.ptr_classic_header_rotate_view);
            imageView.setImageResource(R.drawable.animation);
            animationDrawable = (AnimationDrawable) imageView.getDrawable();
            this.onClickRefreshListener = onClickRefreshListener;

        }

        @Override
        public void showNormal() {

        }


        @Override
        public void showLoading() {
            animationDrawable.start();
        }

        @Override
        public void showFail(Exception exception) {
            animationDrawable.stop();
        }

        @Override
        public void showNomore() {

        }

    }

}
