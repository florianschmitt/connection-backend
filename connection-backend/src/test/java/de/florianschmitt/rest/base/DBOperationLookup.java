package de.florianschmitt.rest.base;

import org.dbunit.operation.CompositeOperation;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.operation.DefaultDatabaseOperationLookup;

public class DBOperationLookup extends DefaultDatabaseOperationLookup {

    @Override
    public org.dbunit.operation.DatabaseOperation get(DatabaseOperation operation) {
        org.dbunit.operation.DatabaseOperation superResult = super.get(operation);

        org.dbunit.operation.DatabaseOperation result = new CompositeOperation(
                new org.dbunit.operation.DatabaseOperation[]{ //
                        ChangeReferencialIntegrityOperation.disable(), //
                        superResult, //
                        ChangeReferencialIntegrityOperation.enable()// , //
                });

        return result;
    }
}