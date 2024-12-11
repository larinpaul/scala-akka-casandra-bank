package com.rockthejvm.bank.http

import akka.http.scaladsl.server.Directives._
import akka.actor.typed.ActorRef
import com.rockthejvm.bank.actors.PersistentBankAccount.Command
import com.rockthejvm.bank.actors.PersistentBankAccount.Command._

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
        }
      }
    }


}
