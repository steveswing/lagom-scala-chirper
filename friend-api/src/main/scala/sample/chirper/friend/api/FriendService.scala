package sample.chirper.friend.api

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.Service._
import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.api.broker.kafka.{KafkaProperties, PartitionKeyStrategy}
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}

import scala.collection.immutable.Seq

trait FriendService extends Service {

  def getUser(userId: String): ServiceCall[NotUsed, User]

  def createUser(): ServiceCall[CreateUser, NotUsed]

  def addFriend(userId: String): ServiceCall[FriendId, NotUsed]

  def getFollowers(userId: String): ServiceCall[NotUsed, Seq[String]]

  def friendsTopic(): Topic[FriendAdded]

  override def descriptor = named("friendservice").withCalls(
    pathCall("/api/users/:userId", getUser _),
    namedCall("/api/users", createUser _),
    pathCall("/api/users/:userId/friends", addFriend _),
    pathCall("/api/users/:userId/followers", getFollowers _)
  ).withTopics(
    topic(FriendService.TopicName, friendsTopic _)
      .addProperty(
        KafkaProperties.partitionKeyStrategy,
        PartitionKeyStrategy[FriendAdded](_.userId)
      )
  ).withAutoAcl(true)
}

object FriendService {
  val TopicName = "friends"
}
