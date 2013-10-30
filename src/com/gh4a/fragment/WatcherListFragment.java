/*
 * Copyright 2011 Azwan Adli Abdullah
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gh4a.fragment;

import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.PageIterator;
import org.eclipse.egit.github.core.service.WatcherService;

import android.os.Bundle;

import com.gh4a.Constants;
import com.gh4a.Gh4Application;
import com.gh4a.R;
import com.gh4a.adapter.RootAdapter;
import com.gh4a.adapter.UserAdapter;

public class WatcherListFragment extends PagedDataBaseFragment<User> {
    private String mRepoOwner;
    private String mRepoName;
    
    public static WatcherListFragment newInstance(String repoOwner, String repoName) {
        WatcherListFragment f = new WatcherListFragment();

        Bundle args = new Bundle();
        args.putString(Constants.Repository.REPO_OWNER, repoOwner);
        args.putString(Constants.Repository.REPO_NAME, repoName);
        f.setArguments(args);
        
        return f;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRepoOwner = getArguments().getString(Constants.Repository.REPO_OWNER);
        mRepoName = getArguments().getString(Constants.Repository.REPO_NAME);
    }
    
    @Override
    protected RootAdapter<User> onCreateAdapter() {
        return new UserAdapter(getActivity(), false);
    }
    
    @Override
    protected int getEmptyTextResId() {
        return R.string.no_watchers_found;
    }

    @Override
    protected void onItemClick(User user) {
        Gh4Application app = Gh4Application.get(getActivity());
        app.openUserInfoActivity(getActivity(), user.getLogin(), user.getName());
    }

    @Override
    protected PageIterator<User> onCreateIterator() {
        WatcherService watcherService = (WatcherService)
                Gh4Application.get(getActivity()).getService(Gh4Application.WATCHER_SERVICE);
        return watcherService.pageWatchers(new RepositoryId(mRepoOwner, mRepoName));
    }
}