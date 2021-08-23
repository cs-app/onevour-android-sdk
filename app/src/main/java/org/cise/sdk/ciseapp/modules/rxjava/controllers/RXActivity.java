package org.cise.sdk.ciseapp.modules.rxjava.controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import org.cise.sdk.ciseapp.R;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * basic tutorial source
 * https://www.androidhive.info/RxJava/android-getting-started-with-reactive-programming/
 */
public class RXActivity extends AppCompatActivity {

    private static final String TAG = "RXSample";

    // disposeable
    private Disposable disposable;

    // dispose multiple list subscribtion
    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx);
        basicRXSingleDisposable();
        basicRXMultipleDisposable();
        bindingData();
    }


    /**
     * basic rx di main thread
     */
    private void basicRXSingleDisposable() {
        // 1. sumber data
//        Observable<String> animalsObservable = Observable.just("Ant", "Bee", "Cat", "Dog", "Fox");
        Observable<String> animalsObservable = Observable.fromArray(
                "Ant", "Ape",
                "Bat", "Bee", "Bear", "Butterfly",
                "Cat", "Crab", "Cod",
                "Dog", "Dove",
                "Fox", "Frog"
        );
        // 2. listener
        Observer<String> animalsObserver = getAnimalsObserver();
        // 1 sumber data animal di subscribe animal observer dengan scheduler di main thread
        animalsObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Exception {
                        return s.toLowerCase().startsWith("b");
                    }
                })
                .subscribe(animalsObserver);
    }

    private void basicRXMultipleDisposable() {
        String[] random = new String[100];
        Random rando = new Random();
        for (int i = 0; i < random.length; i++) {
            random[i] = String.valueOf(i);
        }
        Observable<String> animalsObservable = Observable.fromArray(random);
//        Observable<String> animalsObservable = Observable.fromArray(
//                "Ant", "Ape",
//                "Bat", "Bee", "Bear", "Butterfly",
//                "Cat", "Crab", "Cod",
//                "Dog", "Dove",
//                "Fox", "Frog"
//        );

        DisposableObserver<String> animalsObserver = getForMultiAnimalsObserver();

        DisposableObserver<String> animalsObserverAllCaps = getForMultiAnimalsAllCapsObserver();

        /**
         * filter() is used to filter out the animal names starting with `b`
         * */
        compositeDisposable.add(
                animalsObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .filter(new Predicate<String>() {
                            @Override
                            public boolean test(String s) throws Exception {
                                return s.toLowerCase().startsWith("8");
                            }
                        })
                        .subscribeWith(animalsObserver));

        /**
         * filter() is used to filter out the animal names starting with 'c'
         * map() is used to transform all the characters to UPPER case
         * */

        compositeDisposable.add(
                animalsObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .filter(new Predicate<String>() {
                            @Override
                            public boolean test(String s) throws Exception {
                                return s.toLowerCase().startsWith("7");
                            }
                        })
                        .map(new Function<String, String>() {
                            @Override
                            public String apply(String s) throws Exception {
                                return s.toUpperCase();
                            }
                        })
                        .subscribeWith(animalsObserverAllCaps));
//        finish();
    }

    private Observer<String> getAnimalsObserver() {

        return new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe");
                disposable = d;
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "Name: " + s);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "All items are emitted!");
            }
        };
    }

    // multi dissposeable
    private DisposableObserver<String> getForMultiAnimalsObserver() {
        return new DisposableObserver<String>() {

            @Override
            public void onNext(String s) {
                Log.d(TAG, "Name: " + s);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "All items are emitted!");
            }
        };
    }

    // multi dissposeable
    private DisposableObserver<String> getForMultiAnimalsAllCapsObserver() {
        return new DisposableObserver<String>() {


            @Override
            public void onNext(String s) {
                Log.d(TAG, "Name: " + s);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "All items are emitted!");
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // don't send events once the activity is destroyed
        disposable.dispose();
        compositeDisposable.clear();
    }


    // with view
    Button button, button2, button3;
    FloatingActionButton fab;
    TextView txtBelowEditText, txtBelowButton;
    EditText editText, editText2;

    private void bindingData(){
        button = findViewById(R.id.button);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        fab = findViewById(R.id.fab);
        txtBelowEditText = findViewById(R.id.txtBelowEditText);
        txtBelowButton = findViewById(R.id.txtBelowButton);
        editText = findViewById(R.id.editText);
        editText2 = findViewById(R.id.editText2);


        Observable<Object> observable1 = RxView.clicks(button2).map(o -> button2);
        Observable<Object> observable2 = RxView.clicks(fab).map(o -> fab);


        Disposable d1 = Observable.merge(observable1, observable2).throttleFirst(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread()).
                        subscribe(new Consumer<Object>() {
                            @Override
                            public void accept(Object o) {
                                Toast.makeText(getApplicationContext(), "Avoid multiple clicks using throttleFirst", Toast.LENGTH_SHORT).show();
                                if (o instanceof Button) {
                                    txtBelowButton.setText(((Button) o).getText().toString() + " clicked");
                                } else if (o instanceof FloatingActionButton) {
                                    txtBelowButton.setText("Fab clicked");
                                }
                            }
                        });


        Disposable d = RxView.clicks(button).debounce(5, TimeUnit.SECONDS).
                observeOn(AndroidSchedulers.mainThread()).
                map(o -> button.getText().toString()).
                subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String o) throws Exception {
                        txtBelowButton.setText(o + " was clicked");
                    }
                });

        final CompositeDisposable disposables = new CompositeDisposable();
//        Disposable dt1 = null;
        final Disposable dt1 = Observable.merge(RxTextView.textChanges(editText), RxTextView.textChanges(editText2)).filter(s -> s.toString().length() > 6)
                .debounce(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> disposables.add(disposable))
                .subscribe(charSequence -> {
                    Log.d(TAG, String.valueOf(charSequence));
                    editText2.setText(charSequence);
                    disposables.clear();


                });


/*
        Disposable dtext1 = RxTextView.textChanges(editText)
                .filter(s -> s.toString().length() > 6)
                .debounce(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {
//                        dtext1.dispose();
                        editText2.setText(charSequence);
                    }
                });
        Disposable dtext2 = RxTextView.textChanges(editText2)
                .filter(s -> s.toString().length() > 6)
                .debounce(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {
//                        dtext1.dispose();
                        editText.setText(charSequence);
                    }
                });
*/

        // Observable<String> animalsObservable = SingleSubject<String>.create();



        CompositeDisposable compositeDisposable = new CompositeDisposable();

        Observable<Button> clickObservable = RxView.clicks(button3).map(o -> button3).share();

        Disposable buttonShowToast =
                clickObservable.subscribe(new Consumer<Button>() {
                    @Override
                    public void accept(Button o) throws Exception {
                        Toast.makeText(getApplicationContext(), "Show toast", Toast.LENGTH_SHORT).show();

                    }
                });
        compositeDisposable.add(buttonShowToast);

        Disposable changeButtonText =
                clickObservable.subscribe(new Consumer<Button>() {
                    @Override
                    public void accept(Button o) throws Exception {

                        o.setText("New text");
                    }
                });
        compositeDisposable.add(changeButtonText);
    }
}
