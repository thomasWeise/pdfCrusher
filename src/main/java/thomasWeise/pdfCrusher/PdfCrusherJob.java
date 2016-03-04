package thomasWeise.pdfCrusher;

import java.util.logging.Logger;

import thomasWeise.pdfCrusher.documents.PdfFile;
import thomasWeise.pdfCrusher.tools.FileJob;

/** The main PDF crusher job. */
public final class PdfCrusherJob extends FileJob implements Runnable {

  /**
   * Create the job
   *
   * @param builder
   *          the job builder
   */
  PdfCrusherJob(final PdfCrusherJobBuilder builder) {
    super(builder);
  }

  /** {@inheritDoc} */
  @Override
  public final void run() {
    System.out.println(new PdfFile(this.getSourceFile(), this));
  }

  /** {@inheritDoc} */
  @Override
  public final Logger getLogger() {
    return super.getLogger();
  }
}
