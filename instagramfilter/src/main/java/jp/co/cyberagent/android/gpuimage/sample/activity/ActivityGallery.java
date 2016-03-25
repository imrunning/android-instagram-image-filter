/*
 * Copyright (C) 2012 CyberAgent
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.co.cyberagent.android.gpuimage.sample.activity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImage.OnPictureSavedListener;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;
import jp.co.cyberagent.android.gpuimage.sample.GPUImageFilterTools;
import jp.co.cyberagent.android.gpuimage.sample.GPUImageFilterTools.FilterAdjuster;
import jp.co.cyberagent.android.gpuimage.sample.GPUImageFilterTools.FilterList;
import jp.co.cyberagent.android.gpuimage.sample.GPUImageFilterTools.FilterType;
import jp.co.cyberagent.android.gpuimage.sample.GPUImageFilterTools.OnGpuImageFilterChosenListener;
import jp.co.cyberagent.android.gpuimage.sample.utils.RoundImageView;
import jp.co.cyberagent.android.gpuimage.sample.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityGallery extends Activity implements
		OnSeekBarChangeListener, OnClickListener, OnPictureSavedListener {

	private static final int REQUEST_PICK_IMAGE = 1;
	private GPUImageFilter mFilter;
	private FilterAdjuster mFilterAdjuster;
	private GPUImageView mGPUImageView;
	private LinearLayout imageLL;
	private int[] filterString = {R.string.text_filter_normal,R.string.text_filter_in1977,R.string.text_filter_amaro,
			R.string.text_filter_brannan, R.string.text_filter_early_bird,R.string.text_filter_hefe, R.string.text_filter_hudson,
			R.string.text_filter_inkwell,R.string.text_filter_lomofi, R.string.text_filter_lord_kelvin,
			R.string.text_filter_early_bird,R.string.text_filter_rise,R.string.text_filter_sierra,
			R.string.text_filter_sutro,R.string.text_filter_toaster,R.string.text_filter_valencia,
			R.string.text_filter_walden,R.string.text_filter_xproii};
//			"Contrast", "Brightness", "Sepia", "Vignette", "ToneCurve",
//			"Lookup (Amatorka)" };
	private int[] images = { R.drawable.filter_normal,
			R.drawable.filter_in1977, R.drawable.filter_amaro,
			R.drawable.filter_brannan, R.drawable.filter_early_bird,
			R.drawable.filter_hefe, R.drawable.filter_hudson,
			R.drawable.filter_inkwell, R.drawable.filter_lomofi,
			R.drawable.filter_lord_kelvin, R.drawable.filter_nashville,
			R.drawable.filter_rise, R.drawable.filter_sierra,
			R.drawable.filter_sutro, R.drawable.filter_toaster,
			R.drawable.filter_valencia, R.drawable.filter_walden,
			R.drawable.filter_xproii };
	private  FilterList filters = new FilterList();
	private List<ImageView> selectList = new ArrayList<ImageView>();

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gallery);
		
		initView();
		initData();
		
		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
		photoPickerIntent.setType("image/*");
		startActivityForResult(photoPickerIntent, REQUEST_PICK_IMAGE);
		
		
	}
	
	private void initView(){
		((SeekBar) findViewById(R.id.seekBar)).setOnSeekBarChangeListener(this);
		findViewById(R.id.button_choose_filter).setOnClickListener(this);
		findViewById(R.id.button_save).setOnClickListener(this);

		mGPUImageView = (GPUImageView) findViewById(R.id.gpuimage);
		imageLL = (LinearLayout) findViewById(R.id.images_layout);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		int width = displayMetrics.widthPixels;
		
		imageLL.removeAllViews();
		for (int i = 0; i < images.length; i++) {
			View view = LayoutInflater.from(this).inflate(R.layout.item_image,
					null);
			RoundImageView imageview = (RoundImageView) view
					.findViewById(R.id.image_iv);
			TextView textView = (TextView) view.findViewById(R.id.image_tv);
			ImageView selectIV = (ImageView) view
					.findViewById(R.id.filter_select);
			selectList.add(selectIV);
			imageview.setImageDrawable(getResources().getDrawable(images[i]));
			textView.setText(getResources().getString(filterString[i]));
			view.setOnClickListener(new ImageItemClick(i));
			imageLL.addView(view);

		}
		
		selectList.get(0).setVisibility(View.VISIBLE);
		
		
	}
	
	private void initData(){
		filters.addFilter("default", FilterType.I_1977);
	    filters.addFilter("1977", FilterType.I_1977);
	    filters.addFilter("Amaro", FilterType.I_AMARO);
	    filters.addFilter("Brannan", FilterType.I_BRANNAN);
	    filters.addFilter("Earlybird", FilterType.I_EARLYBIRD);
	    filters.addFilter("Hefe", FilterType.I_HEFE);
	    filters.addFilter("Hudson", FilterType.I_HUDSON);
	    filters.addFilter("Inkwell", FilterType.I_INKWELL);
	    filters.addFilter("Lomo", FilterType.I_LOMO);
	    filters.addFilter("LordKelvin", FilterType.I_LORDKELVIN);
	    filters.addFilter("Nashville", FilterType.I_NASHVILLE);
	    filters.addFilter("Rise", FilterType.I_NASHVILLE);
	    filters.addFilter("Sierra", FilterType.I_SIERRA);
	    filters.addFilter("sutro", FilterType.I_SUTRO);
	    filters.addFilter("Toaster", FilterType.I_TOASTER);
	    filters.addFilter("Valencia", FilterType.I_VALENCIA);
	    filters.addFilter("Walden", FilterType.I_WALDEN);
	    filters.addFilter("Xproll", FilterType.I_XPROII);
	    filters.addFilter("Contrast", FilterType.CONTRAST);
	    filters.addFilter("Brightness", FilterType.BRIGHTNESS);
	    filters.addFilter("Sepia", FilterType.SEPIA);
	    filters.addFilter("Vignette", FilterType.VIGNETTE);
	    filters.addFilter("ToneCurve", FilterType.TONE_CURVE);
	    filters.addFilter("Lookup (Amatorka)", FilterType.LOOKUP_AMATORKA);
	}
	
	
	public class ImageItemClick implements OnClickListener{
		int clickPostion;
		public ImageItemClick(int postion) {
			clickPostion = postion;
		}
		
		
		@Override
		public void onClick(View v) {
			if (clickPostion == 0) {
				switchFilterTo(new GPUImageFilter());
			} else {
				GPUImageFilter filter = GPUImageFilterTools
						.createFilterForType(ActivityGallery.this,
								filters.filters.get(clickPostion));
				switchFilterTo(filter);
			}
			for (int i = 0; i < selectList.size(); i++) {
				if (i == clickPostion) {
					selectList.get(i).setVisibility(View.VISIBLE);
				} else {
					selectList.get(i).setVisibility(View.INVISIBLE);
				}
			}

			mGPUImageView.requestRender();
		}
		
	}

	@Override
	protected void onActivityResult(final int requestCode,
			final int resultCode, final Intent data) {
		switch (requestCode) {
		case REQUEST_PICK_IMAGE:
			if (resultCode == RESULT_OK) {
				handleImage(data.getData());
			} else {
				finish();
			}

			break;

		default:
			super.onActivityResult(requestCode, resultCode, data);
			break;
		}
	}

	@Override
	public void onClick(final View v) {
		switch (v.getId()) {
		case R.id.button_choose_filter:
			GPUImageFilterTools.showDialog(this,
					new OnGpuImageFilterChosenListener() {

						@Override
						public void onGpuImageFilterChosenListener(
								final GPUImageFilter filter) {
							switchFilterTo(filter);
							mGPUImageView.requestRender();
						}

					});
			break;
		case R.id.button_save:
			saveImage();
			break;

		default:
			break;
		}

	}

	@Override
	public void onPictureSaved(final Uri uri) {
		Toast.makeText(this, "Saved: " + uri.toString(), Toast.LENGTH_SHORT)
				.show();
	}

	private void saveImage() {
		String fileName = System.currentTimeMillis() + ".jpg";
		mGPUImageView.saveToPictures("GPUImage", fileName, (jp.co.cyberagent.android.gpuimage.GPUImageView.OnPictureSavedListener) this);
	}

	private void switchFilterTo(final GPUImageFilter filter) {
		if (mFilter == null
				|| (filter != null && !mFilter.getClass().equals(
						filter.getClass()))) {
			mFilter = filter;
			mGPUImageView.setFilter(mFilter);
			mFilterAdjuster = new FilterAdjuster(mFilter);
		}
	}

	@Override
	public void onProgressChanged(final SeekBar seekBar, final int progress,
			final boolean fromUser) {
		if (mFilterAdjuster != null) {
			mFilterAdjuster.adjust(progress);
		}
		mGPUImageView.requestRender();
	}

	@Override
	public void onStartTrackingTouch(final SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(final SeekBar seekBar) {
	}

	private void handleImage(final Uri selectedImage) {
		try {
			Bitmap bitmap = MediaStore.Images.Media.getBitmap(
					getContentResolver(), selectedImage);
			float width = bitmap.getWidth();
			float height = bitmap.getHeight();
			float ratio = width / height;
			mGPUImageView.setRatio(ratio);
			mGPUImageView.setImage(selectedImage);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
