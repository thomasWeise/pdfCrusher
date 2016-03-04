package thomasWeise.pdfCrusher.tools.ghostScript;

import java.nio.file.Path;

import org.optimizationBenchmarking.utils.io.paths.PathUtils;
import org.optimizationBenchmarking.utils.tools.impl.process.EProcessStream;
import org.optimizationBenchmarking.utils.tools.impl.process.ExternalProcess;
import org.optimizationBenchmarking.utils.tools.impl.process.ExternalProcessExecutor;

import thomasWeise.pdfCrusher.tools.PdfCheckerJob;
import thomasWeise.pdfCrusher.tools.PdfCheckerJobBuilder;

/** The GhostScript-based pdf checker job */
final class _GhostScriptCheckerJob extends PdfCheckerJob {

  /**
   * create the job.
   *
   * @param builder
   *          the builder
   */
  _GhostScriptCheckerJob(final PdfCheckerJobBuilder builder) {
    super(builder);
  }

  /** {@inheritDoc} */
  @Override
  protected int check() throws Throwable {
    final Path path;

    path = _GhostScript.GHOST_SCRIPT_PATH;
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
        .addStringArgument("-o")//$NON-NLS-1$
        .addStringArgument("/dev/null")//$NON-NLS-1$
        .addStringArgument("-sDEVICE=nullpage")//$NON-NLS-1$
        .addStringArgument("-dNOPAUSE")//$NON-NLS-1$
        .addStringArgument("-dQUIET")//$NON-NLS-1$
        .addStringArgument("-dBATCH")//$NON-NLS-1$
        .addStringArgument("-dSAFER")//$NON-NLS-1$
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
