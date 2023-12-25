package org.oleksii.user.client;

import org.oleksii.user.databaseAccessors.ClientDatabaseAccessor;

import java.util.ArrayList;

public class ClientsList extends Client {
    public static ArrayList<Client> clientArrayList = ClientDatabaseAccessor.getClientsFromBD();
}
