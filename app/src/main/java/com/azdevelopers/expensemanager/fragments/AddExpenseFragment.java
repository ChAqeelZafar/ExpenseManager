package com.azdevelopers.expensemanager.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.azdevelopers.expensemanager.R;
import com.azdevelopers.expensemanager.viewmodels.DataViewModel;
import com.google.android.material.textfield.TextInputLayout;

public class AddExpenseFragment extends Fragment {

    private TextInputLayout amountInput, placeInput;
    private Spinner catSpinner;
    private DatePicker datePicker;
    private Button addBtn;
    private DataViewModel dataViewModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_expense, container, false );

        bindIds(root);
        setClickListeners();


        return root;
    }

    public void bindIds(View root){
        amountInput = root.findViewById(R.id.edit_input_amount);
        placeInput = root.findViewById(R.id.edit_input_place);
        catSpinner = root.findViewById(R.id.edit_spinner_category);
        datePicker = root.findViewById(R.id.edit_datepicker);
        addBtn = root.findViewById(R.id.edit_btn_add);
    }

    public void setClickListeners(){
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEmpty()){
                    Toast.makeText(getContext(), "Amount Field is empty", Toast.LENGTH_LONG).show();
                }else{

                }
            }
        });
    }

    public boolean isEmpty(){
        if(amountInput.getEditText().getText().toString()==null){
            return true;
        }
        return false;
    }
}
