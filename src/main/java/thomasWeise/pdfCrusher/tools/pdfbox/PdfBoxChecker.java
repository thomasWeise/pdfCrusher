package thomasWeise.pdfCrusher.tools.pdfbox;

import thomasWeise.pdfCrusher.tools.PdfCheckerJob;
import thomasWeise.pdfCrusher.tools.PdfCheckerJobBuilder;
import thomasWeise.pdfCrusher.tools.PdfCheckerTool;

/** The pdfbox-based pdf checker */
public final class PdfBoxChecker extends PdfCheckerTool {

  /** create the Pdf box checker */
  PdfBoxChecker() {
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
    return "PdfBox-based Checker"; //$NON-NLS-1$
  }

  /** {@inheritDoc} */
  @Override
  protected final PdfCheckerJob create(
      final PdfCheckerJobBuilder builder) {
    return new _PdfBoxCheckerJob(builder);
  }

  /**
   * Get the globally shared instance of the PdfBox-based Pdf Checker
   *
   * @return the globally shared instance of the PdfBox-based pdf checker
   */
  public static final PdfBoxChecker getInstance() {
    return __PdfBoxCheckerHolder.INSTANCE;
  }

  /** the pdf checker holder. */
  private static final class __PdfBoxCheckerHolder {
    /** the shared instance of the PdfBox-based PDF checker */
    static final PdfBoxChecker INSTANCE = new PdfBoxChecker();
  }
}
