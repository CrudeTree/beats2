package com.larjam.beats2.controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.larjam.beats2.GoogleSignInService;
import com.larjam.beats2.R;


/**
 * Simple {@link AppCompatActivity} subclass, displaying a button for&mdash;and transferring control
 * to&mdash;Google Sign In, switching to {@link MainActivity} on a successful login.
 */
public class LoginActivity extends AppCompatActivity {

  private static final int LOGIN_REQUEST_CODE = 1000;

  private GoogleSignInService service;

  /**
   * Attempts to refresh most recently logged-in account, switching to {@link MainActivity} if
   * successful, and displaying Google Sign In button otherwise.
   *
   * @param savedInstanceState previously saved state data (ignored).
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    service = GoogleSignInService.getInstance();
    service.refresh()
        .addOnSuccessListener((account) -> switchToMain())
        .addOnFailureListener((ex) -> {
          setContentView(R.layout.activity_login);
          findViewById(R.id.sign_in).setOnClickListener((view) ->
              service.startSignIn(this, LOGIN_REQUEST_CODE));
        });
  }

  /**
   * Receives result returned from {@link #startActivityForResult(Intent, int)}, checking Google
   * Sign In result if appropriate.
   *
   * @param requestCode code submitted with {@link #startActivityForResult(Intent, int)}.
   * @param resultCode abbreviated result (currently ignored).
   * @param data detailed payload of result {@link Intent}.
   */
  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    if (requestCode == LOGIN_REQUEST_CODE) {
      service.completeSignIn(data)
          .addOnSuccessListener((account) -> switchToMain())
          .addOnFailureListener((ex) ->
              Toast.makeText(this, R.string.login_failure_message, Toast.LENGTH_LONG).show());
    } else {
      super.onActivityResult(requestCode, resultCode, data);
    }
  }

  private void switchToMain() {
    Intent intent = new Intent(this, PlayerActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(intent);
  }

}
