package thomasWeise.pdfCrusher.tools.qpdf;

import java.nio.file.Path;

import org.optimizationBenchmarking.utils.io.paths.PathUtils;
import org.optimizationBenchmarking.utils.tools.impl.process.EProcessStream;
import org.optimizationBenchmarking.utils.tools.impl.process.ExternalProcess;
import org.optimizationBenchmarking.utils.tools.impl.process.ExternalProcessExecutor;

import thomasWeise.pdfCrusher.tools.PdfCheckerJob;
import thomasWeise.pdfCrusher.tools.PdfCheckerJobBuilder;

/** The qpdf-based pdf checker job */
final class _QPdfCheckerJob extends PdfCheckerJob {

  /**
   * create the job.
   *
   * @param builder
   *          the builder
   */
  _QPdfCheckerJob(final PdfCheckerJobBuilder builder) {
    super(builder);
  }

  /** {@inheritDoc} */
  @Override
  protected int check() throws Throwable {
    final Path path;

    path = _QPdf.QPDF_PATH;
    if (path == null) {
      return 0;
    }

    try (ExternalProcess prc = ExternalProcessExecutor.getInstance().use()//
        .setDirectory(PathUtils.getTempDir())//
        .setExecutable(path)//
        .setLogger(this.getLogger())//
        .setStdErr(EProcessStream.IGNORE)//
        .setStdIn(EProcessStream.IGNORE)//
        .setStdIn(EProcessStream.IGNORE)//
        .addStringArgument(_QPdf.CMD_CHECK)//
        .addPathArgument(this.getSourceFile())//
        .create()) {
      return ((prc.waitFor() == 0) ? 0 : 1);
    }
  }

  /** {@inheritDoc} */
  @Override
  protected final int getMaxErrors() {
    return 1;
  }
}
