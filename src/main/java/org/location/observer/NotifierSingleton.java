package org.location.observer;

public class NotifierSingleton {
    private static final DataNotifier instance = new DataNotifier();

    private NotifierSingleton() {}

    public static DataNotifier getInstance() {
        return instance;
    }
}
