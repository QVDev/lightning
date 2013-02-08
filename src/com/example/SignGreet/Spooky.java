package com.example.SignGreet;

import android.*;
import android.R;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;

import java.util.Random;

public class Spooky
{
    int x = 10;
    int y = 10;

    private int xVelocity = 3;
    private int yVelocity = 3;
    private final int width;
    private final int height;

    private final BitmapDrawable spookyBitmap;

    private Paint paint;

    private Paint paintEnergyStroke;
    private Paint paintEnergyFill;


    public Spooky(int width, int height, GameSurface gameSurface)
    {
        this.width = width;
        this.height = height;

        spookyBitmap = (BitmapDrawable) gameSurface.getResources().getDrawable(R.drawable.ic_dialog_alert);

        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(5.0f);

        setEnergyPaintStroke();
        setEnergyPaintFill();
    }

    private void setEnergyPaintStroke()
    {
        paintEnergyStroke = new Paint();
        paintEnergyStroke.setStrokeWidth(3);
        paintEnergyStroke.setStyle(Paint.Style.STROKE);
        paintEnergyStroke.setColor(Color.RED);
    }

    private void setEnergyPaintFill()
    {
        paintEnergyFill = new Paint();
        paintEnergyFill.setStrokeWidth(3);
        paintEnergyFill.setStyle(Paint.Style.FILL);
        paintEnergyFill.setColor(Color.GREEN);
    }

    public void drawEnergy(Canvas canvas)
    {
        int width = canvas.getWidth();//100%
        int fullBar = 100;//Max Energy
        int percentage = width/fullBar;//1%

        int remaining = (int) (100*percentage);
        int full = fullBar*percentage;

        canvas.drawRect(33, 33, full, 33+60, paintEnergyStroke);
        canvas.drawRect(33, 33, remaining, 33+60, paintEnergyFill);
    }

    public void updateSpooky(Canvas canvas)
    {
        if (x < 0 && y < 0)
        {
            x = width / 2;
            y = height / 2;
        } else
        {
            x += xVelocity;
            y += yVelocity;
            if ((x > width - spookyBitmap.getBitmap().getWidth()) || (x < 0))
            {
                xVelocity = xVelocity * -1;
            }
            if ((y > height - spookyBitmap.getBitmap().getHeight()) || (y < 0))
            {
                yVelocity = yVelocity * -1;
            }
        }

//        canvas.drawBitmap(spookyBitmap.getBitmap(), x, y, null); spot
        canvas.drawCircle(x,y,40,paint);
    }

    static double data[];  /* Array to store sample */
    static double delta[]; /* Array to hold new variances */
    static double initialVariance = 80;     /* initial variance */
    private int iterations = 10;
    private double hurst = 0.8;

    Random rand = new Random();  /* Random number generator*/

    public void updateBrownian(Canvas canvas, int x, int y)
    {
        Generate(iterations,initialVariance, hurst);

        int i, x1, x2, y1, y2;
        double maxval, minval, facw, fach;
        /* Responsible for canvas where curve is drawn */
        int h, w;
        w=canvas.getWidth()-1;h=canvas.getHeight() - 51;
        //canvas.drawLine(0, h / 2 + 51, w, h / 2 + 51, paint);
//        canvas.setColor(Color.BLUE);
        // Locate maximum and minimum values of sample
        maxval= data[data.length-1]; minval= data[0];
        for (i=1; i < data.length; i++) {
            if (maxval < data[i]) maxval= data[i];
            if (minval > data[i]) minval= data[i];
        }
        //Calculate scaling factors
        facw=new Double(w).doubleValue()/
                new Double(data.length).doubleValue();
        fach=new Double(h).doubleValue()/
                new Double(Math.abs(maxval-minval)).doubleValue();
        //Rescale and draw
        x1=0; y1=0;

        for (i=1; i < data.length; i++) {
            x2=x + new Double(facw*i).intValue();
            y2=y+h/2-new Double(facw* data[i]).intValue();
            if (i > 1) canvas.drawLine(x1, y1, x2, y2, paint);
            x1=x2; y1=y2;
        }
    }

    public void Generate(int n, double v, double hurst) {
        int maxpos;  // Number of samples to generate
        Double d;
        initialVariance =v;       // Gets the initial variance
        d = ( new Double (Math.pow(2,n)) );
        maxpos=d.intValue();
        delta = new double[maxpos+1];
       /* initializes array for new variances */
        for(int i = 1; i<n; i++){
            delta[i] = initialVariance*Math.pow(0.5, i*hurst)*Math.sqrt(1 - Math.pow(2,2*hurst-2));
        }
       /* Creates variances for sample data */
        data = new double[maxpos+1];
        data[0]=0;
        data[maxpos]= initialVariance *rand.nextGaussian();
       /* initializes array for sample */
        Divide(data, 0, maxpos, 1, n);
       /* Split and displace midpoint n times */
    }


    private void Divide(double X[], int a, int b, int level, int n) {
        int c;  //Midpoint position in data
        c=(a+b)/2;
        X[c]=(X[a]+X[b])/2 + delta[level]*rand.nextGaussian();
        // Midpoint gets displaced
        if (level < n) {
            Divide(X, a, c, level+1, n);
            Divide(X, c, b, level+1, n);
        }
    }/*Divide*/

}
