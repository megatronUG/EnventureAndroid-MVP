package org.enventureenterprises.enventure.util.rx;

/**
 * Created by mossplix on 4/28/17.
 */

import android.support.annotation.NonNull;

import com.google.gson.Gson;

import org.enventureenterprises.enventure.data.ApiException;
import org.enventureenterprises.enventure.data.ErrorEnvelope;
import org.enventureenterprises.enventure.data.ResponseException;

import java.io.IOException;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;

/**
 * Takes a {@link retrofit2.Response}, if it's successful send it to {@link Subscriber#onNext}, otherwise
 * attempt to parse the error.
 *
 * Errors that conform to the API's error format are converted into an {@link ApiException} exception and sent to
 * {@link Subscriber#onError}, otherwise a more generic {@link ResponseException} is sent to {@link Subscriber#onError}.
 *
 * @param <T> The response type.
 */
public final class ApiErrorOperator<T> implements Observable.Operator<T, retrofit2.Response<T>> {
    private final Gson gson;

    public ApiErrorOperator(final @NonNull Gson gson) {
        this.gson = gson;
    }

    @Override
    public Subscriber<? super Response<T>> call(final @NonNull Subscriber<? super T> subscriber) {
        return new Subscriber<retrofit2.Response<T>>() {
            @Override
            public void onCompleted() {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onCompleted();
                }
            }

            @Override
            public void onError(final @NonNull Throwable e) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onError(e);
                }
            }

            @Override
            public void onNext(final @NonNull retrofit2.Response<T> response) {
                if (subscriber.isUnsubscribed()) {
                    return;
                }

                if (!response.isSuccessful()) {
                    try {
                        final ErrorEnvelope envelope = gson.fromJson(response.errorBody().string(), ErrorEnvelope.class);
                        subscriber.onError(new ApiException(envelope, response));
                    } catch (final @NonNull IOException e) {
                        subscriber.onError(new ResponseException(response));
                    }
                } else {
                    subscriber.onNext(response.body());
                    subscriber.onCompleted();
                }
            }
        };
    }
}
