package thomasWeise.pdfCrusher.tools.ghostScript;

import java.nio.file.Path;

import org.optimizationBenchmarking.utils.io.paths.PathFinderBuilder;
import org.optimizationBenchmarking.utils.tools.impl.process.ExternalProcessExecutor;

/** the path to GHOST_SCRIPT */
final class _GhostScript {

  /** the GhostScript path */
  static final Path GHOST_SCRIPT_PATH;

  static {
    if (ExternalProcessExecutor.getInstance().canUse()) {
      GHOST_SCRIPT_PATH = new PathFinderBuilder()//
          .addVisitFirst("/usr/bin/gs") //$NON-NLS-1$
          .addVisitFirst("C:/Program Files/gs")//$NON-NLS-1$ "
          .addVisitFirst("C:/Program Files/MiKTeX 3.0/miktex/bin") //$NON-NLS-1$
          .addVisitFirst("C:/Program Files/MiKTeX 2.9/miktex/bin") //$NON-NLS-1$
          .addVisitFirst("C:/Program Files/MiKTeX 2.8/miktex/bin") //$NON-NLS-1$
          .addVisitFirst("C:/Program Files/MiKTeX 2.7/miktex/bin") //$NON-NLS-1$
          .addVisitFirst("C:/Program Files (x86)/MiKTeX 3.0/miktex/bin") //$NON-NLS-1$
          .addVisitFirst("C:/Program Files (x86)/MiKTeX 2.9/miktex/bin") //$NON-NLS-1$
          .addVisitFirst("C:/Program Files (x86)/MiKTeX 2.8/miktex/bin") //$NON-NLS-1$
          .addVisitFirst("C:/Program Files (x86)/MiKTeX 2.7/miktex/bin") //$NON-NLS-1$
          .mustBeExecutableFile()//
          .addNamePredicate(true, "gs", //$NON-NLS-1$
              "gswin64c", //$NON-NLS-1$
              "gswin32c")//$NON-NLS-1$
          .addTextProcessOutputContainsAll(new String[] { "--help" }, //$NON-NLS-1$
              new String[] { "nullpage" }) //$NON-NLS-1$
          .create().call();
    } else {
      GHOST_SCRIPT_PATH = null;
    }
  }

}
