package thomasWeise.pdfCrusher;

import thomasWeise.pdfCrusher.tools.PdfCheckerJob;
import thomasWeise.pdfCrusher.tools.PdfCheckerJobBuilder;
import thomasWeise.pdfCrusher.tools.PdfCheckerTool;

/** The PDF checker tool. */
public final class PdfChecker extends PdfCheckerTool {

  /** create the Pdf checker */
  PdfChecker() {
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
    return "Pdf Checker"; //$NON-NLS-1$
  }

  /** {@inheritDoc} */
  @Override
  protected final PdfCheckerJob create(
      final PdfCheckerJobBuilder builder) {
    return new _PdfCheckerJob(builder);
  }

  /**
   * Get the globally shared instance of the Pdf Checker
   *
   * @return the globally shared instance of the Pdf checker
   */
  public static final PdfChecker getInstance() {
    return __PdfCheckerHolder.INSTANCE;
  }

  /** the pdf checker holder. */
  private static final class __PdfCheckerHolder {
    /** the shared instance of the PDF checker */
    static final PdfChecker INSTANCE = new PdfChecker();
  }
}
