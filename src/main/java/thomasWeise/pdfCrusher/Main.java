package thomasWeise.pdfCrusher;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.optimizationBenchmarking.utils.config.Configuration;

/** The main class of the pdf crusher */
public class Main {

  /**
   * The main routine
   *
   * @param args
   *          the command line arguments
   */
  public static void main(final String[] args) {
    final ForkJoinPool executor;
    final Configuration config;
    final Logger logger;
    int np;

    Configuration.setup(args);
    config = Configuration.getRoot();

    try {
      np = Runtime.getRuntime().availableProcessors();
    } catch (@SuppressWarnings("unused") final Throwable caught) {
      np = 1;
    }
    try {
      executor = new ForkJoinPool(Math.max((np - 1), 1));

      executor.submit(PdfCrusher.getInstance().use()//
          .configure(config).create()).join();
      executor.shutdown();
      executor.awaitTermination(7 * 24, TimeUnit.HOURS);
    } catch (final Throwable caught) {
      logger = Configuration.getGlobalLogger();
      if ((logger != null) && (logger.isLoggable(Level.SEVERE))) {
        logger.log(Level.SEVERE, "Error in main thread.", caught); //$NON-NLS-1$
      }
    }
  }
}
