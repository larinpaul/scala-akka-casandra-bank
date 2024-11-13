package com.rockthejvm.actors

import akka.actor.typed.ActorRef

// a single bank account
class PersistentBankAccount {

  // event sourcing
  // allows to hold not just the recent events, but all the vents
  // good for fault tolerance
  // good for auditing

  // We will make it a persistent actor

  // commands = messages
  sealed trait Command
  case class CreateBankAccount(user: String, currency: String, initialBalance: Double, replyTo: ActorRef[Response]) extends Command
  case class UpdateBalance(id: String, currency: String, amount: Double /* can be < 0*/, replyTo: ActorRef[Response]) extends Command
  case class GetBankAccount(id: String, replyTo: ActorRef[Response]) extends Command

  // events = to persist to Cassandra
  trait Event
  case class BankAccountCreated(bankAccount: BankAccount) extends Event
  case class BalanceUpdated(amount: Double) extends Event

  // state
  case class BankAccount(id: String, user: String, currency: String, balance: Double)

  // response
  sealed trait Response
  case class BankAccountCreatedResponse(id: String) extends Response
  case class BankAccountBalanceUpdatedResponse(maybeBankAccount: Option[BankAccount])
  case class GetBankAccountResponse(maybeBankAccount: Option[BankAccount]) extends Response

}
