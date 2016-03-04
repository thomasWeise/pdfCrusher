package thomasWeise.pdfCrusher.tools.ghostScript;

import org.optimizationBenchmarking.utils.tools.impl.process.ExternalProcessExecutor;

import thomasWeise.pdfCrusher.tools.PdfCheckerJob;
import thomasWeise.pdfCrusher.tools.PdfCheckerJobBuilder;
import thomasWeise.pdfCrusher.tools.PdfCheckerTool;

/** The GhostScript-based pdf checker */
public final class GhostScriptChecker extends PdfCheckerTool {

  /** create the GhostScript checker */
  GhostScriptChecker() {
    super();
  }

  /** {@inheritDoc} */
  @Override
  public final boolean canUse() {
    return (ExternalProcessExecutor.getInstance().canUse()
        && (_GhostScript.GHOST_SCRIPT_PATH != null));
  }

  /** {@inheritDoc} */
  @Override
  public final String toString() {
    return "GhostScript-based Checker"; //$NON-NLS-1$
  }

  /** {@inheritDoc} */
  @Override
  public final void checkCanUse() {
    ExternalProcessExecutor.getInstance().checkCanUse();
    if (_GhostScript.GHOST_SCRIPT_PATH == null) {
      throw new IllegalStateException("No GhostScript binary found.");//$NON-NLS-1$
    }
    super.checkCanUse();
  }

  /** {@inheritDoc} */
  @Override
  protected final PdfCheckerJob create(
      final PdfCheckerJobBuilder builder) {
    return new _GhostScriptCheckerJob(builder);
  }

  /**
   * Get the globally shared instance of the GhostScript-based Pdf Checker
   *
   * @return the globally shared instance of the GhostScript-based pdf
   *         checker
   */
  public static final GhostScriptChecker getInstance() {
    return __PdfTKCheckerHolder.INSTANCE;
  }

  /** the pdf checker holder. */
  private static final class __PdfTKCheckerHolder {
    /** the shared instance of the GhostScript-based PDF checker */
    static final GhostScriptChecker INSTANCE = new GhostScriptChecker();
  }
}
