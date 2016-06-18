package com.ly.badiane.budgetmanager_finalandroidproject.vues;


import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ly.badiane.budgetmanager_finalandroidproject.R;
import com.ly.badiane.budgetmanager_finalandroidproject.activites.SettingsActivity;
import com.ly.badiane.budgetmanager_finalandroidproject.divers.Mois;
import com.ly.badiane.budgetmanager_finalandroidproject.sql.MoisEcoulesDAO;
import com.ly.badiane.budgetmanager_finalandroidproject.sql.TransactionDAO;
import com.ly.badiane.budgetmanager_finalandroidproject.sql.UtilitaireDAO;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
private Intent activitySwitcher; //pour changer d'activiter

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    private MoisEcoulesDAO moisEcoulesDAO;
    private TransactionDAO transactionDAO;
    private UtilitaireDAO utilitaireDAO;

    //    private int nbSlides = 0; //Contient le nombre de pages ou de slides ou encore de tabulation dans le ViewPager
    private ArrayList<Mois> moisEcoulesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.WHITE);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        //set up DAOs for databases querries
        utilitaireDAO = new UtilitaireDAO(this);
        moisEcoulesDAO = new MoisEcoulesDAO(this);
        transactionDAO = new TransactionDAO(this);

        utilitaireDAO.mettreAjourNbLancementApp();
        if (utilitaireDAO.nombreLancementApp() == 1) {
            premierLancementinsertion();
        }

        initMoisEcoulesList();

    }

    private void premierLancementinsertion() {
        moisEcoulesDAO.insertionLorsDuPremierLancement();
    }

    private void initMoisEcoulesList() {
        moisEcoulesList = moisEcoulesDAO.liste();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_settings:
                activitySwitcher = new Intent(this, SettingsActivity.class);
                startActivity(activitySwitcher);
                return true;
            case R.id.action_alarm:
                return true;
            case R.id.addbudget:
                return true;
            case R.id.adddepenses:
                activitySwitcher = new Intent(this,DepenseActivity.class);
                startActivity(activitySwitcher);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return moisEcoulesList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == moisEcoulesList.size())
                return "Mois Future"; //TODO internasionalisation
            return moisEcoulesList.get(position).toString();
        }
    }
}
