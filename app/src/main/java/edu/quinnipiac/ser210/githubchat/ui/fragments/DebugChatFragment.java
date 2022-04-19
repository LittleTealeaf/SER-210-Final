package edu.quinnipiac.ser210.githubchat.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import edu.quinnipiac.ser210.githubchat.R;
import edu.quinnipiac.ser210.githubchat.firebase.dao.FirebaseChatMessage;

public class DebugChatFragment extends Fragment implements ValueEventListener, View.OnClickListener {

    private static String TAG = "DEBUG CHAT";

    DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_debug_chat,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        databaseReference = FirebaseDatabase.getInstance().getReference("debug");
        databaseReference.addValueEventListener(this);

        view.findViewById(R.id.debug_chat_button_send).setOnClickListener(this);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        String text = "";
        for(DataSnapshot child : snapshot.getChildren()) {
            FirebaseChatMessage message = child.getValue(FirebaseChatMessage.class);
            text += message.getSenderUID() + ": " + message.getMessage() + "\n";
        }
        ((TextView) getView().findViewById(R.id.debug_chat_text)).setText(text);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        Log.w(TAG,"Failed to read value.",error.toException());
    }

    @Override
    public void onStop() {
        super.onStop();
        databaseReference.removeEventListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.debug_chat_button_send) {
            String text = ((TextView) getView().findViewById(R.id.debug_chat_enter_text)).getText().toString();
            ((TextView) getView().findViewById(R.id.debug_chat_enter_text)).setText("");
            FirebaseChatMessage message = new FirebaseChatMessage();
            message.setMessage(text);
            message.setSenderUID(FirebaseAuth.getInstance().getCurrentUser().getUid());
            message.setTime(Calendar.getInstance().getTimeInMillis());

            databaseReference.push().setValue(message);
        }
    }
}