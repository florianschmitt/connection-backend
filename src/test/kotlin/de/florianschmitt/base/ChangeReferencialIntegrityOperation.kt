package de.florianschmitt.base

import org.dbunit.DatabaseUnitException
import org.dbunit.database.DatabaseConfig
import org.dbunit.database.IDatabaseConnection
import org.dbunit.database.statement.IStatementFactory
import org.dbunit.dataset.IDataSet
import org.dbunit.operation.DatabaseOperation

import java.sql.SQLException

internal class ChangeReferencialIntegrityOperation private constructor(private val enable: Boolean) : DatabaseOperation() {

    @Throws(DatabaseUnitException::class, SQLException::class)
    override fun execute(connection: IDatabaseConnection, dataSet: IDataSet) {
        val databaseConfig = connection.config
        val statementFactory = databaseConfig
                .getProperty(DatabaseConfig.PROPERTY_STATEMENT_FACTORY) as IStatementFactory
        val statement = statementFactory.createBatchStatement(connection)
        try {
            statement.addBatch(String.format("SET REFERENTIAL_INTEGRITY %s", if (enable) "TRUE" else "FALSE"))
            statement.executeBatch()
            statement.clearBatch()
        } finally {
            statement.close()
        }
    }

    companion object {

        fun enable(): ChangeReferencialIntegrityOperation {
            return ChangeReferencialIntegrityOperation(true)
        }

        fun disable(): ChangeReferencialIntegrityOperation {
            return ChangeReferencialIntegrityOperation(false)
        }
    }
}