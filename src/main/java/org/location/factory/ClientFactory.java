package org.location.factory;

import org.location.models.Client;

public class ClientFactory {
    public Client createClient(String nom, String email) {
        return new Client(nom, email);
    }
}
