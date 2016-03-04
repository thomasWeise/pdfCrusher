package thomasWeise.pdfCrusher.documents;

import java.nio.file.Path;
import java.util.logging.Logger;

import thomasWeise.pdfCrusher.PdfChecker;
import thomasWeise.pdfCrusher.PdfCrusherJob;

/**
 * A pdf file document.
 */
public class PdfFile extends DocumentFile {

  /** the missing fonts */
  int m_missingFonts;

  /** the number of embedded fonts */
  int m_embeddedFonts;

  /**
   * Instantiate the PdfFile.
   *
   * @param f
   *          the file
   * @param owner
   *          the owning job
   */
  public PdfFile(final Path f, final PdfCrusherJob owner) {
    super(f, owner);
  }

  /** {@inheritDoc} */
  @Override
  final void _defFill() {
    super._defFill();
    this.m_missingFonts = Integer.MAX_VALUE;
    this.m_embeddedFonts = Integer.MIN_VALUE;
  }

  /** {@inheritDoc} */
  @Override
  int _init(final Path f, final Logger logger) {
    long errors;
    int tempErrors;

    errors = super._init(f, logger);
    if (errors >= Integer.MAX_VALUE) {
      return Integer.MAX_VALUE;
    }

    tempErrors = this._loadFonts();
    if (tempErrors > 0) {
      errors += tempErrors;
    }

    try {
      errors += PdfChecker.getInstance().use().setLogger(logger)//
          .setSourceFile(f).create().call().intValue();
    } catch (@SuppressWarnings("unused") final Throwable ignore) {
      return Integer.MAX_VALUE;
    }

    return Math.max(0, ((int) (Math.min(Integer.MAX_VALUE, errors))));
  }

  /**
   * Load the fonts
   *
   * @return the amount of errors encountered
   */
  int _loadFonts() {
    return 0;
  }

  /**
   * Get the number of missing fonts
   *
   * @return the number of missing fonts
   */
  public int getMissingFonts() {
    return this.m_missingFonts;
  }

  /**
   * Get the number of embedded fonts
   *
   * @return the number of embedded fonts
   */
  public int getEmbeddedFonts() {
    return this.m_missingFonts;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return (((((('{' + super.toString()) + ",embedded=") //$NON-NLS-1$
        + this.m_embeddedFonts) + ",missing=") //$NON-NLS-1$
        + this.m_missingFonts) + '}');
  }

  /**
   * perform the font comparison
   *
   * @param a
   *          the first pdf file
   * @param b
   *          the second pdf file
   * @return the result
   */
  private static final int _fontCompare(final PdfFile a, final PdfFile b) {
    final int am, bm, af, bf;

    am = a.m_missingFonts;
    af = (am + a.m_embeddedFonts);

    bm = b.m_missingFonts;
    bf = (bm + b.m_embeddedFonts);

    if (af <= 0) {
      if (bf > 0) {
        return 1;
      }
      return 0;
    }
    if (bf <= 0) {
      return (-1);
    }

    return ((am < bm) ? (-1) : ((am > bm) ? 1 : 0));
  }

  /** {@inheritDoc} */
  @Override
  public int compareTo(final DocumentFile o) {
    final PdfFile x;
    int i;

    if (o == this) {
      return 0;
    }
    if (o == null) {
      return -1;
    }

    if (this.equals(o)) {
      return 0;
    }

    if (o instanceof PdfFile) {
      x = ((PdfFile) o);

      i = Integer.compare(this.getErrors(), x.getErrors());
      if (i != 0) {
        return i;
      }

      i = PdfFile._fontCompare(this, x);
      if (i != 0) {
        return i;
      }

      i = DocumentFile._sizeCompare(this, x);
      if (i != 0) {
        return i;
      }

      return Integer.compare(this.m_embeddedFonts, x.m_embeddedFonts);
    }

    return super.compareTo(o);
  }

  /**
   * Is this a root pdf file?
   *
   * @return {@code true} if this is a root pdf file, {@code false}
   *         otherwise
   */
  public boolean isRoot() {
    return false;
  }

  /** {@inheritDoc} */
  @Override
  public synchronized void release() {
    if (!(this.isRoot())) {
      super.release();
    }
  }

  /**
   * perform a pareto comparison
   *
   * @param a
   *          the first file
   * @param b
   *          the second file
   * @return the result
   */
  public static final EParetoResult paretoCompare(final PdfFile a,
      final PdfFile b) {
    final int resErrors, resFonts, resSize;

    if (a == b) {
      return EParetoResult.SAME;
    }

    if (a == null) {
      return EParetoResult.WORSE;
    }

    if (b == null) {
      return EParetoResult.BETTER;
    }

    if (a.equals(b)) {
      return EParetoResult.SAME;
    }

    resErrors = Integer.compare(a.getErrors(), b.getErrors());
    resFonts = PdfFile._fontCompare(a, b);
    resSize = DocumentFile._sizeCompare(a, b);

    if ((resErrors <= 0) && (resFonts <= 0) && (resSize <= 0)) {
      if ((resErrors == 0) && (resFonts == 0) && (resSize == 0)) {
        return EParetoResult.EQUAL;
      }
      return EParetoResult.BETTER;
    }

    if ((resErrors >= 0) && (resFonts >= 0) && (resSize >= 0)) {
      return EParetoResult.WORSE;
    }

    return EParetoResult.NEITHER;
  }
}
