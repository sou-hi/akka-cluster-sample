import akka.actor._
import akka.cluster.Cluster
import akka.cluster.ClusterEvent._
import akka.cluster.routing.ClusterRouterConfig
import akka.cluster.routing.ClusterRouterSettings
import akka.routing.ConsistentHashingRouter
import akka.routing.ConsistentHashingRouter.ConsistentHashableEnvelope

import java.math.BigInteger
import java.util.Random
 
object HeavyPiCluster {
    def main(args: Array[String]): Unit = {
        if (args.nonEmpty){
            System.setProperty("akka.remote.netty.tcp.hostname", args(0))
            System.setProperty("akka.remote.netty.tcp.port", args(1))
        }
 
        val system = ActorSystem("HeavyPiCluster")
        val clusterListener = system.actorOf(Props[HeavyPiListener],
          name = "clusterListener")
 
        Cluster(system).subscribe(clusterListener, classOf[ClusterDomainEvent])

        if(args.length > 2){
            Thread.sleep(3000)

            val router = system.actorOf(Props[HeavyPi].withRouter(
                ClusterRouterConfig(ConsistentHashingRouter(), ClusterRouterSettings(
                totalInstances = 100, maxInstancesPerNode = 3,
                allowLocalRoutees = false, useRole = None))),
                name = "clusterrouter")

            (1 to 100).foreach{ i =>
                router.tell(ConsistentHashableEnvelope(HeavyPi.Request(1000), i), clusterListener)
            }
        }
    }
}

class HeavyPiListener extends Actor with ActorLogging {
    var totalreq = 0
    var totaliter = 0
    var totalp = 0
    def receive = {
        case state: CurrentClusterState =>
            log.info("Current members: {}", state.members.mkString(", "))
        case MemberUp(member) =>
            log.info("Member is Up: {}", member.address)
        case UnreachableMember(member) =>
            log.info("Member detected as unreachable: {}", member)
        case MemberRemoved(member, previousStatus) =>
            log.info("Member is Removed: {} after {}",
            member.address, previousStatus)
        case HeavyPi.Response(i, p) =>
            totalreq += 1
            totaliter += i
            totalp += p
            val pi = 4D * totalp / totaliter
            log.info("Actor Response {}, pi={}", totaliter, pi)
        case _: ClusterDomainEvent => // ignore
    }
}

object HeavyPi {
    case class Request(iter: Integer)
    case class Response(iter: Integer, p: Integer)
}
     
class HeavyPi extends Actor with akka.actor.ActorLogging {
    val b = 256
    val m = new BigInteger("2").pow(b).subtract(new BigInteger("1"))
    val m2 = m.pow(2)

    def receive = {
        case HeavyPi.Request(iter) =>
            var p = 0

            (1 to iter).foreach{ i =>
                val rnd = new Random()
                val x = new BigInteger(b, rnd)
                val y = new BigInteger(b, rnd)
                val r2 = x.multiply(x).add(y.multiply(y))
                if(r2.compareTo(m2) <= 0) p += 1
            }
            sender ! HeavyPi.Response(iter, p)
    }
}