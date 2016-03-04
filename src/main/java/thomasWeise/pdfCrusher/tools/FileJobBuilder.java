package thomasWeise.pdfCrusher.tools;

import java.nio.file.Path;

import org.optimizationBenchmarking.utils.config.Configuration;
import org.optimizationBenchmarking.utils.tools.impl.abstr.ConfigurableToolJobBuilder;

/**
 * The builder for file jobs.
 *
 * @param <FJ>
 *          the file job type
 * @param <FJB>
 *          the file job builder type
 */
public abstract class FileJobBuilder<FJ extends FileJob, FJB extends FileJobBuilder<FJ, FJB>>
    extends ConfigurableToolJobBuilder<FJ, FJB> {

  /** the source path parameter */
  public static final String PARAM_SOURCE_FILE = "source"; //$NON-NLS-1$

  /** the path to the file */
  private Path m_path;

  /** Create the pdf checker builder */
  protected FileJobBuilder() {
    super();
  }

  /** {@inheritDoc} */
  @Override
  public FJB configure(final Configuration config) {
    final FJB res;
    final Path src;

    res = super.configure(config);

    src = config.getPath(FileJobBuilder.PARAM_SOURCE_FILE, null);
    if (src != null) {
      this.setSourceFile(src);
    }

    return res;
  }

  /**
   * check the path to the source file
   *
   * @param sourceFile
   *          the path
   */
  static final void _checkPath(final Path sourceFile) {
    if (sourceFile == null) {
      throw new IllegalArgumentException(
          "Path of source file must not be null."); //$NON-NLS-1$
    }
  }

  /**
   * Set the path of the source file
   *
   * @param path
   *          the path
   * @return this builder
   */
  @SuppressWarnings("unchecked")
  public final FJB setSourceFile(final Path path) {
    FileJobBuilder._checkPath(path);
    this.m_path = path;
    return ((FJB) this);
  }

  /**
   * Get the source file
   *
   * @return the source file
   */
  protected final Path getSourceFile() {
    return this.m_path;
  }

  /** {@inheritDoc} */
  @Override
  protected void validate() {
    super.validate();
    FileJobBuilder._checkPath(this.m_path);
  }
}
