package com.example.nurture_insight.journal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.example.nurture_insight.Prevalent.Prevalent;
import com.example.nurture_insight.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
public class JournalFragment extends Fragment {

    TextView journal_title;
    CalendarView journal_calendar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_journal, container, false);

        journal_title = (TextView) view.findViewById(R.id.journal_title);
        journal_calendar = (CalendarView) view.findViewById(R.id.journal_calendar);

        String journal_title_name = Prevalent.currentOnlineUser.getUsername() + "\'s Journal";
        journal_title.setText(journal_title_name);

        journal_calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar clickedCalendar = Calendar.getInstance();
                Calendar calForDate = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");

                String saveCurrentDate = currentDate.format(calForDate.getTime());
                clickedCalendar.set(year, month, dayOfMonth);

                String clickedDate = currentDate.format(clickedCalendar.getTime());

                if(saveCurrentDate.equals(clickedDate)){
                    Fragment fragment =  new journaling_form();

                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.main_fragmentLayout, fragment );
                    transaction.commit();
                }
                else{
                    Fragment fragment = new journal_display();
                    Bundle bundle = new Bundle();
                    bundle.putString("clickedDate", clickedDate);
                    fragment.setArguments(bundle);

                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.main_fragmentLayout, fragment );
                    transaction.commit();
                }
            }
        });


        return view;
    }
}