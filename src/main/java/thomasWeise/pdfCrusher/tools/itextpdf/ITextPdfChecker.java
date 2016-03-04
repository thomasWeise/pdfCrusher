package thomasWeise.pdfCrusher.tools.itextpdf;

import thomasWeise.pdfCrusher.tools.PdfCheckerJob;
import thomasWeise.pdfCrusher.tools.PdfCheckerJobBuilder;
import thomasWeise.pdfCrusher.tools.PdfCheckerTool;

/** The itextpdf-based pdf checker */
public final class ITextPdfChecker extends PdfCheckerTool {

  /** create the Pdf box checker */
  ITextPdfChecker() {
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
    return "ITextPdf-based Checker"; //$NON-NLS-1$
  }

  /** {@inheritDoc} */
  @Override
  protected final PdfCheckerJob create(
      final PdfCheckerJobBuilder builder) {
    return new _ITextPdfCheckerJob(builder);
  }

  /**
   * Get the globally shared instance of the itextpdf-based Pdf Checker
   *
   * @return the globally shared instance of the itextpdf-based pdf checker
   */
  public static final ITextPdfChecker getInstance() {
    return __PdfBoxCheckerHolder.INSTANCE;
  }

  /** the pdf checker holder. */
  private static final class __PdfBoxCheckerHolder {
    /** the shared instance of the itextpdf-based PDF checker */
    static final ITextPdfChecker INSTANCE = new ITextPdfChecker();
  }
}
