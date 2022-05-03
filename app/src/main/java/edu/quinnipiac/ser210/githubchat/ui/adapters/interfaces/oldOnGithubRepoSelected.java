package edu.quinnipiac.ser210.githubchat.ui.adapters.interfaces;

import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubRepo;
import edu.quinnipiac.ser210.githubchat.ui.adapters.viewholders.GithubRepoViewHolder;

/**
 * @author Thomas Kwashnak
 */
@Deprecated
public interface oldOnGithubRepoSelected extends GithubRepoViewHolder.oldOnGithubRepoSelected {

    void onGithubRepoSelected(GithubRepo githubRepo);
}
