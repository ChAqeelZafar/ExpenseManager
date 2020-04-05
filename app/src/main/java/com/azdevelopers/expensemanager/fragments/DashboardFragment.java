package com.azdevelopers.expensemanager.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.azdevelopers.expensemanager.R;
import com.azdevelopers.expensemanager.models.Entry;
import com.azdevelopers.expensemanager.viewmodels.DataViewModel;

import java.util.List;

public class DashboardFragment extends Fragment {
    private DataViewModel dataViewModel;
    private Button setDataBtn;
    private TextView textView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        textView = root.findViewById(R.id.dashboard_text_text);

       dataViewModel = ViewModelProviders.of(getActivity()).get(DataViewModel.class);
       dataViewModel.init();
       dataViewModel.getData().observeForever(new Observer<List<Entry>>() {
           @Override
           public void onChanged(List<Entry> entries) {
                textView.setText(entries.size()+"");
           }
       });
       setDataBtn = root.findViewById(R.id.dashboard_btn_setData);

       setDataBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               setData(new Entry("new id", "2313", "cattt", "today", "lhr"));
           }
       });

       return root;
    }

    public void setData(Entry entry){
        dataViewModel.setData(entry);
    }


}
