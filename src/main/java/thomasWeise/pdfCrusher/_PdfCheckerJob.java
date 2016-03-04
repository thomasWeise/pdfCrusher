package thomasWeise.pdfCrusher;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.optimizationBenchmarking.utils.parallel.Execute;

import thomasWeise.pdfCrusher.tools.PdfCheckerJob;
import thomasWeise.pdfCrusher.tools.PdfCheckerJobBuilder;
import thomasWeise.pdfCrusher.tools.PdfCheckerTool;
import thomasWeise.pdfCrusher.tools.ghostScript.GhostScriptChecker;
import thomasWeise.pdfCrusher.tools.itextpdf.ITextPdfChecker;
import thomasWeise.pdfCrusher.tools.pdfbox.PdfBoxChecker;
import thomasWeise.pdfCrusher.tools.pdftk.PdfTKChecker;
import thomasWeise.pdfCrusher.tools.qpdf.QPdfChecker;

/** The pdf checker job */
final class _PdfCheckerJob extends PdfCheckerJob {
  /** the available PDf checker tools */
  private static final PdfCheckerTool[] TOOLS;

  static {
    final ArrayList<PdfCheckerTool> tools;
    PdfCheckerTool tool;

    tools = new ArrayList<>();
    tool = PdfBoxChecker.getInstance();
    if (tool.canUse()) {
      tools.add(tool);
    }
    tool = ITextPdfChecker.getInstance();
    if (tool.canUse()) {
      tools.add(tool);
    }
    tool = QPdfChecker.getInstance();
    if (tool.canUse()) {
      tools.add(tool);
    }
    tool = PdfTKChecker.getInstance();
    if (tool.canUse()) {
      tools.add(tool);
    }
    tool = GhostScriptChecker.getInstance();
    if (tool.canUse()) {
      tools.add(tool);
    }

    TOOLS = tools.toArray(new PdfCheckerTool[tools.size()]);
  }

  /**
   * create the job.
   *
   * @param builder
   *          the builder
   */
  _PdfCheckerJob(final PdfCheckerJobBuilder builder) {
    super(builder);
  }

  /** {@inheritDoc} */
  @SuppressWarnings("unchecked")
  @Override
  protected final int check() throws Throwable {
    final Future<Integer>[] results;
    final Path file;
    final Logger logger;
    int index;
    long errors;
    Integer res;

    results = new Future[_PdfCheckerJob.TOOLS.length];
    index = (-1);
    file = this.getSourceFile();
    logger = this.getLogger();

    for (final PdfCheckerTool tool : _PdfCheckerJob.TOOLS) {
      results[++index] = Execute.parallel(//
          tool.use().setSourceFile(file).setLogger(logger).create());
    }

    errors = 0L;
    for (final Future<Integer> result : results) {
      try {
        res = result.get();
      } catch (final Throwable caught) {
        errors += Integer.MAX_VALUE;
        res = null;
        if ((logger != null) && (logger.isLoggable(Level.WARNING))) {
          logger.log(Level.WARNING,
              "Error while waiting for Pdf Checker Job.", //$NON-NLS-1$
              caught);
        }
      }

      if (res == null) {
        errors += Integer.MAX_VALUE;
      } else {
        errors = Math.max(errors, (errors + res.intValue()));
      }
    }

    return Math.max(0, ((int) (Math.min(Integer.MAX_VALUE, errors))));
  }

  /** {@inheritDoc} */
  @Override
  protected final int getMaxErrors() {
    return Integer.MAX_VALUE;
  }
}
