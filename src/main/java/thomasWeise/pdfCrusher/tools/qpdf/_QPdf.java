package thomasWeise.pdfCrusher.tools.qpdf;

import java.nio.file.Path;

import org.optimizationBenchmarking.utils.io.paths.PathFinderBuilder;
import org.optimizationBenchmarking.utils.tools.impl.process.ExternalProcessExecutor;

/** the path to QPDF */
final class _QPdf {

  /** the qpdf path */
  static final Path QPDF_PATH;

  /** the check command */
  static final String CMD_CHECK = "--check";//$NON-NLS-1$

  static {
    if (ExternalProcessExecutor.getInstance().canUse()) {
      QPDF_PATH = new PathFinderBuilder()//
          .addVisitFirst("/usr/bin/qpdf") //$NON-NLS-1$
          .mustBeExecutableFile()//
          .addNamePredicate(true, "qpdf") //$NON-NLS-1$
          .addTextProcessOutputContainsAll(//
              new String[] { "--help" }, //$NON-NLS-1$
              new String[] { _QPdf.CMD_CHECK })
          .create().call();
    } else {
      QPDF_PATH = null;
    }
  }

}
