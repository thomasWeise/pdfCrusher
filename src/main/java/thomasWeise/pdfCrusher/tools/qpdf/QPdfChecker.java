package thomasWeise.pdfCrusher.tools.qpdf;

import org.optimizationBenchmarking.utils.tools.impl.process.ExternalProcessExecutor;

import thomasWeise.pdfCrusher.tools.PdfCheckerJob;
import thomasWeise.pdfCrusher.tools.PdfCheckerJobBuilder;
import thomasWeise.pdfCrusher.tools.PdfCheckerTool;

/** The qpdf-based checker */
public final class QPdfChecker extends PdfCheckerTool {

  /** create the qpdf box checker */
  QPdfChecker() {
    super();
  }

  /** {@inheritDoc} */
  @Override
  public final boolean canUse() {
    return (ExternalProcessExecutor.getInstance().canUse()
        && (_QPdf.QPDF_PATH != null));
  }

  /** {@inheritDoc} */
  @Override
  public final String toString() {
    return "qpdf-based Checker"; //$NON-NLS-1$
  }

  /** {@inheritDoc} */
  @Override
  public final void checkCanUse() {
    ExternalProcessExecutor.getInstance().checkCanUse();
    if (_QPdf.QPDF_PATH == null) {
      throw new IllegalStateException("No qpdf binary found.");//$NON-NLS-1$
    }
    super.checkCanUse();
  }

  /** {@inheritDoc} */
  @Override
  protected final PdfCheckerJob create(
      final PdfCheckerJobBuilder builder) {
    return new _QPdfCheckerJob(builder);
  }

  /**
   * Get the globally shared instance of the qpdf-based Pdf Checker
   *
   * @return the globally shared instance of the qpdf-based pdf checker
   */
  public static final QPdfChecker getInstance() {
    return __QPdfCheckerHolder.INSTANCE;
  }

  /** the pdf checker holder. */
  private static final class __QPdfCheckerHolder {
    /** the shared instance of the qpdf-based PDF checker */
    static final QPdfChecker INSTANCE = new QPdfChecker();
  }
}
