package com.example.mymobilefabi.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mymobilefabi.R;
import com.example.mymobilefabi.database.entities.Course;
import com.example.mymobilefabi.fragments.CoursesFragment;

import java.util.List;

/**
 * CourseAdapter - RecyclerView adapter for courses list
 */
public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    private List<Course> courses;
    private CoursesFragment fragment;

    public CourseAdapter(List<Course> courses, CoursesFragment fragment) {
        this.courses = courses;
        this.fragment = fragment;
    }

    @Override
    public CourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CourseViewHolder(
            LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(CourseViewHolder holder, int position) {
        Course course = courses.get(position);
        holder.bind(course);
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    /**
     * ViewHolder for course items
     */
    public class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, codeText, teacherText, semesterText, creditsText;
        Button deleteBtn;

        public CourseViewHolder(android.view.View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.courseNameText);
            codeText = itemView.findViewById(R.id.courseCodeText);
            teacherText = itemView.findViewById(R.id.courseTeacherText);
            semesterText = itemView.findViewById(R.id.courseSemesterText);
            creditsText = itemView.findViewById(R.id.courseCreditsText);
            deleteBtn = itemView.findViewById(R.id.deleteCourseBtn);
        }

        public void bind(Course course) {
            nameText.setText(course.getName());
            codeText.setText("Code: " + course.getCode());
            teacherText.setText("Teacher: " + course.getTeacher());
            semesterText.setText("Semester: " + course.getSemester());
            creditsText.setText("Credits: " + course.getCredits());

            deleteBtn.setOnClickListener(v -> fragment.deleteCourse(course));
        }
    }
}

