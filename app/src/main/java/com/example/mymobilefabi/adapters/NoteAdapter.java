package com.example.mymobilefabi.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mymobilefabi.R;
import com.example.mymobilefabi.database.entities.Note;
import com.example.mymobilefabi.fragments.NotesFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * NoteAdapter - RecyclerView adapter for notes list
 */
public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private List<Note> notes;
    private NotesFragment fragment;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());

    public NoteAdapter(List<Note> notes, NotesFragment fragment) {
        this.notes = notes;
        this.fragment = fragment;
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NoteViewHolder(
            LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.bind(note);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    /**
     * ViewHolder for note items
     */
    public class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView titleText, contentText, dateText;
        Button deleteBtn;

        public NoteViewHolder(android.view.View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.noteTitleText);
            contentText = itemView.findViewById(R.id.noteContentText);
            dateText = itemView.findViewById(R.id.noteDateText);
            deleteBtn = itemView.findViewById(R.id.deleteNoteBtn);
        }

        public void bind(Note note) {
            titleText.setText(note.getTitle());
            contentText.setText(note.getContent());
            dateText.setText("Created: " + dateFormat.format(new Date(note.getCreatedAt())));

            deleteBtn.setOnClickListener(v -> fragment.deleteNote(note));
        }
    }
}

