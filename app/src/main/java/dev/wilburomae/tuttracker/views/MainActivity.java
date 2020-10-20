package dev.wilburomae.tuttracker.views;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Collections;
import java.util.List;

import dev.wilburomae.tuttracker.Constants;
import dev.wilburomae.tuttracker.R;
import dev.wilburomae.tuttracker.views.adapters.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseUser mUser;
    private Context mContext;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mContext = this;

        ViewPager viewPager = findViewById(R.id.home_viewPager);
        TabLayout tabLayout = findViewById(R.id.home_tabs);
        Toolbar toolbar = findViewById(R.id.home_toolbar);
        mNavigationView = findViewById(R.id.home_navDrawer);
        mDrawerLayout = findViewById(R.id.home_drawerLayout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        SectionsPagerAdapter sectionsAdapter = new SectionsPagerAdapter(mContext, getSupportFragmentManager());

        viewPager.setAdapter(sectionsAdapter);
        tabLayout.setupWithViewPager(viewPager);

        mNavigationView.bringToFront();
        mNavigationView.setNavigationItemSelectedListener(this);

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        if (mUser == null) {
            signInUser();
        }

        modifyNavDrawerUserDetails();
    }

    private void signInUser() {
        List<AuthUI.IdpConfig> providers = Collections.singletonList(new AuthUI.IdpConfig.EmailBuilder().build());
        startActivityForResult(AuthUI.getInstance().
                createSignInIntentBuilder().
                setAvailableProviders(providers).
                setTheme(R.style.AppTheme).
                build(), Constants.RC_FB_SIGNIN);
    }

    private void signOutUser() {
        if (mUser != null) {
            final String displayName = mUser.getDisplayName();
            AuthUI.getInstance().signOut(mContext).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    mUser = null;
                    Toast.makeText(mContext, "Bye, " + displayName, Toast.LENGTH_SHORT).show();
                    modifyNavDrawerUserDetails();
                }
            });
        }
    }

    private void modifyNavDrawerUserDetails() {
        MenuItem navLogout = mNavigationView.getMenu().findItem(R.id.nav_menu_log_out);
        MenuItem navSignin = mNavigationView.getMenu().findItem(R.id.nav_menu_sign_in);
        if (mUser == null) {
            navLogout.setVisible(false);
            navSignin.setVisible(true);
        } else {
            navLogout.setVisible(true);
            navSignin.setVisible(false);
        }
    }

    private void syncNavDrawer() {
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        syncNavDrawer();
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        syncNavDrawer();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.RC_FB_SIGNIN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (response == null) {
                Toast.makeText(mContext, "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                mUser = FirebaseAuth.getInstance().getCurrentUser();
                if (resultCode == RESULT_OK && mUser != null) {
                    Toast.makeText(mContext, "Welcome, " + mUser.getDisplayName(), Toast.LENGTH_SHORT).show();
                    modifyNavDrawerUserDetails();
                } else {
                    Toast.makeText(mContext, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_menu_log_out:
                signOutUser();
                break;
            case R.id.nav_menu_sign_in:
                signInUser();
                break;
            case R.id.nav_menu_share:
                String content = "TuTTracker makes distance learning easier. Download it on the PlayStore today.";
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Sharing TuTTracker app link");
                intent.putExtra(Intent.EXTRA_TEXT, content);
                startActivity(Intent.createChooser(intent, "Share using"));
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item);
    }
}