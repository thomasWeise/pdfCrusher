package thomasWeise.pdfCrusher.tools;

/** The builder for PDF Checker jobs. */
public class PdfCheckerJobBuilder
    extends FileJobBuilder<PdfCheckerJob, PdfCheckerJobBuilder> {

  /** the checker tool. */
  private final PdfCheckerTool m_tool;

  /** should we use the system's temp directory? */
  private boolean m_useSystemTempDir;

  /**
   * Create the pdf checker builder
   *
   * @param tool
   *          the tool
   */
  PdfCheckerJobBuilder(final PdfCheckerTool tool) {
    super();
    this.m_tool = tool;
  }

  /** {@inheritDoc} */
  @Override
  public final PdfCheckerJob create() {
    this.validate();
    return this.m_tool.create(this);
  }

  /**
   * Should we use the system's temp dir ({@code true}) or the directory in
   * which the pdf file itself is located ({@code false}) as temp folder
   *
   * @param useSystemTempDir
   *          {@code true} to use the system's temp dir, {@code false} for
   *          the folder containg the pdf
   * @return this builder
   */
  public final PdfCheckerJobBuilder setUseSystemTempDir(
      final boolean useSystemTempDir) {
    this.m_useSystemTempDir = useSystemTempDir;
    return this;
  }

  /**
   * Should we use the system's temp directory or the directory of the pdf
   * to check as temp folder?
   *
   * @return {@code true} to use the system's temp dir, {@code false} for
   *         the folder containg the pdf
   */
  public final boolean useSystemTempDir() {
    return this.m_useSystemTempDir;
  }
}