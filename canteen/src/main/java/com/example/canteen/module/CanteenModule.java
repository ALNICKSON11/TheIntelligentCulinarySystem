package com.example.canteen.module;

import com.example.canteen.canteenInterface.ConnectionInterface;
import com.example.canteen.database.DatabaseConnection;
import com.google.inject.AbstractModule;

public class CanteenModule extends AbstractModule {
    protected void configure(){
        bind(ConnectionInterface.class).to(DatabaseConnection.class);
    }
}
