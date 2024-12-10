package com.rockthejvm.bank.http

import akka.actor.typed.ActorRef
import com.rockthejvm.bank.actors.PersistentBankAccount.Command
import com.rockthejvm.bank.actors.PersistentBankAccount.Command._

class BankRoutes(bank: ActorRef[Command]) {



}
