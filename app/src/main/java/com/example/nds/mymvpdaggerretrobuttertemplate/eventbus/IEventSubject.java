package com.example.nds.mymvpdaggerretrobuttertemplate.eventbus;

import retrofit2.Response;

public interface IEventSubject {
    void addObserver(final IEventObserver iObserver);

    void removeObserver(final IEventObserver iObserver);

    void removeAllObservers();

    void notifyStartedWithAction(final int action, final int classUniqueId);

    void notifyFinishWithAction(final int action, final int classUniqueId);

    void notifySuccess(final int actionCode, final Response response, final int classUniqueId);

    void notifyFailed(final int actionCode, final RetrofitException e, final String message, final int classUniqueId);

    boolean containObserver(final IEventObserver iObserver);

    void notifyEvent(final Event event);
}
