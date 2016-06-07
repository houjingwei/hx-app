package me.iwf.photopicker.fragment;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.R;
import me.iwf.photopicker.adapter.PhotoGridAdapter;
import me.iwf.photopicker.adapter.PopupDirectoryListAdapter;
import me.iwf.photopicker.entity.Photo;
import me.iwf.photopicker.entity.PhotoDirectory;
import me.iwf.photopicker.event.OnPhotoClickListener;
import me.iwf.photopicker.utils.ImageCaptureManager;
import me.iwf.photopicker.utils.MediaStoreHelper;

import static android.app.Activity.RESULT_OK;
import static me.iwf.photopicker.PhotoPickerActivity.DEFAULT_COLUMN_NUMBER;
import static me.iwf.photopicker.PhotoPickerActivity.EXTRA_SHOW_GIF;
import static me.iwf.photopicker.utils.MediaStoreHelper.INDEX_ALL_PHOTOS;

/**
 * Created by donglua on 15/5/31.
 */
public class PhotoPickerFragment extends Fragment {

  private ImageCaptureManager captureManager;
  private PhotoGridAdapter photoGridAdapter;

  private PopupDirectoryListAdapter listAdapter;
  private List<PhotoDirectory> directories;

  private int SCROLL_THRESHOLD = 30;
  int column;

  private final static String EXTRA_CAMERA = "camera";
  private final static String EXTRA_COLUMN = "column";
  private final static String EXTRA_COUNT = "count";
  private final static String EXTRA_GIF = "gif";

  public static PhotoPickerFragment newInstance(boolean showCamera, boolean showGif, int column, int maxCount) {
    Bundle args = new Bundle();
    args.putBoolean(EXTRA_CAMERA, showCamera);
    args.putBoolean(EXTRA_GIF, showGif);
    args.putInt(EXTRA_COLUMN, column);
    args.putInt(EXTRA_COUNT, maxCount);
    PhotoPickerFragment fragment = new PhotoPickerFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    directories = new ArrayList<>();

    column = getArguments().getInt(EXTRA_COLUMN, DEFAULT_COLUMN_NUMBER);
    boolean showCamera = getArguments().getBoolean(EXTRA_CAMERA, true);

    photoGridAdapter = new PhotoGridAdapter(getContext(), directories, column);
    photoGridAdapter.setShowCamera(showCamera);

    Bundle mediaStoreArgs = new Bundle();

    boolean showGif = getArguments().getBoolean(EXTRA_GIF);
    mediaStoreArgs.putBoolean(EXTRA_SHOW_GIF, showGif);
    MediaStoreHelper.getPhotoDirs(getActivity(), mediaStoreArgs,
            new MediaStoreHelper.PhotosResultCallback() {
              @Override
              public void onResultCallback(List<PhotoDirectory> dirs) {
                directories.clear();
                directories.addAll(dirs);
                photoGridAdapter.notifyDataSetChanged();
                listAdapter.notifyDataSetChanged();
              }
            });

    captureManager = new ImageCaptureManager(getActivity());

  }


  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    setRetainInstance(true);

    final View rootView = inflater.inflate(R.layout.__picker_fragment_photo_picker, container, false);

    listAdapter  = new PopupDirectoryListAdapter(Glide.with(this), directories);

    RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_photos);
    StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(column, OrientationHelper.VERTICAL);
    layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setAdapter(photoGridAdapter);
    recyclerView.setItemAnimator(new DefaultItemAnimator());

    final Button btSwitchDirectory = (Button) rootView.findViewById(R.id.button);

    final ListPopupWindow listPopupWindow = new ListPopupWindow(getActivity());
    listPopupWindow.setWidth(ListPopupWindow.MATCH_PARENT);
    listPopupWindow.setAnchorView(btSwitchDirectory);
    listPopupWindow.setAdapter(listAdapter);
    listPopupWindow.setModal(true);
    listPopupWindow.setDropDownGravity(Gravity.BOTTOM);
    //listPopupWindow.setAnimationStyle(R.style.Animation_AppCompat_DropDownUp);

    listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        listPopupWindow.dismiss();

        PhotoDirectory directory = directories.get(position);

        btSwitchDirectory.setText(directory.getName());

        photoGridAdapter.setCurrentDirectoryIndex(position);
        photoGridAdapter.notifyDataSetChanged();
      }
    });

    photoGridAdapter.setOnPhotoClickListener(new OnPhotoClickListener() {
      @Override
      public void onClick(View v, int position, boolean showCamera) {
        final int index = showCamera ? position - 1 : position;

        List<String> photos = photoGridAdapter.getCurrentPhotoPaths();

        int[] screenLocation = new int[2];
        v.getLocationOnScreen(screenLocation);
        ImagePagerFragment imagePagerFragment =
                ImagePagerFragment.newInstance(photos, index, screenLocation, v.getWidth(),
                        v.getHeight());

        ((PhotoPickerActivity) getActivity()).addImagePagerFragment(imagePagerFragment);
      }
    });

    photoGridAdapter.setOnCameraClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
//        try {
//          Intent intent = captureManager.dispatchTakePictureIntent();
//          startActivityForResult(intent, ImageCaptureManager.REQUEST_TAKE_PHOTO);
//        } catch (IOException e) {
//          e.printStackTrace();
//        }
//
//        // 调用系统相机
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        //intent.addCategory(Intent.CATEGORY_DEFAULT);
//        // 取当前时间为照片名
//        String mPictureFile = DateFormat.format("yyyyMMdd_hhmmss",
//                Calendar.getInstance(Locale.CHINA))
//                + ".jpg";
//        String filePath = getPhotoPath() + mPictureFile;
//        // 通过文件创建一个uri中
//        Uri imageUri = Uri.fromFile(new File(filePath));
//        // 保存uri对应的照片于指定路径
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//        startActivityForResult(intent, Activity.DEFAULT_KEYS_DIALER);

        final Intent takePictureImIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ContentValues values = new ContentValues();
        Uri mPhotoUri = getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        takePictureImIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mPhotoUri);
        startActivityForResult(takePictureImIntent,1);

      }
    });



    btSwitchDirectory.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View v) {

        if (listPopupWindow.isShowing()) {
          listPopupWindow.dismiss();
        } else if (!getActivity().isFinishing()) {
          listPopupWindow.setHeight(Math.round(rootView.getHeight() * 0.8f));
          listPopupWindow.show();
        }
      }
    });


    recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        // Log.d(">>> Picker >>>", "dy = " + dy);
        if (Math.abs(dy) > SCROLL_THRESHOLD) {
          Glide.with(getActivity()).pauseRequests();
        } else {
          Glide.with(getActivity()).resumeRequests();
        }
      }
      @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
          Glide.with(getActivity()).resumeRequests();
        }
      }
    });


    return rootView;
  }


  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == ImageCaptureManager.REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
      captureManager.galleryAddPic();
      if (directories.size() > 0) {
        String path = captureManager.getCurrentPhotoPath();
        PhotoDirectory directory = directories.get(INDEX_ALL_PHOTOS);
        directory.getPhotos().add(INDEX_ALL_PHOTOS, new Photo(path.hashCode(), path));
        directory.setCoverPath(path);
        photoGridAdapter.notifyDataSetChanged();
      }
    }
  }


  public PhotoGridAdapter getPhotoGridAdapter() {
    return photoGridAdapter;
  }


  @Override public void onSaveInstanceState(Bundle outState) {
    captureManager.onSaveInstanceState(outState);
    super.onSaveInstanceState(outState);
  }


  @Override public void onViewStateRestored(Bundle savedInstanceState) {
    captureManager.onRestoreInstanceState(savedInstanceState);
    super.onViewStateRestored(savedInstanceState);
  }

  public ArrayList<String> getSelectedPhotoPaths() {
    return photoGridAdapter.getSelectedPhotoPaths();
  }

  @Override public void onDetach() {
    super.onDetach();

    if (directories == null) {
      return;
    }

    for (PhotoDirectory directory : directories) {
      directory.getPhotoPaths().clear();
      directory.getPhotos().clear();
      directory.setPhotos(null);
    }
    directories.clear();
    directories = null;

  }

  /**
   * 获得照片路径
   *
   * @return
   */
  private String getPhotoPath() {
    return Environment.getExternalStorageDirectory() + "/DCIM/";
  }
}
