package hu.zsoltborza.gymfinderhun.fragments.base;

import hu.zsoltborza.gymfinderhun.fragments.base.BaseFragment;

/**
 * Created by Zsolt Borza on 2018.02.16..
 */

public interface HostActivityInterface {
    public void setSelectedFragment(BaseFragment fragment);
    public void popBackStack();
    public void popBackStackTillTag(String tag);
    public void addFragment(BaseFragment fragment, boolean withAnimation);
    public void addMultipleFragments(BaseFragment fragments[]);
}
