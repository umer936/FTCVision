package org.lasarobotics.vision.detection.objects;

import org.lasarobotics.vision.image.Drawing;
import org.lasarobotics.vision.util.MathUtil;
import org.lasarobotics.vision.util.color.Color;
import org.lasarobotics.vision.util.color.ColorRGBA;
import org.lasarobotics.vision.util.color.ColorSpace;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;

/**
 * A detectable object
 */
public abstract class Detectable {
    public abstract double left();
    public abstract double right();
    public abstract double top();
    public abstract double bottom();

    public abstract double width();
    public abstract double height();

    public Point topLeft()
    {
        return new Point(left(), top());
    }
    public Point bottomRight()
    {
        return new Point(right(), bottom());
    }

    /**
     * Gets the average color of the contour
     * @param img The image matrix, of any color size
     * @param imgSpace The image's color space
     * @return The average color of the region
     */
    public Color averageColor(Mat img, ColorSpace imgSpace, Mat cimg)
    {
        //Coerce values to stay within screen dimensions
        double leftX = MathUtil.coerce(0, img.cols() - 1, left());
        double rightX = MathUtil.coerce(0, img.cols() - 1, right());

        double topY = MathUtil.coerce(0, img.rows() - 1, top());
        double bottomY = MathUtil.coerce(0, img.rows() - 1, bottom());

        //Input points into array for calculation
        //TODO rectangular submatrix-basad calculation isn't perfectly accurate when you have ellipses or weird shapes
        Mat subMat = img.submat((int)topY, (int)bottomY, (int)leftX, (int)rightX);

        //DEBUG
        Point topLeft = new Point(leftX, topY);
        Point bottomRight = new Point(rightX, bottomY);
        Drawing.drawRectangle(cimg, topLeft, bottomRight, new ColorRGBA(255, 80, 255, 255), 2);

        //Calculate average and return new color instance
        return Color.create(Core.mean(subMat), imgSpace);
    }
}
