package org.denys.user.client;

import org.denys.user.databaseAccessors.ClientDatabaseAccessor;

import java.util.ArrayList;

public class ClientsList extends Client {
    public static ArrayList<Client> clientArrayList = ClientDatabaseAccessor.getClientsFromBD();
}
