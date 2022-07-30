package com.nicomot.re_food.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nicomot.re_food.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderBaseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderBaseFragment extends Fragment implements View.OnClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OrderBaseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderBaseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderBaseFragment newInstance(String param1, String param2) {
        OrderBaseFragment fragment = new OrderBaseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    ConstraintLayout btnBuatOrderan , btnLihatOrderan;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnBuatOrderan = view.findViewById(R.id.id_btn_manual_order);
        btnLihatOrderan = view.findViewById(R.id.id_btn_lihat_order);
        btnBuatOrderan.setOnClickListener(this);
        btnLihatOrderan.setOnClickListener(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_base, container, false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.id_btn_lihat_order:
                switchFragment(getActivity().getSupportFragmentManager(),new HomeFragment());
                break;
            case R.id.id_btn_manual_order:
                switchFragment(getActivity().getSupportFragmentManager(),new CreateOrderManualFragment());
                break;
        }
    }
    void switchFragment(FragmentManager fmg , Fragment targetFragment){
        if(fmg != null){
            fmg.beginTransaction().replace(R.id.id_base_frame,targetFragment).commit();
        }
    }
}