package thomasWeise.pdfCrusher.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;

/** A buffered image. */
public final class Image {

  /** the width in pixel */
  private static final int WIDTH = 1024;
  /** the width in pixel */
  private static final int HEIGHT = 768;

  /** the image */
  private final BufferedImage m_image;

  /** create the image buffer */
  Image() {
    super();
    this.m_image = new BufferedImage(Image.WIDTH, Image.HEIGHT,
        BufferedImage.TYPE_INT_RGB);
  }

  /**
   * Create a graphic to write to.
   *
   * @return the graphic to write to
   */
  public final Graphics2D createGraphic() {
    final Graphics2D graphics;
    final Color color;

    graphics = this.m_image.createGraphics();
    color = graphics.getColor();
    graphics.setColor(Color.white);
    graphics.fillRect(0, 0, Image.WIDTH, Image.HEIGHT);
    graphics.setColor(color);

    return graphics;
  }

  /**
   * Create a graphic to write to.
   *
   * @param dims
   *          the dimensions
   * @return the graphic to write to
   */
  public final Graphics2D createGraphic(final Dimension2D dims) {
    final Graphics2D graphics;
    double width, height, temp;

    graphics = this.m_image.createGraphics();
    width = dims.getWidth();
    if (width <= 0d) {
      throw new IllegalArgumentException(//
          "Width must not be less or equal to 0, but is " + width); //$NON-NLS-1$
    }
    height = dims.getHeight();
    if (height <= 0d) {
      throw new IllegalArgumentException(//
          "Height must not be less or equal to 0, but is " + height); //$NON-NLS-1$
    }

    if ((width > height) ^ (Image.WIDTH > Image.HEIGHT)) {
      graphics.rotate(Math.PI * 0.5d);
      temp = width;
      width = height;
      height = temp;
    }

    graphics.scale((width / Image.WIDTH), (height / Image.HEIGHT));

    return graphics;
  }

  /**
   * Get the shared image constant of this thread
   *
   * @return the image buffer of this thread
   */
  public static final Image get() {
    return __GetImage.GET.get();
  }

  /** The image getter */
  private static final class __GetImage extends ThreadLocal<Image> {

    /** the image getter */
    static final __GetImage GET = new __GetImage();

    /** create */
    __GetImage() {
      super();
    }

    /** {@inheritDoc} */
    @Override
    protected Image initialValue() {
      return new Image();
    }

  }
}
