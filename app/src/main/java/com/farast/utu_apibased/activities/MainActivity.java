package com.farast.utu_apibased.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.farast.utu_apibased.R;
import com.farast.utu_apibased.activities.cu.CUArticleActivity;
import com.farast.utu_apibased.activities.cu.CUEventActivity;
import com.farast.utu_apibased.activities.cu.CUExamActivity;
import com.farast.utu_apibased.activities.cu.CUTaskActivity;
import com.farast.utu_apibased.fragments.main_menu.ArticlesFragment;
import com.farast.utu_apibased.fragments.main_menu.EventsFragment;
import com.farast.utu_apibased.fragments.main_menu.ExamsFragment;
import com.farast.utu_apibased.fragments.main_menu.RakingsFragment;
import com.farast.utu_apibased.fragments.main_menu.ServicesFragment;
import com.farast.utu_apibased.fragments.main_menu.SummaryFragment;
import com.farast.utu_apibased.fragments.main_menu.TasksFragment;
import com.farast.utu_apibased.fragments.main_menu.TimetableFragment;
import com.farast.utu_apibased.listeners.StatusOperationListener;
import com.farast.utu_apibased.tasks.UtuDataDownloader;
import com.getbase.floatingactionbutton.FloatingActionButton;

import static com.farast.utu_apibased.Bullshit.dataLoader;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SummaryFragment.OpenFragmentListener {

    FloatingActionButton mFloatingNewEvent;
    FloatingActionButton mFloatingNewExam;
    FloatingActionButton mFloatingNewTask;
    FloatingActionButton mFloatingNewArticle;
    NavigationView mNavigationView;
    View mFloatingActionsMenu;
    MenuItem mRefreshMenuItem = null;
    int mSclassId;
    Activity mActivity;

    Fragment mCurrentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSclassId = getIntent().getExtras().getInt("sclass_id");
        new MainActivityUtuDataDownloader(this, mSclassId).execute();

        mActivity = this;

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        setSelectedFragmentAndOpen(R.id.nav_summary);

        mFloatingActionsMenu = findViewById(R.id.new_action_menu);
        mFloatingNewExam = (FloatingActionButton) findViewById(R.id.action_new_exam);
        mFloatingNewExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, CUExamActivity.class);
                startActivity(intent);
            }
        });
        mFloatingNewEvent = (FloatingActionButton) findViewById(R.id.action_new_event);
        mFloatingNewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, CUEventActivity.class);
                startActivity(intent);
            }
        });
        mFloatingNewTask = (FloatingActionButton) findViewById(R.id.action_new_task);
        mFloatingNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, CUTaskActivity.class);
                startActivity(intent);
            }
        });
        mFloatingNewArticle = (FloatingActionButton) findViewById(R.id.action_new_article);
        mFloatingNewArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, CUArticleActivity.class);
                startActivity(intent);
            }
        });

        if (dataLoader.isAdminLoggedIn())
            mFloatingActionsMenu.setVisibility(View.VISIBLE);

        dataLoader.getOperationManager().setOperationListener(new StatusOperationListener(this));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (mCurrentFragment != null && !(mCurrentFragment instanceof SummaryFragment)) {
            setSelectedFragmentAndOpen(R.id.nav_summary);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        mRefreshMenuItem = menu.getItem(0);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_item_refresh) {
            new MainActivityUtuDataDownloader(this, mSclassId).execute();
        }
        if (id == R.id.menu_item_web_version) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://utu.herokuapp.com"));
            startActivity(browserIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        openFragment(id);

        return true;
    }

    public void setSelectedFragmentAndOpen(int id) {
        openFragment(id);
        mNavigationView.setCheckedItem(id);
    }

    private void openFragment(int id) {
        mCurrentFragment = null;
        if (id == R.id.nav_summary) {
            mCurrentFragment = new SummaryFragment();
        } else if (id == R.id.nav_events) {
            mCurrentFragment = new EventsFragment();
        } else if (id == R.id.nav_exams) {
            mCurrentFragment = new ExamsFragment();
        } else if (id == R.id.nav_tasks) {
            mCurrentFragment = new TasksFragment();
        } else if (id == R.id.nav_timetables) {
            mCurrentFragment = new TimetableFragment();
        } else if (id == R.id.nav_rakings) {
            mCurrentFragment = new RakingsFragment();
        } else if (id == R.id.nav_articles) {
            mCurrentFragment = new ArticlesFragment();
        } else if (id == R.id.nav_services) {
            mCurrentFragment = new ServicesFragment();
        }

        if (mCurrentFragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_fragment_placeholder, mCurrentFragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    private class MainActivityUtuDataDownloader extends UtuDataDownloader {
        public MainActivityUtuDataDownloader(Context context, int sclassId) {
            super(context, sclassId);
        }

        @Override
        protected void onPreExecute() {
            if (mRefreshMenuItem != null)
                mRefreshMenuItem.setEnabled(false);
            super.onPreExecute();
        }

        @Override
        protected void onFinished(boolean success) {
            if (mRefreshMenuItem != null)
                mRefreshMenuItem.setEnabled(true);
            if (success) {

            } else if (!mActivity.isFinishing())
                new AlertDialog.Builder(mActivity)
                        .setTitle("Failed to download data")
                        .setMessage("Do you want to try again?")
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                System.exit(0);
                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                new UtuDataDownloader(mContext, mSclassId);
                            }
                        })
                        .setCancelable(false)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
        }
    }
}
