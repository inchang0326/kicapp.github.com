/*
 * Copyright (C) 2014 Bluetooth Connection Template
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wjthinkbig.a10010952.btproject.fragments;

import java.util.Locale;

import android.content.Context;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class FragmentAdapter extends FragmentPagerAdapter {

    public static final String TAG = "FragmentAdapter";

    // TODO: Total count
    public static final int FRAGMENT_COUNT = 6;

    // TODO: Fragment position
    public static final int FRAGMENT_POS_CONTROL = 0;   //기본_짧은 영단어
    public static final int FRAGMENT_POS_CONTROL2 = 1;  //기본_긴 영단어
    public static final int FRAGMENT_POS_CONTROL4 = 2;  //심화_짧은 영단어
    public static final int FRAGMENT_POS_CONTROL5 = 3;  //심화_긴 영단어
    public static final int FRAGMENT_POS_CONTROL3 = 4;  //심화_영어문장
    public static final int FRAGMENT_POS_EXAMPLE = 5;   //자유코딩

    // System
    private Context mContext = null;
    private Handler mHandler = null;
    private IFragmentListener mFragmentListener = null;

    private Fragment mExampleFragment = null;
    private Fragment mGameFragment = null;
    private Fragment mGameFragment2 = null;
    private Fragment mGameFragment3 = null;
    private Fragment mGameFragment4 = null;
    private Fragment mGameFragment5 = null;

    public FragmentAdapter(FragmentManager fm, Context c, IFragmentListener l, Handler h) {
        super(fm);
        mContext = c;
        mFragmentListener = l;
        mHandler = h;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        Fragment fragment;
        //boolean needToSetArguments = false;

        if (position == FRAGMENT_POS_EXAMPLE) {
            if (mExampleFragment == null) {
                mExampleFragment = new ExampleFragment(mContext, mFragmentListener, mHandler);
                //needToSetArguments = true;
            }
            fragment = mExampleFragment;

        } else if (position == FRAGMENT_POS_CONTROL) {
            if (mGameFragment == null) {
                mGameFragment = new GameFragment(mContext, mFragmentListener);
                //needToSetArguments = true;
            }
            fragment = mGameFragment;

        } else if (position == FRAGMENT_POS_CONTROL2) {
            if (mGameFragment2 == null) {
                mGameFragment2 = new GameFragment2(mContext, mFragmentListener);
                //needToSetArguments = true;
            }
            fragment = mGameFragment2;

        } else if (position == FRAGMENT_POS_CONTROL3) {
            if (mGameFragment3 == null) {
                mGameFragment3 = new GameFragment3(mContext, mFragmentListener);
                //needToSetArguments = true;
            }
            fragment = mGameFragment3;

        } else if (position == FRAGMENT_POS_CONTROL4) {
            if (mGameFragment4 == null) {
                mGameFragment4 = new GameFragment4(mContext, mFragmentListener);
                //needToSetArguments = true;
            }
            fragment = mGameFragment4;

        } else if (position == FRAGMENT_POS_CONTROL5) {
            if (mGameFragment5 == null) {
                mGameFragment5 = new GameFragment5(mContext, mFragmentListener);
                //needToSetArguments = true;
            }
            fragment = mGameFragment5;

        } else {
            fragment = null;
        }

        // TODO: If you have something to notify to the fragment.
        /*
		if(needToSetArguments) {
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
		}
		*/

        return fragment;
    }

    @Override
    public int getCount() {
        return FRAGMENT_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (position) {
            case FRAGMENT_POS_EXAMPLE: {
                return "문제풀기&자유코딩";
            }
            case FRAGMENT_POS_CONTROL: {
                return "기본_짧은 영단어";
            }
            case FRAGMENT_POS_CONTROL2: {
                return "기본_긴 영단어";
            }
            case FRAGMENT_POS_CONTROL3: {
                return "심화_영어 문장";
            }
            case FRAGMENT_POS_CONTROL4: {
                return "심화_짧은 영단어";
            }
            case FRAGMENT_POS_CONTROL5: {
                return "심화_긴 영단어";
            }
        }

        return null;
    }
}
