package com.example.mymobilefabi.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mymobilefabi.R;
import com.example.mymobilefabi.database.AppDatabase;
import com.example.mymobilefabi.database.entities.Assignment;
import com.example.mymobilefabi.fragments.AssignmentsFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * AssignmentAdapter - RecyclerView adapter for assignments list
 */
public class AssignmentAdapter extends RecyclerView.Adapter<AssignmentAdapter.AssignmentViewHolder> {

    private List<Assignment> assignments;
    private AssignmentsFragment fragment;
    private AppDatabase database;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());

    public AssignmentAdapter(List<Assignment> assignments, AssignmentsFragment fragment, AppDatabase database) {
        this.assignments = assignments;
        this.fragment = fragment;
        this.database = database;
    }

    @Override
    public AssignmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AssignmentViewHolder(
            LayoutInflater.from(parent.getContext()).inflate(R.layout.item_assignment, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(AssignmentViewHolder holder, int position) {
        Assignment assignment = assignments.get(position);
        holder.bind(assignment);
    }

    @Override
    public int getItemCount() {
        return assignments.size();
    }

    /**
     * ViewHolder for assignment items
     */
    public class AssignmentViewHolder extends RecyclerView.ViewHolder {
        TextView titleText, descText, dueDateText, priorityText, statusText;
        Button completeBtn, deleteBtn;

        public AssignmentViewHolder(android.view.View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.assignmentTitleText);
            descText = itemView.findViewById(R.id.assignmentDescText);
            dueDateText = itemView.findViewById(R.id.assignmentDueDateText);
            priorityText = itemView.findViewById(R.id.assignmentPriorityText);
            statusText = itemView.findViewById(R.id.assignmentStatusText);
            completeBtn = itemView.findViewById(R.id.completeAssignmentBtn);
            deleteBtn = itemView.findViewById(R.id.deleteAssignmentBtn);
        }

        public void bind(Assignment assignment) {
            titleText.setText(assignment.getTitle());
            descText.setText(assignment.getDescription());
            dueDateText.setText("Due: " + dateFormat.format(new Date(assignment.getDueDate())));
            priorityText.setText("Priority: " + assignment.getPriority());
            statusText.setText("Status: " + assignment.getStatus());

            // Set priority color
            int priorityColor = getPriorityColor(assignment.getPriority());
            priorityText.setTextColor(priorityColor);

            completeBtn.setOnClickListener(v -> markAsComplete(assignment));
            deleteBtn.setOnClickListener(v -> fragment.deleteAssignment(assignment));
        }

        private void markAsComplete(Assignment assignment) {
            new Thread(() -> {
                assignment.setStatus("completed");
                database.assignmentDao().updateAssignment(assignment);
            }).start();
        }

        private int getPriorityColor(String priority) {
            switch (priority.toLowerCase()) {
                case "high":
                    return itemView.getContext().getColor(android.R.color.holo_red_dark);
                case "medium":
                    return itemView.getContext().getColor(android.R.color.holo_orange_dark);
                case "low":
                    return itemView.getContext().getColor(android.R.color.holo_green_dark);
                default:
                    return itemView.getContext().getColor(android.R.color.darker_gray);
            }
        }
    }
}

