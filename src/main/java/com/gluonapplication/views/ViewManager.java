package com.gluonapplication.views;

import com.gluonhq.charm.glisten.application.AppManager;
import com.gluonhq.charm.glisten.mvc.View;

import javax.security.auth.DestroyFailedException;
import javax.security.auth.Destroyable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ViewManager {
    // Changed to public to allow access from HomeView
    public static final Map<String, View> registeredViews = new HashMap<>();
    private static final Map<String, Supplier<View>> viewFactories = new HashMap<>();

    public static void registerView(String name, Supplier<View> viewSupplier) {
        if (!registeredViews.containsKey(name)) {
            viewFactories.put(name, viewSupplier);
            AppManager.getInstance().addViewFactory(name, () -> {
                View view = viewSupplier.get();
                registeredViews.put(name, view);
                return view;
            });
        }
    }

    public static void switchToView(String name) {
        if (registeredViews.containsKey(name)) {
            AppManager.getInstance().switchView(name);
        } else if (viewFactories.containsKey(name)) {
            View view = viewFactories.get(name).get();
            registeredViews.put(name, view);
            AppManager.getInstance().switchView(name);
        }
    }

    public static void cleanupView(String name) {
        if (registeredViews.containsKey(name)) {
            View view = registeredViews.get(name);
            if (view instanceof Destroyable) {
                try {
                    ((Destroyable) view).destroy();
                } catch (DestroyFailedException e) {
                    throw new RuntimeException(e);
                }
            }
            registeredViews.remove(name);
        }
    }
}