package sample.chirper.friend.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.broker.kafka.LagomKafkaComponents
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.server.{LagomApplication, LagomApplicationContext, LagomApplicationLoader}
import com.softwaremill.macwire._
import play.api.libs.ws.ahc.AhcWSComponents
import sample.chirper.friend.api.FriendService

class FriendLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext) = new FriendApplication(context) {
    override def serviceLocator = NoServiceLocator
  }

  override def loadDevMode(context: LagomApplicationContext) = new FriendApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[FriendService])

}

abstract class FriendApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with CassandraPersistenceComponents
    with LagomKafkaComponents
    with AhcWSComponents {

  override lazy val lagomServer = serverFor[FriendService](wire[FriendServiceImpl])

  override def jsonSerializerRegistry = FriendSerializerRegistry

  persistentEntityRegistry.register(wire[FriendEntity])

  readSide.register(wire[FriendEventProcessor])

}

