package thomasWeise.pdfCrusher.tools;

import org.optimizationBenchmarking.utils.tools.impl.abstr.Tool;

/** The PDF checker tool base class. */
public abstract class PdfCheckerTool extends Tool {

  /** create */
  protected PdfCheckerTool() {
    super();
  }

  /** {@inheritDoc} */
  @Override
  public final PdfCheckerJobBuilder use() {
    return new PdfCheckerJobBuilder(this);
  }

  /**
   * create the job.
   *
   * @param builder
   *          the builder
   * @return the job
   */
  protected abstract PdfCheckerJob create(
      final PdfCheckerJobBuilder builder);
}
