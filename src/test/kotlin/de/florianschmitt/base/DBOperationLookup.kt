package de.florianschmitt.base

import org.dbunit.operation.CompositeOperation

import com.github.springtestdbunit.annotation.DatabaseOperation
import com.github.springtestdbunit.operation.DefaultDatabaseOperationLookup

class DBOperationLookup : DefaultDatabaseOperationLookup() {

    override fun get(operation: DatabaseOperation): org.dbunit.operation.DatabaseOperation {
        val superResult = super.get(operation)

        return CompositeOperation(
                arrayOf(//
                        ChangeReferencialIntegrityOperation.disable(), //
                        superResult, //
                        ChangeReferencialIntegrityOperation.enable()// , //
                ))
    }
}