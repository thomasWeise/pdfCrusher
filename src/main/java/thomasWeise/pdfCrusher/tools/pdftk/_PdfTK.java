package thomasWeise.pdfCrusher.tools.pdftk;

import java.nio.file.Path;

import org.optimizationBenchmarking.utils.io.paths.PathFinderBuilder;
import org.optimizationBenchmarking.utils.tools.impl.process.ExternalProcessExecutor;

/** the path to PDFTK */
final class _PdfTK {

  /** the PdfTk path */
  static final Path PDFTK_PATH;

  /** the check command */
  static final String CMD_CHECK = "dump_data";//$NON-NLS-1$

  static {
    if (ExternalProcessExecutor.getInstance().canUse()) {
      PDFTK_PATH = new PathFinderBuilder()//
          .addVisitFirst("/usr/bin/pdftk") //$NON-NLS-1$
          .mustBeExecutableFile()//
          .addNamePredicate(true, "pdftk") //$NON-NLS-1$
          .addTextProcessOutputContainsAll(//
              new String[] { "--help" }, //$NON-NLS-1$
              new String[] { _PdfTK.CMD_CHECK })
          .create().call();
    } else {
      PDFTK_PATH = null;
    }
  }

}
