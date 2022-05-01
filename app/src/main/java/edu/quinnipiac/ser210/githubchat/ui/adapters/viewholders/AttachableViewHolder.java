package edu.quinnipiac.ser210.githubchat.ui.adapters.viewholders;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.MessageFormat;

import edu.quinnipiac.ser210.githubchat.R;
import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubAttachable;
import edu.quinnipiac.ser210.githubchat.ui.adapters.AttachableAdapter;
import edu.quinnipiac.ser210.githubchat.ui.util.ImageLoader;
import edu.quinnipiac.ser210.githubchat.ui.util.OnImageLoaded;

public class AttachableViewHolder extends RecyclerView.ViewHolder implements OnImageLoaded {

    private int channelLoadImage;

    private final AttachableAdapter adapter;

    private final ImageView imageView;
    private final TextView numberView;
    private final TextView titleView;


    public AttachableViewHolder(AttachableAdapter adapter, @NonNull View itemView) {
        super(itemView);
        this.adapter = adapter;
        this.imageView = itemView.findViewById(R.id.list_attachable_owner_avatar);
        numberView = itemView.findViewById(R.id.list_attachable_textview_number);
        titleView = itemView.findViewById(R.id.list_attachable_textview_title);
    }

    public void bindAttachable(GithubAttachable attachable) {
        imageView.setVisibility(View.GONE);
        if(attachable.getGithubUser() != null) {
            channelLoadImage = ImageLoader.loadImage(attachable.getGithubUser().getAvatarUrl(),this);
        }
        numberView.setText(MessageFormat.format(" #{0}: ", attachable.getNumber()));
        titleView.setText(attachable.getTitle());
    }

    @Override
    public void onImageLoaded(Bitmap bitmap, int channel) {
        imageView.setImageBitmap(bitmap);
        imageView.setVisibility(View.VISIBLE);
    }
}
