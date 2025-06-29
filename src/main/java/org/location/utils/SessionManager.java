package org.location.utils;

import org.location.models.User;
import org.location.models.Client;

/**
 * Gestionnaire de session utilisant le pattern Singleton
 * Stocke les informations de l'utilisateur connecté
 */
public class SessionManager {
    private static SessionManager instance;
    private User currentUser;
    private Client currentClient;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public static void setCurrentUser(User user) {
        getInstance().currentUser = user;
    }

    public static User getCurrentUser() {
        return getInstance().currentUser;
    }

    public static void setCurrentClient(Client client) {
        getInstance().currentClient = client;
    }

    public static Client getCurrentClient() {
        return getInstance().currentClient;
    }

    public static void logout() {
        getInstance().currentUser = null;
        getInstance().currentClient = null;
    }

    public static boolean isLoggedIn() {
        return getInstance().currentUser != null;
    }

    public static boolean isAdmin() {
        return getInstance().currentUser != null &&
                (getInstance().currentUser.getRole() == User.Role.ADMIN ||
                        getInstance().currentUser.getRole() == User.Role.EMPLOYEE);
    }

    public static boolean isClient() {
        return getInstance().currentUser != null &&
                getInstance().currentUser.getRole() == User.Role.CLIENT;
    }
}