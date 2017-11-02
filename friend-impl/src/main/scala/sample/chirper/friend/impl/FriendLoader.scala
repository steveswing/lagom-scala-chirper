package sample.chirper.friend.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.server.{LagomApplication, LagomApplicationContext, LagomApplicationLoader}
import com.softwaremill.macwire._
import play.api.libs.ws.ahc.AhcWSComponents
import sample.chirper.friend.api.FriendService

class FriendLoader extends LagomApplicationLoader {
  override def load(context: LagomApplicationContext) = new FriendApplication(context) {
    override def serviceLocator = NoServiceLocator
  }
}

abstract class FriendApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with AhcWSComponents {

  override lazy val lagomServer = serverFor[FriendService](wire[FriendServiceImpl])

}
