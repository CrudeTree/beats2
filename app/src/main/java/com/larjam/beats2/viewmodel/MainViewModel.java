package com.larjam.beats2.viewmodel;

import android.app.Application;
import android.net.Uri;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.larjam.beats2.R;

public class MainViewModel extends AndroidViewModel implements LifecycleObserver {

  private static final String LOG_TAG = "MainViewModel";
  private final MutableLiveData<GoogleSignInAccount> account;
  private final MutableLiveData<Throwable> throwable;
  private final MutableLiveData<Uri> uri;

//  private Uri uri = Uri.parse("/storage/0EBB-01CB/MySongList/Pumped up Kicks- Foster The People.mp3");
//  private Uri uri;

  public MainViewModel(@NonNull Application application) {
    super(application);

    account = new MutableLiveData<>();
    throwable = new MutableLiveData<>();
    uri = new MutableLiveData<>(Uri.parse("/storage/0EBB-01CB/MySongList/Pumped up Kicks- Foster The People.mp3"));
  }

  public LiveData<Throwable> getThrowable() {
    return throwable;
  }

  public void setAccount(GoogleSignInAccount account) {
    this.account.setValue(account);
  }


  private String getAuthorizationHeader(GoogleSignInAccount account) {
    String token = getApplication().getString(R.string.oauth_header, account.getIdToken());
    Log.d("OAuth2.0 token", token); // FIXME Remove before shipping.
    return token;
  }


  public MutableLiveData<Uri> getURI() {
    return uri;
  }

  public void setUri(String uri) {
    Log.d(LOG_TAG, "Changing song to :" +uri);
    this.uri.setValue(Uri.parse(uri));
  }
}
