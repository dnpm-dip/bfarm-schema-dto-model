package de.dnpm.bfarm.model.onco


import de.dnpm.mvh.submission.test.BaseMappingTest
import de.dnpm.dip.mtb.model.MTBPatientRecord
import de.dnpm.dip.mtb.gens.Generators._
import de.dnpm.bfarm.model.onco.MTBMappings._


class Tests extends BaseMappingTest[MTBPatientRecord,OncologySubmission](
  "Oncology",
  "https://raw.githubusercontent.com/BfArM-MVH/MVGenomseq_KDK/main/KDK/Oncology.json"
)
