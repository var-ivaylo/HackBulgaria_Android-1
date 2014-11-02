package com.hackbulgaria.androidcourse.week1.puzzlegame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.activity_splash);

		GridLayout gameLayout = (GridLayout) findViewById(R.id.game);

		int totalPieces = 16;
		int columns = 4;
		int rows = totalPieces / columns;
		
		gameLayout.setRowCount(rows);
		gameLayout.setColumnCount(columns);		
		
		final List<Drawable> images = this.getImages();
		final List<Drawable> shuffledImages = this.shuffleImages(images);
		
		Point screenSize = new Point();
		this.getWindowManager().getDefaultDisplay().getSize(screenSize);
		
		for (int i = 0, col = 0, row = 0; i < totalPieces; i++, col++) {
			if (col == columns) {
				col = 0;
				row++;
			}
			
			final ImageView piece = new ImageView(this);
			piece.setAdjustViewBounds(true);
			piece.setScaleType(ImageView.ScaleType.FIT_XY);
			piece.setImageDrawable(shuffledImages.get(i));
			
			GridLayout.LayoutParams params = new GridLayout.LayoutParams();
			params.height = screenSize.y / rows;
			params.width = screenSize.x / columns;
			params.columnSpec = GridLayout.spec(col);
			params.rowSpec = GridLayout.spec(row);
			
			piece.setLayoutParams(params);		
			
			piece.setOnTouchListener(new View.OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					View.DragShadowBuilder builder = new DragShadowBuilder(v);
					v.startDrag(null, builder, v, 0);
					
					return true;
				}
			});
			
			piece.setOnDragListener(new View.OnDragListener() {
				
				@Override
				public boolean onDrag(View v, DragEvent event) {
					if (event.getAction() == DragEvent.ACTION_DROP) {
	                    final ImageView dest = (ImageView) v;
	                    final ImageView src = (ImageView) event.getLocalState();
	                    
						int destIndex = shuffledImages.indexOf(dest.getDrawable());
						int srcIndex = shuffledImages.indexOf(src.getDrawable());
						
						shuffledImages.set(destIndex, src.getDrawable());
						shuffledImages.set(srcIndex, dest.getDrawable());
						
						dest.setImageDrawable(shuffledImages.get(destIndex));
						src.setImageDrawable(shuffledImages.get(srcIndex));
	                    
						if (won(images, shuffledImages)) {
							Toast.makeText(getApplicationContext(), "You won!", Toast.LENGTH_LONG).show();
						}
					}
					
					return true;
				}
			});
			
			gameLayout.addView(piece);
		}
	}
	
	private List<Drawable> getImages() {
		TypedArray pieces = this.getResources().obtainTypedArray(R.array.pieces);
		List<Drawable> piecesDrawables = new ArrayList<Drawable>();
		
		for (int i = 0; i < pieces.length(); i++) {
			piecesDrawables.add(pieces.getDrawable(i));
		}
		
		pieces.recycle();
		
		return piecesDrawables;
	}
	
	private List<Drawable> shuffleImages(List<Drawable> images) {
		List<Drawable> imagesShuffled = new ArrayList<Drawable>(images);
		Collections.shuffle(imagesShuffled);
		return imagesShuffled;
	}
	
	private boolean won(List<Drawable> images, List<Drawable> currentImages) {
		return images.equals(currentImages);
	}
}
