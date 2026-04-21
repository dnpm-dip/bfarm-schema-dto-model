package de.dnpm.bfarm.model.rd


import de.dnpm.mvh.submission.test.BaseMappingTest
import de.dnpm.dip.rd.model.RDPatientRecord
import de.dnpm.dip.rd.gens.Generators._
import de.dnpm.bfarm.model.rd.RDMappings._


class Tests extends BaseMappingTest[RDPatientRecord,RDSubmission](
  "Rare Diseases",
  "https://raw.githubusercontent.com/BfArM-MVH/MVGenomseq_KDK/main/KDK/RareDiseases.json"
)
