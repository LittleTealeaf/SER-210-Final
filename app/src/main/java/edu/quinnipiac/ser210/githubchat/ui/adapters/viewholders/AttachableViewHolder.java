package edu.quinnipiac.ser210.githubchat.ui.adapters.viewholders;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.MessageFormat;

import edu.quinnipiac.ser210.githubchat.R;
import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubAttachment;
import edu.quinnipiac.ser210.githubchat.ui.adapters.AttachableAdapter;
import edu.quinnipiac.ser210.githubchat.ui.util.OnImageLoaded;

@Deprecated
public class AttachableViewHolder extends RecyclerView.ViewHolder implements OnImageLoaded, View.OnClickListener {

    private final AttachableAdapter adapter;
    private final ImageView imageView;
    private final TextView numberView;
    private final TextView titleView;
    private int channelLoadImage;
    private GithubAttachment githubAttachment;

    public AttachableViewHolder(AttachableAdapter adapter, @NonNull View itemView) {
        super(itemView);
        this.adapter = adapter;

        itemView.setOnClickListener(this);

        this.imageView = itemView.findViewById(R.id.list_attachment_imageview_icon);
        numberView = itemView.findViewById(R.id.list_attachment_textview_number);
        titleView = itemView.findViewById(R.id.list_attachment_textview_title);
    }

    public void bindAttachable(GithubAttachment attachable) {
        this.githubAttachment = attachable;
        imageView.setVisibility(View.GONE);
        imageView.setVisibility(View.VISIBLE);
        imageView.setImageResource(attachable.getStatusDrawable());
//        if (attachable.getGithubUser() != null) {
//            channelLoadImage = ImageLoader.loadImage(attachable.getGithubUser().getAvatarUrl(), this);
//        }
        numberView.setText(MessageFormat.format(" #{0}: ", attachable.getNumber()));
        titleView.setText(attachable.getTitle());
    }

    @Override
    public void onImageLoaded(Bitmap bitmap, int channel) {
        imageView.setImageBitmap(bitmap);
        imageView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        if (githubAttachment != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(githubAttachment.getURL()));
            adapter.getContext().startActivity(intent);
        }
    }
}
