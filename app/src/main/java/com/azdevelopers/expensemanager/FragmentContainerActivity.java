package com.azdevelopers.expensemanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.azdevelopers.expensemanager.fragments.DashboardFragment;
import com.azdevelopers.expensemanager.fragments.HomeFragment;
import com.azdevelopers.expensemanager.fragments.EditFragment;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Arrays;
import java.util.List;

public class FragmentContainerActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    private static final int RC_SIGN_IN = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);


// Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build(),
                new AuthUI.IdpConfig.TwitterBuilder().build());

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser()==null){
            // Create and launch sign-in intent
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    RC_SIGN_IN);
        }else{
            Toast.makeText(FragmentContainerActivity.this, "Signed in succesfully:" + auth.getUid(), Toast.LENGTH_LONG).show();
            successfullySignedIn();
        }

            successfullySignedIn();







    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(response.isNewUser()){

                }
                // ...
            } else {
                if(response==null){
                    Toast.makeText(FragmentContainerActivity.this, "Signed in Failed", Toast.LENGTH_LONG).show();

                }else{
                    Toast.makeText(FragmentContainerActivity.this, "Error: "+ response.getError(), Toast.LENGTH_LONG).show();

                }
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }



    public void successfullySignedIn(){
        bottomNavigationView = findViewById(R.id.main_navigation_bottom);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //Toast.makeText(Main2Activity.this, "Clicked", Toast.LENGTH_LONG).show();
                switch (item.getItemId()){
                    case R.id.navigation_dashboard:
                        loadFragment(new DashboardFragment());
                        break;
                    case R.id.navigation_home:
                        loadFragment(new HomeFragment());
                        break;
                    case R.id.navigation_edit:
                        loadFragment(new EditFragment());
                        break;
                }
                return true;
            }
        });

        loadFragment(new DashboardFragment());
    }





    public void loadFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_frame_container, fragment);
        fragmentTransaction.commit();
    }




}
