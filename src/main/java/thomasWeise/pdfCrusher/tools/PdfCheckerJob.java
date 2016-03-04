package thomasWeise.pdfCrusher.tools;

import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/** A job for checking PDFs. */
public abstract class PdfCheckerJob extends FileJob
    implements Callable<Integer> {

  /** the maximum number of permitted page errors */
  protected static final int MAX_PAGE_ERRORS = 10000;

  /** should we use the system's temp directory? */
  private final boolean m_useSystemTempDir;

  /**
   * create the job.
   *
   * @param builder
   *          the job builder
   */
  protected PdfCheckerJob(final PdfCheckerJobBuilder builder) {
    super(builder);
    this.m_useSystemTempDir = builder.useSystemTempDir();
  }

  /**
   * Should we use the system's temp directory or the directory of the pdf
   * to check as temp folder?
   *
   * @return {@code true} to use the system's temp dir, {@code false} for
   *         the folder containg the pdf
   */
  protected final boolean useSystemTempDir() {
    return this.m_useSystemTempDir;
  }

  /**
   * Check the given PDF file
   *
   * @return {@code 0} if the file is OK, or the number of detected errors
   *         otherwise
   * @throws Throwable
   *           if something fails fails
   */
  protected abstract int check() throws Throwable;

  /** {@inheritDoc} */
  @Override
  public final Integer call() {
    final Logger logger;

    try {
      return Integer.valueOf(Math.max(0, this.check()));
    } catch (final Throwable error) {
      logger = this.getLogger();
      if ((logger != null) && (logger.isLoggable(Level.FINE))) {
        logger.log(Level.FINE, //
            ((((this.getClass().getSimpleName() + //
                " detected an error in '") + this.getSourceFile()) + '\'') //$NON-NLS-1$
                + '.'),
            error);
      }
      return Integer.valueOf(this.getMaxErrors());
    }
  }

  /**
   * get the maximum number of errors.
   *
   * @return the maximum number of errors.
   */
  protected int getMaxErrors() {
    return 1;
  }
}
