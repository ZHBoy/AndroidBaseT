package com.ychd.ycwwz.base_library.base

import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

/**
 * @author zhou_hao
 * @date 2020-02-12
 * @description: viewPager 对应的fragment切换adapter
 */
class CustomFragmentStatePagerAdapter(
    @NonNull fm: FragmentManager, behavior: Int,
    private var fragments: ArrayList<out Fragment>,
    private var titleList: List<String>?
) : FragmentStatePagerAdapter(fm, behavior) {

    @NonNull
    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titleList?.get(position)
    }

    override fun getCount(): Int {
        return fragments.size
    }
}