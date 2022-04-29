package edu.quinnipiac.ser210.githubchat.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.quinnipiac.ser210.githubchat.R;
import edu.quinnipiac.ser210.githubchat.database.DatabaseWrapper;
import edu.quinnipiac.ser210.githubchat.firebase.dataobjects.Message;
import edu.quinnipiac.ser210.githubchat.github.GithubWrapper;
import edu.quinnipiac.ser210.githubchat.ui.adapters.MessageAdapter;

/**
 * @author Thomas Kwashnak
 */
public class ChatFragment extends Fragment implements View.OnClickListener {

    private MessageAdapter adapter;

    private DatabaseReference databaseReference;

    private EditText inputText;

    private GithubWrapper githubWrapper;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        githubWrapper = GithubWrapper.from(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        String ref = requireArguments().getString(DatabaseWrapper.KEY_REPO_NAME);
        for (char c : ".#$[]".toCharArray()) {
            ref = ref.replace(c, '_');
        }

        databaseReference = FirebaseDatabase.getInstance().getReference(ref);

        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.frag_chat_recycler_messages);
        LinearLayoutManager manager = new LinearLayoutManager(requireContext());
        manager.setStackFromEnd(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter = new MessageAdapter(requireContext(), recyclerView));
        databaseReference.get().addOnSuccessListener(adapter::setInitialData);

        inputText = view.findViewById(R.id.frag_chat_edittext_insert);
        inputText.setInputType(InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE);

        view.findViewById(R.id.frag_chat_fab_send).setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        databaseReference.addChildEventListener(adapter);
    }

    @Override
    public void onPause() {
        super.onPause();
        databaseReference.removeEventListener(adapter);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.frag_chat_fab_send) {

            Message message = new Message();

            message.setMessage(inputText.getText().toString());
            message.setSender(githubWrapper.getGithubUser().getLogin());

            inputText.setText("");

            databaseReference.push().setValue(message);
        }
    }
}