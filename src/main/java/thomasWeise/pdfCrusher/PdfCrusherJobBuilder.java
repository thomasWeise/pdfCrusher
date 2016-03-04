package thomasWeise.pdfCrusher;

import thomasWeise.pdfCrusher.tools.FileJobBuilder;

/** The builder for the Pdf Crusher main job. */
public class PdfCrusherJobBuilder
    extends FileJobBuilder<PdfCrusherJob, PdfCrusherJobBuilder> {

  /** create */
  PdfCrusherJobBuilder() {
    super();
  }

  /** {@inheritDoc} */
  @Override
  public final PdfCrusherJob create() {
    this.validate();
    return new PdfCrusherJob(this);
  }
}
