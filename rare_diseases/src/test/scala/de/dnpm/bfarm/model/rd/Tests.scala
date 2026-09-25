package de.dnpm.bfarm.model.rd


import de.dnpm.mvh.submission.test.BaseMappingTest
import de.dnpm.dip.util.Completer.syntax._
import de.dnpm.dip.coding.CodeSystem
import de.dnpm.dip.service.mvh.Submission
import de.dnpm.dip.rd.model.{
  HPO,
  RDPatientRecord
}
import de.ekut.tbi.generators.Gen
import de.dnpm.dip.rd.gens.Generators._
import de.dnpm.bfarm.model.rd.RDMappings._


class Tests extends BaseMappingTest[RDPatientRecord,RDSubmission](
  "Rare Diseases",
  "https://raw.githubusercontent.com/BfArM-MVH/MVGenomseq_KDK/main/KDK/RareDiseases.json"
){

  implicit val hpOntology: CodeSystem[HPO] =
    HPO.Ontology
      .getInstance[cats.Id]
      .get
      .latest

  override val submissions =
    LazyList.continually(Gen.of[Submission[RDPatientRecord]].next)
      .filter(_.record.diagnoses.forall(_.codes.size == 3))
      // This is required as a "hack": The schema requires a version on HPO-Codings, but it's not set by default on the generated data
      .map(
        submission => submission.copy(
          record = submission.record.copy(
            hpoTerms = submission.record.hpoTerms.map(
              hpoTerm => hpoTerm.copy(
                value = hpoTerm.value.complete
              )
            )
          )
        )
      )
      .take(42)
      
}
