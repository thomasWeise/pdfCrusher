package thomasWeise.pdfCrusher.tools;

import java.nio.file.Path;

import org.optimizationBenchmarking.utils.tools.impl.abstr.ToolJob;

/** A base class for jobs with an input file. */
public abstract class FileJob extends ToolJob {

  /** the path to the file */
  private final Path m_sourceFile;

  /**
   * create the job.
   *
   * @param builder
   *          the file job builder
   */
  protected FileJob(final FileJobBuilder<?, ?> builder) {
    super(builder);

    final Path sourceFile;
    sourceFile = builder.getSourceFile();
    FileJobBuilder._checkPath(sourceFile);
    this.m_sourceFile = sourceFile;
  }

  /**
   * Get the source file
   *
   * @return the path to the source file
   */
  protected final Path getSourceFile() {
    return this.m_sourceFile;
  }
}
