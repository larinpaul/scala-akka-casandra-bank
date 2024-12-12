package com.rockthejvm.bank.http

import akka.http.scaladsl.server.Directives._
import akka.actor.typed.ActorRef
import com.rockthejvm.bank.actors.PersistentBankAccount.Command
import com.rockthejvm.bank.actors.PersistentBankAccount.Command._
// Will allow me to use a special directive that will allow to convert a payload to a special case class
import io.circe.generic.auto._
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._

case class BankAccountCreationRequest(user: String, balance: Double)

class BankRoutes(bank: ActorRef[Command]) {

  /*
    POST /bank
      Payload: bank account creation request as JSON
      Response:
      201 Created
      Location: /bank/uuid
  */
  val routes =
    pathPrefix("bank") {
      pathEndOrSingleSlash {
        post {
          // parse the payload
          entity(as[BankAccountCreationRequest]) { =>
            /*
            - convert the request into a Command for the bank actor
            - send the command to the bank
            - expect a reply
            - send back an HTTP response (the first 3 will be abstracted away into other methods)
           */
          }
        }
      }
    }


}
