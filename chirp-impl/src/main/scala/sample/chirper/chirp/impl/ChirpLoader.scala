package sample.chirper.chirp.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.server.{LagomApplication, LagomApplicationContext, LagomApplicationLoader}
import com.softwaremill.macwire._
import play.api.libs.ws.ahc.AhcWSComponents
import sample.chirper.chirp.api.ChirpService

class ChirpLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext) = new ChirpApplication(context) {
    override def serviceLocator = NoServiceLocator
  }

  override def loadDevMode(context: LagomApplicationContext) = new ChirpApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[ChirpService])

}

abstract class ChirpApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with CassandraPersistenceComponents
    // with LagomKafkaComponents
    with AhcWSComponents {

  override lazy val lagomServer = serverFor[ChirpService](wire[ChirpServiceImpl])

    override def jsonSerializerRegistry = ChirpTimelineSerializerRegistry

    persistentEntityRegistry.register(wire[ChirpTimelineEntity])

  readSide.register(wire[ChirpTimelineEventReadSideProcessor])

}
