package com.example.surfaceviewtest;

import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

public class SurfaceViewTest extends Activity {

	FastRenderView renderView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		renderView = new FastRenderView(this);
		setContentView(renderView);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		renderView.resume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		renderView.pause();
	}

	class FastRenderView extends SurfaceView implements Runnable {

		Thread renderThread = null;
		SurfaceHolder holder;
		volatile boolean running = false;
		Random random = new Random(255);

		public FastRenderView(Context context) {
			super(context);
			holder = getHolder();
		}

		@Override
		public void run() {
			while (running) {
				if (!holder.getSurface().isValid()) {
					continue;
				}
				Canvas canvas = holder.lockCanvas();
				canvas.drawRGB(random.nextInt(), random.nextInt(), random.nextInt());
				holder.unlockCanvasAndPost(canvas);
			}
		}

		private void resume() {
			running = true;
			renderThread = new Thread(this);
			renderThread.start();
		}

		private void pause() {
			running = false;
			while (true) {
				try {
					renderThread.join();
					return;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
