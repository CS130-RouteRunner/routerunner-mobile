package com.cs130.routerunner.TapHandler;

import com.cs130.routerunner.Actor;

/**
 * Created by julianyang on 10/22/15.
 * This is the "State" interface for the State design pattern.  We use the
 * state design pattern because the interpretation of a "Tap" depends on
 * whether the user has already entered route editing on an actor or not.
 * As of now, we have two States: RouteEditMode, NormalMode
 */
public interface TapMode {
    void Tap(float x, float y, int count);
    void SetSelectedActor(Actor a);
}
