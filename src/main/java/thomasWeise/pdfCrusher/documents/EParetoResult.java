package thomasWeise.pdfCrusher.documents;

/** the result of entering a new element into the pareto set */
public enum EParetoResult {

  /**
   * the pareto better: the file is better thanat least one file in the
   * pareto front
   */
  BETTER,
  /** the pareto better */
  EQUAL,
  /** the identiy */
  SAME,
  /** the worse */
  WORSE,
  /** the pareto neither */
  NEITHER;

}
