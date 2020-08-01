package com.developndesign.telehealthpatient.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.developndesign.telehealthpatient.R;
import com.developndesign.telehealthpatient.adapter.SliderAdapterExample;
import com.developndesign.telehealthpatient.model.SliderItem;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;


public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        SliderView sliderView = view.findViewById(R.id.imageSlider);
        ArrayList<SliderItem> sliderItems = new ArrayList<>();
        sliderItems.add(new SliderItem("https://images.theconversation.com/files/304957/original/file-20191203-66986-im7o5.jpg?ixlib=rb-1.1.0&q=45&auto=format&w=926&fit=clip", "Get best consulting online"));
        sliderItems.add(new SliderItem("https://img.medscape.com/thumbnail_library/dt_140605_serious_male_doctor_hospital_800x600.jpg", "Ask your health related doubts"));
        sliderView.setSliderAdapter(new SliderAdapterExample(sliderItems));
        sliderView.setAutoCycle(false);
        return view;
    }
}