package nl.frankkie.ventilatorapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by fbouwens on 02-07-15.
 */
public class FanView extends View {

    Context context;
    Bitmap imageToRotate;
    Paint paint = new Paint();
    public float rotateSpeed = 0;
    float currentRotation = 0;
    int drawLoopRunning = 0;
    boolean visible = true;
    long lastFpsTime = 0;
    int showFps = 0;
    int fps = 0;
    Handler handler = new Handler();

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        if (imageToRotate == null) {
            imageToRotate = BitmapFactory.decodeResource(context.getResources(), R.drawable.tempimage);
        }

        int centerX = canvas.getWidth() / 2;
        int centerY = canvas.getHeight() / 2;

        int drawX = centerX - imageToRotate.getWidth()/2;
        int drawY = centerY - imageToRotate.getHeight() /2;

        currentRotation += rotateSpeed;
        currentRotation %= 360;
        canvas.rotate(currentRotation, centerX, centerY);
        canvas.drawBitmap(imageToRotate, 0, 0, paint);
        canvas.rotate(-currentRotation, centerX, centerY);

        if (drawLoopRunning <= 0) {
            if (visible) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        drawLoop();
                    }
                });
                t.start();
            }
        }
    }


    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        if (visibility == VISIBLE) {
            visible = true;
            invalidate();
        } else {
            visible = false;
        }
    }

    void drawLoop() {
        long lastLoopTime = System.nanoTime();
        final int TARGET_FPS = 30;
        final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;
        drawLoopRunning++;
        while (visible) {
            long now = System.nanoTime();
            long updateLength = now - lastLoopTime;
            lastLoopTime = now;
            double delta = updateLength / ((double) OPTIMAL_TIME);
            // update the frame counter
            lastFpsTime += updateLength;
            fps++;

            // update our FPS counter if a second has passed since
            // we last recorded
            if (lastFpsTime >= 1000000000) {
                //System.out.println("(FPS: " + fps + ")");
                lastFpsTime = 0;
                showFps = fps;
                fps = 0;
            }
            //Draw
            handler.post(new Runnable() {
                @Override
                public void run() {
                    draw();
                }
            });

            try {
                Thread.sleep((lastLoopTime - System.nanoTime() + OPTIMAL_TIME) / 1000000);
            } catch (Exception e) {
                //ignore
            }

        }
        drawLoopRunning--;
    }

    public void draw() {
        invalidate();
    }

    /**
     * Simple constructor to use when creating a view from code.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     */
    public FanView(Context context) {
        super(context);
        this.context = context;
    }

    /**
     * Constructor that is called when inflating a view from XML. This is called
     * when a view is being constructed from an XML file, supplying attributes
     * that were specified in the XML file. This version uses a default style of
     * 0, so the only attribute values applied are those in the Context's Theme
     * and the given AttributeSet.
     * <p/>
     * <p/>
     * The method onFinishInflate() will be called after all children have been
     * added.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     * @param attrs   The attributes of the XML tag that is inflating the view.
     */
    public FanView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    /**
     * Perform inflation from XML and apply a class-specific base style from a
     * theme attribute. This constructor of View allows subclasses to use their
     * own base style when they are inflating. For example, a Button class's
     * constructor would call this version of the super class constructor and
     * supply <code>R.attr.buttonStyle</code> for <var>defStyleAttr</var>; this
     * allows the theme's button style to modify all of the base view attributes
     * (in particular its background) as well as the Button class's attributes.
     *
     * @param context      The Context the view is running in, through which it can
     *                     access the current theme, resources, etc.
     * @param attrs        The attributes of the XML tag that is inflating the view.
     * @param defStyleAttr An attribute in the current theme that contains a
     *                     reference to a style resource that supplies default values for
     *                     the view. Can be 0 to not look for defaults.
     */
    public FanView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

}
