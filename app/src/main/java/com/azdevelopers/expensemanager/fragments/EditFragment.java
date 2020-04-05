package com.azdevelopers.expensemanager.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.azdevelopers.expensemanager.R;

public class EditFragment extends Fragment {

    private CardView addCard, deleteCard, editCard, catCard;
    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_edit, container, false);

        bindIds(root);
        setClickListeners();



        return root;
    }

    public void bindIds(View root){
        addCard = root.findViewById(R.id.edit_card_add);
        editCard = root.findViewById(R.id.edit_card_edit);
        deleteCard = root.findViewById(R.id.edit_card_delete);
        catCard = root.findViewById(R.id.edit_card_category);
    }

    public void setClickListeners(){
        addCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new AddExpenseFragment());
            }
        });
    }

    public void loadFragment(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_frame_container, fragment);
        fragmentTransaction.commit();
    }


}
