package com.example.mymobilefabi.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mymobilefabi.R;
import com.example.mymobilefabi.database.AppDatabase;
import com.example.mymobilefabi.database.entities.Assignment;
import com.example.mymobilefabi.database.entities.Course;
import com.example.mymobilefabi.database.entities.Grade;

import java.util.List;

/**
 * GradeAdapter - RecyclerView adapter for grades list
 */
public class GradeAdapter extends RecyclerView.Adapter<GradeAdapter.GradeViewHolder> {

    private List<Grade> grades;
    private AppDatabase database;

    public GradeAdapter(List<Grade> grades, AppDatabase database) {
        this.grades = grades;
        this.database = database;
    }

    @Override
    public GradeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GradeViewHolder(
            LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grade, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(GradeViewHolder holder, int position) {
        Grade grade = grades.get(position);
        holder.bind(grade, database);
    }

    @Override
    public int getItemCount() {
        return grades.size();
    }

    /**
     * ViewHolder for grade items
     */
    public class GradeViewHolder extends RecyclerView.ViewHolder {
        TextView courseText, assignmentText, scoreText, maxScoreText, percentageText;

        public GradeViewHolder(android.view.View itemView) {
            super(itemView);
            courseText = itemView.findViewById(R.id.courseNameText);
            assignmentText = itemView.findViewById(R.id.assignmentNameText);
            scoreText = itemView.findViewById(R.id.scoreText);
            maxScoreText = itemView.findViewById(R.id.maxScoreText);
            percentageText = itemView.findViewById(R.id.percentageText);
        }

        public void bind(Grade grade, AppDatabase db) {
            // Set score and max score
            scoreText.setText("Score: " + grade.getScore());
            maxScoreText.setText("Max: " + grade.getMaxScore());

            // Calculate and set percentage
            double percentage = grade.getPercentage();
            percentageText.setText(String.format("Percentage: %.2f%%", percentage));

            // Set color based on percentage
            int color = getGradeColor(percentage);
            percentageText.setTextColor(color);

            // Get and set course and assignment names
            new Thread(() -> {
                Assignment assignment = db.assignmentDao().getAssignmentById(grade.getAssignment_id());
                if (assignment != null) {
                    // Get course name
                    Course course = db.courseDao().getCourseById(assignment.getCourse_id());
                    String courseName = (course != null) ? course.getName() : "Unknown Course";
                    String assignmentTitle = assignment.getTitle();

                    courseText.post(() -> courseText.setText("Course: " + courseName));
                    assignmentText.post(() -> assignmentText.setText("Assignment: " + assignmentTitle));
                } else {
                    courseText.post(() -> courseText.setText("Course: Unknown"));
                    assignmentText.post(() -> assignmentText.setText("Assignment: Unknown"));
                }
            }).start();
        }

        private int getGradeColor(double percentage) {
            if (percentage >= 90) return itemView.getContext().getColor(android.R.color.holo_green_dark);
            else if (percentage >= 80) return itemView.getContext().getColor(android.R.color.holo_blue_bright);
            else if (percentage >= 70) return itemView.getContext().getColor(android.R.color.holo_orange_dark);
            else return itemView.getContext().getColor(android.R.color.holo_red_dark);
        }
    }
}

