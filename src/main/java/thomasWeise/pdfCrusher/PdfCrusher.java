package thomasWeise.pdfCrusher;

import org.optimizationBenchmarking.utils.tools.impl.abstr.Tool;

/** The PDF crusher tool. */
public final class PdfCrusher extends Tool {

  /** create the Pdf checker */
  PdfCrusher() {
    super();
  }

  /** {@inheritDoc} */
  @Override
  public final boolean canUse() {
    return true;
  }

  /** {@inheritDoc} */
  @Override
  public final String toString() {
    return "Pdf Crusher"; //$NON-NLS-1$
  }

  /** {@inheritDoc} */
  @Override
  public final PdfCrusherJobBuilder use() {
    return new PdfCrusherJobBuilder();
  }

  /**
   * Get the globally shared instance of the Pdf Crusher
   *
   * @return the globally shared instance of the Pdf Crusher
   */
  public static final PdfCrusher getInstance() {
    return __PdfCrusherHolder.INSTANCE;
  }

  /** the pdf crusher holder. */
  private static final class __PdfCrusherHolder {
    /** the shared instance of the PDF crusher */
    static final PdfCrusher INSTANCE = new PdfCrusher();
  }
}
