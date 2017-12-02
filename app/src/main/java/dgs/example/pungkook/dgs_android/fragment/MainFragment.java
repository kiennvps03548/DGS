package dgs.example.pungkook.dgs_android.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.daniribalbert.customfontlib.views.CustomFontTextView;

import java.util.ArrayList;
import java.util.List;
import dgs.example.pungkook.dgs_android.MainActivity;
import dgs.example.pungkook.dgs_android.R;
import dgs.example.pungkook.dgs_android.utils.AppDialogManager;
import static android.content.Context.INPUT_METHOD_SERVICE;

public class MainFragment extends Fragment {

    TabLayout mTabLayout;
    public static ViewPager mViewPager;
    private Typeface typeface;
    private MaterialDialog mDialogLoading;
    Dialog mDialog;

    private void setupViewPager(final ViewPager viewPager) {
        final MainFragment.ViewPagerAdapter adapter = new MainFragment.ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(InputFragment.newInstance(), getString(R.string.title_tab_input));
        adapter.addFragment(OutputFragment.newInstance(), getString(R.string.title_tab_output));
        adapter.addFragment(SetPlanFragment.newInstance(), getString(R.string.title_tab_set_plan));
        adapter.addFragment(ReportFragment.newInstance(), getString(R.string.title_tab_report));
        //adapter.addFragment(SettingConfigFragment.newInstance(), getString(R.string.title_tab_config));
        viewPager.setAdapter(adapter);

        typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto_Light.ttf");
        ViewGroup vg = (ViewGroup) mTabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(typeface);
                }
            }

        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==0){
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                    if(imm != null){
                        imm.hideSoftInputFromWindow(viewPager.getWindowToken(), 0);
                    }
                    //mToolbar.setTitle( getString(R.string.checklist_daily));
                }else{
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                    if(imm != null){
                        imm.hideSoftInputFromWindow(viewPager.getWindowToken(), 0);
                    }
                    adapter.getItem(position).onResume();
                    // mToolbar.setTitle( getString(R.string.checklist_weekly));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_main, container, false);

        mTabLayout = (TabLayout) root.findViewById(R.id.tab_layout);
        mDialogLoading = AppDialogManager.onCreateDialogLoading(getActivity());
        mViewPager = (ViewPager) root.findViewById(R.id.view_pager);

        Toolbar mToolbar = (Toolbar) ((AppCompatActivity) getActivity()).findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        CustomFontTextView txtTitle = ((CustomFontTextView) mToolbar.findViewById(R.id.title_toolbar));
        txtTitle.setText(getString(R.string.app_name));

        MainActivity.imgBack.setVisibility(View.GONE);


        mTabLayout.setupWithViewPager(mViewPager);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        setupViewPager(mViewPager);
    }

    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
