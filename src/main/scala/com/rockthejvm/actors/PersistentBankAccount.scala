package com.rockthejvm.actors

import akka.actor.typed.{ActorRef, Behavior}
import akka.persistence.typed.PersistenceId
import akka.persistence.typed.scaladsl.{Effect, EventSourcedBehavior}

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

  // Adding a persistent data actor:

  // command handler = message handler => persist an event
  // event handler => update state
  // state

  val commandHandler: (BankAccount, Command) => Effect[Event, BankAccount] = (state, command) =>
    command match {
      case CreateBankAccount(user, currency, initialBalance, replyTo) =>
        val id = state.id
        /*
          - bank creates me
          - bank sends me CreateBankAccount
          - I persist BankAccountCreated
          - I update my state
          - reply back with the BankAccountCreatedResponse
          - (the bank surfaces the response to the HTTP server)
        */
      Effect
        .persist(BankAccountCreated(BankAccount(id, user, currency, initialBalance))) // persisted into Cassandra
        .thenReply(replyTo)(_ => BankAccountCreatedResponse(id))
    }
  val eventHandler: (BankAccount, Event) => BankAccount = ???

  def apply(id: String): Behavior[Command] =
    EventSourcedBehavior[Command, Event, BankAccount](
      persistenceId = PersistenceId.ofUniqueId(id),
      emptyState = BankAccount(id, "", "", 0.0), // unused
      commandHandler = commandHandler,
      eventHandler = eventHandler
    )




}
