package com.rockthejvm.bank.actors

import akka.actor.typed.ActorRef
import akka.persistence.typed.scaladsl.Effect

class Bank {

  // commands = messages
  import PersistentBankAccount.Command._
  import PersistentBankAccount.Command

  // events
  sealed trait Event
  case class BankAccountCreated(id: String) extends Event

  // state
  case class State(accounts: Map[String, ActorRef[Command]])

  // command handler aka message handler
  val commandHandler: (State, Command) => Effect[Event, State] = ???

  // event handler
  val eventHandler: (State, Event) => State => ???

  // behavior


}
