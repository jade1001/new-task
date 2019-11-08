package com.template.contracts

import net.corda.core.contracts.CommandData
import net.corda.core.contracts.Contract
import net.corda.core.contracts.TypeOnlyCommandData
import net.corda.core.transactions.LedgerTransaction


class TaskContract : Contract {
    companion object {
        const val ID = "com.template.contracts.TaskContract"
    }

    override fun verify(tx: LedgerTransaction) {

    }

    interface Commands : CommandData {
        class Register : TypeOnlyCommandData(), Commands
        class Move : TypeOnlyCommandData(), Commands
    }
}