package de.dnpm.bfarm.model.base


import java.io.FileInputStream
import scala.util.{
  Failure,
  Try,
  Using
}
import play.api.libs.json.{
  Json,
  Reads
}
import de.dnpm.dip.coding.Code
import de.dnpm.dip.model.{
  Id,
  Site
}
import de.dnpm.dip.util.Logging
import de.dnpm.dip.service.mvh.UseCase


final case class Config
(
  dataNodeIds: Map[UseCase.Value,Id[CDN]],
  sites: Map[Code[Site],Config.SiteInfo]
){

  def submitterId(site: Code[Site]): Id[Site] =
    sites(site).submitterId

  def gdcId(site: Code[Site]): Id[GDC] =
    sites(site).gdcId
}


object Config extends Logging
{

  final case class SiteInfo
  (
    submitterId: Id[Site],
    gdcId: Id[GDC]
  )


  implicit val readsSiteInfo: Reads[SiteInfo] =
    Json.reads[SiteInfo]

  implicit val reads: Reads[Config] =
    Json.reads[Config]

  lazy val instance: Config =
    Try(sys.env("CCDN_BFARM_MAPPINGS_CONFIG_FILE")).orElse(Try(sys.props("ccdn.bfarm.mappings.config.file")))
      .transform(
        in => Using(new FileInputStream(in))(Json.parse(_)).map(
          Json.fromJson[Config](_)
            .fold(
              errs => {
                log.error(errs.toString)
                throw new Exception(errs.toString)
              },
              identity
            )
        ),
        t => {
          log.warn("Failed to load specified externaly mapping config file. This will lead to fallback to default Config", t)
          Failure(t)
        }
      )
     .getOrElse(
       Config( 
         dataNodeIds = Map(
           UseCase.MTB -> Id[CDN]("KDKTUE005"),
           UseCase.RD  -> Id[CDN]("KDKTUE002")
         ),
         sites = Map(
           "Charité" -> ("261101015","GRZB00007"),
           "KUM"     -> ("260914050","GRZM00006"),
           "MHH"     -> ("260320597","GRZDD0004"),
           "MRI"     -> ("260913195","GRZM00006"),
           "UKA"     -> ("260530012","GRZK00001"),
           "UKB"     -> ("260530103","GRZK00001"),
           "UKD"     -> ("260510018","GRZK00001"),
           "UKDD"    -> ("261401030","GRZDD0004"),
           "UKE"     -> ("260200013","GRZTUE002"),
           "UKER"    -> ("260950567","GRZM00006"),
           "UKFR"    -> ("260832299","GRZTUE002"),
           "UKHD"    -> ("260820466","GRZHD0003"),
           "UKJ"     -> ("261600736","GRZDD0004"),
           "UKK"     -> ("260530283","GRZK00001"),
           "UKL"     -> ("261401052","GRZDD0004"),
           "UKM"     -> ("260550131","GRZK00001"),
           "UKMR"    -> ("260620431","GRZK00001"),
           "UKR"     -> ("260930608","GRZM00006"),
           "UKSH"    -> ("260102343","GRZB00007"),
           "UKT"     -> ("260840108","GRZTUE002"),
           "UKU"     -> ("260840200","GRZHD0003"),
           "UKW"     -> ("260960079","GRZM00006"),
           "UM"      -> ("260730161","GRZHD0003"),
           "UME"     -> ("260510381","GRZK00001"),
           "UMG"     -> ("260310378","GRZTUE002"),
           "UMH"     -> ("261500702","GRZDD0004")
         )
         .map {
           case (site,(submitterId,gdcId)) => Code[Site](site) -> SiteInfo(Id[Site](submitterId),Id[GDC](gdcId))
         }
       )
     )

}
