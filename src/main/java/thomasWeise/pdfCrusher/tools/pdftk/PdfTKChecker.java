package thomasWeise.pdfCrusher.tools.pdftk;

import org.optimizationBenchmarking.utils.tools.impl.process.ExternalProcessExecutor;

import thomasWeise.pdfCrusher.tools.PdfCheckerJob;
import thomasWeise.pdfCrusher.tools.PdfCheckerJobBuilder;
import thomasWeise.pdfCrusher.tools.PdfCheckerTool;

/** The PdfTk-based pdf checker */
public final class PdfTKChecker extends PdfCheckerTool {

  /** create the PdfTk checker */
  PdfTKChecker() {
    super();
  }

  /** {@inheritDoc} */
  @Override
  public final boolean canUse() {
    return (ExternalProcessExecutor.getInstance().canUse()
        && (_PdfTK.PDFTK_PATH != null));
  }

  /** {@inheritDoc} */
  @Override
  public final String toString() {
    return "PdfTk-based Checker"; //$NON-NLS-1$
  }

  /** {@inheritDoc} */
  @Override
  public final void checkCanUse() {
    ExternalProcessExecutor.getInstance().checkCanUse();
    if (_PdfTK.PDFTK_PATH == null) {
      throw new IllegalStateException("No PdfTk binary found.");//$NON-NLS-1$
    }
    super.checkCanUse();
  }

  /** {@inheritDoc} */
  @Override
  protected final PdfCheckerJob create(
      final PdfCheckerJobBuilder builder) {
    return new _PdfTKCheckerJob(builder);
  }

  /**
   * Get the globally shared instance of the PdfTk-based Pdf Checker
   *
   * @return the globally shared instance of the PdfTk-based pdf checker
   */
  public static final PdfTKChecker getInstance() {
    return __PdfTKCheckerHolder.INSTANCE;
  }

  /** the pdf checker holder. */
  private static final class __PdfTKCheckerHolder {
    /** the shared instance of the PdfTk-based PDF checker */
    static final PdfTKChecker INSTANCE = new PdfTKChecker();
  }
}
