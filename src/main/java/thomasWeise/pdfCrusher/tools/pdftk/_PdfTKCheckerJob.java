package thomasWeise.pdfCrusher.tools.pdftk;

import java.nio.file.Path;

import org.optimizationBenchmarking.utils.io.paths.PathUtils;
import org.optimizationBenchmarking.utils.tools.impl.process.EProcessStream;
import org.optimizationBenchmarking.utils.tools.impl.process.ExternalProcess;
import org.optimizationBenchmarking.utils.tools.impl.process.ExternalProcessExecutor;

import thomasWeise.pdfCrusher.tools.PdfCheckerJob;
import thomasWeise.pdfCrusher.tools.PdfCheckerJobBuilder;

/** The PdfTk-based pdf checker job */
final class _PdfTKCheckerJob extends PdfCheckerJob {

  /**
   * create the job.
   *
   * @param builder
   *          the builder
   */
  _PdfTKCheckerJob(final PdfCheckerJobBuilder builder) {
    super(builder);
  }

  /** {@inheritDoc} */
  @Override
  protected int check() throws Throwable {
    final Path path;

    path = _PdfTK.PDFTK_PATH;
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
        .addPathArgument(this.getSourceFile())//
        .addStringArgument(_PdfTK.CMD_CHECK)//
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
