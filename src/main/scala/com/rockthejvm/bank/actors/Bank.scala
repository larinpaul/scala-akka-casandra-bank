package com.rockthejvm.bank.actors

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import akka.persistence.typed.PersistenceId
import akka.persistence.typed.scaladsl.Effect
import com.rockthejvm.bank.actors.PersistentBankAccount.{BankAccount, eventHandler}

import java.util.UUID

class Bank {

  // commands = messages

  import PersistentBankAccount.Command._
  import PersistentBankAccount.Command

  // events
  sealed trait Event

  case class BankAccountCreated(id: String) extends Event

  // state
  case class State(accounts: Map[String, ActorRef[Command]])

  // command handler aka message handler                // run my little function here...
  val commandHandler: (State, Command) => Effect[Event, State] = (state, command) =>
    command match {
      case CreateBankAccount(user, currency, initialBalance, replyTo) =>
        val id = UUID.randomUUID().toString
        val new BankAccount = context.spawn
    }

  // behavior
  def apply(): Behavior[Command] = Behaviors.setup { context =>
    EventSourceBehavior[Command, Event, State](
      persistenceId = PersistenceId.ofUniqueId("bank"),
      emptyState = State(Map()),
      commandHandler = commandHandler,
      eventHandler = eventHandler
    )
  }

}
