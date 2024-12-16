package com.rockthejvm.bank.http

import akka.http.scaladsl.server.Directives._
import akka.actor.typed.ActorRef
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.headers.Location
import akka.util.Timeout
import com.rockthejvm.bank.actors.PersistentBankAccount.Command
import com.rockthejvm.bank.actors.PersistentBankAccount.Response
import com.rockthejvm.bank.actors.PersistentBankAccount.Response._
import com.rockthejvm.bank.actors.PersistentBankAccount.Command._
// Will allow me to use a special directive that will allow to convert a payload to a special case class
import io.circe.generic.auto._
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import akka.actor.typed.scaladsl.AskPattern._

import scala.concurrent.Future
import scala.concurrent.duration._

case class BankAccountCreationRequest(user: String, balance: Double) { // converting a request into a command that an Akka actor can understand
  def toCommand(replyTo: ActorRef[Response]): Command = CreateBankAccount(user, currency, balance, replyTo)
}

class BankRoutes(bank: ActorRef[Command])(implicit system: ActorRef[_]) {

  implicit val timeout: Timeout = Timeout()

  def createBankAccount(request: BankAccountCreationRequest): Future[Response] =
    bank.ask(replyTo => request.toCommand(replyTo))


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
           */
            onSuccess(createBankAccount(request)) {
              // - send back an HTTP response (the first 3 will be abstracted away into other methods)
              case BankAccountCreatedResponse(id) =>
                respondWithHeader(Location(s"/bank/$id")) {
                  complete(StatusCodes.Created)
                }
            }
          }
        }
      }
    }


}
