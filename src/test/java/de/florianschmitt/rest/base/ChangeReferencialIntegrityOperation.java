package de.florianschmitt.rest.base;

import java.sql.SQLException;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.statement.IBatchStatement;
import org.dbunit.database.statement.IStatementFactory;
import org.dbunit.dataset.IDataSet;
import org.dbunit.operation.DatabaseOperation;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class ChangeReferencialIntegrityOperation extends DatabaseOperation {

    private final boolean enable;

    @Override
    public void execute(IDatabaseConnection connection, IDataSet dataSet) throws DatabaseUnitException, SQLException {
        DatabaseConfig databaseConfig = connection.getConfig();
        IStatementFactory statementFactory = (IStatementFactory) databaseConfig
                .getProperty(DatabaseConfig.PROPERTY_STATEMENT_FACTORY);
        IBatchStatement statement = statementFactory.createBatchStatement(connection);
        try {
            statement.addBatch(String.format("SET REFERENTIAL_INTEGRITY %s", enable ? "TRUE" : "FALSE"));
            statement.executeBatch();
            statement.clearBatch();
        } finally {
            statement.close();
        }
    }

    public static ChangeReferencialIntegrityOperation enable() {
        return new ChangeReferencialIntegrityOperation(true);
    }

    public static ChangeReferencialIntegrityOperation disable() {
        return new ChangeReferencialIntegrityOperation(false);
    }
}